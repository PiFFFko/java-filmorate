package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Qualifier(value = "ratingDbStorage")
public class RatingDbStorage implements RatingStorage {

    private static final String GET_RATING_QUERY = "select * from ratings where rating_id = ?";
    private static final String INSERT_RATING_QUERY = "insert into ratings(rating_name) values (?)";
    private static final String DELETE_RATING_QUERY = "delete from ratings where rating_id = ?";
    private static final String UPDATE_RATING_QUERY = "update ratings set rating_name = ? where rating_id =?";
    private static final String GET_ALL_RATINGS_QUERY = "select * from ratings";
    private static final String RATING_NOT_EXIST_MESSAGE = "Рейтинга с id %s не существует";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Rating get(Integer id) {
        List<Rating> ratings = jdbcTemplate.query(GET_RATING_QUERY, (rs, rowNum) -> makeRating(rs), id);
        if (!ratings.isEmpty())
            return ratings.stream().findFirst().get();
        throw new EntityNotExistException(String.format(RATING_NOT_EXIST_MESSAGE, id));
    }

    @Override
    public Rating add(Rating rating) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_RATING_QUERY, new String[]{"rating_id"});
            statement.setString(1, rating.getName());
            return statement;
        }, keyHolder);
        rating.setId(keyHolder.getKey().intValue());
        return rating;
    }

    @Override
    public Rating remove(Rating rating) {
        if (jdbcTemplate.update(DELETE_RATING_QUERY, rating.getId()) > 0)
            return rating;
        throw new EntityNotExistException(String.format(RATING_NOT_EXIST_MESSAGE, rating.getId()));
    }

    @Override
    public Rating update(Rating rating) {
        if (jdbcTemplate.update(UPDATE_RATING_QUERY,
                rating.getName(),
                rating.getId()) > 0)
            return rating;
        throw new EntityNotExistException(String.format(RATING_NOT_EXIST_MESSAGE, rating.getId()));
    }

    @Override
    public Boolean contains(Integer id) {
        return null;
    }

    @Override
    public Collection<Rating> getAll() {
        return jdbcTemplate.query(GET_ALL_RATINGS_QUERY, (rs, rowNum) -> makeRating(rs));
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        return new Rating(rs.getInt("rating_id"), rs.getString("rating_name"));
    }

}
