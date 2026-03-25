package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FilmGenreRepository extends BaseRepository<FilmGenre> {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String GENERATOR_NAME = "s_film_genre_id";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT * FROM film_genre WHERE film_id = ?";
    private static final String DELETE_BY_IDS_QUERY = "DELETE FROM film_genre WHERE film_genre_id in (:ids)";
    private static final String INSERT_QUERY = "INSERT INTO film_genre (film_genre_id, film_id, genre_id)" +
            "VALUES (?, ?, ?)";

    public FilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper,
                               NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbc, mapper);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<FilmGenre> merge(Long filmId, List<FilmGenre> filmGenres) {

        List<FilmGenre> currentFilmGenre = findAll(FIND_BY_FILM_ID_QUERY, filmId);

        List<Long> deleteFilmGenre = currentFilmGenre.stream()
                .filter(filmGenre -> filmGenres.stream().noneMatch(fg -> fg.getGenreId() == filmGenre.getGenreId()))
                .map(FilmGenre::getId)
                .toList();

        SqlParameterSource parameters = new MapSqlParameterSource("ids", deleteFilmGenre);
        namedParameterJdbcTemplate.update(DELETE_BY_IDS_QUERY, parameters);

        List<Object[]> insertSet = filmGenres.stream()
                .filter(fg -> currentFilmGenre.stream().noneMatch(cfg -> cfg.getGenreId() == fg.getGenreId()))
                .map(fg -> new Object[]{
                        getNextId(GENERATOR_NAME),
                        fg.getFilmId(),
                        fg.getGenreId()
                })
                .toList();

        batchSave(INSERT_QUERY, insertSet);

        return filmGenres;
    }

    public List<FilmGenre> batchSave(List<FilmGenre> filmGenres) {

        List<Object[]> insertSet = filmGenres.stream()
                        .map(filmGenre -> new Object[]{
                                getNextId(GENERATOR_NAME),
                                filmGenre.getFilmId(),
                                filmGenre.getGenreId()
                        })
                        .toList();

        batchSave(INSERT_QUERY, insertSet);

        return filmGenres;
    }

}
