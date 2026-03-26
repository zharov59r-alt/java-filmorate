package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmLikeRowMapper implements RowMapper<FilmLike> {

    @Override
    public FilmLike mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmLike friendLike = new FilmLike();
        friendLike.setId(resultSet.getLong("film_like_id"));
        friendLike.setFilmId(resultSet.getLong("film_id"));
        friendLike.setUserId(resultSet.getLong("user_id"));
        return friendLike;
    }
}
