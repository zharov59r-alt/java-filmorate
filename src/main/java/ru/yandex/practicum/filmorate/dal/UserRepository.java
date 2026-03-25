package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepository extends BaseRepository<User> {

    private static final String GENERATOR_NAME = "public.s_user_id";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE user_email = ?";
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
