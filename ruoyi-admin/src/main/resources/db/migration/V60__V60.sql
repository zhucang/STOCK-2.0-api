ALTER TABLE `stock`.`recharge_channel_config`
ADD COLUMN `bank_phone` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑定手机号' AFTER `holder`,
ADD COLUMN `bank_country` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行国家' AFTER `bank_phone`,
ADD COLUMN `aba_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ABA代码' AFTER `bank_country`,
ADD COLUMN `swift` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'SWIFT' AFTER `aba_code`,
ADD COLUMN `post_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮编号码' AFTER `swift`,
ADD COLUMN `user_real_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户地址' AFTER `post_code`,
ADD COLUMN `account_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户类型' AFTER `user_real_address`;