package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendLink;

import java.util.Optional;

@Slf4j
@Repository
public class FriendLinkRepository extends BaseRepository<FriendLink> {

    private static final String GENERATOR_NAME = "public.s_friend_link_id";
    private static final String FIND_BY_USER_ID_FRIEND_USER_ID_QUERY = "SELECT * FROM friend_link where user_id = ? and friend_user_id = ?";
    private static final String DELETE_QUERY = "DELETE from friend_link where friend_link_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO friend_link (friend_link_id, user_id, friend_user_id, approved)" +
            "VALUES (?, ?, ?, ?)";

    public FriendLinkRepository(JdbcTemplate jdbc, RowMapper<FriendLink> mapper) {
        super(jdbc, mapper);
    }

    public Optional<FriendLink> findByUserIdFriendUserId(Long userId, Long friendUserId) {
        return findOne(FIND_BY_USER_ID_FRIEND_USER_ID_QUERY, userId, friendUserId);
    }

    public FriendLink save(FriendLink friendLink) {

        friendLink.setId(getNextId(GENERATOR_NAME));

        save(
            INSERT_QUERY,
            friendLink.getId(),
            friendLink.getUserId(),
            friendLink.getFriendUserId(),
            friendLink.getApproved()
        );

        return friendLink;
    }

    public void delete(Long id) {
        delete(DELETE_QUERY, id);
    }

}
