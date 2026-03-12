package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.Set;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Long id) {
        return userStorage.findById(id);
    }

    public void addFriend(Long id, Long friendId) {
        log.info("addFriend id = {}, friendId = {}", id,  friendId);

        User user = userStorage.findById(id);
        User userFriend = userStorage.findById(friendId);

        if (user == null) {
            log.warn("id not exists");
            throw new NotFoundException("id = " + id + " не найден");
        }

        if (userFriend == null) {
            log.warn("friendId not exists");
            throw new NotFoundException("friendId = " + friendId + " не найден");
        }

        if (!user.getFriends().contains(friendId)) {

            Set<Long> friendIds = user.getFriends();
            friendIds.add(friendId);
            user.setFriends(friendIds);
            userStorage.update(user);

            friendIds = userFriend.getFriends();
            friendIds.add(id);
            userFriend.setFriends(friendIds);
            userStorage.update(userFriend);

        }

    }

    public void removeFriend(Long id, Long friendId) {
        log.info("removeFriend id = {}, friendId = {}", id,  friendId);

        User user = userStorage.findById(id);
        User userFriend = userStorage.findById(friendId);

        if (user == null) {
            log.warn("id not exists");
            throw new NotFoundException("id = " + id + " не найден");
        }

        if (userFriend == null) {
            log.warn("friendId not exists");
            throw new NotFoundException("friendId = " + friendId + " не найден");
        }

        if (user.getFriends().contains(friendId)) {

            Set<Long> friendIds = user.getFriends();
            friendIds.remove(friendId);
            user.setFriends(friendIds);
            userStorage.update(user);

            friendIds = userFriend.getFriends();
            friendIds.remove(id);
            userFriend.setFriends(friendIds);
            userStorage.update(userFriend);

        }
    }

    public Collection<User> findFriendById(Long id) {
        log.info("findFriendById id = {}", id);

        User user = userStorage.findById(id);

        if (user == null) {
            log.warn("id not exists");
            throw new NotFoundException("id = " + id + " не найден");
        }

        return user.getFriends().stream()
                .map(userStorage::findById)
                .toList();
    }

    public Collection<User> findCommonFriends(Long id, Long otherId) {
        log.info("removeFriend id = {}, otherId = {}", id,  otherId);

        User user = userStorage.findById(id);
        User userOther = userStorage.findById(otherId);

        if (user == null) {
            log.warn("id not exists");
            throw new NotFoundException("id = " + id + " не найден");
        }

        if (userOther == null) {
            log.warn("otherId not exists");
            throw new NotFoundException("otherId = " + otherId + " не найден");
        }

        return user.getFriends().stream()
                .filter(userOther.getFriends()::contains)
                .map(userStorage::findById)
                .toList();
    }

    public User create(User user) {
        log.info("create {}", user);

        List<String> validation = UserValidator.check(user);

        if (!validation.isEmpty()) {
            log.warn("validation {}", validation);
            throw new ValidationException("Проверка входных параметров", validation);
        }

        if (UserValidator.checkEmailDublicate(userStorage.findAll(), user)) {
            log.warn("dublicate email {}", user.getEmail());
            throw new ValidationException("Этот имейл уже используется");
        }

        userStorage.create(user);
        return user;
    }

    public User update(User newUser) {
        log.info("update {}", newUser);

        if (newUser.getId() == null) {
            log.warn("id is null");
            throw new ValidationException("Id должен быть указан");
        }

        if (userStorage.findById(newUser.getId()) != null) {

            List<String> validation = UserValidator.check(newUser);

            if (!validation.isEmpty()) {
                log.warn("validation {}", validation);
                throw new ValidationException("Проверка входных параметров", validation);
            }

            if (UserValidator.checkEmailDublicate(userStorage.findAll(), newUser)) {
                log.warn("dublicate email {}", newUser.getEmail());
                throw new ValidationException("Этот имейл уже используется");
            }

            return userStorage.update(newUser);
        }

        log.warn("id not exists");
        throw new NotFoundException("id = " + newUser.getId() + " не найден");
    }


}
