ALTER TABLE `stock`.`site_message`
DROP INDEX `is_private`,
ADD INDEX `is_private`(`is_private`, `id`) USING BTREE,
ADD INDEX(`is_private`, `user_id`);