package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class Film {

    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @Max(200)
    private String description;

    private LocalDate releaseDate;
    @Positive
    private Number duration;

}
