package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
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
    private final FeedStorage feedStorage;

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
            Review result = reviewStorage.add(review);
            feedStorage.add(Feed.builder()
                    .userId(review.getUserId())
                    .eventType(EventType.REVIEW)
                    .operation(Operation.ADD)
                    .entityId(review.getReviewId())
                    .build());
            return result;
        }
    }

    @Override
    public Review update(Review review) {
        if (filmStorage.get(review.getFilmId()) == null) {
            throw new EntityNotExistException("filmId Not Found");
        } else if (userStorage.get(review.getUserId()) == null) {
            throw new EntityNotExistException("userId Not Found");
        } else if (reviewStorage.containsKey(review.getReviewId())) {
            Review result = reviewStorage.update(review);
            feedStorage.add(Feed.builder()
                    .userId(result.getUserId())
                    .eventType(EventType.REVIEW)
                    .operation(Operation.UPDATE)
                    .entityId(result.getReviewId())
                    .build());
            return result;
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
        Review review = get(id);
        Integer result = reviewStorage.remove(id);
        feedStorage.add(Feed.builder()
                .userId(review.getUserId())
                .eventType(EventType.REVIEW)
                .operation(Operation.REMOVE)
                .entityId(review.getFilmId())
                .build());
        return result;
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
