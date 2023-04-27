package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier(value = "filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String GET_FILM_QUERY = "select * from films " +
            "left join ratings r on r.rating_id = films.rating_id " +
            "left join film_category fc on fc.film_id = films.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
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
            "group by f.film_id " +
            "order by count(l.user_id) desc";

    private static final String INSERT_FILM_GENRES = "insert into film_category(film_id, genre_id) values (?,?)";
    private static final String DELETE_FILM_GENRES = "delete from film_category where film_id = ?";
    private static final String GET_FILM_GENRES = "select * from genres inner join film_category on " +
            "genres.genre_id = film_category.genre_id  where film_id = ?";

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
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film get(Integer id) {
        List<Film> films = jdbcTemplate.query(GET_FILM_QUERY, (rs, rowNum) -> makeFilmFromComplexTable(rs), id);
        if (!films.isEmpty())
            return films.stream().findFirst().get();
        throw new EntityNotExistException(String.format(FILM_NOT_EXIST_MESSAGE, id));
    }

    private Film makeFilmFromComplexTable(ResultSet rs) throws SQLException {
        LocalDate releaseDate = LocalDate.parse(rs.getString("release_date"), DATE_FORMATTER);
        Film film = new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                releaseDate,
                rs.getInt("duration"));
        film.setMpa(new Rating(rs.getInt("rating_id"), rs.getString("rating_name")));
        film.setGenres(getGenresForFilm(film.getId()));
        return film;
    }

    private List<Genre> getGenresForFilm(Integer filmId) {
        return jdbcTemplate.query(GET_FILM_GENRES, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
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
        return film;
    }

    @Override
    public Film remove(Film film) {
        if (jdbcTemplate.update(DELETE_FILM_QUERY, film.getId()) > 0)
            return film;
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
            for (Genre genre : film.getGenres())
                jdbcTemplate.update(INSERT_FILM_GENRES, film.getId(), genre.getId());
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
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        return jdbcTemplate.query(GET_COMMON_FILMS, (rs, rowNum) -> makeFilmFromComplexTable(rs), userId, friendId);
    }
}
