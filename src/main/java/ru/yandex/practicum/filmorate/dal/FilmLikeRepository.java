package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.Optional;

@Slf4j
@Repository
public class FilmLikeRepository extends BaseRepository<FilmLike> {

    private static final String GENERATOR_NAME = "s_film_like_id";
    private static final String FIND_BY_FILM_ID_USER_ID_QUERY = "SELECT * FROM film_like where film_id = ? and user_id = ?";
    private static final String DELETE_QUERY = "DELETE from film_like where film_like_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO film_like (film_like_id, film_id, user_id)" +
            "VALUES (?, ?, ?)";

    public FilmLikeRepository(JdbcTemplate jdbc, RowMapper<FilmLike> mapper) {
        super(jdbc, mapper);
    }

    public Optional<FilmLike> findByFilmIdUserId(Long filmId, Long userId) {
        return findOne(FIND_BY_FILM_ID_USER_ID_QUERY, filmId, userId);
    }

    public FilmLike save(FilmLike filmLike) {

        filmLike.setId(getNextId(GENERATOR_NAME));

        save(
            INSERT_QUERY,
            filmLike.getId(),
            filmLike.getFilmId(),
            filmLike.getUserId()
        );

        return filmLike;
    }

    public void delete(Long id) {
        delete(DELETE_QUERY, id);
    }

}
