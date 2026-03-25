package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAll() {
        return findAll(FIND_ALL_QUERY);
    }

    public Optional<Genre> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }


}
