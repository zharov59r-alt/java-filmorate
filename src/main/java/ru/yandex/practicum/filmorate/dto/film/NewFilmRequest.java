package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class NewFilmRequest {

    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;
    @Positive
    private Double duration;

    private RatingMPA mpa;

    private List<Genre> genres = new ArrayList<>();

}
