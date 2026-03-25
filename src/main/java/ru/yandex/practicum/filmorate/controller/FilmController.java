package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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

/*
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Long id,
                          @PathVariable("userId") Long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") Long id,
                             @PathVariable("userId") Long userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> findTop(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return filmService.findTop(count);
    }
*/
    @PostMapping
    public FilmResponse create(@Valid @RequestBody NewFilmRequest request) {
        return filmService.create(request);
    }

    @PutMapping
    public FilmResponse update(@Valid @RequestBody UpdateFilmRequest request) {
        return filmService.update(request);
    }

}
