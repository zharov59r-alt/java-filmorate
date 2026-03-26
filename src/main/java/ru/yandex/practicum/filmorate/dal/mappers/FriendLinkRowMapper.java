package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendLink;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendLinkRowMapper implements RowMapper<FriendLink> {

    @Override
    public FriendLink mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FriendLink friendLink = new FriendLink();
        friendLink.setId(resultSet.getLong("friend_link_id"));
        friendLink.setUserId(resultSet.getLong("user_id"));
        friendLink.setFriendUserId(resultSet.getLong("friend_user_id"));
        friendLink.setApproved(resultSet.getBoolean("approved"));
        return friendLink;
    }
}
