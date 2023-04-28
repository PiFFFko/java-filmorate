package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.RatingDbStorage;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RatingDbTest {

    private final RatingDbStorage ratingDbStorage;
    private Mpa testRating;

    @Test
    void getRatingById() {
        testRating = ratingDbStorage.get(1);
        Assertions.assertThat(testRating).hasFieldOrPropertyWithValue("id", 1);
        Assertions.assertThat(testRating).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    void removeRating() {
        testRating = ratingDbStorage.get(1);
        ratingDbStorage.remove(testRating);
        Assertions.assertThatThrownBy(() -> ratingDbStorage.get(1)).isInstanceOf(EntityNotExistException.class);
    }

    @Test
    void updateRating() {
        Mpa updateRating = new Mpa(1, "X");
        ratingDbStorage.update(updateRating);
        testRating = ratingDbStorage.get(1);
        Assertions.assertThat(testRating).hasFieldOrPropertyWithValue("id", 1);
        Assertions.assertThat(testRating).hasFieldOrPropertyWithValue("name", "X");

    }

}
