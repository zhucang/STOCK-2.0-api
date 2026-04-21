ALTER TABLE `stock`.`user_bill_detail`
    MODIFY COLUMN `order_class` int NOT NULL COMMENT '订单类型：0：充值 1：提现  2：上分 3：下分 4：用户股票下单 5：用户股票平仓 6：用户加密货币下单\r\n7：用户加密货币平仓 8:极速交易下单  9：极速交易结算赢返还本金 10：下级返佣  11：资金互转扣除 12：资金互转收入 13：用户取消出金返还 14：提现驳回资金返还 15：极速交易结算获利 16:资金兑换手续费扣除\r\n17:理财产品购买扣除 18:理财产品日收益结算 19：理财产品本金退回 20:理财产品提前赎回违约金 21:新股申购下单扣除 22:申购未中签退回 23:现货交易下单扣除 24:现货交易卖出收入 25:用户期货下单 26：用户期货平仓 27：极速交易结算输返还本金  28：美股极速交易下单扣除 29：加密货币极速交易下单扣除 30：期货极速交易下单扣除  31：美股极速交易结算赢返还本金 32：加密货币极速交易结算赢返还本金 33：期货极速交易结算赢返还本金 34：美股极速交易结算获利 35：加密货币极速交易结算获利 36：期货极速交易结算获利 37：美股极速交易结算输返还本金 38：加密货币极速交易结算输返还本金 39：期货极速交易结算输返还本金 40:彩金赠送（系统充值）41：彩金赠送（福利彩金）42：彩金回收 43:用户外汇下单 44：用户外汇平仓 45:外汇极速交易结算赢返还本金 46：外汇极速交易结算获利 47：外汇极速交易结算输返还本金  48:外汇极速交易下单 49：股票极速交易结算平返还本金 50：加密货币极速交易结算平返还本金 51：期货极速交易结算平返还本金 52：外汇极速交易结算平返还本金 53：冻结资金 54：解冻资金 55：充值赠送彩金 56：注册赠送彩金 57：美股极速交易下单手续费扣除 58：加密货币极速交易下单手续费扣除 59：期货极速交易下单手续费扣除 60：外汇极速交易下单手续费扣除 61:理财产品期满收益结算 62：理财订单驳回本金返还 63：贷款收入 64：贷款还款 65：贷款每日利息扣除' AFTER `relate_order_id`;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_63','贷款收入' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_63') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_63','贷款收入到账' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_63') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_64','贷款还款' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_64') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_64','贷款还款成功' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_64') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_65','贷款每日利息扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_65') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_65','贷款每日利息扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_65') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_74','订单未在进行中' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_74') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_75','还款失败，余额不足' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_75') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_76','此产品最多允许贷款{1}次' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_76') limit 1;


ALTER TABLE `stock`.`loan_order`
    ADD COLUMN `already_loan_days` int NULL DEFAULT 0 COMMENT '已贷款天数' AFTER `create_time`,
ADD COLUMN `need_pay_interest` decimal(22, 6) NULL DEFAULT 0 COMMENT '需要支付利息' AFTER `already_loan_days`;

ALTER TABLE `stock`.`loan_order`
DROP INDEX `user_id`,
ADD INDEX `user_id`(`user_id`, `order_status`, `loan_product_id`) USING BTREE;

ALTER TABLE `stock`.`scheduled_task_exception_log`
    MODIFY COLUMN `type` int NOT NULL COMMENT '类型：1：理财派发利息任务 2：股票止盈止损触发定时任务 3：加密货币止盈止损触发定时任务 4：期货止盈止损触发定时任务 5:新股新币上市定时器 6:新股新币开始申购定时器 7:留仓费收取定时任务 8:股票留仓到期强制平仓任务 9:同步节假日开关定时任务 10:每日16点收盘时保存每日数据定时任务  11:删除分时图数据定时任务\r\n12：外汇止盈止损定时任务 13:股票强制平仓定时任务 14：加密货币强制平仓定时任务 15：期货强制平仓定时任务 16：外汇强制平仓定时任务 17：贷款收取利息定时任务' AFTER `exception_info_detail`;
