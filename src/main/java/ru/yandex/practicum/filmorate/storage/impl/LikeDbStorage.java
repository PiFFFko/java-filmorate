package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
@Qualifier(value = "likeDbStorage")
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private static final String GET_LIKES_FOR_FILM_QUERY = "select * from likes where film_id = ? and user_id = ?";
    private static final String INSERT_LIKE_QUERY = "insert into likes(film_id, user_id) values(?, ?)";
    private static final String DELETE_LIKE_QUERY = "delete from likes where film_id = ? and user_id = ?";
    private static final String GET_ALL_LIKES_QUERY = "select * from likes where film_id = ?";
    private static final String LIKE_NOT_EXIST_MESSAGE = "Лайка от юзера %s на фильм %s не существует.";
    private static final String LIKE_OR_FILM_NOT_EXIST_MESSAGE = "Фильма %s не существует или его никто не лайкал.";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Like getLike(Integer filmId, Integer userId) {
        List<Like> likes = jdbcTemplate.query(GET_LIKES_FOR_FILM_QUERY, (rs, rowNum) -> makeLike(rs), filmId, userId);
        if (!likes.isEmpty())
            return likes.stream().findFirst().get();
        throw new EntityNotExistException(String.format(LIKE_NOT_EXIST_MESSAGE, filmId, userId));
    }



    @Override
    public Like removeLike(Like like) {
        if (jdbcTemplate.update(DELETE_LIKE_QUERY, like.getFilmId(), like.getUserId()) > 0)
            return like;
        throw new EntityNotExistException(String.format(LIKE_NOT_EXIST_MESSAGE, like.getUserId(), like.getFilmId()));
    }

    @Override
    public Collection<Like> getAllLikesForFilm(Integer filmId) {
        List<Like> likes = jdbcTemplate.query(GET_ALL_LIKES_QUERY, (rs, rowNum) -> makeLike(rs), filmId);
        if (!likes.isEmpty())
            return likes;
        throw new EntityNotExistException(String.format(LIKE_OR_FILM_NOT_EXIST_MESSAGE, filmId));
    }

    @Override
        public Like likeFilm(Integer filmId, Integer userId) {
            jdbcTemplate.update(INSERT_LIKE_QUERY, filmId, userId);
        return new Like(filmId, userId);
    }

    @Override
    public Like deleteLike(Integer filmId, Integer userId) {
        if (jdbcTemplate.update(DELETE_LIKE_QUERY, filmId, userId) > 0)
            return new Like(filmId, userId);
        throw new EntityNotExistException(String.format(LIKE_NOT_EXIST_MESSAGE, filmId, userId));
    }

    private Like makeLike(ResultSet rs) throws SQLException {
        return new Like(rs.getInt("film_id"),rs.getInt("user_id"));
    }

}
