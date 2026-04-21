ALTER TABLE `stock`.`self_sell_product_daily_data_config`
    ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者' AFTER `product_type`,
ADD COLUMN `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间' AFTER `create_by`,
ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者' AFTER `create_time`,
ADD COLUMN `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间' AFTER `update_by`;