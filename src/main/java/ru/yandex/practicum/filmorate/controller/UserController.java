package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userService.update(user);
    }

}
