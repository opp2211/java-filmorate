package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
public class Director {
    private Integer id;

    @NotBlank
    private String name;
}