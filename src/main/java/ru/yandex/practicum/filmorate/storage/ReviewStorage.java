package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Grade;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {
    Optional<Review> get(Integer id);
    Review add(Review review);
    Review update(Review review);
    Integer remove(Integer id);
    boolean containsKey(Integer id);
    boolean containsValue(Review review);
    Collection<Review> getAll();
    Collection<Review> getReviewsFromFilm(Integer filmId, Integer count);
    boolean addUserGrade(Integer reviewId, Integer userId, Grade grade);
    boolean deleteUserGrade(Integer reviewId, Integer userId, Grade grade);


}

