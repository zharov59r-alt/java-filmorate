package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("create {}", user);

        Optional<List<String>> valid = UserValidator.check(user);

        if (valid.isPresent()) {
            log.warn("validation {}", valid.get());
            throw new ValidationException("Проверка входных параметров", valid.get());
        }

        if (users.values()
                .stream()
                .map(u -> u.getEmail())
                .anyMatch(email -> email.equals(user.getEmail()))) {
            log.warn("dublicate email {}", user.getEmail());
            throw new ValidationException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("update {}", newUser);

        if (newUser.getId() == null) {
            log.warn("id is null");
            throw new ValidationException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {

            Optional<List<String>> valid = UserValidator.check(newUser);

            if (valid.isPresent()) {
                log.warn("validation {}", valid.get());
                throw new ValidationException("Проверка входных параметров", valid.get());
            }

            User oldUser = users.get(newUser.getId());

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(
                    (newUser.getName() == null || newUser.getName().isBlank()) ?
                            newUser.getLogin() :
                            newUser.getName()
                    );
            oldUser.setBirthday(newUser.getBirthday());

            return oldUser;
        }

        log.warn("id not exists");
        throw new ValidationException("id = " + newUser.getId() + " не найден", HttpStatus.NOT_FOUND);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
