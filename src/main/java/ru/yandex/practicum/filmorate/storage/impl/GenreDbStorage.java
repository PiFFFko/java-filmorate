package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Qualifier(value = "genreDbStorage")
public class GenreDbStorage implements GenreStorage {

    private static final String GET_GENRE_QUERY = "select * from genres where genre_id = ?";
    private static final String INSERT_GENRE_QUERY = "insert into genres (genre_name) values (?)";
    private static final String DELETE_GENRE_QUERY = "delete from genres where genre_id = ?";
    private static final String UPDATE_GENRE_QUERY = "update genres set genre_name = ? where genre_id = ?";
    private static final String GET_ALL_GENRES_QUERY = "select * from genres";
    private static final String GENRE_NOT_EXIST_MESSAGE = "Жанра с id %s не существует";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre get(Integer id) {
        List<Genre> genres = jdbcTemplate.query(GET_GENRE_QUERY, (rs, rowNum) -> makeGenre(rs), id);
        if (!genres.isEmpty())
            return genres.stream().findFirst().get();
        throw new EntityNotExistException(String.format(GENRE_NOT_EXIST_MESSAGE, id));
    }

    @Override
    public Genre add(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_GENRE_QUERY, new String[]{"genre_id"});
            statement.setString(1, genre.getName());
            return statement;
        }, keyHolder);
        genre.setId(keyHolder.getKey().intValue());
        return genre;
    }

    @Override
    public Genre remove(Genre genre) {
        if (jdbcTemplate.update(DELETE_GENRE_QUERY, genre.getId()) > 0)
            return genre;
        throw new EntityNotExistException(String.format(GENRE_NOT_EXIST_MESSAGE, genre.getId()));
    }

    @Override
    public Genre update(Genre genre) {
        if (jdbcTemplate.update(UPDATE_GENRE_QUERY,
                genre.getName(),
                genre.getId()) > 0)
            return genre;
        throw new EntityNotExistException(String.format(GENRE_NOT_EXIST_MESSAGE, genre.getId()));
    }

    @Override
    public Boolean contains(Integer id) {
        return null;
    }

    @Override
    public Collection<Genre> getAll() {
        return jdbcTemplate.query(GET_ALL_GENRES_QUERY, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
    }

}
