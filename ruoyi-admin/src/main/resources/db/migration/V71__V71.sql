insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_67','币币交易买入' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_67') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_67','币币交易买入' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_67') limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_68','币币交易卖出' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_68') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_68','币币交易卖出' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_68') limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_69','币币交易买入手续费' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_69') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_69','币币交易买入手续费' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_69') limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_70','币币交易卖出手续费' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_70') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_70','币币交易卖出手续费' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_70') limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'deType_orderClass_71','币币交易买入订单撤销返还' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deType_orderClass_71') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'deSummary_orderClass_71','币币交易买入订单撤销返还' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'deSummary_orderClass_71') limit 1;