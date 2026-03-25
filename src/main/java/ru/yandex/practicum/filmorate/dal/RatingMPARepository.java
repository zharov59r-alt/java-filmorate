package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class RatingMPARepository extends BaseRepository<RatingMPA> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM rating_mpa";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating_mpa WHERE rating_mpa_id = ?";

    public RatingMPARepository(JdbcTemplate jdbc, RowMapper<RatingMPA> mapper) {
        super(jdbc, mapper);
    }

    public List<RatingMPA> findAll() {
        return findAll(FIND_ALL_QUERY);
    }

    public Optional<RatingMPA> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }


}
