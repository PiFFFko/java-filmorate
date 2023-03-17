package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User create(User user) {
        user.setName(Objects.isNull(user.getName()) ? user.getLogin() : user.getName());
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public Collection<User> getAll() {
        return userStorage.getAll();
    }
}
