package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Collection;
import java.util.Set;
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
        film.setGenres((Set<Genre>) film.getGenres().stream().distinct().collect(Collectors.toList()));
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {
        film.setGenres((Set<Genre>) film.getGenres().stream().distinct().collect(Collectors.toList()));
        return filmStorage.update(film);
    }

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Collection<Film> getPopular(int count, int genreId, int year) {
        if (genreId == 0 && year == 0) {
            return filmStorage.getPopular(count);
        } else if (genreId != 0 && year != 0) {
            return filmStorage.getPopularByGenreAndYear(genreId, year, count);
        } else if (genreId != 0) {
            return filmStorage.getPopularByGenre(genreId, count);
        } else {
            return filmStorage.getPopularByYear(year, count);
        }
    }

    @Override
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        return null;
    }

    @Override
    public Collection<Film> getDirectorsFilms(Integer directorId, String sortBy) {
        return filmStorage.getDirectorsFilms(directorId, sortBy);
    }

    @Override
    public Film remove(Integer id) {
        return filmStorage.remove(get(id));
    }


}
