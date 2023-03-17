package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage extends BaseStorage<Film> {
    Film likeFilm(Integer id, Integer userId);
    Film deleteLike(Integer id, Integer userId);
    Collection<Film> getPopular(Integer count);

}
