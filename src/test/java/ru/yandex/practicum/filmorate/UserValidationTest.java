package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;


class UserValidationTest {
    private static final String CORRECT_MAIL = "mail@mail.ru";
    private static final String CORRECT_LOGIN = "login";
    User user;
    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            factory.getConstraintValidatorFactory();
            validator = factory.getValidator();
        }
        user = new User();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "mail"})
    void incorrectFormatOfEmailShouldFailValidation(String mail) {
        user.setEmail(mail);
        user.setLogin(CORRECT_LOGIN);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void correctMailShouldPassValidation() {
        user.setEmail(CORRECT_MAIL);
        user.setLogin(CORRECT_LOGIN);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "lo gin"})
    void incorrectLoginShouldFailValidation(String login) {
        user.setEmail(CORRECT_MAIL);
        user.setLogin(login);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void correctLoginShouldPassValidation() {
        user.setEmail(CORRECT_MAIL);
        user.setLogin(CORRECT_LOGIN);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void dateInTheFutureShouldFailValidation() {
        user.setEmail(CORRECT_MAIL);
        user.setLogin(CORRECT_LOGIN);
        user.setBirthday(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void correctDateShouldPassValidation() {
        user.setEmail(CORRECT_MAIL);
        user.setLogin(CORRECT_LOGIN);
        user.setBirthday(LocalDate.now().minusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

}
