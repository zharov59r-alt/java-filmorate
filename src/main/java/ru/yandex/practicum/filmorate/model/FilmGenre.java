package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilmGenre {

    private Long id;
    private Long filmId;
    private Long genreId;

    public FilmGenre(Long filmId, Long genreId) {
        this.filmId = filmId;
        this.genreId = genreId;
    }

}
