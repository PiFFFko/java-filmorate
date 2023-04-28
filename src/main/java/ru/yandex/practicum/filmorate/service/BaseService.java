package ru.yandex.practicum.filmorate.service;

import java.util.Collection;

public interface BaseService<T> {
    T get(Integer id);

    T create(T t);

    T update(T t);

    Collection<T> getAll();
}
