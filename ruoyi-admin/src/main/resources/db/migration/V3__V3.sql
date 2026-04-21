ALTER TABLE `stock`.`user_vip_level_config`
    ADD COLUMN `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态： 0：启用 1：禁用' AFTER `order_num`;