package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage extends BaseStorage<Film> {
   Collection<Film> getCommonFilms(Integer userId, Integer friendId);

    Collection<Film> get();

    Collection<Film> getDirectorsFilms(Integer directorId, String sortBy);

    Collection<Film> searchFilms(String query, String by);

    Collection<Film> getPopularByGenreAndYear(int count, int genreId, int year);
}
