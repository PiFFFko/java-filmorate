package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage extends BaseStorage<User> {

    User addFriend(Integer id, Integer friendId);
    User deleteFriend(Integer id, Integer friendId);
    Collection<User> getFriends(Integer id);
    Collection<User> getCommonFriends(Integer id, Integer otherId);
}
