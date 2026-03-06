package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("create {}", film);

        Optional<List<String>> valid = FilmValidator.check(film);

        if (valid.isPresent()) {
            log.warn("validation {}", valid.get());
            throw new ValidationException("Проверка входных параметров", valid.get());
        }

        if (films.values()
                .stream()
                .map(u -> u.getEmail())
                .anyMatch(email -> email.equals(film.getEmail()))) {
            log.warn("dublicate email {}", film.getEmail());
            throw new ValidationException("Этот имейл уже используется");
        }

        film.setId(getNextId());
        if (film.getName() == null || film.getName().isBlank()) {
            film.setName(film.getLogin());
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("update {}", newFilm);

        if (newFilm.getId() == null) {
            log.warn("id is null");
            throw new ValidationException("Id должен быть указан");
        }

        if (films.containsKey(newFilm.getId())) {

            Optional<List<String>> valid = FilmValidator.check(newFilm);

            if (valid.isPresent()) {
                log.warn("validation {}", valid.get());
                throw new ValidationException("Проверка входных параметров", valid.get());
            }

            Film oldFilm = films.get(newFilm.getId());

            if (films.values()
                    .stream()
                    .filter(u -> !u.equals(oldFilm))
                    .map(u -> u.getEmail())
                    .anyMatch(email -> email.equals(newFilm.getEmail()))) {
                log.warn("dublicate email {}", newFilm.getEmail());
                throw new ValidationException("Этот имейл уже используется");
            }

            oldFilm.setEmail(newFilm.getEmail());
            oldFilm.setLogin(newFilm.getLogin());
            oldFilm.setName(
                    (newFilm.getName() == null || newFilm.getName().isBlank()) ?
                            newFilm.getLogin() :
                            newFilm.getName()
            );
            oldFilm.setBirthday(newFilm.getBirthday());

            return oldFilm;
        }

        log.warn("id not exists");
        throw new ValidationException("id = " + newFilm.getId() + " не найден", HttpStatus.NOT_FOUND);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
