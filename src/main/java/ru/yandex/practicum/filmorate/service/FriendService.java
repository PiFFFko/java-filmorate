package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.FriendRequest;

public interface FriendService {
    FriendRequest addFriend(Integer userIdFrom, Integer friendId);

    FriendRequest deleteFriend(Integer userIdFrom, Integer userIdTo);
}
