package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpa")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public Collection<Rating> getAllRating() {
        return ratingService.getAll();
    }

    @PostMapping
    private Rating createRating(@RequestBody Rating genre) {
        return ratingService.create(genre);
    }

    @PutMapping
    private Rating updateRating(@RequestBody Rating genre) {
        return ratingService.update(genre);
    }

    @GetMapping("/{id}")
    public Rating getRating(@PathVariable Integer id) {
        return ratingService.get(id);
    }

}
