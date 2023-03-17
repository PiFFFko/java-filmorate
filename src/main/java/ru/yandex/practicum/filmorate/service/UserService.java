package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService extends BaseService<User> {
    User addFriend(Integer Id, Integer friendId);
    User deleteFriend(Integer id, Integer friendId);
    Collection<User> getFriends(Integer id);
    Collection<User> getCommonFriends(Integer id, Integer otherId);
}
