package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public static User toUser(NewUserRequest user) {
        User dto = new User();
        dto.setEmail(user.getEmail());
        dto.setLogin(user.getLogin());
        if (user.getName() == null || user.getName().isBlank()) {
            dto.setName(user.getLogin());
        } else {
            dto.setName(user.getName());
        }
        dto.setBirthday(user.getBirthday());

        return dto;
    }

    public static User toUser(User user, UpdateUserRequest request) {
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasLogin()) {
            user.setLogin(request.getLogin());
        }
        if (request.hasName()) {
            user.setName(request.getName());
        }
        if (request.hasBirthday()) {
            user.setBirthday(request.getBirthday());
        }
        return user;
    }

}
