ALTER TABLE `stock`.`user_rebate_rate`
    MODIFY COLUMN `rebate_type` tinyint(1) NULL DEFAULT 0 COMMENT '返佣类型：0：充值返佣 1:理财返佣 2：极速下单返佣' AFTER `rebate_rate`;

ALTER TABLE `stock`.`user_commission_record`
    MODIFY COLUMN `commission_type` tinyint(1) NOT NULL COMMENT '返佣类型 0：充值 1：理财 2：极速交易下单' AFTER `currency_id`;


insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 82,'下级理财下单返佣',82,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'下级理财下单返佣' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 82) limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_82','下级理财下单返佣' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_82') limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_82','下级理财下单返佣' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_82') limit 1;


insert into `stock`.`sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
select 83,'下级极速交易下单返佣',83,'user_bill_detail_order_class',null,'default','N',0,'admin',now(),null,null,'下级极速交易下单返佣' from `stock`.`sys_dict_data` where not exists (select dict_value from `stock`.`sys_dict_data` where dict_type = 'user_bill_detail_order_class' and dict_value = 83) limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_83','下级极速交易下单返佣' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_83') limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_83','下级极速交易下单返佣' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_83') limit 1;
