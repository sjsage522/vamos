INSERT INTO AUTHORITY (AUTHORITY_NAME) VALUES ('ROLE_USER');
INSERT INTO AUTHORITY (AUTHORITY_NAME) VALUES ('ROLE_ADMIN');

INSERT INTO USERS (ID, USERNAME, PASSWORD, NICKNAME, PHONE_NUMBER, X, Y, ADDRESS_NAME) VALUES (0, 'admin', '$2a$10$wc/Je/WEvp2.0A3FcAe1jeN8UkZYQ.Irxqks4XSL63pUz/Dg1zI4a', 'admin', '01053402457', 0, 0, '?');
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_NAME) VALUES (0, 'ROLE_ADMIN');
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_NAME) VALUES (0, 'ROLE_USER');

INSERT INTO CATEGORY (ID, NAME) VALUES (1, 'DIGITAL_DEVICE');
INSERT INTO CATEGORY (ID, NAME) VALUES (2, 'HOME_APPLIANCES');
INSERT INTO CATEGORY (ID, NAME) VALUES (3, 'FURNITURE_INTERIOR');
INSERT INTO CATEGORY (ID, NAME) VALUES (4, 'INFANT_CHILD');
INSERT INTO CATEGORY (ID, NAME) VALUES (5, 'TODDLER_BOOK');
INSERT INTO CATEGORY (ID, NAME) VALUES (6, 'PROCESSED_FOOD');
INSERT INTO CATEGORY (ID, NAME) VALUES (7, 'SPORTS_LEISURE');
INSERT INTO CATEGORY (ID, NAME) VALUES (8, 'WOMEN_FASHION');
INSERT INTO CATEGORY (ID, NAME) VALUES (9, 'MEN_FASHION');
INSERT INTO CATEGORY (ID, NAME) VALUES (10, 'GAME_HOBBY');
INSERT INTO CATEGORY (ID, NAME) VALUES (11, 'BEAUTY');
INSERT INTO CATEGORY (ID, NAME) VALUES (12, 'PET_SUPPLIES');
INSERT INTO CATEGORY (ID, NAME) VALUES (13, 'BOOKS_TICKETS_RECORDS');
INSERT INTO CATEGORY (ID, NAME) VALUES (14, 'PLANT');
INSERT INTO CATEGORY (ID, NAME) VALUES (15, 'ETC');
INSERT INTO CATEGORY (ID, NAME) VALUES (16, 'WANT_BUY');


