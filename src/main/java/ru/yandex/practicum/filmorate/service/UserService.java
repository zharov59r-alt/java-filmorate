package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendLinkRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FriendLinkMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.FriendLink;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final FriendLinkRepository friendLinkRepository;

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));
    }


    public void addFriend(Long id, Long friendId) {
        log.info("addFriend id = {}, friendId = {}", id,  friendId);

        userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));

        userRepository.findById(friendId)
            .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + friendId));

        if (friendLinkRepository.findByUserIdFriendUserId(id, friendId).isEmpty()) {
            friendLinkRepository.save(FriendLinkMapper.toFriendLink(id, friendId));
        }

    }


    public void removeFriend(Long id, Long friendId) {
        log.info("removeFriend id = {}, friendId = {}", id,  friendId);

        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));

        userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + friendId));

        Optional<FriendLink> friendLink = friendLinkRepository.findByUserIdFriendUserId(id, friendId);

        if (friendLink.isPresent()) {
            friendLinkRepository.delete(friendLink.get().getId());
        }
    }


    public Collection<User> findFriendById(Long id) {
        log.info("findFriendById id = {}", id);

        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));

        return userRepository.findAllFriendsByUserId(id);

    }

    public Collection<User> findCommonFriends(Long id, Long otherId) {
        log.info("removeFriend id = {}, otherId = {}", id,  otherId);

        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));

        userRepository.findById(otherId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + otherId));

        return userRepository.findAllCommonFriends(id, otherId);
    }

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
