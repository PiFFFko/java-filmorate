package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.constraint.ReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;

import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Data
@Builder
@AllArgsConstructor

public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    private long duration;
    private Rating mpa;
    private Set<Genre> genres;
    private Integer rate;
    @JsonIgnore
    private Set<Integer> likesFromUsers;
    private Set<Director> directors;

    public Integer getPopularity() {
        return likesFromUsers.size();
    }
}
