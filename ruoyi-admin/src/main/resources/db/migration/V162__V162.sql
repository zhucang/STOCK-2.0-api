CREATE TABLE `stock`.`staking_product`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `staking_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '质押名称',
                                     `staking_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '质押图标',
                                     `min_price` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '最低购买',
                                     `max_price` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '最高购买',
                                     `staking_time` int NULL DEFAULT NULL COMMENT '质押天数',
                                     `float_daily_income_min_rate` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '浮动日收益最低',
                                     `float_daily_income_max_rate` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '浮动日收益最高',
                                     `fixed_income_rate` decimal(27, 10) NOT NULL DEFAULT 0.0000000000 COMMENT '固定收益率',
                                     `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态：0：启用 1：禁用',
                                     `sort` int NULL DEFAULT NULL COMMENT '排序',
                                     `break_contract_rate` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '违约金比率',
                                     `user_amount_limit` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '用户余额最低要求',
                                     `currency_id` bigint NOT NULL DEFAULT 1 COMMENT '币种id',
                                     `vip_level_limit` int NULL DEFAULT 0 COMMENT 'vip等级最小限制',
                                     `sql_version` bigint NULL DEFAULT 0 COMMENT 'sql版本',
                                     `staking_name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-英文',
                                     `staking_name_tc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-繁体',
                                     `staking_name_de` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-德国',
                                     `staking_name_es` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-西班牙',
                                     `staking_name_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-法国',
                                     `staking_name_idn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-印度尼西亚',
                                     `staking_name_jp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-日本',
                                     `staking_name_ko` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-韩国',
                                     `staking_name_my` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-马来西亚',
                                     `staking_name_th` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-泰国',
                                     `staking_name_vi` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-越南',
                                     `staking_name_pt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-葡萄牙',
                                     `staking_name_rus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-俄语',
                                     `staking_name_blr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-白俄罗斯',
                                     `staking_name_ida` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-印度',
                                     `staking_name_sa` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-沙特阿拉伯',
                                     `staking_name_ar` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-阿拉伯',
                                     `staking_name_it` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-意大利',
                                     `staking_name_tr` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '质押名称-土耳其',
                                     `is_del` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志 0：未删除 1：已删除',
                                     PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '质押产品配置';

CREATE TABLE `stock`.`staking_order`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
                                     `order_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
                                     `staking_product_id` bigint NOT NULL COMMENT '质押产品ID',
                                     `staking_time` int NULL DEFAULT 1 COMMENT '质押天数',
                                     `buy_price` decimal(27, 10) NOT NULL DEFAULT 0.0000000000 COMMENT '质押初始金额',
                                     `staking_pool_amount` decimal(27, 10) NOT NULL COMMENT '质押池金额',
                                     `user_id` bigint NOT NULL COMMENT '用户ID',
                                     `start_date` datetime(0) NULL DEFAULT NULL COMMENT '质押开始日期',
                                     `end_date` datetime(0) NULL DEFAULT NULL COMMENT '质押结束日期',
                                     `last_interest_time` datetime(0) NULL DEFAULT NULL COMMENT '最后派息时间',
                                     `already_interest_count` int NULL DEFAULT 0 COMMENT '已经派息次数',
                                     `break_contract_rate` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '违约金比率',
                                     `daily_income_rate` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '日收益率',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '下单时间',
                                     `order_status` tinyint(1) NULL DEFAULT 0 COMMENT '订单状态：0：待审核  1：进行中 2：已完成 3：已驳回 4：已赎回',
                                     `currency_id` bigint NOT NULL DEFAULT 3 COMMENT '币种ID',
                                     `float_daily_income_min_rate` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '浮动日收益最低',
                                     `float_daily_income_max_rate` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '浮动日收益最高',
                                     `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
                                     `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                     `sql_version` bigint NULL DEFAULT 0 COMMENT 'sql版本',
                                     PRIMARY KEY (`id`),
                                     UNIQUE INDEX `id`(`id`, `sql_version`) USING BTREE,
                                     INDEX `user_id`(`user_id`) USING BTREE,
                                     INDEX `order_status`(`order_status`, `start_date`, `end_date`) USING BTREE
) ENGINE = InnoDB COMMENT = '质押订单';

