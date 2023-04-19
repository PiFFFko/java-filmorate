package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.constraint.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    Integer id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @ReleaseDate
    LocalDate releaseDate;
    @Positive
    long duration;
    Rating mpa;
    List<Genre> genres = new ArrayList<>();
    Set<Integer> likesFromUsers = new TreeSet<>();

    public Film(Integer id, String name, String description, LocalDate releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Integer getPopularity() {
        return likesFromUsers.size();
    }
}
