package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage extends BaseStorage<Film> {
    Collection<Film> getPopular(Integer count);

    Collection<Film> getCommonFilms(Integer userId, Integer friendId);

    Collection<Film> getDirectorsFilms(Integer directorId, String sortBy);
}
