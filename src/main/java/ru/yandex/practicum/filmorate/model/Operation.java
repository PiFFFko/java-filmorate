package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Operation {
    REMOVE(1, "REMOVE"),
    ADD(2, "ADD"),
    UPDATE(3, "UPDATE");
    private final int id;
    private final String name;

    public static Operation getById(int id) {
        for (Operation e : values()) {
            if (e.id == id) return e;
        }
        return null;
    }
}
