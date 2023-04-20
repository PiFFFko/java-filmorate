package ru.yandex.practicum.filmorate;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.awt.image.FilteredImageSource;
import java.time.LocalDate;
import java.util.Set;

public class FilmValidationTest {
    private Validator validator;
    Film film;

    @BeforeEach
    void setUp(){
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            factory.getConstraintValidatorFactory();
            validator = factory.getValidator();
        }
        film = new Film();
    }

    @Test
    void blankNameShouldFailValidation() {
        film.setName("");
        Set<ConstraintViolation<Film>> violations =validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void correctNameShouldPassValidation() {
        film.setName("Die Hard");
        film.setDuration(130);
        Set<ConstraintViolation<Film>> violations =validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void descriptionMore200SymboldShouldFailValidation() {
        film.setName("Die Hard");
        film.setDescription("«Крепкий орешек» - американский боевик 1988 года режиссёра " +
                "Джон Мактирнан по сценарию Джеба Стюарта и Стивена де Соузы , " +
                "основанный на романе Родерика Торпа «Ничто не вечно ». " +
                "В главных ролях Брюс Уиллис, Алан Рикман, Александр Годунов и Бонни Беделиа, " +
                "а также Реджинальд Велджонсон, Уильям Атертон, Пол Глисон и Харт Бокнер, " +
                "исполняющие второстепенные роли. Фильм рассказывает о детективе полиции Нью-Йорка Джоне Макклейне, " +
                "который попал в террористический захват небоскреба " +
                "Лос-Анджелеса во время посещения своей отчуждённой жены.");
        Set<ConstraintViolation<Film>> violations =validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void dateBefor28December1895ShouldFailValidation() {
        film.setName("Die Hard");
        film.setReleaseDate(LocalDate.of(1800,1,1));
        film.setDuration(130);
        Set<ConstraintViolation<Film>> violations =validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void correctDateShouldPassValidation() {
        film.setName("Die Hard");
        film.setReleaseDate(LocalDate.of(1988,12,25));
        film.setDuration(130);
        Set<ConstraintViolation<Film>> violations =validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void negativeDurationShouldFailValidation() {
        film.setName("Die Hard");
        film.setDuration(-10);
        Set<ConstraintViolation<Film>> violations =validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void zeroDurationShouldFailValidation() {
        film.setName("Die Hard");
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations =validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void positiveDurationShouldPassValidation() {
        film.setName("Die Hard");
        film.setDuration(100);
        Set<ConstraintViolation<Film>> violations =validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }
}
