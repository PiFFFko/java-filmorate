package ru.yandex.practicum.filmorate.constraint;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = WithoutSpacesValidator.class)
public @interface WithoutSpaces {
    String message() default "Login should be without spaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class WithoutSpacesValidator implements ConstraintValidator<WithoutSpaces, String> {

    @Override
    public void initialize(WithoutSpaces constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.split(" ").length == 1;
    }
}
