update `stock`.`user_info` set `phone` = null where `phone` = '';
update `stock`.`user_info` set `email` = null where `email` = '';

ALTER TABLE `stock`.`user_info`
ADD INDEX `account_type`(`account_type`) USING BTREE;