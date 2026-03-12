package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(Long id) {
        return filmStorage.findById(id);
    }

    public Film addLike(Long id, Long userId) {
        log.info("addLike id = {}, userId = {}", id,  userId);

        Film film = filmStorage.findById(id);

        if (film == null) {
            log.warn("id not exists");
            throw new NotFoundException("id = " + id + " не найден");
        }

        User user = userStorage.findById(userId);

        if (user == null) {
            log.warn("id not exists");
            throw new NotFoundException("id = " + id + " не найден");
        }


        if (!film.getLikes().contains(userId)) {

            Set<Long> likes = film.getLikes();
            likes.add(userId);
            film.setLikes(likes);
            filmStorage.update(film);
        }

        return film;

    }

    public Film removeLike(Long id, Long userId) {
        log.info("removeLike id = {}, userId = {}", id,  userId);

        Film film = filmStorage.findById(id);

        if (film == null) {
            log.warn("id not exists");
            throw new NotFoundException("id = " + id + " не найден");
        }

        User user = userStorage.findById(userId);

        if (user == null) {
            log.warn("id not exists");
            throw new NotFoundException("id = " + id + " не найден");
        }

        if (film.getLikes().contains(userId)) {

            Set<Long> likes = film.getLikes();
            likes.remove(userId);
            film.setLikes(likes);
            filmStorage.update(film);
        }

        return film;
    }

    public Collection<Film> findTop(Integer count) {

        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .toList();

    }


    public Film create(Film film) {
        log.info("create {}", film);

        List<String> validation = FilmValidator.check(film);

        if (!validation.isEmpty()) {
            log.warn("validation {}", validation);
            throw new ValidationException("Проверка входных параметров", validation);
        }

        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        log.info("update {}", newFilm);

        if (newFilm.getId() == null) {
            log.warn("id is null");
            throw new ValidationException("Id должен быть указан");
        }

        if (filmStorage.findById(newFilm.getId()) != null) {

            List<String> validation = FilmValidator.check(newFilm);

            if (!validation.isEmpty()) {
                log.warn("validation {}", validation);
                throw new ValidationException("Проверка входных параметров", validation);
            }

            return filmStorage.update(newFilm);
        }

        log.warn("id not exists");
        throw new NotFoundException("id = " + newFilm.getId() + " не найден");
    }

}
