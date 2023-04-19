package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FriendRequest;

public interface FriendStorage {
    FriendRequest sendFriendRequest(Integer userIdFrom, Integer userIdTo);

    FriendRequest deleteFriendRequest(Integer userIdFrom, Integer userIdTo);
}
