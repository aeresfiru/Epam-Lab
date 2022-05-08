drop table if exists `account` CASCADE;
drop table if exists `gift_certificate` CASCADE;
drop table if exists `gift_certificate_tag` CASCADE;
drop table if exists `tag` CASCADE;
drop table if exists `user_order` CASCADE;
drop table if exists `user_order_gift_certificate` CASCADE;

create table `account`
(
    `id`       BIGINT AUTO_INCREMENT,
    `login`    VARCHAR(255) UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `created`  TIMESTAMP    DEFAULT NOW(),
    `updated`  TIMESTAMP    DEFAULT NOW(),
    `status`   VARCHAR(255) DEFAULT 'ACTIVE',
    PRIMARY KEY (`id`)
);

create table `gift_certificate`
(
    `id`          bigint auto_increment,
    `name`        varchar(255) UNIQUE,
    `description` varchar(255)   not null,
    `price`       numeric(19, 2) not null,
    `duration`    smallint       not null,
    `created`     timestamp    DEFAULT NOW(),
    `updated`     timestamp    DEFAULT NOW(),
    `status`      VARCHAR(255) DEFAULT 'ACTIVE',
    primary key (`id`)
);

create table `tag`
(
    `id`      bigint auto_increment,
    `name`    varchar(255) UNIQUE,
    `created` timestamp    DEFAULT NOW(),
    `updated` timestamp    DEFAULT NOW(),
    `status`  VARCHAR(255) DEFAULT 'ACTIVE',
    primary key (`id`)
);

create table `gift_certificate_tag`
(
    `gift_certificate_id` bigint not null,
    `tag_id`              bigint not null,
    primary key (`gift_certificate_id`, `tag_id`),
    CONSTRAINT `FK_gift_certificate_tag_gift_certificate_id`
        FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id)
            ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT `FK_gift_certificate_tag_tag_id`
        FOREIGN KEY (tag_id) REFERENCES tag (id)
            ON UPDATE NO ACTION ON DELETE NO ACTION
);

create table `user_order`
(
    `id`      bigint auto_increment,
    `cost`    numeric(19, 2) not null,
    `user_id` bigint         not null,
    `created` timestamp    DEFAULT NOW(),
    `updated` timestamp    DEFAULT NOW(),
    `status`  VARCHAR(255) DEFAULT 'ACTIVE',
    primary key (`id`)
);

create table `user_order_gift_certificate`
(
    `user_order_id`       bigint not null,
    `gift_certificate_id` bigint not null,
    primary key (`user_order_id`, `gift_certificate_id`),
    CONSTRAINT `FK_user_order_gift_certificate_user_order_id`
        FOREIGN KEY (user_order_id) REFERENCES user_order (id)
            ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT `FK_user_order_gift_certificate_gift_certificate_id`
        FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id)
            ON UPDATE NO ACTION ON DELETE NO ACTION
);
