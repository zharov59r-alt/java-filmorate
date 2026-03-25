package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FilmGenre {

    private Long id;
    private Long filmId;
    private Long genreId;

    public FilmGenre (Long filmId, Long genreId) {
        this.filmId = filmId;
        this.genreId = genreId;
    }

}
