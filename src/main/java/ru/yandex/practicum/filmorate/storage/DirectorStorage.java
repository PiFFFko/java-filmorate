package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.BaseService;

public interface DirectorStorage extends BaseService<Director> {
    Director delete(Integer id);
}
