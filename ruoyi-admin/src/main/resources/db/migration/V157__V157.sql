ALTER TABLE `stock`.`recharge_channel_config`
    ADD COLUMN `user_id` bigint NULL COMMENT '用户ID' AFTER `channel_type`,
ADD INDEX(`user_id`);