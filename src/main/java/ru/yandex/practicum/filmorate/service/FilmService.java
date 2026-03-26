package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final RatingMPARepository ratingMPARepository;
    private final FilmGenreRepository filmGenreRepository;
    private final UserRepository userRepository;
    private final FilmLikeRepository filmLikeRepository;

    public Collection<FilmResponse> findAll() {

        return filmRepository.findAll().stream()
                    .map(film -> FilmMapper.toFilmResponse(
                            film,
                            (film.getRatingMpaId() != null) ? ratingMPARepository.findById(film.getRatingMpaId()).get() : null,
                            genreRepository.findAllByFilmId(film.getId())
                            ))
                    .toList();

    }

    public FilmResponse findById(Long id) {

         return filmRepository.findById(id)
                    .map(film -> FilmMapper.toFilmResponse(
                            film,
                            (film.getRatingMpaId() != null) ? ratingMPARepository.findById(film.getRatingMpaId()).get() : null,
                            genreRepository.findAllByFilmId(film.getId())
                            ))
                    .orElseThrow(() -> new NotFoundException("Фильм не найден с ID: " + id));

    }


    public void addLike(Long filmId, Long userId) {
        log.info("addLike filmId = {}, userId = {}", filmId,  userId);

        filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден с ID: " + filmId));

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));


        if (filmLikeRepository.findByFilmIdUserId(filmId, userId).isEmpty()) {
            filmLikeRepository.save(FilmLikeMapper.toFilmLike(filmId, userId));
        }

    }

    public void removeLike(Long filmId, Long userId) {
        log.info("removeLike filmId = {}, userId = {}", filmId,  userId);

        filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден с ID: " + filmId));

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

        Optional<FilmLike> filmLike = filmLikeRepository.findByFilmIdUserId(filmId, userId);

        if (filmLike.isPresent()) {
            filmLikeRepository.delete(filmLike.get().getId());
        }

    }

    public Collection<FilmResponse> findTop(Integer count) {

        return filmRepository.findTop(count).stream()
                .map(film -> FilmMapper.toFilmResponse(
                        film,
                        (film.getRatingMpaId() != null) ? ratingMPARepository.findById(film.getRatingMpaId()).get() : null,
                        genreRepository.findAllByFilmId(film.getId())
                ))
                .toList();

    }

    public FilmResponse create(NewFilmRequest request) {
        log.info("create {}", request);

        Film film = FilmMapper.toFilm(request);

        List<String> validation = FilmValidator.check(film);

        if (!validation.isEmpty()) {
            log.warn("validation {}", validation);
            throw new ValidationException("Проверка входных параметров", validation);
        }

        RatingMPA ratingMPA = findRatingMPA(film.getRatingMpaId());

        List<Genre> genres = findGenres(request.getGenres());

        film = filmRepository.save(film);

        filmGenreRepository.batchSave(FilmGenreMapper.toFilmGenreList(film.getId(), genres));

        return FilmMapper.toFilmResponse(
                film,
                ratingMPA,
                genres);

    }

    public FilmResponse update(UpdateFilmRequest request) {
        log.info("update {}", request);

        Film film = filmRepository.findById(request.getId())
                .map(f -> FilmMapper.toFilm(f, request))
                .orElseThrow(() -> new NotFoundException("id = " + request.getId() + " не найден"));

        List<String> validation = FilmValidator.check(film);

        if (!validation.isEmpty()) {
            log.warn("validation {}", validation);
            throw new ValidationException("Проверка входных параметров", validation);
        }

        RatingMPA ratingMPA = findRatingMPA(film.getRatingMpaId());

        List<Genre> genres = findGenres(request.getGenres());

        film = filmRepository.update(film);

        filmGenreRepository.merge(film.getId(), FilmGenreMapper.toFilmGenreList(film.getId(), genres));

        return FilmMapper.toFilmResponse(
                film,
                ratingMPA,
                genres);

    }

    private RatingMPA findRatingMPA(Long id) {
        if (id != null) {
            return ratingMPARepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("МПА не найден с ID: " + id));
        } else {
            return null;
        }
    }

    private List<Genre> findGenres(List<Genre> genres) {

        List<Genre> result = new ArrayList<>();

        if (!genres.isEmpty()) {

            List<Long> ids = genres.stream().map(Genre::getId).distinct().collect(Collectors.toList());
            result = genreRepository.findAllByIds(ids);

            if (ids.size() != result.size()) {
                ids.removeAll(result.stream().map(Genre::getId).toList());
                throw new NotFoundException("Жанр не найден с ID: " + ids);
            }

        }

        return result;

    }


}
