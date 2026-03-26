package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("film_name"));
        film.setDescription(resultSet.getString("film_description"));
        film.setReleaseDate(resultSet.getDate("film_release_date").toLocalDate());
        film.setDuration(resultSet.getDouble("film_duration"));
        film.setRatingMpaId((Long) resultSet.getObject("rating_mpa_id"));
        return film;
    }
}

