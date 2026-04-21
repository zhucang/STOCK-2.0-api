ALTER TABLE `stock`.`platform_currency`
    ADD COLUMN `currency_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '币种图标' AFTER `currency_desc`;