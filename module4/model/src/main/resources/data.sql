insert into `roles`(name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

insert into `tags`(name)
values ('Tag 1'),
       ('Tag 2'),
       ('Tag 3');

insert into `certificates`(name, description, price, duration, status, create_date, last_update_date)
values ('Certificate 1', 'This is 1 certificate description', 10.00, 3, 'ACTIVE', NOW(), NOW()),
       ('Certificate 2', 'This is 2 certificate description', 20.00, 3, 'ACTIVE', NOW(), NOW()),
       ('Certificate 3', 'This is 3 certificate description', 30.00, 3, 'ACTIVE', NOW(), NOW());

insert into `gift_certificate_tag`(gift_certificate_id, tag_id)
values (1, 1),
       (2, 2),
       (3, 2),
       (3, 3);

insert into `users`(first_name, last_name, username, password, status)
values ('Ivan', 'Ivanov', 'login 1', '$2a$04$diF.vjclauU/GLySt3uom.8Odla8qsuPprnZrCyy85D18XAe9wwyq', 'ACTIVE'),
       ('Petr', 'Petrov', 'login 2', '$2a$04$diF.vjclauU/GLySt3uom.8Odla8qsuPprnZrCyy85D18XAe9wwyq', 'ACTIVE'),
       ('Pavel', 'Pavlov', 'login 3', '$2a$04$diF.vjclauU/GLySt3uom.8Odla8qsuPprnZrCyy85D18XAe9wwyq', 'ACTIVE');

insert into `users_roles`(user_id, roles_id)
values (1, 1),
       (2, 1),
       (3, 2);

insert into `orders`(cost, user_id, create_date)
values (10.00, 1, current_timestamp),
       (20.00, 2, CURRENT_TIMESTAMP),
       (50.00, 1, CURRENT_TIMESTAMP);

insert into `user_order_gift_certificate`(user_order_id, gift_certificate_id)
values (1, 1),
       (2, 3),
       (3, 2),
       (3, 3);
