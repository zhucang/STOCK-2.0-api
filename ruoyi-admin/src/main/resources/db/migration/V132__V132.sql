ALTER TABLE `stock`.`user_info`
    ADD COLUMN `relate_user_id` bigint NULL COMMENT '关联账号ID' AFTER `is_del`;