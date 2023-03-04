package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    private Map<Integer, Film> films = new HashMap<>();
    private Integer idGenerator = 1;

    @Override
    public Film create(Film film) {
        film.setId(idGenerator++);
        films.put(film.getId(), film);
        log.error("Фильм был добавлен. :", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм с id {} был обновлен {}", film.getId(), film);
            return film;
        } else {
            log.error("Фильма с id {} не существует. ", film.getId());
            throw new FilmNotExistException(String.format("Фильма с id %s не существует. ", film.getId()));
        }
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }
}
