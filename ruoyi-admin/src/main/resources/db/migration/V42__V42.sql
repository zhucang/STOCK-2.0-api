ALTER TABLE `stock`.`financial_order`
DROP COLUMN `interest_time`;

ALTER TABLE `stock`.`financial_order`
    ADD COLUMN `order_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号' AFTER `id`;

update `stock`.`financial_order` set order_code = id;
