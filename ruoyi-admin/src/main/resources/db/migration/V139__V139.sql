truncate table `stock`.`user_api_key`;

ALTER TABLE `stock`.`user_api_key`
    ADD COLUMN `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'app id' AFTER `user_id`,
DROP INDEX `api_key`,
ADD UNIQUE INDEX(`app_id`);