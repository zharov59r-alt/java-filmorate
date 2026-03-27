package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.RatingMPAService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class RatingMPAController {

    private final RatingMPAService ratingMPAService;

    @GetMapping
    public Collection<RatingMPA> findAll() {
        return ratingMPAService.findAll();
    }

    @GetMapping("/{id}")
    public RatingMPA findById(@PathVariable Long id) {
        return ratingMPAService.findById(id);
    }

}
