package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService extends BaseService<Film> {

    Collection<Film> getPopular(int count, int genreId, int year);



    Collection<Film> getDirectorsFilms(Integer directorId, String sortBy);

    Film remove(Integer id);
}
