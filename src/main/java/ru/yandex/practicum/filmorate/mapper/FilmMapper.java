package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static FilmResponse toFilmResponse(Film film, RatingMPA mpa, List<Genre> genres) {
        FilmResponse dto = new FilmResponse();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpa(mpa);
        dto.setGenres(genres);
        return dto;
    }

    public static Film toFilm(NewFilmRequest film) {
        Film dto = new Film();
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        if (film.getMpa() != null)
            dto.setRatingMpaId(film.getMpa().getId());

        return dto;
    }

    public static Film toFilm(Film film, UpdateFilmRequest request) {

        if (request.hasName()) {
            film.setName(request.getName());
        }
        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }
        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }
        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }
        if (request.hasMpa()) {
            film.setRatingMpaId(request.getMpa().getId());
        }
        return film;
    }


}