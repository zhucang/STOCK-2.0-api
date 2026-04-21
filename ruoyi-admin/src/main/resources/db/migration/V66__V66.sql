CREATE TABLE `stock`.`bibi_trade_order`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `order_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
                                     `user_id` bigint NOT NULL COMMENT '用户id',
                                     `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码',
                                     `product_type` tinyint(1) NOT NULL COMMENT '产品类型 （1：股票 2：加密货币 3：期货 4：外汇）',
                                     `order_amount` decimal(22, 6) NOT NULL COMMENT '订单金额',
                                     `turnover_amount` decimal(22, 6) NOT NULL DEFAULT 0 COMMENT '成交额',
                                     `order_volume` decimal(22, 6) NOT NULL DEFAULT 0 COMMENT '成交量',
                                     `handing_fee` decimal(22, 6) NULL DEFAULT 0.000000 COMMENT '手续费',
                                     `product_price` decimal(22, 6) NOT NULL COMMENT '产品价格',
                                     `order_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '订单类型 0：买入 1：卖出',
                                     `order_method` tinyint(1) NOT NULL DEFAULT 0 COMMENT '订单方式 0：限价委托 1：市价委托',
                                     `order_status` tinyint(1) NULL DEFAULT 0 COMMENT '订单状态 0：委托中 1：成交 2：撤单 3：冻结',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
                                     PRIMARY KEY (`id`),
                                     INDEX(`user_id`, `product_type`, `order_status`) USING BTREE
) ENGINE = InnoDB COMMENT = '币币交易订单';

CREATE TABLE `stock`.`user_bibi_assets`  (
                                     `id` bigint NOT NULL COMMENT 'id',
                                     `user_id` bigint NOT NULL COMMENT '用户id',
                                     `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码',
                                     `product_type` tinyint(1) NOT NULL COMMENT '产品类型：1：美股 2：加密货币 3:期货 4：外汇',
                                     `bibi_amount` decimal(22, 6) NOT NULL DEFAULT 0 COMMENT '持有数量',
                                     `buy_amount_all` decimal(22, 6) NULL DEFAULT 0 COMMENT '买入总金额',
                                     `sell_amount_all` decimal(22, 6) NULL DEFAULT 0 COMMENT '卖出总金额',
                                     `buy_and_sell_amount_difference` decimal(22, 6) NULL DEFAULT 0 COMMENT '买入与卖出金额的差值',
                                     `sql_version` bigint NULL DEFAULT 0 COMMENT 'sql版本',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     PRIMARY KEY (`id`),
                                     INDEX(`user_id`, `product_code`, `product_type`) USING BTREE
) ENGINE = InnoDB COMMENT = '用户币币资产';