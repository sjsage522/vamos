CREATE SEQUENCE IF NOT EXISTS user_sequence START 1 INCREMENT 50;
CREATE SEQUENCE IF NOT EXISTS mms_sequence START 1 INCREMENT 50;
CREATE SEQUENCE IF NOT EXISTS category_sequence START 1 INCREMENT 50;
CREATE SEQUENCE IF NOT EXISTS board_sequence START 1 INCREMENT 50;
CREATE SEQUENCE IF NOT EXISTS comment_sequence START 1 INCREMENT 50;
CREATE SEQUENCE IF NOT EXISTS upload_photo_sequence START 1 INCREMENT 50;
CREATE SEQUENCE IF NOT EXISTS chatting_room_sequence START 1 INCREMENT 50;
CREATE SEQUENCE IF NOT EXISTS chatting_content_sequence START 1 INCREMENT 50;

CREATE TABLE IF NOT EXISTS users
(
    id           bigint       NOT NULL, --사용자 PK
    email        varchar(256) NOT NULL, --사용자 이메일
    username     varchar(256) NOT NULL, --사용자 이름
    nickname     varchar(256),          --사용자 별명
    picture      varchar(512),          --사용자 프로필 사진
    password     varchar(256),          --사용자 비밀번호
    role         varchar(256) NOT NULL, --사용자 권한
    phone_number varchar(256),          --사용자 연락처
    x            float8,                --사용자 위치(경도)
    y            float8,                --사용자 위치(위도)
    address_name varchar(512),          --사용자 주소명
    provider     varchar(256) NOT NULL, --인증 제공처
    provider_id  varchar(512),          --인증 제공처 고유값
    created_at   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS mms
(
    id                   bigint       NOT NULL, --mms PK
    phone_number         varchar(256) NOT NULL, --문자 인증 요청자 핸드폰 번호
    certification_number varchar(256) NOT NULL, --인증 번호
    created_at           timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at          timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS category
(
    id          bigint       NOT NULL, --카테고리 PK
    name        varchar(256) NOT NULL, --카테고리 명
    created_at  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS board
(
    id           bigint       NOT NULL,                --게시글 PK
    title        varchar(256) NOT NULL,                --게시글 제목
    content      text         NOT NULL,                --게시글 내용
    price        integer      NOT NULL,                --거래 가격
    user_id      bigint       NOT NULL,                --사용자 PK
    category_id  bigint       NOT NULL,                --카테고리 PK
    x            float8       NOT NULL,                --게시글 위치(경도)
    y            float8       NOT NULL,                --게시글 위치(위도)
    address_name varchar(512),                         --게시글 주소명
    status       varchar(256) NOT NULL DEFAULT 'SALE', --게시글 상태
    created_at   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_board_to_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_board_to_category FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE IF NOT EXISTS comment
(
    id          bigint    NOT NULL, --답글 PK
    content     text      NOT NULL, --답글 내용
    user_id     bigint    NOT NULL, --사용자 PK
    board_id    bigint    NOT NULL, --게시글 PK
    parent_id   bigint,             --부모답글 PK
    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_comment_to_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_comment_to_board FOREIGN KEY (board_id) REFERENCES board (id),
    CONSTRAINT fk_comment_to_comment FOREIGN KEY (parent_id) REFERENCES comment (id)
);

CREATE TABLE IF NOT EXISTS upload_photo
(
    id                 bigint    NOT NULL, --첨부이미지 PK
    board_id           bigint    NOT NULL, --게시글 PK
    original_file_name text,               --원본 이미지 이름
    stored_file_name   text,               --저장된 이미지 이름
    file_download_uri  text,               --이미지 경로
    file_type          varchar(256),       --파일 형식
    file_size          float8,             --파일 크기
    created_at         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at        timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_upload_photo_to_board FOREIGN KEY (board_id) REFERENCES board (id)
);

CREATE TABLE IF NOT EXISTS chatting_room
(
    id          bigint       NOT NULL,                  --채팅방 PK
    seller_id   bigint       NOT NULL,                  --판매자 PK
    buyer_id    bigint       NOT NULL,                  --구매자 PK
    board_id    bigint       NOT NULL,                  --게시글 PK
    category    varchar(256) NOT NULL DEFAULT 'NORMAL', --채팅방 상태
    created_at  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_chatting_room_to_seller FOREIGN KEY (seller_id) REFERENCES users (id),
    CONSTRAINT fk_chatting_room_to_buyer FOREIGN KEY (buyer_id) REFERENCES users (id),
    CONSTRAINT fk_chatting_room_to_board FOREIGN KEY (board_id) REFERENCES board (id)
);

CREATE TABLE IF NOT EXISTS chatting_content
(
    id               bigint    NOT NULL,               --채팅 내용 PK
    user_id          bigint    NOT NULL,               --사용자 PK
    chatting_room_id bigint    NOT NULL,               --채팅방 PK
    content          text      NOT NULL,               --채팅 내용
    is_read          boolean   NOT NULL DEFAULT false, --읽음 여부
    created_at       timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_chatting_content_to_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_chatting_content_to_chatting_room FOREIGN KEY (chatting_room_id) REFERENCES chatting_room (id)
);
