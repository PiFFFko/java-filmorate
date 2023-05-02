package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.RecommendationService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final FilmService filmService;
    private final UserService userService;
    private final LikeStorage likeStorage;
    private final Map<Film, Map<Film, Double>> diff = new HashMap<>();
    private final Map<Film, Map<Film, Integer>> freq = new HashMap<>();
    private final Map<User, HashMap<Film, Double>> inputData = new HashMap<>();

    @Override
    public void buildDifferencesMatrix() {
        diff.clear();
        freq.clear();
        inputData.clear();
        for (Like like : likeStorage.getAllLikes()) {
            User user = userService.get(like.getUserId());
            HashMap<Film, Double> likes = inputData.getOrDefault(user, new HashMap<>());
            likes.put(filmService.get(like.getFilmId()), 1.);
            inputData.put(user, likes);
        }

        for (HashMap<Film, Double> user : inputData.values()) {
            for (Entry<Film, Double> userEntry : user.entrySet()) {
                if (!diff.containsKey(userEntry.getKey())) {
                    diff.put(userEntry.getKey(), new HashMap<>());
                    freq.put(userEntry.getKey(), new HashMap<>());
                }
                for (Entry<Film, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(userEntry.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(userEntry.getKey()).get(e2.getKey());
                    }
                    double oldDiff = 0.0;
                    if (diff.get(userEntry.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(userEntry.getKey()).get(e2.getKey());
                    }
                    double observedDiff = userEntry.getValue() - e2.getValue();
                    freq.get(userEntry.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(userEntry.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (Film j : diff.keySet()) {
            for (Film i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i);
                int count = freq.get(j).get(i);
                diff.get(j).put(i, oldValue / count);
            }
        }
    }

    @Override
    public Collection<Film> getRecommendation(int userId) {
        User user = userService.get(userId);
        HashMap<Film, Double> userLikes = inputData.getOrDefault(user, new HashMap<>());

        HashMap<Film, Double> uPred = new HashMap<>();
        HashMap<Film, Integer> uFreq = new HashMap<>();
        for (Film j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        for (Film j : userLikes.keySet()) {
            for (Film k : diff.keySet()) {
                try {
                    double predictedValue = diff.get(k).get(j) + userLikes.get(j);
                    double finalValue = predictedValue * freq.get(k).get(j);
                    uPred.put(k, uPred.get(k) + finalValue);
                    uFreq.put(k, uFreq.get(k) + freq.get(k).get(j));
                } catch (NullPointerException e1) {
                    // This is expected
                }
            }
        }
        HashMap<Film, Double> result = new HashMap<>();
        for (Film j : uPred.keySet()) {
            if (!userLikes.containsKey(j) && uFreq.get(j) > 0) {
                result.put(j, uPred.get(j) / uFreq.get(j));
            }
        }
        return result.keySet();
    }
}
