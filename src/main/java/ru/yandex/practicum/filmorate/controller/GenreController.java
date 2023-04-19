package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public Collection<Genre> getAllGenres() {
        return genreService.getAll();
    }

    @PostMapping
    private Genre createGenre(@RequestBody Genre genre) {
        return genreService.create(genre);
    }

    @PutMapping
    private Genre updateGenre(@RequestBody Genre genre) {
        return genreService.update(genre);
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable Integer id) {
        return genreService.get(id);
    }

}
