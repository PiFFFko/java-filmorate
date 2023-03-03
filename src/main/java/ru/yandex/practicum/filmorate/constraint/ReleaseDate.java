package ru.yandex.practicum.filmorate.constraint;

import lombok.NoArgsConstructor;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface ReleaseDate {
    String message() default "Date release is before 28 December of 1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

