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
}
