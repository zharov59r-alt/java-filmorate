package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public Collection<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public Genre findById(@PathVariable Long id) {
        return genreService.findById(id);
    }

}
