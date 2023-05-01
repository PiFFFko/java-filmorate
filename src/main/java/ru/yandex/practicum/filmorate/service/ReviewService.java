package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Grade;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewService extends BaseService<Review> {

    Collection<Review> getReviewsFromFilm(Integer filmId, Integer count);

    Integer deleteReview(Integer id);

    Integer addUserGrade(Integer reviewId, Integer userId, Grade grade);

    Integer deleteUserGrade(Integer reviewId, Integer userId, Grade grade);
}
