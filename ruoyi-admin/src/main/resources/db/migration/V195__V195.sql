delete from recharge_channel_config where user_id is not null;

ALTER TABLE `stock`.`recharge_channel_config`
    ADD COLUMN `original_channel_id` bigint NULL COMMENT '原始通道ID' AFTER `user_id`;

