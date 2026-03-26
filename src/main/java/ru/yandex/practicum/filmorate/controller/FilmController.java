package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<FilmResponse> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public FilmResponse findById(@PathVariable Long id) {
        return filmService.findById(id);
    }


    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") Long filmId,
                          @PathVariable("userId") Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable("filmId") Long filmId,
                             @PathVariable("userId") Long userId) {
        filmService.removeLike(filmId, userId);
    }


    @GetMapping("/popular")
    public Collection<FilmResponse> findTop(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return filmService.findTop(count);
    }

    @PostMapping
    public FilmResponse create(@Valid @RequestBody NewFilmRequest request) {
        return filmService.create(request);
    }

    @PutMapping
    public FilmResponse update(@Valid @RequestBody UpdateFilmRequest request) {
        return filmService.update(request);
    }

}
