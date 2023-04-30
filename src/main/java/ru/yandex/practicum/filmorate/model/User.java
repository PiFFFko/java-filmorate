package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.constraint.WithoutSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Integer id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @WithoutSpaces
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
   private Set<Integer> friends;

}
