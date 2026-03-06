package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FilmValidator {

    public static List<String> check(Film film) {

        List<String> errors = new ArrayList<>();

        if (film.getName() == null || film.getName().isBlank()) {
            errors.add("Название должено быть указано");
        }

        if (film.getDescription() != null && film.getDescription().length() >= 200) {
            errors.add("Максимальная длина описания — 200 символов");
        }

        if (film.getReleaseDate() != null && !film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            errors.add("Дата релиза — не раньше 28 декабря 1895 года");
        }

        if (film.getDuration() != null && film.getDuration() <= 0) {
            errors.add("Продолжительность фильма должна быть положительным числом");
        }

        return errors;

    }

}
