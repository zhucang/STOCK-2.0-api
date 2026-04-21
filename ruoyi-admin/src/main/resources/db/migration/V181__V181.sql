ALTER TABLE `stock`.`user_info`
    ADD COLUMN `dapp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'dapp_id' AFTER `real_name`;