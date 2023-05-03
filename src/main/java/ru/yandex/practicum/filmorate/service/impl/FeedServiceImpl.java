package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements ru.yandex.practicum.filmorate.service.FeedService {
    private final FeedStorage feedStorage;

    @Override
    public Collection<Feed> getFeedByUser(int id) {
        return feedStorage.getByUser(id);
    }

    @Override
    public Feed create(Feed feed) {
        return feedStorage.add(feed);
    }
}
