package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Grade;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    /**
     * API :
     * <p>
     * POST /reviews - Добавление нового отзыва.
     * PUT /reviews - Редактирование уже имеющегося отзыва.
     * DELETE /reviews/{id} - Удаление уже имеющегося отзыва.
     * GET /reviews/{id} - Получение отзыва по идентификатору.
     * GET /reviews?filmId={filmId}&count={count} - Получение всех отзывов по идентификатору фильма, если фильм не указан, то все. Если кол-во не указано то 10.
     * <p>
     * PUT /reviews/{id}/like/{userId}`  — пользователь ставит лайк отзыву.
     * PUT /reviews/{id}/dislike/{userId}`  — пользователь ставит дизлайк отзыву.
     * DELETE /reviews/{id}/like/{userId}`  — пользователь удаляет лайк отзыву.
     * DELETE /reviews/{id}/dislike/{userId}`  — пользователь удаляет дизлайк отзыву.
     */
    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Integer id) {
        return reviewService.get(id);
    }

    @GetMapping()
    public Collection<Review> getReviewsFromFilm(@RequestParam(name = "filmId", required = false, defaultValue = "-1") Integer filmId,
                                                 @RequestParam(name = "count", required = false, defaultValue = "10") Integer count
    ) {
        return reviewService.getReviewsFromFilm(filmId, count);
    }

    @PostMapping()
    public Review addReview(@RequestBody @Valid Review review) {
        return reviewService.create(review);
    }

    @PutMapping()
    public Review updateExistReview(@RequestBody @Valid Review review) {
        return reviewService.update(review);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Integer addDislikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.addUserGrade(id, userId, Grade.DISLIKE);
    }

    @PutMapping("/{id}/like/{userId}")
    public Integer addLikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.addUserGrade(id, userId, Grade.LIKE);
    }

    @DeleteMapping("/{id}")
    public Integer deleteReview(@PathVariable Integer id) {
        return reviewService.deleteReview(id);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public Integer deleteDislikeFromReview(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.deleteUserGrade(id, userId, Grade.DISLIKE);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Integer deleteLikeFromReview(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.deleteUserGrade(id, userId, Grade.LIKE);
    }
}
