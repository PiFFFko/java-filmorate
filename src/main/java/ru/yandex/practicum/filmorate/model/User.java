package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.constraint.WithoutSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Integer id;
    @Email
    @NotBlank
    String email;
    @NotBlank
    @WithoutSpaces
    String login;
    String name;
    @Past
    LocalDate birthday;
}
