package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new HashMap<>();
    private Integer idGenerator = 1;
    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    @Override
    public Film get(Integer id) {
        log.info("Запрос на получение фильма c id {}", id);
        if (films.containsKey(id))
            return films.get(id);
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
        return null;
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
        throw new EntityNotExistException(
                String.format("Пользователя с id %s или фильма с id %s не существует",userId,id));
    }

    public Film deleteLike(Integer id, Integer userId) {
        if (films.containsKey(id) && userStorage.contains(userId)) {
            Set<Integer> likes = films.get(id).getLikesFromUsers();
            likes.remove(userId);
            films.get(id).setLikesFromUsers(likes);
            return films.get(id);
        }
        throw new EntityNotExistException(
                String.format("Пользователя с id %s или фильма с id %s не существует",userId,id));
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        return films.values().stream()
                .sorted(Comparator.comparing(Film::getPopularity).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return null;
    }

    @Override
    public Set<Genre> getFilmGenresFromDB(int filmId) {
        return null;
    }

    @Override
    public Film makeFilmFromComplexTable(ResultSet resultSet, int rowNum) throws SQLException {
        return null;
    }

    @Override
    public Collection<Film> getPopularByGenre(int genreId, int count) {
        return null;
    }

    @Override
    public Collection<Film> getPopularByYear(int year, int count) {
        return null;
    }

    @Override
    public Collection<Film> getPopularByGenreAndYear(int genreId, int year, int count) {
        return null;
    }
}
