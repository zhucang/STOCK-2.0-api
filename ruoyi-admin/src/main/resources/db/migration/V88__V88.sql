ALTER TABLE `stock`.`self_sell_product`
    MODIFY COLUMN `initial_price` decimal(22, 6) NULL DEFAULT 0 COMMENT '初始价格' AFTER `product_code`,
    ADD COLUMN `is_direct_listing` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否直接上市 0：是 1：否' AFTER `initial_price`,
    MODIFY COLUMN `relate_product_id` bigint NOT NULL COMMENT '关联产品列表的id' AFTER `product_type`,
DROP INDEX `product_code`,
ADD UNIQUE INDEX `product_code`(`product_type`, `product_code`, `is_direct_listing`, `status`) USING BTREE;

ALTER TABLE `stock`.`cryptocurrency_product`
    MODIFY COLUMN `standard` decimal(22, 6) NULL DEFAULT NULL COMMENT '一个标准手 = 数量N' AFTER `sort`;


ALTER TABLE `stock`.`product_setting`
    MODIFY COLUMN `buy_min_num` decimal(22, 6) NULL DEFAULT 1 COMMENT '最小购买数量' AFTER `buy_min_amt`,
    MODIFY COLUMN `buy_max_num` decimal(22, 6) NULL DEFAULT 1000000 COMMENT '最大购买数量' AFTER `buy_min_num`,
    MODIFY COLUMN `hand_num` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '50/100/150/200' COMMENT '快捷交易手数' AFTER `site_lever`,
    MODIFY COLUMN `buy_num_times` int NULL DEFAULT NULL COMMENT 'N分钟内交易手数不能超过M手（N）' AFTER `buy_same_nums`,
    MODIFY COLUMN `buy_num_lots` decimal(22, 6) NULL DEFAULT NULL COMMENT 'N分钟内交易手数不能超过M手（M）' AFTER `buy_num_times`,
    MODIFY COLUMN `hand_correspond_num` decimal(22, 6) NULL DEFAULT 100 COMMENT '一手对应的数量' AFTER `cant_sell_times`;