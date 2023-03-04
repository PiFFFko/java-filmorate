package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private Map<Integer, User> users = new HashMap<>();
    private Integer idGenerator = 1;

    @Override
    public User create(User user) {
        user.setId(idGenerator++);
        user.setName(Objects.isNull(user.getName()) ? user.getLogin() : user.getName());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {} ", user);
        return user;
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
