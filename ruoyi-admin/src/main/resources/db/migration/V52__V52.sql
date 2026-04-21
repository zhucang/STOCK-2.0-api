insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 90,'贷款金额是否计入打码量',1,1,90 from switch_set where not exists (select id from switch_set where id = 90)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 91,'贷款未结清前是否禁止提现',0,1,91 from switch_set where not exists (select id from switch_set where id = 91)limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_77','还有未结清的贷款订单，无法继续提现' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_77') limit 1;
