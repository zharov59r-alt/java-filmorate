package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class BaseRepository<T> {

    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected List<T> findAll(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    public int save(String query, Object... params) {
        return jdbc.update(query, params);
    }

    public int[] batchSave(String query, List<Object[]> params) {
        return jdbc.batchUpdate(query, params);
    }

    public int delete(String query, Long id) {
        return jdbc.update(query, id);
    }

    protected long getNextId(String sequenceName) {
        return jdbc.queryForObject("select nextval(?)", Long.class, sequenceName);
    }

}