ALTER TABLE `stock`.`user_bill_detail`
    MODIFY COLUMN `order_class` int NOT NULL COMMENT '订单类型：0：充值 1：提现  2：上分 3：下分 4：用户股票下单 5：用户股票平仓 6：用户加密货币下单\r\n7：用户加密货币平仓 8:极速交易下单  9：极速交易结算赢返还本金 10：下级返佣  11：资金互转扣除 12：资金互转收入 13：用户取消出金返还 14：提现驳回资金返还 15：极速交易结算获利 16:资金兑换手续费扣除\r\n17:理财产品购买扣除 18:理财产品日收益结算 19：理财产品本金退回 20:理财产品提前赎回违约金 21:新股申购下单扣除 22:申购未中签退回 23:现货交易下单扣除 24:现货交易卖出收入 25:用户期货下单 26：用户期货平仓 27：极速交易结算输返还本金  28：美股极速交易下单扣除 29：加密货币极速交易下单扣除 30：期货极速交易下单扣除  31：美股极速交易结算赢返还本金 32：加密货币极速交易结算赢返还本金 33：期货极速交易结算赢返还本金 34：美股极速交易结算获利 35：加密货币极速交易结算获利 36：期货极速交易结算获利 37：美股极速交易结算输返还本金 38：加密货币极速交易结算输返还本金 39：期货极速交易结算输返还本金 40:彩金赠送（系统充值）41：彩金赠送（福利彩金）42：彩金回收 43:用户外汇下单 44：用户外汇平仓 45:外汇极速交易结算赢返还本金 46：外汇极速交易结算获利 47：外汇极速交易结算输返还本金  48:外汇极速交易下单 49：股票极速交易结算平返还本金 50：加密货币极速交易结算平返还本金 51：期货极速交易结算平返还本金 52：外汇极速交易结算平返还本金 53：冻结资金 54：解冻资金 55：充值赠送彩金 56：注册赠送彩金 57：美股极速交易下单手续费扣除 58：加密货币极速交易下单手续费扣除 59：期货极速交易下单手续费扣除 60：外汇极速交易下单手续费扣除 61:理财产品期满收益结算 62：理财订单驳回本金返还 63：贷款收入 64：贷款还款 65：贷款每日利息扣除 66:提现手续费扣除 67：币币交易买入 68：币币交易卖出 69：币币交易买入手续费 70：币币交易卖出手续费 71：币币交易买入订单撤销返还 72：首次充值赠送彩金 73：转账扣除 74：转账收入 75：转入灵活投资资金扣除 76：转出灵活投资资金返还 77：灵活投资收益 78：质押代币扣除 79：质押结算 80：质押提前赎回违约金' AFTER `relate_order_id`;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 78,'质押代币扣除',78,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'质押代币扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 78) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 79,'质押结算',79,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'质押结算' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 79) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 80,'质押提前赎回违约金',80,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'质押提前赎回违约金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 80) limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_78','质押代币扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_78') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_79','质押结算' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_79') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_80','质押提前赎回违约金' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_80') limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_78','质押代币扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_78') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_79','质押结算' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_79') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_80','质押提前赎回违约金' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_80') limit 1;

ALTER TABLE `stock`.`scheduled_task_exception_log`
    MODIFY COLUMN `type` int NOT NULL COMMENT '类型：1：理财派发利息任务 2：股票止盈止损触发定时任务 3：加密货币止盈止损触发定时任务 4：期货止盈止损触发定时任务 5:新股新币上市定时器 6:新股新币开始申购定时器 7:留仓费收取定时任务 8:股票留仓到期强制平仓任务 9:同步节假日开关定时任务 10:每日16点收盘时保存每日数据定时任务  11:删除分时图数据定时任务\r\n12：外汇止盈止损定时任务 13:股票强制平仓定时任务 14：加密货币强制平仓定时任务 15：期货强制平仓定时任务 16：外汇强制平仓定时任务 17：贷款收取利息定时任务 18：币币交易委托订单自动通过定时任务 19：质押发放收益定时任务' AFTER `exception_info_detail`;
