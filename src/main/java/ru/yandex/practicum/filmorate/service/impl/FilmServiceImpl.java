package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    @Autowired
    private FilmStorage filmStorage;

    public FilmServiceImpl(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film get(Integer id) {
        return filmStorage.get(id);
    }

    @Override
    public Film create(Film film) {
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film likeFilm(Integer id, Integer userId) {
        return filmStorage.likeFilm(id, userId);
    }

    @Override
    public Film deleteLike(Integer id, Integer userId) {
        return filmStorage.deleteLike(id, userId);
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        return filmStorage.getPopular(count);
    }
}
