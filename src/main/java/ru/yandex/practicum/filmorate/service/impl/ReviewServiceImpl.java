package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Grade;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewStorage reviewStorage;
    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;
    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    @Override
    public Review get(Integer id) {
        return reviewStorage.get(id).orElseThrow(() -> {
            throw new EntityNotExistException("Отзыв с id = " + id + " не найден");
        });
    }

    @Override
    public Review create(Review review) {
        if (reviewStorage.containsValue(review)) {
            throw new ObjectAlreadyExistException("Отзыв: " + review + " уже существует");
        } else if (filmStorage.get(review.getFilmId()) == null) {
            throw new EntityNotExistException("filmId Not Found");
        } else if (userStorage.get(review.getUserId()) == null) {
            throw new EntityNotExistException("userId Not Found");
        } else {
            review.setUseful(0);
            return reviewStorage.add(review);
        }
    }

    @Override
    public Review update(Review review) {
        if (filmStorage.get(review.getFilmId()) == null) {
            throw new EntityNotExistException("filmId Not Found");
        } else if (userStorage.get(review.getUserId()) == null) {
            throw new EntityNotExistException("userId Not Found");
        } else if (reviewStorage.containsKey(review.getReviewId())) {
            return reviewStorage.update(review);
        } else {
            throw new EntityNotExistException(review + " не существует. Воспользуйтесь методом add");
        }
    }

    @Override
    public Collection<Review> getAll() {
        return reviewStorage.getAll();
    }

    @Override
    public Collection<Review> getReviewsFromFilm(Integer filmId, Integer count) {
        if (filmId == -1) {
            return getAll();
        }
        return reviewStorage.getReviewsFromFilm(filmId, count);
    }

    @Override
    public Integer deleteReview(Integer id) {
        return reviewStorage.remove(id);
    }

    @Override
    public Integer addUserGrade(Integer reviewId, Integer userId, Grade grade) {
        if (reviewStorage.containsKey(reviewId)) {
            reviewStorage.addUserGrade(reviewId, userId, grade);
            return reviewId;
        }
        throw new EntityNotExistException("Review c reviewId = " + reviewId + " Not Found");
    }

    @Override
    public Integer deleteUserGrade(Integer reviewId, Integer userId, Grade grade) {
        reviewStorage.deleteUserGrade(reviewId, userId, grade);
        return reviewId;
    }
}
