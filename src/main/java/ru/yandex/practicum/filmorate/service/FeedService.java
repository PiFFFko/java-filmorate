package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.Collection;

public interface FeedService {
    Collection<Feed> getFeedByUser(int id);

    Feed create(Feed feed);
}
