package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventType {
    LIKE(1, "LIKE"),
    REVIEW(2, "REVIEW"),
    FRIEND(3, "FRIEND");
    private final int id;
    private final String name;

    public static EventType getById(int id) {
        for (EventType e : values()) {
            if (e.id == id) return e;
        }
        return null;
    }
}
