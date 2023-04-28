package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String GET_DIRECTOR_QUERY = "SELECT * FROM DIRECTORS WHERE ID = ?";
    private static final String GET_ALL_DIRECTORS_QUERY = "SELECT * FROM DIRECTORS";
    private static final String INSERT_DIRECTOR_QUERY = "INSERT INTO DIRECTORS(NAME) VALUES (?)";
    private static final String UPDATE_DIRECTOR_QUERY = "UPDATE DIRECTORS SET NAME = ?";
    private static final String DELETE_DIRECTOR_QUERY = "DELETE FROM DIRECTORS WHERE ID = ?";
    private static final String DELETE_FILM_DIRECTOR_QUERY = "DELETE FROM FILM_DIRECTOR WHERE DIRECTOR_ID = ?";

    private static final String DIRECTOR_NOT_EXIST_MESSAGE = "Режиссера с id %s не существует";

    @Override
    public Director get(Integer id) {
        List<Director> directors = jdbcTemplate.query(GET_DIRECTOR_QUERY, (rs, rowNum) -> makeDirectorFromComplexTable(rs), id);
        if (!directors.isEmpty()) {
            return directors.stream().findFirst().get();
        }
        throw new EntityNotExistException(String.format(DIRECTOR_NOT_EXIST_MESSAGE, id));
    }

    @Override
    public Director create(Director director) {
        jdbcTemplate.update(INSERT_DIRECTOR_QUERY,
                director.getName());
        SqlRowSet directorRow = jdbcTemplate.queryForRowSet("SELECT * FROM DIRECTORS WHERE NAME = ?", director.getName());
        if (directorRow.next()) {
            return get(directorRow.getInt("id"));
        }
        return director;
    }

    @Override
    public Director update(Director director) {
        jdbcTemplate.update(UPDATE_DIRECTOR_QUERY, director.getName());
        SqlRowSet directorRow = jdbcTemplate.queryForRowSet("SELECT * FROM DIRECTORS WHERE ID = ?", director.getId());
        if (directorRow.next()) {
            return director;
        } else {
            throw new EntityNotExistException(String.format(DIRECTOR_NOT_EXIST_MESSAGE, director.getId()));
        }
    }

    @Override
    public Collection<Director> getAll() {
        return jdbcTemplate.query(GET_ALL_DIRECTORS_QUERY, (rs, rowNum) -> makeDirectorFromComplexTable(rs));
    }

    @Override
    public Director delete(Integer id) {
        Director director = get(id);
        jdbcTemplate.update(DELETE_FILM_DIRECTOR_QUERY, id);
        jdbcTemplate.update(DELETE_DIRECTOR_QUERY, id);
        return director;
    }

    private Director makeDirectorFromComplexTable(ResultSet rs) throws SQLException {
        return new Director(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
