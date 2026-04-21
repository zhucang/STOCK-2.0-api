ALTER TABLE `stock`.`user_withdraw`
    ADD COLUMN `order_code` varchar(100) NULL COMMENT '订单号' AFTER `user_id`,
ADD UNIQUE INDEX(`order_code`) USING BTREE;

update `stock`.`user_withdraw` set order_code = id;