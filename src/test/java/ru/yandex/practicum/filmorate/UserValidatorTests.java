package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidatorTests {

    @Test
    void checkUserOk() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@email.com");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(now());

        assertTrue(UserValidator.check(user).isEmpty());
    }

    @Test
    void checkUserEmail() {
        User user = new User();
        user.setId(1L);
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(now());

        assertTrue(UserValidator.check(user).contains("Имейл должен быть указан"));
        user.setEmail("");
        assertTrue(UserValidator.check(user).contains("Имейл должен быть указан"));
        user.setEmail("email");
        assertTrue(UserValidator.check(user).contains("Имейл должен содержать символ @"));
    }

    @Test
    void checkUserLogin() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@email.com");
        user.setName("name");
        user.setBirthday(now());

        assertTrue(UserValidator.check(user).contains("Логин должен быть указан"));
        user.setLogin("");
        assertTrue(UserValidator.check(user).contains("Логин должен быть указан"));
        user.setLogin("login login");
        assertTrue(UserValidator.check(user).contains("Логин не должен содержать пробелы"));
    }

    @Test
    void checkUserBirthday() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@email.com");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(now().plusDays(1));

        assertTrue(UserValidator.check(user).contains("Дата рождения не может быть в будущем"));
    }


}
