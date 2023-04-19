package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

public interface LikeStorage {
    Like getLike(Integer filmId, Integer userId);

    Like removeLike(Like like);

    Collection<Like> getAllLikesForFilm(Integer filmId);

    Like likeFilm(Integer filmId, Integer userId);

    Like deleteLike(Integer id, Integer userId);
}
