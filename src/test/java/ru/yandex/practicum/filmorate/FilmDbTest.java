package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;

import java.time.LocalDate;
import java.time.Month;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDbTest {

    private final FilmDbStorage filmDbStorage;
    private Film testFilm;

    private static Rating rating;

    @BeforeEach
    void addFilm() {
        rating = new Rating(1, null);
        testFilm = new Film(0, "name", "description",
                LocalDate.of(1895, 12, 28), 10, 0, rating, null, null, null);
        filmDbStorage.add(testFilm);
    }

    @Test
    void getFilmById() {
        testFilm = filmDbStorage.get(1);
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("id", 1);
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("name", "Die Hard");
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1988, Month.JULY, 12));
    }

    @Test
    void removeFilm() {
        testFilm.setId(1);
        filmDbStorage.remove(testFilm);
        Assertions.assertThatThrownBy(() -> filmDbStorage.get(1)).isInstanceOf(EntityNotExistException.class);
    }

    @Test
    void updateFilm() {
        rating = new Rating(1, null);
        Film updateFilm = testFilm = new Film(0, "nameo", "description",
                LocalDate.of(1895, 12, 28), 10, 0, rating, null, null, null);
        updateFilm.setId(1);
        updateFilm.setName("Terminator 2 Judgement Day");
        updateFilm.setReleaseDate(LocalDate.of(1999, Month.JULY, 1));
        updateFilm.setMpa(new Rating(4, "R"));
        filmDbStorage.update(updateFilm);
        testFilm = filmDbStorage.get(1);
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("id", 1);
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("name", "Terminator 2 Judgement Day");
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1999, Month.JULY, 1));

    }

}
