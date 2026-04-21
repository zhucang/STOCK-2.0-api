CREATE TABLE `stock`.`self_sell_product_real_time`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码',
                                     `product_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '类型：1：股票 2：加密货币 3：期货 4:外汇',
                                     `reference_price` decimal(30, 10) NOT NULL COMMENT '参考价格',
                                     `reference_change_rate` decimal(30, 10) NOT NULL COMMENT '参考涨跌幅',
                                     `open_price` decimal(30, 10) NOT NULL COMMENT '开盘价格',
                                     `close_price` decimal(30, 10) NOT NULL COMMENT '收盘价格',
                                     `high_price` decimal(30, 10) NOT NULL COMMENT '最高价格',
                                     `low_price` decimal(30, 10) NOT NULL COMMENT '最低价格',
                                     `average_price` decimal(30, 10) NOT NULL DEFAULT 0.000000 COMMENT '平均价格',
                                     `volumes` decimal(30, 10) NOT NULL COMMENT '成交量',
                                     `amount` decimal(30, 10) NOT NULL COMMENT '成交额',
                                     `specific_time` datetime(0) NOT NULL COMMENT '具体时间',
                                     `create_time` datetime(0) NULL COMMENT '创建时间',
                                     PRIMARY KEY (`id`),
                                     INDEX(`product_type`, `product_code`, `specific_time`) USING BTREE
) ENGINE = InnoDB COMMENT = '自营产品分时图数据';