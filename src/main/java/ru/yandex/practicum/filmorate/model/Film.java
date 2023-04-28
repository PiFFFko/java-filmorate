package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.constraint.ReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;

import java.util.Set;

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
    private Mpa mpa;
    private Set<Genre> genres;
    private Integer rate;
    @JsonIgnore
    private Set<Integer> likesFromUsers;

    public Integer getPopularity() {
        return likesFromUsers.size();
    }
}
