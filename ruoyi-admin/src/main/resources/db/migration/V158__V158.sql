CREATE TABLE `stock`.`user_product_position`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `order_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
                                     `user_id` bigint NOT NULL COMMENT '用户id',
                                     `product_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品代码',
                                     `product_type` tinyint(1) NOT NULL COMMENT '产品类型 1：股票 2：加密货币 3：期货 4：外汇',
                                     `buy_order_time` datetime(0) NOT NULL COMMENT '购买时间',
                                     `buy_order_price` decimal(27, 10) NOT NULL COMMENT '购买价格',
                                     `sell_order_time` datetime(0) NULL DEFAULT NULL COMMENT '卖出时间',
                                     `sell_order_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '卖出价格',
                                     `order_direction` tinyint(1) NOT NULL COMMENT '下单方向 0：买涨 1：买跌',
                                     `order_total_price` decimal(27, 10) NOT NULL COMMENT '订单买入花费',
                                     `order_fee` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '手续费',
                                     `profit_and_lose` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '浮动盈亏',
                                     `all_profit_and_lose` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '总盈亏',
                                     `is_lock` tinyint(1) NULL DEFAULT 0 COMMENT '是否锁仓 0：否 1：是',
                                     `lock_msg` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '锁仓原因',
                                     `currency_id` bigint NOT NULL DEFAULT 1 COMMENT '币种id',
                                     `stop_profit_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '止盈线',
                                     `stop_loss_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '止损线',
                                     `order_status` tinyint(1) NULL DEFAULT 0 COMMENT '订单状态：0：持仓中 1：已平仓',
                                     `sql_version` bigint NULL DEFAULT 0 COMMENT 'sql版本',
                                     PRIMARY KEY (`id`),
                                     UNIQUE INDEX `order_code`(`order_code`) USING BTREE,
                                     INDEX(`product_code`, `product_type`),
                                     INDEX(`order_status`, `user_id`)
) ENGINE = InnoDB COMMENT = '用户合约交易订单';

ALTER TABLE `stock`.`stock_product`
    ADD COLUMN `position_income_coefficient` decimal(27, 10) NULL DEFAULT 10.0000000000 COMMENT '合约收益系数（1：N%）' AFTER `is_self_sell`;

ALTER TABLE `stock`.`cryptocurrency_product`
    ADD COLUMN `position_income_coefficient` decimal(27, 10) NULL DEFAULT 10 COMMENT '合约收益系数（1：N%）' AFTER `standard`;

ALTER TABLE `stock`.`futures_product`
    ADD COLUMN `position_income_coefficient` decimal(27, 10) NULL DEFAULT 10.0000000000 COMMENT '合约收益系数（1：N%）' AFTER `is_self_sell`;

ALTER TABLE `stock`.`forex_product`
    ADD COLUMN `position_income_coefficient` decimal(27, 10) NULL DEFAULT 10.0000000000 COMMENT '合约收益系数（1：N%）' AFTER `is_self_sell`;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新合约交易', 2092, '4',  'hy', 'holdOrder/hy/stockHold', 1, 0, 'C', '0', '0', 'system:userProductPosition:list', 'excel', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where menu_name = '新合约交易' and component = 'holdOrder/hy/stockHold';
