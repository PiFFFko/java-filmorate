package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreDbTest {
    private final GenreDbStorage genreDbStorage;
    private Genre testGenre;

    @Test
    void getGenreById() {
        testGenre = genreDbStorage.get(1);
        Assertions.assertThat(testGenre).hasFieldOrPropertyWithValue("id", 1);
        Assertions.assertThat(testGenre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    void removeGenre() {
        testGenre = genreDbStorage.get(1);
        genreDbStorage.remove(testGenre);
        Assertions.assertThatThrownBy(() -> genreDbStorage.get(1)).isInstanceOf(EntityNotExistException.class);
    }

    @Test
    void updateGenre() {
        Genre updateGenre = new Genre(1, "Ужасы");
        genreDbStorage.update(updateGenre);
        testGenre = genreDbStorage.get(1);
        Assertions.assertThat(testGenre).hasFieldOrPropertyWithValue("id", 1);
        Assertions.assertThat(testGenre).hasFieldOrPropertyWithValue("name", "Ужасы");

    }

}
