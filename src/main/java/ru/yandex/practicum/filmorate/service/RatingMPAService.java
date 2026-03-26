package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.RatingMPARepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.Collection;


@Slf4j
@RequiredArgsConstructor
@Service
public class RatingMPAService {
    private final RatingMPARepository ratingMPARepository;

    public Collection<RatingMPA> findAll() {
        return ratingMPARepository.findAll();
    }

    public RatingMPA findById(Long id) {
        return ratingMPARepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг МПА не найден с ID: " + id));
    }

}
