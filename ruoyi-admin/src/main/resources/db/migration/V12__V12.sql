ALTER TABLE `stock`.`user_bill_detail`
    ADD COLUMN `operator_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人名称' AFTER `currency_id`;