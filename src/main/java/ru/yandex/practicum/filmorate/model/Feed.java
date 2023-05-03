package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Feed {
    @Builder.Default
    long timestamp = System.currentTimeMillis();
    int userId;
    EventType eventType;
    Operation operation;
    @JsonProperty("eventId")
    int id;
    int entityId;
}
