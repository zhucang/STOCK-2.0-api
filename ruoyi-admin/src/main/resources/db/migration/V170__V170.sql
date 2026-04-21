ALTER TABLE `stock`.`user_info`
    MODIFY COLUMN `need_order_amount` decimal(27, 10) NULL DEFAULT 0 COMMENT '打码量' AFTER `status`;
