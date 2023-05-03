package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeStorage likeStorage;
    private final FeedStorage feedStorage;

    @Override
    public Like likeFilm(Integer filmId, Integer userId) {
        Like result = likeStorage.likeFilm(filmId, userId);
        feedStorage.add(Feed.builder()
                .userId(userId)
                .eventType(EventType.LIKE)
                .operation(Operation.ADD)
                .entityId(filmId)
                .build());
        return result;
    }

    @Override
    public Like deleteLike(Integer filmId, Integer userId) {
        Like result = likeStorage.deleteLike(filmId, userId);
        feedStorage.add(Feed.builder()
                .userId(userId)
                .eventType(EventType.LIKE)
                .operation(Operation.REMOVE)
                .entityId(filmId)
                .build());
        return result;
    }
}
