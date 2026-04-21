insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_91','模拟账户禁止提现' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_91') limit 1;
