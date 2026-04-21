ALTER TABLE `stock`.`customer_service`
    ADD COLUMN `customer_service_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '客服图标' AFTER `customer_service_line`;