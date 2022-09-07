drop schema if exists `gifts`;
create schema if not exists `gifts`;
use `gifts`;

CREATE TABLE `certificates`
(
    `id`          bigint         NOT NULL AUTO_INCREMENT,
    `name`        varchar(100)   NOT NULL UNIQUE,
    `description` varchar(500)   NOT NULL,
    `price`       decimal(10, 2) NOT NULL,
    `duration`    smallint       NOT NULL,
    `created`     timestamp      NOT NULL default CURRENT_TIMESTAMP(),
    `updated`     timestamp      NOT NULL default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `PK_gift_certificate`
        PRIMARY KEY (`id`)
);

CREATE TABLE `tags`
(
    `id`   bigint      NOT NULL AUTO_INCREMENT,
    `name` varchar(20) NOT NULL UNIQUE,
    CONSTRAINT `PK_tag`
        PRIMARY KEY (`id`)
);

CREATE TABLE `certificate_tags`
(
    `certificate_id` bigint NOT NULL,
    `tag_id`         bigint NOT NULL,
    CONSTRAINT `PK_gift_certificate_tag`
        PRIMARY KEY (`certificate_id`, `tag_id`),
    CONSTRAINT `FK_gift_certificate_tag_certificate`
        FOREIGN KEY (`certificate_id`) REFERENCES `certificates` (`id`)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_gift_certificate_tag_tag`
        FOREIGN KEY (`tag_id`) REFERENCES tags (`id`)
            ON DELETE CASCADE ON UPDATE CASCADE
);
