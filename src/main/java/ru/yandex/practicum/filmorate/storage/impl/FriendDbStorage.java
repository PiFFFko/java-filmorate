package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.FriendRequest;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

@Component
@RequiredArgsConstructor
@Qualifier(value = "friendDbStorage")
public class FriendDbStorage implements FriendStorage {
    private static final String SEND_FRIEND_REQUEST_QUERY = "insert into friend_requests(user_id_from, user_id_to) " +
            "values (?,?)";
    private static final String DELETE_FRIEND_REQUEST_QUERY = "delete from friend_requests where " +
            "user_id_from = ? and user_id_to = ?";
    private static final String NO_SUCH_FRIEND_REQUEST_MESSAGE = "Заявки в друзья между %s и %s не существует.";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public FriendRequest sendFriendRequest(Integer userIdFrom, Integer userIdTo) {
        if (jdbcTemplate.update(SEND_FRIEND_REQUEST_QUERY, userIdFrom, userIdTo) > 0)
            return new FriendRequest(userIdFrom, userIdTo, false);
        throw new EntityNotExistException("");
    }

    @Override
    public FriendRequest deleteFriendRequest(Integer userIdFrom, Integer userIdTo) {
        if (jdbcTemplate.update(DELETE_FRIEND_REQUEST_QUERY, userIdFrom, userIdTo) > 0)
            return new FriendRequest(userIdFrom, userIdTo, false);
        throw new EntityNotExistException(String.format(NO_SUCH_FRIEND_REQUEST_MESSAGE, userIdFrom, userIdTo));
    }
}
