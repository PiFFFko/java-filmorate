package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final DirectorStorage directorStorage;
    private final JdbcTemplate jdbcTemplate;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String GET_FILM_QUERY = "select * from films " +
            "left join ratings r on r.rating_id = films.rating_id " +
            "left join film_category fc on fc.film_id = films.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
            "left join FILM_DIRECTOR FD on FILMS.FILM_ID = FD.FILM_ID " +
            "left join DIRECTORS D on D.ID = FD.DIRECTOR_ID " +
            "where films.film_id = ?";
    private static final String INSERT_FILM_QUERY = "insert into films(name, description, release_date, duration, rating_id) " +
            "values(?,?,?,?,?)";
    private static final String DELETE_FILM_QUERY = "delete from films where film_id = ?";
    private static final String UPDATE_FILM_QUERY = "update films set name = ?, release_date = ?, description = ?, " +
            "duration = ?, rating_id = ? where film_id = ?";
    private static final String GET_ALL_FILMS_QUERY = "select * from films " +
            "inner join ratings r on r.rating_id = films.rating_id";
    private static final String FILM_NOT_EXIST_MESSAGE = "Фильма с id %s не существует";
    private static final String GET_POPULAR_FILMS_QUERY = "select * " +
            "from films f " +
            "left join likes l ON l.film_id = f.film_id " +
            "left join ratings r on r.rating_id = f.rating_id " +
            "left join film_category fc on fc.film_id = f.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
            "group by fc.genre_id, f.film_id, l.user_id " +
            "order by count(l.user_id) desc";
    private static final String INSERT_FILM_GENRES = "insert into film_category(film_id, genre_id) values (?,?)";
    private static final String INSERT_FILM_DIRECTORS = "insert into film_director(film_id, director_id) values (?,?)";
    private static final String DELETE_FILM_GENRES = "delete from film_category where film_id = ?";
    private static final String DELETE_FILM_DIRECTORS = "delete from film_director where film_id = ?";
    private static final String GET_FILM_GENRES = "select * from genres inner join film_category on " +
            "genres.genre_id = film_category.genre_id  where film_id = ?";
    private static final String GET_FILM_DIRECTORS = "select * from DIRECTORS inner join FILM_DIRECTOR FD on " +
            "DIRECTORS.ID = FD.DIRECTOR_ID where film_id = ?";
    private static final String GET_DIRECTORS_FILMS_SORT_BY_YEAR = "select * from films f " +
            "left join likes l ON l.film_id = f.film_id " +
            "left join ratings r on r.rating_id = f.rating_id " +
            "left join film_category fc on fc.film_id = f.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
            "left join FILM_DIRECTOR FD on f.FILM_ID = FD.FILM_ID " +
            "left join DIRECTORS on FD.DIRECTOR_ID = DIRECTORS.ID " +
            "where DIRECTORS.ID = ?" +
            "group by f.film_id " +
            "order by f.RELEASE_DATE";
    private static final String GET_DIRECTORS_FILMS_SORT_BY_LIKES = "select * from films f " +
            "left join likes l ON l.film_id = f.film_id " +
            "left join ratings r on r.rating_id = f.rating_id " +
            "left join film_category fc on fc.film_id = f.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
            "left join FILM_DIRECTOR FD on f.FILM_ID = FD.FILM_ID " +
            "left join DIRECTORS on FD.DIRECTOR_ID = DIRECTORS.ID " +
            "where DIRECTORS.ID = ?" +
            "group by f.film_id " +
            "order by count(l.user_id)";
    private static final String SEARCH_FILM_BY_TITLE = "select * from films " +
            "left join ratings r on r.rating_id = films.rating_id " +
            "left join film_category fc on fc.film_id = films.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
            "left join FILM_DIRECTOR FD on FILMS.FILM_ID = FD.FILM_ID " +
            "left join DIRECTORS D on D.ID = FD.DIRECTOR_ID " +
            "left join likes l ON l.film_id = films.film_id " +
            "where lower(films.name) like ?" +
            "order by count(l.user_id) desc";
    private static final String SEARCH_FILM_BY_DIRECTOR = "select * from films " +
            "left join ratings r on r.rating_id = films.rating_id " +
            "left join film_category fc on fc.film_id = films.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
            "left join FILM_DIRECTOR FD on FILMS.FILM_ID = FD.FILM_ID " +
            "left join DIRECTORS D on D.ID = FD.DIRECTOR_ID " +
            "left join likes l ON l.film_id = films.film_id " +
            "where lower(d.NAME) like ?" +
            "order by count(l.user_id) desc";
    private static final String SEARCH_FILM = "select distinct * from films " +
            "left join ratings r on r.rating_id = films.rating_id " +
            "left join film_category fc on fc.film_id = films.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
            "left join FILM_DIRECTOR FD on FILMS.FILM_ID = FD.FILM_ID " +
            "left join DIRECTORS D on D.ID = FD.DIRECTOR_ID " +
            "left join likes l ON l.film_id = films.film_id " +
            "where lower(d.NAME) like ? or lower(films.NAME) like ?" +
            "group by FILMS.FILM_ID " +
            "order by count(l.user_id) desc";
    private static final String GET_COMMON_FILMS = "select f.film_id, description, name, release_date, duration, f.rating_id, rating_name " +
            "from films f " +
            "inner join ratings r on r.rating_id = f.rating_id " +
            "inner join likes l on l.film_id = f.film_id " +
            "where l.user_id = ? " +
            "intersect " +
            "select f.film_id, description, name, release_date, duration, f.rating_id, rating_name " +
            "from films f " +
            "inner join ratings r on r.rating_id = f.rating_id " +
            "inner join likes l on l.film_id = f.film_id " +
            "where l.user_id = ?";
    private static final String GET_MOST_POPULAR_BY_GENRE = "select f.film_id, f.name, f.description, f.release_date, " +
            "f.rating_id, r.rating_name, f.duration, COUNT(l.user_id) AS COUNT " +
            "from films f " +
            "left join likes l ON l.film_id = f.film_id " +
            "left join ratings r on r.rating_id = f.rating_id " +
            "left join film_category fc on fc.film_id = f.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
            "where fc.genre_id = ? " +
            "group by f.film_id " +
            "order by count(l.user_id) desc " +
            "limit ?";
    private static final String GET_MOST_POPULAR_BY_YEAR = "select f.film_id, f.name, f.description, f.release_date, " +
            "f.rating_id, r.rating_name, f.duration, COUNT(l.user_id) AS COUNT " +
            "from films f " +
            "left join likes l ON l.film_id = f.film_id " +
            "left join ratings r on r.rating_id = f.rating_id " +
            "left join film_category fc on fc.film_id = f.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
            "where extract(year from f.release_date) = ? " +
            "group by f.film_id " +
            "order by count(l.user_id) desc " +
            "limit ?";
    private static final String GET_MOST_POPULAR_BY_GENRE_AND_YEAR = "select f.film_id, f.name, f.description, f.release_date, " +
            "f.rating_id, r.rating_name, f.duration, COUNT(l.user_id) AS COUNT " +
            "from films f " +
            "left join likes l ON l.film_id = f.film_id " +
            "left join ratings r on r.rating_id = f.rating_id " +
            "left join film_category fc on fc.film_id = f.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
            "where fc.genre_id = ? and extract(year from f.release_date) = ? " +
            "group by f.film_id " +
            "order by count(l.user_id) desc " +
            "limit ?";

    @Override
    public Film get(Integer id) {
        List<Film> films = jdbcTemplate.query(GET_FILM_QUERY, (rs, rowNum) -> makeFilmFromComplexTable(rs), id);
        if (!films.isEmpty()) {
            return films.stream().findFirst().get();
        }
        throw new EntityNotExistException(String.format(FILM_NOT_EXIST_MESSAGE, id));
    }

    private Film makeFilmFromComplexTable(ResultSet rs) throws SQLException {
        LocalDate releaseDate = LocalDate.parse(rs.getString("release_date"), DATE_FORMATTER);
        Film film = new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                releaseDate,
                rs.getInt("duration")
        );
        film.setMpa(new Rating(
                rs.getInt("rating_id"),
                rs.getString("rating_name")
        ));
        film.setGenres(getGenresForFilm(film.getId()));
        film.setDirectors(getDirectorsForFilm(film.getId()));
        return film;
    }

    private List<Genre> getGenresForFilm(Integer filmId) {
        return jdbcTemplate.query(GET_FILM_GENRES, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
    }

    private List<Director> getDirectorsForFilm(Integer filmId) {
        return jdbcTemplate.query(GET_FILM_DIRECTORS, (rs, rowNum) -> makeDirector(rs), filmId);
    }

    private Director makeDirector(ResultSet rs) throws SQLException {
        return new Director(
                rs.getInt("id"),
                rs.getString("name")
        );
    }

    @Override
    public Film add(Film film) {
        String releaseDate = film.getReleaseDate().format(DATE_FORMATTER);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_FILM_QUERY, new String[]{"film_id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setString(3, releaseDate);
            statement.setLong(4, film.getDuration());
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(INSERT_FILM_GENRES, film.getId(), genre.getId());
        }
        for (Director director : film.getDirectors()) {
            jdbcTemplate.update(INSERT_FILM_DIRECTORS, film.getId(), director.getId());
        }

        return film;
    }

    @Override
    public Film remove(Film film) {
        if (jdbcTemplate.update(DELETE_FILM_QUERY, film.getId()) > 0) {
            return film;
        }
        throw new EntityNotExistException(String.format(FILM_NOT_EXIST_MESSAGE, film.getId()));
    }

    @Override
    public Film update(Film film) {
        if (jdbcTemplate.update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getReleaseDate().format(DATE_FORMATTER),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) > 0) {
            jdbcTemplate.update(DELETE_FILM_GENRES, film.getId());
            jdbcTemplate.update(DELETE_FILM_DIRECTORS, film.getId());

            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(INSERT_FILM_GENRES, film.getId(), genre.getId());
            }
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update(INSERT_FILM_DIRECTORS, film.getId(), director.getId());
            }

            return film;
        }
        throw new EntityNotExistException(String.format(FILM_NOT_EXIST_MESSAGE, film.getId()));
    }

    @Override
    public Boolean contains(Integer id) {
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        return jdbcTemplate.query(GET_ALL_FILMS_QUERY, (rs, rowNum) -> makeFilmFromComplexTable(rs));
    }


    @Override
    public Collection<Film> getPopular(Integer count) {
        List<Film> films = jdbcTemplate.query(GET_POPULAR_FILMS_QUERY, (rs, rowNum) -> makeFilmFromComplexTable(rs));
        return films.stream().limit(count).collect(Collectors.toList());
    }

    @Override
    public Collection<Film> getDirectorsFilms(Integer directorId, String sortBy) {
        Director director = directorStorage.get(directorId);
        String query;

        if (sortBy.equals("year")) {
            query = GET_DIRECTORS_FILMS_SORT_BY_YEAR;
        } else {
            query = GET_DIRECTORS_FILMS_SORT_BY_LIKES;
        }

        return jdbcTemplate.query(query, (rs, rowNum) -> makeFilmFromComplexTable(rs), directorId);
    }

    @Override
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        return jdbcTemplate.query(GET_COMMON_FILMS, (rs, rowNum) -> makeFilmFromComplexTable(rs), userId, friendId);
    }

    @Override
    public Collection<Film> searchFilms(String query, String by) {
        List<String> searchBy = List.of(by.split(","));
        query = query.toLowerCase();
        String sql;

        if (searchBy.size() == 1) {
            if (by.equals("director")) {
                sql = SEARCH_FILM_BY_DIRECTOR;
            } else if (by.equals("title")) {
                sql = SEARCH_FILM_BY_TITLE;
            } else {
                throw new EntityNotExistException("Можно искать только по режиссеру (director) или названию (title)");
            }

            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmFromComplexTable(rs), "%" + query + "%");
        } else {
            sql = SEARCH_FILM;
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmFromComplexTable(rs),
                    "%" + query + "%",
                    "%" + query + "%");
        }
    }

    @Override
    public Collection<Film> getPopularByGenreAndYear(int count, int genreId, int year) {
        if (genreId == 0 && year == 0) {
            return getPopular(count);
        } else if (genreId == 0) {
            return jdbcTemplate.query(GET_MOST_POPULAR_BY_YEAR, (rs, rowNum) -> makeFilmFromComplexTable(rs), year, count);
        } else if (year == 0) {
            return jdbcTemplate.query(GET_MOST_POPULAR_BY_GENRE, (rs, rowNum) -> makeFilmFromComplexTable(rs), genreId, count);
        } else {
            return jdbcTemplate.query(GET_MOST_POPULAR_BY_GENRE_AND_YEAR, (rs, rowNum) -> makeFilmFromComplexTable(rs), genreId, year, count);
        }
    }
}
