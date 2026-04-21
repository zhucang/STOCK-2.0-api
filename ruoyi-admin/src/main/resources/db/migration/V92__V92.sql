ALTER TABLE `stock`.`recharge_channel_config`
    MODIFY COLUMN `channel_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '通道名称' AFTER `id`;