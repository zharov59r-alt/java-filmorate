package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.FriendLink;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FriendLinkMapper {

    public static FriendLink toFriendLink(Long userId, Long friendId) {
        FriendLink dto = new FriendLink();
        dto.setUserId(userId);
        dto.setFriendUserId(friendId);
        dto.setApproved(true);
        return dto;
    }

}