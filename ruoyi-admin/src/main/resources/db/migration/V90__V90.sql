ALTER TABLE `stock`.`fast_trade_order_options`
    ADD COLUMN `sort` int NULL COMMENT '排序' AFTER `lose_money_method`;