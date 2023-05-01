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
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private RatingDbStorage ratingDbStorage;
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

    @Override
    public Film get(Integer id) {
        List<Film> films = jdbcTemplate.query(GET_FILM_QUERY, this::makeFilmFromComplexTable, id);
        if (!films.isEmpty()) {
            return films.stream().findFirst().get();
        }
        throw new EntityNotExistException(String.format(FILM_NOT_EXIST_MESSAGE, id));
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
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

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
        return jdbcTemplate.query(GET_ALL_FILMS_QUERY, this::makeFilmFromComplexTable);
    }


    @Override
    public Collection<Film> getPopular(Integer count) {
        List<Film> films = jdbcTemplate.query(GET_POPULAR_FILMS_QUERY, this::makeFilmFromComplexTable);
        return films.stream().limit(count).collect(Collectors.toList());
    }

    @Override
    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }

    @Override
    public Set<Genre> getFilmGenresFromDB(int filmId) {
        String sqlQuery = "select G.GENRE_ID, G.GENRE_NAME " +
                "from FILMS_GENRES as FG join GENRES as G on FG.GENRE_ID = G.GENRE_ID " +
                "where FG.FILM_ID = ? " +
                "group by G.GENRE_ID";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId));
    }

    @Override
    public Film makeFilmFromComplexTable(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .rate(resultSet.getInt("RATE"))
                .mpa(ratingDbStorage.get(resultSet.getInt("RATING_ID")))
                .genres(getFilmGenresFromDB(resultSet.getInt("FILM_ID")))
                .directors(getFilmDirectorsFromDB(resultSet.getInt("FILM_ID")))
                .build();
    }

    private Set<Director> getFilmDirectorsFromDB(int filmid) {
        String sqlQuery = "SELECT d.id, d.name " +
                "FROM FILM_DIRECTOR AS fd " +
                "JOIN DIRECTORS AS d on fd.director_id = d.id " +
                "WHERE fd.film_id = ? " +
                "GROUP BY d.id";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToDirector, filmid));
    }

    private Director mapRowToDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public Collection<Film> getPopularByGenre(int genreId, int count) {
        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATE, f.RATING_ID " +
                "from FILMS f " +
                "left join LIKES l on f.FILM_ID = l.FILM_ID " +
                "join FILMS_GENRES fg on f.FILM_ID = fg.FILM_ID " +
                "where fg.GENRE_ID = ? " +
                "group by f.FILM_ID " +
                "order by COUNT(l.USER_ID) desc " +
                "limit ?";

        return jdbcTemplate.query(sqlQuery, this::makeFilmFromComplexTable, genreId, count);
    }

    @Override
    public Collection<Film> getPopularByYear(int year, int count) {
        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATE, f.RATING_ID " +
                "from FILMS f " +
                "left join LIKES l on f.FILM_ID = l.FILM_ID " +
                "join FILMS_GENRES fg on f.FILM_ID = fg.FILM_ID " +
                "where EXTRACT(YEAR from release_date) = ? " +
                "group by f.FILM_ID " +
                "order by COUNT(l.USER_ID) desc " +
                "limit ?";

        return jdbcTemplate.query(sqlQuery, this::makeFilmFromComplexTable, year, count);
    }

    @Override
    public Collection<Film> getPopularByGenreAndYear(int genreId, int year, int count) {
        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATE, f.RATING_ID " +
                "from FILMS f " +
                "left join LIKES l on f.FILM_ID = l.FILM_ID " +
                "join FILMS_GENRES fg on f.FILM_ID = fg.FILM_ID " +
                "where fg.GENRE_ID = ? and " +
                "EXTRACT(YEAR from release_date) = ? " +
                "group by f.FILM_ID " +
                "order by COUNT(l.USER_ID) desc " +
                "limit ?";

        return jdbcTemplate.query(sqlQuery, this::makeFilmFromComplexTable, genreId, year, count);
    }

    @Override
    public Collection<Film> getDirectorsFilms(Integer directorId, String sortBy) {
        String query;

        if (sortBy.equals("year")) {
            query = GET_DIRECTORS_FILMS_SORT_BY_YEAR;
        } else {
            query = GET_DIRECTORS_FILMS_SORT_BY_LIKES;
        }

        return jdbcTemplate.query(query, this::makeFilmFromComplexTable, directorId);
    }

    @Override
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        return jdbcTemplate.query(GET_COMMON_FILMS, this::makeFilmFromComplexTable, userId, friendId);
    }
}
