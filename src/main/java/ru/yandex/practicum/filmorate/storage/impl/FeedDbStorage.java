package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

@Primary
@Component
@RequiredArgsConstructor
public class FeedDbStorage implements FeedStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    private static final String INSERT_FEED_QUERY = "insert into FEED(timestamp, user_id, eventType, operation, " +
            "entityId) " +
            "values(?,?,?,?,?)";

    private static final String GET_FEED_BY_USER_QUERY = "select * from FEED where user_id=?";

    @Override
    public Feed add(Feed feed) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_FEED_QUERY, new String[]{"id"});
            statement.setTimestamp(1, new Timestamp(feed.getTimestamp()));
            statement.setInt(2, feed.getUserId());
            statement.setInt(3, feed.getEventType().getId());
            statement.setInt(4, feed.getOperation().getId());
            statement.setInt(5, feed.getEntityId());
            return statement;
        }, keyHolder);
        return feed.toBuilder().id(keyHolder.getKey().intValue()).build();
    }

    @Override
    public Collection<Feed> getByUser(int id) {
        userStorage.get(id);
        return jdbcTemplate.query(GET_FEED_BY_USER_QUERY, (rs, rowNum) -> makeFeed(rs), id);
    }

    private Feed makeFeed(ResultSet rs) throws SQLException {
        return Feed.builder()
                .timestamp(rs.getTimestamp("timestamp").getTime())
                .userId(rs.getInt("user_id"))
                .eventType(EventType.getById(rs.getInt("eventType")))
                .operation(Operation.getById(rs.getInt("operation")))
                .id(rs.getInt("id"))
                .entityId(rs.getInt("entityId"))
                .build();
    }
}
