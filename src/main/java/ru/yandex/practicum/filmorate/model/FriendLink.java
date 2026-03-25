package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FriendLink {

    private Long id;
    private Long userIdFrom;
    private Long userIdTo;
    private Boolean approved;

}
