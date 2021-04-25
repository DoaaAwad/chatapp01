CREATE USER 'edafa'@'localhost' IDENTIFIED BY 'edafa';

GRANT ALL PRIVILEGES ON * . * TO 'edafa'@'localhost';

/**Create DB Scheme**/
CREATE DATABASE  IF NOT EXISTS `chat_app` ;
USE `chat_app`;

/** Create User Table  **/
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
`user_id` INT NOT NULL,
`user_name` VARCHAR(45) NULL,
`password` VARCHAR(45) NULL,

PRIMARY KEY (`user_id`),

UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC) );

# Insert some data
INSERT INTO user (user_id, username, password)
VALUES (1, 'Doaa', '123');
INSERT INTO user (user_id, username, password)
VALUES (2, 'Mohamed','456');
INSERT INTO user (user_id, username, password)
VALUES (3, 'mary','789');
