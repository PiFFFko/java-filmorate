package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer reviewId = rs.getInt("review_id");
        String content = rs.getString("content");
        Boolean isPositive = rs.getBoolean("is_positive");
        Integer userId = rs.getInt("user_id");
        Integer filmId = rs.getInt("film_id");
        Integer useful = rs.getInt("useful");
        return Review.builder()
                .reviewId(reviewId)
                .content(content)
                .isPositive(isPositive)
                .userId(userId)
                .filmId(filmId)
                .useful(useful)
                .build();
    }
}