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

        List<String> validation = UserValidator.check(user);

        if (!validation.isEmpty()) {
            log.warn("validation {}", validation);
            throw new ValidationException("Проверка входных параметров", validation);
        }

        if (UserValidator.checkEmailDublicate(users.values(), user)) {
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

            List<String> validation = UserValidator.check(newUser);

            if (!validation.isEmpty()) {
                log.warn("validation {}", validation);
                throw new ValidationException("Проверка входных параметров", validation);
            }

            User oldUser = users.get(newUser.getId());

            if (UserValidator.checkEmailDublicate(users.values(), newUser)) {
                log.warn("dublicate email {}", newUser.getEmail());
                throw new ValidationException("Этот имейл уже используется");
            }

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
