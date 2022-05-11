/*INSERT INTO `tag`(name, created, updated, status)
VALUES ('Tag 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
       ('Tag 2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
       ('Tag 3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO `gift_certificate`(name, description, price, duration, created, updated, status)
VALUES ('Certificate 1', 'This is 1 certificate description', 10.00, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
       ('Certificate 2', 'This is 2 certificate description', 20.00, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
       ('Certificate 3', 'This is 3 certificate description', 30.00, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO `gift_certificate_tag`(gift_certificate_id, tag_id)
VALUES (1, 1),
       (2, 2),
       (3, 2),
       (3, 3);

INSERT INTO `account` (login, password, created, updated, status)
VALUES ('login 1', '12345678qW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
       ('login 2', '12345678qW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
       ('login 3', '12345678qW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO `user_order` (cost, user_id, created, updated, status)
VALUES (10.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
       (20.00, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
       (50.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO `user_order_gift_certificate` (user_order_id, gift_certificate_id)
VALUES (1, 1),
       (2, 3),
       (3, 2),
       (3, 3);
*/