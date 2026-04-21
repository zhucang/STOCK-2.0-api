ALTER TABLE `stock`.`ip_black_list`
    MODIFY COLUMN `ip_address` varchar(70) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'ip地址' AFTER `id`;