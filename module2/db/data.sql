use gifts;

INSERT INTO certificates(name, description, price, duration)
VALUES ('Certificate 1', 'Description 1', 10.00, 90),
       ('Certificate 2', 'Description 1', 5.00, 30),
       ('Certificate 3', 'Description 1', 7.00, 31);

INSERT INTO tags(name)
VALUES ('Tag 1'),
       ('Tag 2'),
       ('Tag 3'),
       ('Tag 4'),
       ('Tag 5');

INSERT INTO certificate_tags (certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 3),
       (2, 4),
       (2, 5),
       (3, 3);
