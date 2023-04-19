package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendRequest {
    private int userIdFrom;
    private int userIdTo;
    private boolean friendStatus;

}
