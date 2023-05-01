package ru.yandex.practicum.filmorate.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {

    @Qualifier("filmDbStorage")
    @NonNull
    private final FilmStorage filmStorage;

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
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }

    @Override
    public Collection<Film> getDirectorsFilms(Integer directorId, String sortBy) {
        return filmStorage.getDirectorsFilms(directorId, sortBy);
    }

    @Override
    public Film remove(Integer id) {
        return filmStorage.remove(get(id));
    }

    public Collection<Film> getTop(int count, int genreId, int year) {
        if (genreId == 0 && year == 0) {
            return filmStorage.get().stream()
                    .sorted(this::compare)
                    .limit(count)
                    .collect(Collectors.toList());
        }
        return filmStorage.getPopularByGenreAndYear(count, genreId, year);
    }

    private int compare(Film f0, Film f1) {
        return -1 * (f0.getLikesFromUsers().size() - f1.getLikesFromUsers().size()); //обратный порядок
    }

}
