ALTER TABLE `stock`.`user_withdraw`
    MODIFY COLUMN `receipt_account_info` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收款账户信息' AFTER `operator_name`;