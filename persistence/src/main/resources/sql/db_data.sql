INSERT INTO `gift_certificate`(id, name, description, price, duration, create_date, last_update_date)
VALUES (2, 'Certificate 2', 'This is 2 certificate description', 100.00, 3, '2022-04-01 00:01', '2022-04-01T00:02'),
       (3, 'Certificate 3', 'This is 3 certificate description', 100.00, 3, '2022-04-01 00:05', '2022-04-01T00:06'),
       (4, 'Certificate 4', 'This is 4 certificate description', 100.00, 3, '2022-04-01 00:10', '2022-04-01T00:11'),
       (5, 'Certificate 5', 'This is 5 certificate description', 100.00, 3, '2022-04-01 00:15', '2022-04-01T00:16'),
       (6, 'Certificate 6', 'This is 6 certificate description', 100.00, 3, '2022-04-01 00:20', '2022-04-01T00:21'),
       (7, 'Certificate 7', 'This is 7 certificate description', 100.00, 3, '2022-04-01 00:25', '2022-04-01T00:26'),
       (8, 'Certificate 8', 'This is 8 certificate description', 100.00, 3, '2022-04-01 00:30', '2022-04-01T00:30'),
       (9, 'Certificate 9', 'This is 9 certificate description', 100.00, 3, '2022-04-01 00:35', '2022-04-01T00:35'),
       (10, 'Certificate 10', 'This is 10 certificate description', 100.00, 3, '2022-04-01 00:40', '2022-04-01T00:40');

INSERT INTO `tag`(id, name)
VALUES (2, 'Tag 2'),
       (3, 'Tag 3'),
       (4, 'Tag 4'),
       (5, 'Tag 5'),
       (6, 'Tag 6'),
       (7, 'Tag 7'),
       (8, 'Tag 8'),
       (9, 'Tag 9'),
       (10, 'Tag 10');

INSERT INTO `gift_certificate_tag`(certificate_id, tag_id)
VALUES (2, 2),
       (2, 4),
       (3, 5),
       (3, 8),
       (4, 3),
       (5, 2),
       (6, 5),
       (6, 2),
       (6, 7),
       (7, 4),
       (7, 6),
       (7, 10),
       (8, 10),
       (10, 10),
       (10, 6),
       (10, 4),
       (9, 8);
