package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
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

    public User addFriend(Integer id, Integer friendId){
        return userStorage.addFriend(id, friendId);
    }

    @Override
    public User deleteFriend(Integer id, Integer friendId) {
        return userStorage.deleteFriend(id, friendId);
    }

    @Override
    public Collection<User> getFriends(Integer id) {
        return userStorage.getFriends(id);
    }

    @Override
    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

}
