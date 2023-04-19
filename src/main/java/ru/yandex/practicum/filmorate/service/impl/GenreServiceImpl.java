package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    @Autowired
    private final GenreStorage genreStorage;

    @Override
    public Genre get(Integer id) {
        return genreStorage.get(id);
    }

    @Override
    public Genre create(Genre genre) {
        return genreStorage.add(genre);
    }

    @Override
    public Genre update(Genre genre) {
        return genreStorage.update(genre);
    }

    @Override
    public Collection<Genre> getAll() {
        return genreStorage.getAll();
    }
}
