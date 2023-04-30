package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
public class UserValidationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String CORRECT_MAIL = "mail@mail.ru";
    private static final String CORRECT_LOGIN = "login";
    User user;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            factory.getConstraintValidatorFactory();
            validator = factory.getValidator();
        }
        user = new User(0, "mail@ya.ru", "login", "name", LocalDate.of(2000, 1, 1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "mail"})
    public void incorrectFormatOfEmailShouldFailValidation(String mail) {
        user.setEmail(mail);
        user.setLogin(CORRECT_LOGIN);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctMailShouldPassValidation() {
        user.setEmail(CORRECT_MAIL);
        user.setLogin(CORRECT_LOGIN);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "lo gin"})
    public void incorrectLoginShouldFailValidation(String login) {
        user.setEmail(CORRECT_MAIL);
        user.setLogin(login);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctLoginShouldPassValidation() {
        user.setEmail(CORRECT_MAIL);
        user.setLogin(CORRECT_LOGIN);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void dateInTheFutureShouldFailValidation() {
        user.setBirthday(LocalDate.now().plusDays(1));

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

    }

    @Test
    public void correctDateShouldPassValidation() {
        user.setEmail(CORRECT_MAIL);
        user.setLogin(CORRECT_LOGIN);
        user.setBirthday(LocalDate.now().minusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

}
