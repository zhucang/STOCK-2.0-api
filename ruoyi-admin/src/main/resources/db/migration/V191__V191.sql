insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_97','此IP注册数量达到上限' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_97') limit 1;
