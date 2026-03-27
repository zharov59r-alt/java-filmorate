package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String FIND_BY_IDS_QUERY = "SELECT * FROM genre WHERE genre_id in (:ids)";
    private static final String FIND_ALL_BY_FILM_ID_QUERY = "SELECT g.* " +
            "FROM film_genre fg " +
            "JOIN genre g on g.genre_id = fg.genre_id " +
            "WHERE fg.film_id = ?";

    public GenreRepository(
            JdbcTemplate jdbc,
            RowMapper<Genre> mapper,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbc, mapper);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Genre> findAll() {
        return findAll(FIND_ALL_QUERY);
    }

    public List<Genre> findAllByIds(List<Long> ids) {
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        return namedParameterJdbcTemplate.query(FIND_BY_IDS_QUERY, parameters, mapper);
    }

    public List<Genre> findAllByFilmId(Long filmId) {
        return findAll(FIND_ALL_BY_FILM_ID_QUERY, filmId);
    }

    public Optional<Genre> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

}
