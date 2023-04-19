package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Like;

public interface LikeService {
    Like likeFilm(Integer id, Integer userId);

    Like deleteLike(Integer id, Integer userId);
}
