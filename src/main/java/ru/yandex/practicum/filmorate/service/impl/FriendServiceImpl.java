package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FriendRequest;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendStorage friendStorage;
    private final FeedStorage feedStorage;

    @Override
    public FriendRequest addFriend(Integer userIdFrom, Integer userIdTo) {
        FriendRequest result = friendStorage.sendFriendRequest(userIdFrom, userIdTo);
        feedStorage.add(Feed.builder()
                .userId(userIdFrom)
                .eventType(EventType.FRIEND)
                .operation(Operation.ADD)
                .entityId(userIdTo)
                .build());
        return result;
    }

    @Override
    public FriendRequest deleteFriend(Integer userIdFrom, Integer userIdTo) {
        FriendRequest result = friendStorage.deleteFriendRequest(userIdFrom, userIdTo);
        feedStorage.add(Feed.builder()
                .userId(userIdFrom)
                .eventType(EventType.FRIEND)
                .operation(Operation.REMOVE)
                .entityId(userIdTo)
                .build());
        return result;
    }
}
