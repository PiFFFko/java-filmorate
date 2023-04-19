package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;
    @Autowired
    @Qualifier("likeDbStorage")
    private LikeStorage likeStorage;

    public FilmServiceImpl(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film get(Integer id) {
        return filmStorage.get(id);
    }

    @Override
    public Film create(Film film) {
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        return filmStorage.update(film);
    }

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        return filmStorage.getPopular(count);
    }
}
