ALTER TABLE `stock`.`staking_product`
    ADD COLUMN `auto_approve` tinyint(1) NULL DEFAULT 0 COMMENT '是否自动审核 0：是 1：否' AFTER `user_amount_limit`;

ALTER TABLE `stock`.`user_bill_detail`
    MODIFY COLUMN `order_class` int NOT NULL COMMENT '订单类型：0：充值 1：提现  2：上分 3：下分 4：用户股票下单 5：用户股票平仓 6：用户加密货币下单\\r\\n7：用户加密货币平仓 8:极速交易下单  9：极速交易结算赢返还本金 10：下级返佣  11：资金互转扣除 12：资金互转收入 13：用户取消出金返还 14：提现驳回资金返还 15：极速交易结算获利 16:资金兑换手续费扣除\\r\\n17:理财产品购买扣除 18:理财产品日收益结算 19：理财产品本金退回 20:理财产品提前赎回违约金 21:新股申购下单扣除 22:申购未中签退回 23:现货交易下单扣除 24:现货交易卖出收入 25:用户期货下单 26：用户期货平仓 27：极速交易结算输返还本金  28：美股极速交易下单扣除 29：加密货币极速交易下单扣除 30：期货极速交易下单扣除  31：美股极速交易结算赢返还本金 32：加密货币极速交易结算赢返还本金 33：期货极速交易结算赢返还本金 34：美股极速交易结算获利 35：加密货币极速交易结算获利 36：期货极速交易结算获利 37：美股极速交易结算输返还本金 38：加密货币极速交易结算输返还本金 39：期货极速交易结算输返还本金 40:彩金赠送（系统充值）41：彩金赠送（福利彩金）42：彩金回收 43:用户外汇下单 44：用户外汇平仓 45:外汇极速交易结算赢返还本金 46：外汇极速交易结算获利 47：外汇极速交易结算输返还本金  48:外汇极速交易下单 49：股票极速交易结算平返还本金 50：加密货币极速交易结算平返还本金 51：期货极速交易结算平返还本金 52：外汇极速交易结算平返还本金 53：冻结资金 54：解冻资金 55：充值赠送彩金 56：注册赠送彩金 57：美股极速交易下单手续费扣除 58：加密货币极速交易下单手续费扣除 59：期货极速交易下单手续费扣除 60：外汇极速交易下单手续费扣除 61:理财产品期满收益结算 62：理财订单驳回本金返还 63：贷款收入 64：贷款还款 65：贷款每日利息扣除 66:提现手续费扣除 67：币币交易买入 68：币币交易卖出 69：币币交易买入手续费 70：币币交易卖出手续费 71：币币交易买入订单撤销返还 72：首次充值赠送彩金 73：转账扣除 74：转账收入 75：转入灵活投资资金扣除 76：转出灵活投资资金返还 77：灵活投资收益 78：质押代币扣除 79：质押结算 81：质押订单驳回本金返还 81' AFTER `relate_order_id`;
insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 81,'质押订单驳回本金返还',81,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'质押订单驳回本金返还' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 81) limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_81','质押订单驳回本金返还' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_81') limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_81','质押订单驳回本金返还' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_81') limit 1;

