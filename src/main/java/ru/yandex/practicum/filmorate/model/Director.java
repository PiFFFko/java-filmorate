package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@SuperBuilder
public class Director {
    Integer id;
    @NotBlank
    String name;
}
