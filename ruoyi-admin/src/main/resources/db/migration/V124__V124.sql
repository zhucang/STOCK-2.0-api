ALTER TABLE `stock`.`loan_order`
    ADD COLUMN `real_loan_amount` decimal(27, 10) NULL DEFAULT 0 COMMENT '实际放款金额' AFTER `order_price`;