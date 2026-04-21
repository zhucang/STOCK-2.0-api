insert into `stock`.`sys_dict_type` (dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) select '用户流水记录类型','user_bill_detail_order_class',0,'admin',now(),null,null,'用户流水记录类型' from `stock`.`sys_dict_type` where not exists (select dict_type from `stock`.`sys_dict_type` where dict_type = 'user_bill_detail_order_class') limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 0,'充值',0,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'充值' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 0) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 1,'提现',1,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'提现' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 1) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 2,'上分',2,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'上分' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 2) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 3,'下分',3,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'下分' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 3) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 4,'用户股票下单',4,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'用户股票下单' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 4) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 5,'用户股票平仓',5,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'用户股票平仓' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 5) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 6,'用户加密货币下单',6,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'用户加密货币下单' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 6) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 7,'用户加密货币平仓',7,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'用户加密货币平仓' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 7) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 8,'极速交易下单',8,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'极速交易下单' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 8) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 9,'极速交易结算赢返还本金',9,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'极速交易结算赢返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 9) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 10,'下级返佣',10,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'下级返佣' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 10) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 11,'资金互转扣除',11,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'资金互转扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 11) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 12,'资金互转收入',12,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'资金互转收入' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 12) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 13,'用户取消出金返还',13,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'用户取消出金返还' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 13) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 14,'提现驳回资金返还',14,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'提现驳回资金返还' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 14) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 15,'极速交易结算获利',15,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'极速交易结算获利' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 15) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 16,'资金兑换手续费扣除',16,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'资金兑换手续费扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 16) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 17,'理财产品购买扣除',17,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'理财产品购买扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 17) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 18,'理财产品日收益结算',18,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'理财产品日收益结算' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 18) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 19,'理财产品本金退回',19,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'理财产品本金退回' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 19) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 20,'理财产品提前赎回违约金',20,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'理财产品提前赎回违约金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 20) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 21,'新股申购下单扣除',21,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'新股申购下单扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 21) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 22,'申购未中签退回',22,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'申购未中签退回' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 22) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 23,'现货交易下单扣除',23,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'现货交易下单扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 23) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 24,'现货交易卖出收入',24,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'现货交易卖出收入' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 24) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 25,'用户期货下单',25,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'用户期货下单' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 25) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 26,'用户期货平仓',26,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'用户期货平仓' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 26) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 27,'极速交易结算输返还本金',27,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'极速交易结算输返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 27) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 28,'美股极速交易下单扣除',28,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'美股极速交易下单扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 28) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 29,'加密货币极速交易下单扣除',29,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'加密货币极速交易下单扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 29) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 30,'期货极速交易下单扣除',30,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'期货极速交易下单扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 30) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 31,'美股极速交易结算赢返还本金',31,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'美股极速交易结算赢返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 31) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 32,'加密货币极速交易结算赢返还本金',32,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'加密货币极速交易结算赢返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 32) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 33,'期货极速交易结算赢返还本金',33,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'期货极速交易结算赢返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 33) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 34,'美股极速交易结算获利',34,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'美股极速交易结算获利' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 34) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 35,'加密货币极速交易结算获利',35,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'加密货币极速交易结算获利' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 35) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 36,'期货极速交易结算获利',36,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'期货极速交易结算获利' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 36) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 37,'美股极速交易结算输返还本金',37,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'美股极速交易结算输返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 37) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 38,'加密货币极速交易结算输返还本金',38,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'加密货币极速交易结算输返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 38) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 39,'期货极速交易结算输返还本金',39,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'期货极速交易结算输返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 39) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 40,'彩金赠送（系统充值）',40,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'彩金赠送（系统充值）' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 40) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 41,'彩金赠送（福利彩金）',41,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'彩金赠送（福利彩金）' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 41) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 42,'彩金回收',42,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'彩金回收' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 42) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 43,'用户外汇下单',43,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'用户外汇下单' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 43) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 44,'用户外汇平仓',44,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'用户外汇平仓' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 44) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 45,'外汇极速交易结算赢返还本金',45,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'外汇极速交易结算赢返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 45) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 46,'外汇极速交易结算获利',46,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'外汇极速交易结算获利' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 46) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 47,'外汇极速交易结算输返还本金',47,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'外汇极速交易结算输返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 47) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 48,'外汇极速交易下单',48,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'外汇极速交易下单' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 48) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 49,'股票极速交易结算平返还本金',49,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'股票极速交易结算平返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 49) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 50,'加密货币极速交易结算平返还本金',50,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'加密货币极速交易结算平返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 50) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 51,'期货极速交易结算平返还本金',51,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'期货极速交易结算平返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 51) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 52,'外汇极速交易结算平返还本金',52,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'外汇极速交易结算平返还本金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 52) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 53,'冻结资金',53,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'冻结资金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 53) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 54,'解冻资金',54,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'解冻资金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 54) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 55,'充值赠送彩金',55,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'充值赠送彩金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 55) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 56,'注册赠送彩金',56,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'注册赠送彩金' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 56) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 57,'美股极速交易下单手续费扣除',57,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'美股极速交易下单手续费扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 57) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 58,'加密货币极速交易下单手续费扣除',58,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'加密货币极速交易下单手续费扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 58) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 59,'期货极速交易下单手续费扣除',59,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'期货极速交易下单手续费扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 59) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 60,'外汇极速交易下单手续费扣除',60,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'外汇极速交易下单手续费扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 60) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 61,'理财产品期满收益结算',61,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'理财产品期满收益结算' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 61) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 62,'理财订单驳回本金返还',62,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'理财订单驳回本金返还' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 62) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 63,'贷款收入',63,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'贷款收入' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 63) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 64,'贷款还款',64,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'贷款还款' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 64) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 65,'贷款每日利息扣除',65,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'贷款每日利息扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 65) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 66,'提现手续费扣除',66,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'提现手续费扣除' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 66) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 67,'币币交易买入',67,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'币币交易买入' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 67) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 68,'币币交易买入手续费',68,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'币币交易买入手续费' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 68) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 69,'币币交易卖出手续费',69,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'币币交易卖出手续费' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 69) limit 1;

insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 70,'币币交易买入订单撤销返还',70,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'币币交易买入订单撤销返还' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 70) limit 1;

