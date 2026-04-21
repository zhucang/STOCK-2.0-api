ALTER TABLE `stock`.`mail_config`
    MODIFY COLUMN `email_account` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '邮箱账户' AFTER `id`,
    MODIFY COLUMN `email_password` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '邮箱密码' AFTER `email_account`,
    MODIFY COLUMN `smtp_domain` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'smtp域名' AFTER `email_password`;