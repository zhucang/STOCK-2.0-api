ALTER TABLE `stock`.`user_cryptocurrency_position`
DROP INDEX `user_id`,
ADD INDEX `order_status`(`order_status`, `user_id`) USING BTREE;