package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.FilmLike;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmLikeMapper {

    public static FilmLike toFilmLike(Long filmId, Long userId) {
        FilmLike dto = new FilmLike();
        dto.setFilmId(filmId);
        dto.setUserId(userId);
        return dto;
    }

}