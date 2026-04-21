ALTER TABLE `stock`.`staking_product`
    ADD COLUMN `open_quantity` decimal(27, 10) NULL DEFAULT NULL COMMENT '开放数量' AFTER `user_amount_limit`,
ADD COLUMN `remaining_quantity` decimal(27, 10) NULL DEFAULT NULL COMMENT '剩余数量' AFTER `open_quantity`;