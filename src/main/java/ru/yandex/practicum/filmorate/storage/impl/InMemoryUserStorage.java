package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new HashMap<>();
    private Integer idGenerator = 1;

    @Override
    public User add(User user) {
        user.setId(idGenerator++);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {} ", user);
        return user;
    }

    @Override
    public User remove(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь с id {} был обновлен: ", user.getId(), user);
            return user;
        } else {
            log.info("Пользователя с id {} не существует. ", user.getId());
            throw new UserNotExistException(String.format("Пользователя с id {} не существует. ", user.getId()));
        }
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }
}
