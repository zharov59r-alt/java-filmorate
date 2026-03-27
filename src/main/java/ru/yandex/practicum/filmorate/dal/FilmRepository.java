package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> {

    private static final String GENERATOR_NAME = "public.s_film_id";
    private static final String FIND_ALL_QUERY = "SELECT * FROM film";
    private static final String FIND_TOP =  "SELECT f.* " +
                                            "FROM   (" +
                                                "SELECT fl.film_id, count(1) cnt " +
                                                "FROM film_like fl " +
                                                "group by fl.film_id " +
                                                "order by count(1) " +
                                                "limit ?) tb " +
                                            "JOIN   film f on f.film_id = tb.film_id " +
                                            "ORDER BY tb.cnt desc";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO film (film_id, film_name, film_description, film_release_date, film_duration, rating_mpa_id)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film SET film_name = ?, film_description = ?, film_release_date = ?, film_duration = ?, rating_mpa_id = ? WHERE film_id = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findAll(FIND_ALL_QUERY);
    }

    public List<Film> findTop(Integer count) {
        return findAll(FIND_TOP, count);
    }

    public Optional<Film> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Film save(Film film) {

        film.setId(getNextId(GENERATOR_NAME));

        save(
            INSERT_QUERY,
            film.getId(),
            film.getName(),
            film.getDescription(),
            film.getReleaseDate(),
            film.getDuration(),
            film.getRatingMpaId()
        );

        return film;
    }

    public Film update(Film film) {

        save(
            UPDATE_QUERY,
            film.getName(),
            film.getDescription(),
            film.getReleaseDate(),
            film.getDuration(),
            film.getRatingMpaId(),
            film.getId()
        );

        return film;

    }

}
