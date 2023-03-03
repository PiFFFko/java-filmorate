package ru.yandex.practicum.filmorate.service;

import java.util.Collection;

public interface BaseService<T> {
    T create(T t);
    T update(T t);
    Collection<T> getAll();
}
