ALTER TABLE `stock`.`user_stock_position`
    MODIFY COLUMN `order_num` decimal(27, 10) NOT NULL COMMENT '订单数量' AFTER `order_direction`;

ALTER TABLE `stock`.`user_cryptocurrency_position`
    MODIFY COLUMN `order_num` decimal(27, 10) NOT NULL COMMENT '订单数量' AFTER `order_direction`;

ALTER TABLE `stock`.`user_futures_position`
    MODIFY COLUMN `order_num` decimal(27, 10) NOT NULL COMMENT '订单数量' AFTER `order_direction`;

ALTER TABLE `stock`.`user_forex_position`
    MODIFY COLUMN `order_num` decimal(27, 10) NOT NULL COMMENT '订单数量' AFTER `order_direction`;