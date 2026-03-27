package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmGenreMapper {

    public static List<FilmGenre> toFilmGenreList(Long filmId, List<Genre> genres) {

        return genres.stream()
                .map(genre -> new FilmGenre(filmId, genre.getId()))
                .toList();

    }

}