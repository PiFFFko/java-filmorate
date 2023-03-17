package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService extends BaseService<Film>{

    Film likeFilm(Integer id, Integer userId);
    Film deleteLike(Integer id, Integer userId);
    Collection<Film> getPopular(Integer count);

}
