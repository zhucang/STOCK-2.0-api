insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 81,'打码量计算模式（0：用户充值 1：用户充值+上分 2：用户充值+上分+彩金）',0,null,81 from switch_set where not exists (select id from switch_set where id = 81)limit 1;

ALTER TABLE `stock`.`financial_order`
    ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者' AFTER `float_daily_income_max_rate`,
ADD COLUMN `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间' AFTER `update_by`;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_62','理财订单驳回本金返还' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_62') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_62','理财申请被驳回，金额返还' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_62') limit 1;

ALTER TABLE `stock`.`user_bill_detail`
    MODIFY COLUMN `order_class` int NOT NULL COMMENT '订单类型：0：充值 1：提现  2：上分 3：下分 4：用户股票下单 5：用户股票平仓 6：用户加密货币下单\r\n7：用户加密货币平仓 8:极速交易下单  9：极速交易结算赢返还本金 10：下级返佣  11：资金互转扣除 12：资金互转收入 13：用户取消出金返还 14：提现驳回资金返还 15：极速交易结算获利 16:资金兑换手续费扣除\r\n17:理财产品购买扣除 18:理财产品日收益结算 19：理财产品本金退回 20:理财产品提前赎回违约金 21:新股申购下单扣除 22:申购未中签退回 23:现货交易下单扣除 24:现货交易卖出收入 25:用户期货下单 26：用户期货平仓 27：极速交易结算输返还本金  28：美股极速交易下单扣除 29：加密货币极速交易下单扣除 30：期货极速交易下单扣除  31：美股极速交易结算赢返还本金 32：加密货币极速交易结算赢返还本金 33：期货极速交易结算赢返还本金 34：美股极速交易结算获利 35：加密货币极速交易结算获利 36：期货极速交易结算获利 37：美股极速交易结算输返还本金 38：加密货币极速交易结算输返还本金 39：期货极速交易结算输返还本金 40:彩金赠送（系统充值）41：彩金赠送（福利彩金）42：彩金回收 43:用户外汇下单 44：用户外汇平仓 45:外汇极速交易结算赢返还本金 46：外汇极速交易结算获利 47：外汇极速交易结算输返还本金  48:外汇极速交易下单 49：股票极速交易结算平返还本金 50：加密货币极速交易结算平返还本金 51：期货极速交易结算平返还本金 52：外汇极速交易结算平返还本金 53：冻结资金 54：解冻资金 55：充值赠送彩金 56：注册赠送彩金 57：美股极速交易下单手续费扣除 58：加密货币极速交易下单手续费扣除 59：期货极速交易下单手续费扣除 60：外汇极速交易下单手续费扣除 61:理财产品期满收益结算 62：理财订单驳回本金返还' AFTER `relate_order_id`;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
('理财订单审核', 2148, '1',  null, '', 1, 0, 'F', '0', '0', 'system:financialOrder:updateFinancialOrderStatus',       '#', 'admin', sysdate(), '', null, ''),
('理财产品人工赎回', 2148, '1',  null, '', 2, 0, 'F', '0', '0', 'system:financialOrder:manualRedemption',       '#', 'admin', sysdate(), '', null, ''),
('理财产品人工结算', 2148, '1',  null, '', 3, 0, 'F', '0', '0', 'system:financialOrder:manualSettlement',       '#', 'admin', sysdate(), '', null, '');
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:financialOrder:updateFinancialOrderStatus','system:financialOrder:manualRedemption','system:financialOrder:manualSettlement');

ALTER TABLE `stock`.`financial_order`
DROP INDEX `order_status`,
ADD INDEX `order_status`(`order_status`, `start_date`, `end_date`) USING BTREE;