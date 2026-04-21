ALTER TABLE `stock`.`recharge_channel_config`
    MODIFY COLUMN `channel_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款账号' AFTER `channel_name`,
    MODIFY COLUMN `channel_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道图标' AFTER `wallet_receipt_code`,
    MODIFY COLUMN `channel_min_limit` decimal(27, 10) NULL DEFAULT 0 COMMENT '最小入金' AFTER `account_type`,
    MODIFY COLUMN `channel_max_limit` decimal(27, 10) NULL DEFAULT 0 COMMENT '最大入金' AFTER `channel_min_limit`;