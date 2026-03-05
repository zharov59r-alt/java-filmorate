package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        // проверяем выполнение необходимых условий
        Optional<List<String>> valid = UserValidator.check(user);

        if (valid.isPresent()) {
            throw new ValidationException(String.join("; ", valid.get()));
        }

        if (users.values()
                .stream()
                .map(User::getEmail)
                .anyMatch(user.getEmail()::equals)) {
            throw new ValidationException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getEmail());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            if (users.values()
                    .stream()
                    .filter(u -> !u.equals(oldUser))
                    .map(User::getEmail)
                    .anyMatch(newUser.getEmail()::equals)) {
                throw new ValidationException("Этот имейл уже используется");
            }

            if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {

                if (!newUser.getEmail().contains("@")) {
                    throw new ValidationException("Имейл должен содержать символ @");
                }

                oldUser.setEmail(newUser.getEmail());
            }

            if (newUser.getLogin() != null && !newUser.getLogin().isBlank()) {
                oldUser.setLogin(newUser.getLogin());
            }

            if (newUser.getName() != null && !newUser.getName().isBlank()) {
                oldUser.setName(newUser.getName());
            }

            if (newUser.getBirthday() != null) {
                oldUser.setBirthday(newUser.getBirthday());
            }

            return oldUser;
        }
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(ValidationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
