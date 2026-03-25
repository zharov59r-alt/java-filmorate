package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FriendLink {

    private Long id;
    private Long userId;
    private Long friendUserId;
    private Boolean approved;

}
