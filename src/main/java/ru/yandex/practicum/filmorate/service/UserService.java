package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.Instant;
import java.util.Collection;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));
    }

    /*
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

     */

    public User create(NewUserRequest request) {
        log.info("create {}", request);

        User user = UserMapper.toUser(request);

        List<String> validation = UserValidator.check(user);

        if (!validation.isEmpty()) {
            log.warn("validation {}", validation);
            throw new ValidationException("Проверка входных параметров", validation);
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.warn("dublicate email {}", user.getEmail());
            throw new ValidationException("Этот имейл уже используется");
        }

        user = userRepository.save(user);

        return user;
    }

    public User update(UpdateUserRequest request) {
        log.info("update {}", request);


        User user = userRepository.findById(request.getId())
                .map(u -> UserMapper.toUser(u, request))
                .orElseThrow(() -> new NotFoundException("id = " + request.getId() + " не найден"));

        List<String> validation = UserValidator.check(user);

        if (!validation.isEmpty()) {
            log.warn("validation {}", validation);
            throw new ValidationException("Проверка входных параметров", validation);
        }

        if (userRepository.findAllByEmail(user.getEmail()).size() > 1) {
            log.warn("dublicate email {}", user.getEmail());
            throw new ValidationException("Этот имейл уже используется");
        }

        return userRepository.update(user);

    }


}
