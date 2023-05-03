package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final UserStorage userStorage;
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idGenerator = 1;

    @Override
    public Film get(Integer id) {
        log.info("Запрос на получение фильма c id {}", id);
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new EntityNotExistException(String.format("Фильма с id %s не существует", id));
    }

    @Override
    public Film add(Film film) {
        film.setId(idGenerator++);
        films.put(film.getId(), film);
        log.info("Фильм был добавлен. :{}", film);
        return film;
    }

    @Override
    public Film remove(Film film) {
        return films.remove(film.getId());
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм с id {} был обновлен {}", film.getId(), film);
            return film;
        } else {
            log.error("Фильма с id {} не существует. ", film.getId());
            throw new EntityNotExistException(String.format("Фильма с id %s не существует. ", film.getId()));
        }
    }

    @Override
    public Boolean contains(Integer id) {
        return films.containsKey(id);
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    public Film likeFilm(Integer id, Integer userId) {
        if (films.containsKey(id) && userStorage.contains(userId)) {
            Set<Integer> likes = films.get(id).getLikesFromUsers();
            likes.add(userId);
            films.get(id).setLikesFromUsers(likes);
            return films.get(id);
        }
        throw new EntityNotExistException(String.format("Пользователя с id %s или фильма с id %s не существует", userId, id));
    }

    public Film deleteLike(Integer id, Integer userId) {
        if (films.containsKey(id) && userStorage.contains(userId)) {
            Set<Integer> likes = films.get(id).getLikesFromUsers();
            likes.remove(userId);
            films.get(id).setLikesFromUsers(likes);
            return films.get(id);
        }
        throw new EntityNotExistException(String.format("Пользователя с id %s или фильма с id %s не существует", userId, id));
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        return films.values().stream()
                .sorted(Comparator.comparing(Film::getPopularity).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Film> getDirectorsFilms(Integer directorId, String sortBy) {
        return null;
    }

    @Override
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        return null;
    }

    @Override
    public Collection<Film> searchFilms(String query, String by) {
        return null;
    }

    @Override
    public Collection<Film> getPopularByGenreAndYear(int count, int genreId, int year) {
        return null;
    }
}
