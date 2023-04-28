package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpa")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public Collection<Mpa> getAllRating() {
        return ratingService.getAll();
    }

    @PostMapping
    private Mpa createRating(@RequestBody Mpa genre) {
        return ratingService.create(genre);
    }

    @PutMapping
    private Mpa updateRating(@RequestBody Mpa genre) {
        return ratingService.update(genre);
    }

    @GetMapping("/{id}")
    public Mpa getRating(@PathVariable Integer id) {
        return ratingService.get(id);
    }

}
