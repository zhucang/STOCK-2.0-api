insert into `stock`.`other_value` (other_key,`name`,other_value,remark,visible_flag) select 'user_financial_max_buy_count_every_day','用户理财每日可购买次数','2',null,0 from other_value where not exists (select other_key from other_value where other_key = 'user_financial_max_buy_count_every_day')limit 1;
insert into `stock`.`other_value` (other_key,`name`,other_value,remark,visible_flag) select 'user_financial_max_buy_count_every_month','用户理财每月可购买次数','5',null,0 from other_value where not exists (select other_key from other_value where other_key = 'user_financial_max_buy_count_every_month')limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_85','已达到每日可购买上限' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_85') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_86','已达到每月可购买上限' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_86') limit 1;
