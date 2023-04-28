package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public Collection<Director> getAllDirectors() {
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public Director getDirector(@PathVariable Integer id) {
        return directorService.get(id);
    }

    @PostMapping
    public Director createDirector(@RequestBody @Valid Director director) {
        return directorService.create(director);
    }

    @PutMapping
    public Director updateDirector(@RequestBody @Valid Director director) {
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public Director deleteDirector(@PathVariable Integer id) {
        return directorService.delete(id);
    }
}
