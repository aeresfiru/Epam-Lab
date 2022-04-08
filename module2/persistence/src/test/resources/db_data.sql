INSERT INTO `gift_certificate`(id, name, description, price, duration, create_date, last_update_date)
VALUES (2, 'Certificate 2', 'This is 2 certificate description', 100.00, 3, '2022-04-01 00:01', '2022-04-01T00:02'),
       (3, 'Certificate 3', 'This is 3 certificate description', 100.00, 3, '2022-04-01 00:01', '2022-04-01T00:02'),
       (4, 'Certificate 4', 'This is 4 certificate description', 100.00, 3, '2022-04-01 00:01', '2022-04-01T00:02');

INSERT INTO `tag`(id, name)
VALUES (2, 'Tag 2'),
       (3, 'Tag 3'),
       (4, 'Tag 4'),
       (5, 'Tag 5');


INSERT INTO `gift_certificate_tag`(certificate_id, tag_id)
VALUES (2, 2),
       (2, 4),
       (3, 3),
       (3, 4),
       (4, 5);
