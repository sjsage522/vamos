CREATE TABLE users
(
    id           bigint generated always as identity,
    phone_number varchar(50) NOT NULL,
    nick_name    varchar(50) NOT NULL,
    created_at   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at  timestamp            DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_phone_number_nick_name UNIQUE (phone_number, nick_name)
);

CREATE TABLE following_user
(
    id          bigint generated always as identity,
    user_id     bigint    NOT NULL,
    target_id   bigint    NOT NULL,
    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp          DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_user_follow_target UNIQUE (user_id, target_id),
    CONSTRAINT fk_following_user_to_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_following_user_to_target FOREIGN KEY (target_id) REFERENCES users (id)
);

CREATE TABLE blocking_user
(
    id          bigint generated always as identity,
    user_id     bigint    NOT NULL,
    target_id   bigint    NOT NULL,
    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp          DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_user_block_target UNIQUE (user_id, target_id),
    CONSTRAINT fk_blocking_user_to_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_blocking_user_to_target FOREIGN KEY (target_id) REFERENCES users (id)
);

CREATE TABLE user_category_keyword
(
    id           bigint generated always as identity,
    user_id      bigint      NOT NULL,
    keyword_name varchar(50) NOT NULL,
    created_at   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at  timestamp            DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_user_keyword UNIQUE (user_id, keyword_name),
    CONSTRAINT fk_user_category_keyword_to_users FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TYPE board_status AS ENUM ('SALE', 'RESERVE', 'SOLD');
CREATE TABLE board
(
    id           bigint generated always as identity,
    title        varchar(50)   NOT NULL,
    content      varchar(1000) NOT NULL,
    user_id      bigint        NOT NULL,
    category_id  bigint        NOT NULL,
    dong_id      bigint        NOT NULL,
    board_status board_status           DEFAULT 'SALE' NOT NULL,
    created_at   timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at  timestamp              DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_board_to_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_board_to_category FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT fk_board_to_dong FOREIGN KEY (dong_id) REFERENCES dong (id)
);

CREATE TABLE following_board
(
    id          bigint generated always as identity,
    user_id     bigint    NOT NULL,
    board_id    bigint    NOT NULL,
    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp          DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_user_board UNIQUE (user_id, board_id),
    CONSTRAINT fk_following_board_to_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_following_board_to_board FOREIGN KEY (board_id) REFERENCES board (id)
);

CREATE TABLE chatting_room
(
    id          bigint generated always as identity,
    user_id     bigint    NOT NULL,
    board_id    bigint    NOT NULL,
    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp          DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_user_board UNIQUE (user_id, board_id),
    CONSTRAINT fk_chatting_room_to_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_chatting_room_to_board FOREIGN KEY (board_id) REFERENCES board (id)
);

CREATE TYPE chatting_category as ENUM ('NORMAL', 'TRADE');
CREATE TABLE chatting_content
(
    id                bigint generated always as identity,
    user_id           bigint       NOT NULL,
    chatting_room_id  bigint       NOT NULL,
    content           varchar(500) NOT NULL,
    chatting_category chatting_category     DEFAULT 'NORMAL' NOT NULL,
    is_read           int          NOT NULL,
    created_at        timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at       timestamp             DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_user_chatting_room UNIQUE (user_id, chatting_room_id),
    CONSTRAINT fk_chatting_content_to_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_chatting_content_to_chatting_room FOREIGN KEY (chatting_room_id) REFERENCES chatting_room (id)
);

CREATE TABLE comment
(
    id          bigint generated always as identity,
    content     varchar(500) NOT NULL,
    user_id     bigint       NOT NULL,
    board_id    bigint       NOT NULL,
    created_at  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp             DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_comment_to_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_comment_to_board FOREIGN KEY (board_id) REFERENCES board (id)
);

CREATE TYPE my_dong_range AS ENUM ('LOW', 'MIDDLE', 'HIGH');
CREATE TABLE my_dong
(
    id            bigint generated always as identity,
    auth_count    int                DEFAULT 0,
    user_id       bigint    NOT NULL,
    dong_id       bigint    NOT NULL,
    my_dong_range my_dong_range      DEFAULT 'LOW' NOT NULL,
    created_at    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at   timestamp          DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_my_dong_to_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_my_dong_to_dong FOREIGN KEY (dong_id) REFERENCES dong (id)
);

CREATE TABLE dong
(
    id             bigint generated always as identity,
    dong_name      varchar(50) NOT NULL,
    x              float       NOT NULL,
    y              float       NOT NULL,
    city_gu_gun_id bigint      NOT NULL,
    created_at     timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at    timestamp            DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_dong_to_city_gu_gun FOREIGN KEY (city_gu_gun_id) REFERENCES city_gu_gun (id)
);

CREATE TABLE city_gu_gun
(
    id               bigint generated always as identity,
    city_gu_gun_name varchar(50) NOT NULL,
    city_do_id       bigint      NOT NULL,
    created_at       timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at      timestamp            DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_city_gu_gun_to_city_do FOREIGN KEY (city_do_id) REFERENCES city_do (id)
);

CREATE TABLE city_do
(
    id           bigint generated always as identity,
    city_do_name varchar(50) NOT NULL,
    created_at   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at  timestamp            DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE category
(
    id          bigint generated always as identity,
    name        varchar(50) NOT NULL,
    created_at  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp            DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE upload_photo
(
    id                 bigint generated always as identity,
    board_id           bigint       NOT NULL,
    original_file_name varchar(500) NOT NULL,
    stored_file_name   varchar(500) NOT NULL,
    created_at         timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at        timestamp             DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_upload_photo_to_board FOREIGN KEY (board_id) REFERENCES board (id)
);

CREATE TABLE admin
(
    id          bigint generated always as identity,
    nickname    varchar(50) NOT NULL,
    password    varchar(50) NOT NULL,
    created_at  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp            DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE notice
(
    id          bigint generated always as identity,
    title       varchar(50) NOT NULL,
    content     text        NOT NULL,
    admin_id    bigint      NOT NULL,
    created_at  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp            DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_notice_to_admin FOREIGN KEY (admin_id) REFERENCES admin (id)
);