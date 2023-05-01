package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.constraint.WithoutSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    private int id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @WithoutSpaces
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    @JsonIgnore
    private final Set<Integer> friends = new HashSet<>();

    public void setFriends(Set<Integer> friends) {
    }

    public Set<Integer> getFriends() {
        return friends;
    }
}

