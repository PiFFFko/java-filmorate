package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User get(Integer id) {
        return userStorage.get(id);
    }

    @Override
    public User create(User user) {
        user.setName(Objects.isNull(user.getName()) ? user.getLogin() : user.getName());
        user.setName(user.getName().isBlank() ? user.getLogin() : user.getName());
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

    @Override
    public Collection<User> getFriends(Integer id) {
        return userStorage.getFriends(id);
    }

    @Override
    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

    @Override
    public User remove(Integer id) {
        return userStorage.remove(get(id));
    }
}
