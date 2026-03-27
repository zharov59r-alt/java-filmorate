package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilmLike {

    private Long id;
    private Long filmId;
    private Long userId;

}
