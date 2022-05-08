delete
from gift_certificate_tag
where true;

delete
from gift_certificate
where true;

delete
from tag
where true;

delete
from user_order_gift_certificate
where true;

delete
from user_order
where true;

delete
from account
where true;

drop procedure if exists insert_tags;
drop procedure if exists insert_certificates;
drop procedure if exists insert_certificate_tags;
drop procedure if exists insert_users;
drop procedure if exists insert_orders;
drop procedure if exists insert_user_order_gift_certificates;

CREATE
    DEFINER = `root`@`localhost` PROCEDURE `insert_tags`()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 1000
        DO
            INSERT INTO tag(id, name)
                VALUE (i, CONCAT('Tag_', i));
            SET i = i + 1;
        END WHILE;
END;

create
    definer = root@localhost procedure insert_certificates()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 10000
        DO
            INSERT INTO gift_certificate(id, name, description, price, duration)
                VALUE (i, CONCAT('Certificate', i), CONCAT('This is ', i, ' certificate description'),
                       ROUND(RAND() * 99.99 + 0.01, 2), FLOOR(RAND() * (366 + 4)));
            SET i = i + 1;
        END WHILE;
END;

CREATE
    DEFINER = `root`@`localhost` PROCEDURE `insert_certificate_tags`()
BEGIN
    DECLARE c_id INT DEFAULT 1;
    DECLARE tQty INT;
    DECLARE t_id INT;
    WHILE c_id <= 10000
        DO
            SET tQty = ROUND(RAND() * 5 + 1);
            WHILE tQty > 0
                DO
                    SET t_id = ROUND(RAND() * (1000 - 21) + 22);
                    INSERT IGNORE INTO gift_certificate_tag(tag_id, gift_certificate_id)
                        VALUE (t_id, c_id);
                    SET tQty = tQty - 1;
                END WHILE;
            SET c_id = c_id + 1;
        END WHILE;
END;

CREATE
    DEFINER = `root`@`localhost` PROCEDURE `insert_users`()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 1000
        DO
            INSERT INTO account(id, login, password)
                VALUE (i, CONCAT('Login_', i), TO_BASE64(CONCAT('Psw123_', i)));
            SET i = i + 1;
        END WHILE;
END;

CREATE
    DEFINER = `root`@`localhost` PROCEDURE `insert_orders`()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE user_id INT;
    WHILE i <= 755
        DO
            SET user_id = ROUND(RAND() * (1000 - 21) + 22);
            INSERT IGNORE INTO user_order(id, user_id, cost)
                VALUE (i, user_id, ROUND(RAND() * 299.99 + 0.01, 2));
            SET i = i + 1;
        END WHILE;
END;

CREATE
    DEFINER = `root`@`localhost` PROCEDURE `insert_user_order_gift_certificates`()
BEGIN
    DECLARE uo_id INT DEFAULT 1;
    DECLARE cQty INT;
    DECLARE c_id INT;
    WHILE uo_id <= 755
        DO
            SET cQty = ROUND(RAND() * 5 + 1);
            WHILE cQty > 0
                DO
                    SET c_id = ROUND(RAND() * (1000 - 21) + 22);
                    INSERT IGNORE INTO user_order_gift_certificate(user_order_id, gift_certificate_id)
                        VALUE (uo_id, c_id);
                    SET cQty = cQty - 1;
                END WHILE;
            SET uo_id = uo_id + 1;
        END WHILE;
END;

call insert_users();
call insert_tags();
call insert_certificates();
call insert_certificate_tags();
call insert_orders();
call insert_user_order_gift_certificates();
