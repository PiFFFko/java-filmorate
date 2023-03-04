package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;


public class UserValidationTest {
    private static final String CORRECT_MAIL = "mail@mail.ru";
    private static final String CORRECT_LOGIN = "login";
    private Validator validator;
    User user;
    @BeforeEach
    public void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        factory.getConstraintValidatorFactory();
        validator = factory.getValidator();
        user = new User();
    }

    @Test
    public void blankEmailShouldFailValidation(){
        User user = new User();
        user.setEmail("");
        user.setLogin(CORRECT_LOGIN);
        Set<ConstraintViolation<User>> violations =validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void incorrectFormatOfEmailShouldFailValidation(){
        user.setEmail("mail");
        user.setLogin(CORRECT_LOGIN);
        Set<ConstraintViolation<User>> violations =validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctMailShouldPassValidation(){
        user.setEmail(CORRECT_MAIL);
        user.setLogin(CORRECT_LOGIN);
        Set<ConstraintViolation<User>> violations =validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void blankLoginShouldFailValidation(){
        user.setEmail(CORRECT_MAIL);
        user.setLogin("");
        Set<ConstraintViolation<User>> violations =validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void loginWithSpacesShouldFailValidation(){
        user.setEmail(CORRECT_MAIL);
        user.setLogin("lo gin");
        Set<ConstraintViolation<User>> violations =validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctLoginShouldPassValidation(){
        user.setEmail(CORRECT_MAIL);
        user.setLogin(CORRECT_LOGIN);
        Set<ConstraintViolation<User>> violations =validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

   @Test
   public void dateInTheFutureShouldFailValidation(){
       user.setEmail(CORRECT_MAIL);
       user.setLogin(CORRECT_LOGIN);
       user.setBirthday(LocalDate.now().plusDays(1));
       Set<ConstraintViolation<User>> violations =validator.validate(user);
       Assertions.assertFalse(violations.isEmpty());
   }

   @Test
    public void correctDateShouldPassValidation(){
       user.setEmail(CORRECT_MAIL);
       user.setLogin(CORRECT_LOGIN);
       user.setBirthday(LocalDate.now().minusDays(1));
       Set<ConstraintViolation<User>> violations =validator.validate(user);
       Assertions.assertTrue(violations.isEmpty());
   }

}
