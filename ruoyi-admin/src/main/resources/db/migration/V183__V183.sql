insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 135,'有未结算极速交易订单时禁止下单',1,1,135 from switch_set where not exists (select id from switch_set where id = 135)limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh,en) select 'hint_96','请等待上一笔订单结算完成','Please wait for the previous order to be settled.' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_96') limit 1;
