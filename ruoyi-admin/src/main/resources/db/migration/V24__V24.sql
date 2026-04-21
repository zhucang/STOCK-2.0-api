ALTER TABLE `stock`.`financial_order`
    MODIFY COLUMN `order_status` tinyint(1) NULL DEFAULT 0 COMMENT '订单状态：0：待审核  1：进行中 2：已完成 3：已驳回 4：已赎回' AFTER `create_time`;

update `stock`.`financial_order` set order_status = 4 where order_status = 2 and financial_time > already_interest_count;