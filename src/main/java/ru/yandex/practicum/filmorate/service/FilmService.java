package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmGenreRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.RatingMPARepository;
import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmGenreMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final RatingMPARepository ratingMPARepository;
    private final FilmGenreRepository filmGenreRepository;


    public Collection<FilmResponse> findAll() {
        return filmRepository.findAll().stream()
                    .map(film -> FilmMapper.toFilmResponse(
                            film,
                            ratingMPARepository.findById(film.getRatingMpaId()),
                            genreRepository.findAllByFilmId(film.getId())
                            ))
                    .toList();

    }

    public FilmResponse findById(Long id) {

         return filmRepository.findById(id)
                    .map(film -> FilmMapper.toFilmResponse(
                            film,
                            ratingMPARepository.findById(film.getRatingMpaId()),
                            genreRepository.findAllByFilmId(film.getId())
                            ))
                    .orElseThrow(() -> new NotFoundException("Фильм не найден с ID: " + id));

    }

    /*
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
*/

    public FilmResponse create(NewFilmRequest request) {
        log.info("create {}", request);

        Film film = FilmMapper.toFilm(request);

        List<String> validation = FilmValidator.check(film);

        if (!validation.isEmpty()) {
            log.warn("validation {}", validation);
            throw new ValidationException("Проверка входных параметров", validation);
        }

        film = filmRepository.save(film);

        filmGenreRepository.batchSave(FilmGenreMapper.toFilmGenreList(film.getId(), request.getGenres()));

        return FilmMapper.toFilmResponse(
                film,
                ratingMPARepository.findById(film.getRatingMpaId()),
                genreRepository.findAllByIds(
                        request.getGenres().stream()
                                .map(Genre::getId)
                                .toList()
                ));

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

        film = filmRepository.update(film);

        filmGenreRepository.merge(film.getId(), FilmGenreMapper.toFilmGenreList(film.getId(), request.getGenres()));

        return FilmMapper.toFilmResponse(
                film,
                ratingMPARepository.findById(film.getRatingMpaId()),
                genreRepository.findAllByIds(
                        request.getGenres().stream()
                                .map(Genre::getId)
                                .toList()
                ));

    }

}
