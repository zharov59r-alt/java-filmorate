package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserValidator {

    public static List<String> check(User user) {

        List<String> errors = new ArrayList<>();

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            errors.add("Имейл должен быть указан");
        } else if (!user.getEmail().contains("@")) {
            errors.add("Имейл должен содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            errors.add("Логин должен быть указан");
        } else if (user.getLogin().contains(" ")) {
            errors.add("Логин не должен содержать пробелы");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            errors.add("Дата рождения не может быть в будущем");
        }

        return errors;

    }

    public static Boolean checkEmailDublicate(Collection<User> users, User user) {

        return users
                .stream()
                .filter(u -> !u.equals(user))
                .map(u -> u.getEmail())
                .anyMatch(email -> email.equals(user.getEmail()));

    }

}
