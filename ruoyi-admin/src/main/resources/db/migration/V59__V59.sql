insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_79','该贷款订单有正在审核的还款订单' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_79') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_80','请先结束未还清的贷款订单' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_80') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_81','还有申请中的贷款订单，无法继续贷款' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_81') limit 1;
