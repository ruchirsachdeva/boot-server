CREATE TABLE `user` (
  `id`       BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `name`     VARCHAR(30)  NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `username` VARCHAR(30)  NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_username` (`username`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `micropost` (
  `id`         BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `content`    VARCHAR(255) NOT NULL,
  `created_at` DATETIME     NOT NULL,
  `user_id`    BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_user` (`user_id`),
  CONSTRAINT `FK_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `relationship` (
  `id`          BIGINT(20) NOT NULL AUTO_INCREMENT,
  `followed_id` BIGINT(20) NOT NULL,
  `follower_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_followed` (`followed_id`),
  KEY `FK_follower` (`follower_id`),
  CONSTRAINT `FK_follower` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_followed` FOREIGN KEY (`followed_id`) REFERENCES `user` (`id`),
  UNIQUE KEY `UK_follower_followed` (`follower_id`, `followed_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

create table `UserConnection` (
    `userId` varchar(255) not null,
    `providerId` varchar(255) not null,
    `providerUserId` varchar(255),
    `rank` int not null,
    `displayName` varchar(255),
    `profileUrl` varchar(512),
    `imageUrl` varchar(512),
    `accessToken` varchar(255) not null,
    `secret` varchar(255),
    `refreshToken` varchar(255),
    `expireTime` bigint,
    primary key (`userId`, `providerId`, `providerUserId`))

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

create unique index `UserConnectionRank` on `UserConnection`(`userId`, `providerId`, `rank`);
create unique index `UserConnectionProviderUser` on `UserConnection`(`providerId`, `providerUserId`);

CREATE TABLE `roles` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_roles` (
  `user_id` BIGINT(20) NOT NULL,
  `role_id` BIGINT(20) NOT NULL,
  KEY `user` (`user_id`),
  KEY `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO roles (role) VALUES ('ROLE_BUYER'), ('ROLE_PARTNER');
