package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepository extends BaseRepository<User> {

    private static final String GENERATOR_NAME = "public.s_user_id";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE user_email = ?";
    private static final String FIND_ALL_FRIENDS_BY_USER_ID_QUERY =
            "SELECT u.* " +
            "FROM   friend_link fl " +
            "JOIN   users u on u.user_id = fl.friend_user_id " +
            "WHERE  fl.user_id = ? " +
            "AND    fl.approved = true";

    private static final String FIND_ALL_COMMON_FRIENDS_QUERY =
            "SELECT u.* " +
            "FROM   friend_link fl1 " +
            "JOIN   friend_link fl2 on fl2.friend_user_id = fl1.friend_user_id " +
                                    "and fl2.user_id = ? " +
                                    "and fl2.approved = true " +
            "JOIN   users u on u.user_id = fl2.friend_user_id " +
            "WHERE  fl1.user_id = ? " +
            "AND    fl1.approved = true";
    private static final String INSERT_QUERY = "INSERT INTO users (user_id, user_email, user_login, user_name, user_birthday)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET user_email = ?, user_login = ?, user_name = ? , user_birthday = ? WHERE user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findAll(FIND_ALL_QUERY);
    }

    public List<User> findAllByEmail(String email) {
        return findAll(FIND_BY_EMAIL_QUERY, email);
    }

    public List<User> findAllFriendsByUserId(Long id) {
        return findAll(FIND_ALL_FRIENDS_BY_USER_ID_QUERY, id);
    }

    public List<User> findAllCommonFriends(Long id, Long otherId) {
        return findAll(FIND_ALL_COMMON_FRIENDS_QUERY, otherId, id);
    }

    public Optional<User> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Optional<User> findByEmail(String email) {
        return findOne(FIND_BY_EMAIL_QUERY, email);
    }

    public User save(User user) {

        user.setId(getNextId(GENERATOR_NAME));

        save(
            INSERT_QUERY,
            user.getId(),
            user.getEmail(),
            user.getLogin(),
            user.getName(),
            user.getBirthday()
        );

        return user;
    }

    public User update(User user) {

        save(
            UPDATE_QUERY,
            user.getEmail(),
            user.getLogin(),
            user.getName(),
            user.getBirthday(),
            user.getId()
        );

        return user;

    }

}
