package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService extends BaseService<Film> {
    Collection<Film> getPopular(Integer count);

    Film remove(Integer id);
}
