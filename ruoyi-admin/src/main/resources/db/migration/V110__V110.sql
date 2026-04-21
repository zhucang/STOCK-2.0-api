ALTER TABLE `stock`.`bank_property_config`
    ADD COLUMN `input_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '输入类型' AFTER `sort`;

ALTER TABLE `stock`.`platform_currency`
    ADD COLUMN `sava_scale` tinyint NULL DEFAULT 6 COMMENT '保留小数位数' AFTER `currency_type`;