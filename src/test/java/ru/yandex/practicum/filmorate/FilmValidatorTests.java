package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;


import java.time.LocalDate;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmValidatorTests {

    @Test
    void checkFilmOk() {
        Film film = new Film();
        film.setId(1L);
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(now());
        film.setDuration(10.0);

        assertTrue(FilmValidator.check(film).isEmpty());
    }

    @Test
    void checkFilmName() {
        Film film = new Film();
        film.setId(1L);
        film.setDescription("description");
        film.setReleaseDate(now());
        film.setDuration(10.0);

        assertTrue(FilmValidator.check(film).contains("Название должно быть указано"));
        film.setName("");
        assertTrue(FilmValidator.check(film).contains("Название должно быть указано"));
    }

    @Test
    void checkFilmDescription() {
        Film film = new Film();
        film.setId(1L);
        film.setName("name");
        film.setDescription(
                "1234567890123456789012345678901234567890123456789012345678901234567890" +
                "1234567890123456789012345678901234567890123456789012345678901234567890" +
                "1234567890123456789012345678901234567890123456789012345678901234567890");
        film.setReleaseDate(now());
        film.setDuration(10.0);

        assertTrue(FilmValidator.check(film).contains("Максимальная длина описания — 200 символов"));
    }

    @Test
    void checkFilmReleaseDate() {
        Film film = new Film();
        film.setId(1L);
        film.setName("name");
        film.setDescription("description");
        film.setDuration(10.0);

        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        assertFalse(FilmValidator.check(film).contains("Дата релиза — не раньше 28 декабря 1895 года"));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertFalse(FilmValidator.check(film).contains("Дата релиза — не раньше 28 декабря 1895 года"));
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertTrue(FilmValidator.check(film).contains("Дата релиза — не раньше 28 декабря 1895 года"));
    }

    @Test
    void checkFilmDuration() {
        Film film = new Film();
        film.setId(1L);
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(now());
        film.setDuration(-10.0);

        assertTrue(FilmValidator.check(film).contains("Продолжительность фильма должна быть положительным числом"));
    }

}
