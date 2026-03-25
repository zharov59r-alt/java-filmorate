create sequence if not exists s_film_id;
create sequence if not exists s_film_genre_id;
create sequence if not exists s_film_like_id;
create sequence if not exists s_friend_link_id;
create sequence if not exists s_genre_id;
create sequence if not exists s_rating_mpa_id;
create sequence if not exists s_user_id;



create table if not exists rating_mpa (
rating_mpa_id        INT8                 primary key,
rating_mpa_name      TEXT                 not null
);

create table if not exists film (
film_id              INT8                 primary key,
rating_mpa_id        INT8                 null,
film_name            TEXT                 not null,
film_description     TEXT                 null,
film_release_date    DATE                 null,
film_duration        NUMERIC              null
);

create table if not exists genre (
genre_id             INT8                 primary key,
genre_name           TEXT                 not null
);

create table if not exists film_genre (
film_genre_id        INT8                 primary key,
film_id              INT8                 not null,
genre_id             INT8                 not null
);

create table if not exists users (
user_id              INT8                 primary key,
user_email           TEXT                 not null,
user_login           TEXT                 null,
user_name            TEXT                 null,
user_birthday        DATE                 null
);

create table if not exists film_like (
film_like_id         INT8                 primary key,
film_id              INT8                 not null,
user_id              INT8                 not null
);

create table if not exists friend_link (
friend_link_id       INT8                 primary key,
user_id_from         INT8                 not null,
user_id_to           INT8                 not null,
approved             BOOL                 not null
);


alter table film
    add constraint FK_FILM_FK_FILM_R_RATING_M foreign key (rating_mpa_id)
        references rating_mpa (rating_mpa_id);

alter table film_genre
    add constraint FK_FILM_GEN_REFERENCE_FILM foreign key (film_id)
        references film (film_id);

alter table film_genre
    add constraint FK_FILM_GEN_REFERENCE_GENRE foreign key (genre_id)
        references genre (genre_id);

alter table film_like
    add constraint FK_FILM_LIK_REFERENCE_FILM foreign key (film_id)
        references film (film_id);

alter table film_like
    add constraint FK_FILM_LIK_REFERENCE_USER foreign key (user_id)
        references users (user_id);

alter table friend_link
    add constraint FK_friend_link_user_id_from foreign key (user_id_from)
        references users (user_id);

alter table friend_link
    add constraint FK_friend_link_user_id_to foreign key (user_id_to)
        references users (user_id);

MERGE INTO rating_mpa (rating_mpa_id, rating_mpa_name)
KEY (rating_mpa_id)
VALUES
    (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');



MERGE INTO genre (genre_id, genre_name)
KEY (genre_id)
VALUES
    (1, 'Драма'),
    (2, 'Комедия'),
    (3, 'Боевик'),
    (4, 'Триллер'),
    (5, 'Ужасы'),
    (6, 'Фантастика'),
    (7, 'Фэнтези'),
    (8, 'Мелодрама'),
    (9, 'Детектив'),
    (10, 'Приключения');















