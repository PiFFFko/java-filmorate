package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new HashMap<>();
    private Integer idGenerator = 1;

    @Override
    public User get(Integer id) {
        log.info("Запрос на получение пользователя c id {}", id);
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new EntityNotExistException(String.format("Пользователя с id %s не существует", id));
    }

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
            log.info("Пользователь с id {} был обновлен: {}", user.getId(), user);
            return user;
        } else {
            log.info("Пользователя с id {} не существует. ", user.getId());
            throw new EntityNotExistException(String.format("Пользователя с id %s не существует. ", user.getId()));
        }
    }

    @Override
    public Boolean contains(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public Collection<User> getAll() {
        log.info("Запрошен список всех пользователей");
        return users.values();
    }

    public User addFriend(Integer id, Integer friendId) {
        if (users.containsKey(id) && users.containsKey(friendId)) {
            Set<Integer> friends = users.get(id).getFriends();
            friends.add(friendId);
            users.get(id).setFriends(friends);
            log.info("Пользователю с id {} добавлен друг: {}", id, users.get(friendId));
            friends = users.get(friendId).getFriends();
            friends.add(id);
            users.get(friendId).setFriends(friends);
            log.info("Пользователю с id {} добавлен друг: {}", friendId, users.get(id));
            return users.get(id);
        }
        throw new EntityNotExistException(String.format("Пользователя с id {} или {} не существует", id, friendId));
    }


    public User deleteFriend(Integer id, Integer friendId) {
        if (users.containsKey(id) && users.containsKey(friendId)) {
            Set<Integer> friends = users.get(id).getFriends();
            friends.remove(friendId);
            users.get(id).setFriends(friends);
            log.info("У пользователя с id {} удален друг: {}", id, users.get(friendId));
            friends = users.get(friendId).getFriends();
            friends.remove(id);
            users.get(friendId).setFriends(friends);
            log.info("У пользователя с id {} удален друг: {}", id, users.get(friendId));
            return users.get(id);
        }
        throw new EntityNotExistException(String.format("Пользователя с id {} или {} не существует", id, friendId));

    }

    @Override
    public Collection<User> getFriends(Integer id) {
        if (users.containsKey(id)) {
            log.info("Запрошен список друзей пользователя {} ", users.get(id));
            Set<Integer> friends = users.get(id).getFriends();
            return friends.stream().map(i -> users.get(i)).collect(Collectors.toList());
        }
        throw new EntityNotExistException(String.format("Пользователя с id %s не существует. ", id));
    }

    @Override
    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        if (users.containsKey(id) && users.containsKey(otherId)) {
            log.info("Запрошен список общих друзей для пользователей {} и {} ", users.get(id), users.get(otherId));
            Set<Integer> friends = users.get(id).getFriends();
            Set<Integer> otherFriends = users.get(otherId).getFriends();
            Set<Integer> intersection = new HashSet<>(friends);
            intersection.retainAll(otherFriends);
            return intersection.stream().map(i -> users.get(i)).collect(Collectors.toList());
        }
        throw new EntityNotExistException(String.format("Пользователя с id {} или {} не существует", id, otherId));
    }

}
