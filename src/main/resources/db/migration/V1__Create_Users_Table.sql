CREATE TABLE `authentication`.`users`
(
  `uid`      INT      NOT NULL,
  `password` CHAR(60) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE INDEX `idx_users_uid_UNIQUE` (`uid` ASC) VISIBLE
);
