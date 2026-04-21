insert into switch_set (id,switch_name,status,`type`) values (63,'用户实名认证是否上传手持身份证',0,1);

ALTER TABLE `stock`.`cryptocurrency_product`
    ADD COLUMN `product_desc` varchar(200) NULL COMMENT '产品描述' AFTER `product_code`;

ALTER TABLE `stock`.`spot_trade_order` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `user_id`;

ALTER TABLE `stock`.`fast_trade_order` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `user_id`;

ALTER TABLE `stock`.`user_stock_position` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `user_id`;

ALTER TABLE `stock`.`user_cryptocurrency_position` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `user_id`;

ALTER TABLE `stock`.`user_futures_position` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `user_id`;

ALTER TABLE `stock`.`user_forex_position` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `user_id`;

ALTER TABLE `stock`.`stock_everyday_record` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `id`;

ALTER TABLE `stock`.`cryptocurrency_everyday_record` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `id`;

ALTER TABLE `stock`.`futures_everyday_record` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `id`;

ALTER TABLE `stock`.`forex_everyday_record` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `id`;

ALTER TABLE `stock`.`fast_order_control_config` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `id`;

ALTER TABLE `stock`.`fast_trade_order_options` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `product_name`;

ALTER TABLE `stock`.`product_quote_control` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `id`;

ALTER TABLE `stock`.`realtime` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `id`;

ALTER TABLE `stock`.`realtime_temp` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `id`;

ALTER TABLE `stock`.`self_sell_product` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `product_name`;

ALTER TABLE `stock`.`self_sell_product_daily_data_config` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `self_sell_product_id`;

ALTER TABLE `stock`.`self_sell_product_realtime` MODIFY COLUMN `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码' AFTER `id`;