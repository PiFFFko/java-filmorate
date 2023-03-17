package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface BaseStorage<T> {

    T add(T t);
    T remove(T t);
    T update(T t);
    Collection<T> getAll();

}
