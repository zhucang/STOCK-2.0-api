insert into order_fee_setting (`key`,fee_rate,remark)
values
('stock_fast_trade_buy_fee',0.300000,'美股极速交易买入手续费率'),
('cryptocurrency_fast_trade_buy_fee',0.300000,'加密货币极速交易买入手续费率'),
('futures_fast_trade_buy_fee',0.300000,'期货极速交易买入手续费率'),
('forex_fast_trade_buy_fee',0.300000,'外汇极速交易买入手续费率');

ALTER TABLE `stock`.`fast_trade_order`
    ADD COLUMN `handing_fee` decimal(22, 6) NULL DEFAULT 0 COMMENT '手续费' AFTER `order_price`;

insert into text_lang(content,remark,content_type) values ('福利中心','福利中心',14);

ALTER TABLE `stock`.`user_bill_detail`
    MODIFY COLUMN `order_class` int NOT NULL COMMENT '订单类型：0：充值 1：提现  2：上分 3：下分 4：用户股票下单 5：用户股票平仓 6：用户加密货币下单\r\n7：用户加密货币平仓 8:极速交易下单  9：极速交易结算赢返还本金 10：下级返佣  11：资金互转扣除 12：资金互转收入 13：用户取消出金返还 14：提现驳回资金返还 15：极速交易结算获利 16:资金兑换手续费扣除\r\n17:理财产品购买扣除 18:理财产品日收益结算 19：理财产品本金退回 20:理财产品提前赎回违约金 21:新股申购下单扣除 22:申购未中签退回 23:现货交易下单扣除 24:现货交易卖出收入 25:用户期货下单 26：用户期货平仓 27：极速交易结算输返还本金  28：美股极速交易下单扣除 29：加密货币极速交易下单扣除 30：期货极速交易下单扣除  31：美股极速交易结算赢返还本金 32：加密货币极速交易结算赢返还本金 33：期货极速交易结算赢返还本金 34：美股极速交易结算获利 35：加密货币极速交易结算获利 36：期货极速交易结算获利 37：美股极速交易结算输返还本金 38：加密货币极速交易结算输返还本金 39：期货极速交易结算输返还本金 40:彩金赠送（系统充值）41：彩金赠送（福利彩金）42：彩金回收 43:用户外汇下单 44：用户外汇平仓 45:外汇极速交易结算赢返还本金 46：外汇极速交易结算获利 47：外汇极速交易结算输返还本金  48:外汇极速交易下单 49：股票极速交易结算平返还本金 50：加密货币极速交易结算平返还本金 51：期货极速交易结算平返还本金 52：外汇极速交易结算平返还本金 53：冻结资金 54：解冻资金 55：充值赠送彩金 56：注册赠送彩金 57：美股极速交易下单手续费扣除 58：加密货币极速交易下单手续费扣除 59：期货极速交易下单手续费扣除 60：外汇极速交易下单手续费扣除' AFTER `relate_order_id`;

insert into lang_mgr (lang_key,zh) select 'deType_orderClass_57','美股极速交易下单手续费扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_57') limit 1;
insert into lang_mgr (lang_key,zh) select 'deSummary_orderClass_57','美股极速交易下单手续费扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_57') limit 1;
insert into lang_mgr (lang_key,zh) select 'deType_orderClass_58','加密货币极速交易下单手续费扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_58') limit 1;
insert into lang_mgr (lang_key,zh) select 'deSummary_orderClass_58','加密货币极速交易下单手续费扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_58') limit 1;
insert into lang_mgr (lang_key,zh) select 'deType_orderClass_59','期货极速交易下单手续费扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_59') limit 1;
insert into lang_mgr (lang_key,zh) select 'deSummary_orderClass_59','期货极速交易下单手续费扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_59') limit 1;
insert into lang_mgr (lang_key,zh) select 'deType_orderClass_60','外汇极速交易下单手续费扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_60') limit 1;
insert into lang_mgr (lang_key,zh) select 'deSummary_orderClass_60','外汇极速交易下单手续费扣除' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_60') limit 1;

ALTER TABLE `stock`.`text_lang`
    MODIFY COLUMN `content_type` int NULL DEFAULT NULL COMMENT '类型：0：App实名认证页面规格 1：App银行卡页面规则 2：App提现页面规格 3:App按天配资规则 4:App挖矿收益计算 5:App挖矿关于违约金 6:公司简介 7:开户须知 8:操盘须知 9:投资风险 10：产品细节 12：注册协议 13：推广规则 14：福利中心' AFTER `remark`;