ALTER TABLE `stock`.`user_recharge`
    MODIFY COLUMN `order_status` tinyint(1) NULL DEFAULT 0 COMMENT '状态：0：审核中 1:通过 2：驳回 3：待审核 4：废单' AFTER `recharge_amount`;