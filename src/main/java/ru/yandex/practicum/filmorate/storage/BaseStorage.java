package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface BaseStorage<T> {
    T get(Integer id);

    T add(T t);

    T remove(T t);

    T update(T t);

    Boolean contains(Integer id);

    Collection<T> getAll();
}
