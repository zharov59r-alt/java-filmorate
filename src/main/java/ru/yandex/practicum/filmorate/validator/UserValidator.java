package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserValidator {

    public static Optional<List<String>> check(User user) {

        List<String> errors = new ArrayList<>();

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            errors.add("Имейл должен быть указан");
        }
        else if (!user.getEmail().contains("@")) {
            errors.add("Имейл должен содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            errors.add("Логин должен быть указан");
        }
        else if (!user.getLogin().contains(" ")) {
            errors.add("Логин не должен содержать пробелы");
        }

        if (errors.isEmpty())
            return Optional.empty();
        else
            return Optional.of(errors);

    }

}
