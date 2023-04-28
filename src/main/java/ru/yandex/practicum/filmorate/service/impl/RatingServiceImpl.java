package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    @Autowired
    private final RatingStorage ratingStorage;

    @Override
    public Mpa get(Integer id) {
        return ratingStorage.get(id);
    }

    @Override
    public Mpa create(Mpa rating) {
        return ratingStorage.add(rating);
    }

    @Override
    public Mpa update(Mpa rating) {
        return ratingStorage.update(rating);
    }

    @Override
    public Collection<Mpa> getAll() {
        return ratingStorage.getAll();
    }
}
