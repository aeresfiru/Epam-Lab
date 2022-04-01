INSERT INTO `gift_certificate`(id, name, description, price, duration, create_date, last_update_date)
VALUES (11, 'Sweat couple', 'Romantic breakfast at "Luigi" restaurant', 59.99, 10, '2022-02-04 10:31',
        '2022-02-04 22:33'),
       (12, 'My only love', 'Spa day for your mother', 259.99, 10, '2022-01-04 20:31', '2022-01-04 20:54'),
       (13, 'Trampolines', 'Visit trampolines for 5 days', 50.00, 5, '2022-04-01 20:50', '2022-04-01 20:55');

INSERT INTO `tag`(id, name)
VALUES (11, 'Romance'),
       (12, 'Mothers day'),
       (13, '14 February'),
       (14, 'Active');


INSERT INTO `gift_certificate_tag`(certificate_id, tag_id)
VALUES (11, 11),
       (11, 13),
       (12, 12),
       (12, 13),
       (13, 14);
