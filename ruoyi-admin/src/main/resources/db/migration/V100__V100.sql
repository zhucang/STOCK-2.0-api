insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_88','请输入至少{1}位数密码' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_88') limit 1;
