DROP SCHEMA `gifts-dev`;
CREATE SCHEMA `gifts-dev`;
use `gifts-dev`;
#DROP SCHEMA `gifts`;
#CREATE SCHEMA `gifts`;
#use `gifts`;
CREATE TABLE `gift_certificate`
(
    `id`               bigint         NOT NULL AUTO_INCREMENT,
    `name`             varchar(100)   NOT NULL UNIQUE,
    `description`      varchar(500)   NOT NULL,
    `price`            decimal(10, 2) NOT NULL,
    `duration`         smallint       NOT NULL,
    `create_date`      timestamp      NOT NULL default CURRENT_TIMESTAMP(),
    `last_update_date` timestamp      NOT NULL default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `PK_gift_certificate`
        PRIMARY KEY (`id`)
);

CREATE TABLE `tag`
(
    `id`   bigint      NOT NULL AUTO_INCREMENT,
    `name` varchar(20) NOT NULL UNIQUE,
    CONSTRAINT `PK_tag`
        PRIMARY KEY (`id`)
);

CREATE TABLE `gift_certificate_tag`
(
    `certificate_id` bigint NOT NULL,
    `tag_id`         bigint NOT NULL,
    CONSTRAINT `PK_gift_certificate_tag`
        PRIMARY KEY (`certificate_id`, `tag_id`),
    CONSTRAINT `FK_gift_certificate_tag_certificate`
        FOREIGN KEY (`certificate_id`) REFERENCES `gift_certificate` (`id`)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_gift_certificate_tag_tag`
        FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
            ON DELETE CASCADE ON UPDATE CASCADE
);
