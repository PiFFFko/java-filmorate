package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

public interface DirectorService extends BaseService<Director> {
    Director delete(Integer id);
}
