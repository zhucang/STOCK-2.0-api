insert into `stock`.`other_value` (other_key,`name`,other_value,remark,visible_flag) select 'new_user_default_vip_level','新用户默认vip等级','1',null,0 from other_value where not exists (select other_key from other_value where other_key = 'new_user_default_vip_level')limit 1;

insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 77,'是否要求实名认证名字与绑定提现银行卡名字一致',1,1,77 from switch_set where not exists (select id from switch_set where id = 77)limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_70','银行卡持有人名称与实名认证真实姓名不符' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_70') limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_71','填写的真实姓名与初级实名认证真实姓名不符' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_71') limit 1;