package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/films")
public class FilmController {
    private final FilmService filmService;
    private final LikeService likeService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAll();
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmService.get(id);
    }

    @PutMapping("{id}/like/{userId}")
    public Like likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        return likeService.likeFilm(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Like deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return likeService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopular(count);
    }

    @GetMapping("/director/{directorId}")
    public Collection<Film> getDirectorsFilms(@PathVariable Integer directorId, @RequestParam String sortBy) {
        return filmService.getDirectorsFilms(directorId, sortBy);
    }
}
