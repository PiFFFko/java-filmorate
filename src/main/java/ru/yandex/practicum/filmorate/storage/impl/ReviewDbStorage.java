package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Grade;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.mappers.ReviewMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Review> get(Integer id) {
        String sqlQuery = "SELECT * " +
                "FROM REVIEWS " +
                "WHERE review_id = ?";
        return jdbcTemplate.query(sqlQuery, new ReviewMapper(), id).stream().findFirst();
    }

    @Override
    public Review add(Review review) {
        String sqlQuery = "INSERT INTO REVIEWS(content, is_positive, user_id, film_id, useful) " +
                "VALUES(?,?,?,?,?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, review.getContent());
            statement.setBoolean(2, review.getIsPositive());
            statement.setInt(3, review.getUserId());
            statement.setInt(4, review.getFilmId());
            statement.setInt(5, review.getUseful());
            return statement;
        }, kh);
        review.setReviewId((Integer) kh.getKey());
        return review;
    }

    @Override
    public Integer remove(Integer id) {
        String sqlQuery = "DELETE " +
                "FROM REVIEWS " +
                "WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery, id);
        return id;
    }

    @Override
    public Review update(Review review) {
        String sqlQuery = "UPDATE REVIEWS " +
                "SET content = ?, is_positive = ? " +
                "WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery, review.getContent(), review.getIsPositive(), review.getReviewId());
        return get(review.getReviewId()).get();
    }

    @Override
    public boolean containsKey(Integer id) {
        String sqlQuery = "SELECT * " +
                "FROM REVIEWS " +
                "WHERE review_id = ?";
        Optional<Review> reviewOptional = jdbcTemplate.query(sqlQuery, new ReviewMapper(), id).stream().findFirst();
        if (reviewOptional.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Review review) {
        String sqlQuery = "SELECT * " +
                "FROM REVIEWS " +
                "WHERE user_id = ? AND film_id = ? AND content = ? AND is_positive = ?";
        Optional<Review> reviewOptional = jdbcTemplate.query(sqlQuery, new ReviewMapper(), review.getUserId(), review.getFilmId(),
                review.getContent(), review.getIsPositive()).stream().findFirst();
        if (reviewOptional.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<Review> getAll() {
        String sqlQuery = "SELECT * " +
                "FROM REVIEWS";
        return jdbcTemplate.query(sqlQuery, new ReviewMapper())
                .stream()
                .sorted((r1, r2) -> {
                    if (r1.getUseful() > r2.getUseful()) return -1;
                    return 1;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Review> getReviewsFromFilm(Integer filmId, Integer count) {
        String sqlQuery = "SELECT * FROM REVIEWS " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sqlQuery, new ReviewMapper(), filmId)
                .stream()
                .sorted((r1, r2) -> {
                    if (r1.getUseful() > r2.getUseful()) return -1;
                    return 1;
                })
                .limit(count).collect(Collectors.toList());
    }

    @Override
    public boolean addUserGrade(Integer reviewId, Integer userId, Grade grade) {
        boolean booleanGrade = getGrade(grade);
        String sqlQuery = "INSERT INTO REVIEWS_GRADES(review_id, user_id, grade_type) " +
                "VALUES(?,?,?) "; /*+
                "ON CONFLICT (review_id OR user_id OR grade_type) DO UPDATE SET review_id = EXCLUDED.review_id, " +
                "user_id = EXCLUDED.user_id, grade_type = EXCLUDED.grade_type";*/
        jdbcTemplate.update(sqlQuery, reviewId, userId, booleanGrade);
        changeUseful(reviewId, booleanGrade, ChangeType.ADD);
        return true;
    }

    @Override
    public boolean deleteUserGrade(Integer reviewId, Integer userId, Grade grade) {
        boolean booleanGrade = getGrade(grade);
        String sqlQuery = "DELETE FROM REVIEWS_GRADES " +
                "WHERE review_id = ?, user_id = ?, grade_type = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId, booleanGrade);
        changeUseful(reviewId, booleanGrade, ChangeType.DELETE);
        return true;
    }

    private static boolean getGrade(Grade grade) {
        if (grade.name() == "LIKE") {
            return true;
        }
        return false;
    }

    private void changeUseful(Integer reviewId, boolean grade, ChangeType changeType) {
        if (grade == true) {
            if (changeType == ChangeType.ADD) {
                //Добавлен лайк, сл-но увеличиваем useful + 1
                increaseUseful(reviewId);
            } else {
                //Удален лайк, сл-но уменьшаем useful - 1
                decreaseUseful(reviewId);
            }
        } else {
            if (changeType == ChangeType.ADD) {
                //Добавлен дислайк, сл-но уменьшаем useful - 1
                decreaseUseful(reviewId);
            } else {
                //Убран дислайк, сл-но увеличиваем useful + 1
                increaseUseful(reviewId);
            }
        }
    }

    private void increaseUseful(Integer reviewId) {
        String sqlQuery = "UPDATE REVIEWS " +
                "SET useful = useful + 1 " +
                "WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery, reviewId);
    }

    private void decreaseUseful(Integer reviewId) {
        String sqlQuery = "UPDATE REVIEWS " +
                "SET useful = useful - 1 " +
                "WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery, reviewId);
    }

    private enum ChangeType {
        DELETE,
        ADD
    }
}