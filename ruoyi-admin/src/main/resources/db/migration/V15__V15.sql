ALTER TABLE `stock`.`user_recharge`
    ADD COLUMN `recharge_method` tinyint(1) NULL DEFAULT 0 COMMENT '充值方式： 0：线下充值 1：在线支付' AFTER `user_amount_after`,
ADD INDEX(`recharge_method`, `user_id`, `order_status`, `currency_id`);

insert into other_value (other_key,`name`,other_value,remark,visible_flag) values ('onlineRechargeSupportFiatsCurrency','在线支付支持的法币','EUR',null,0);

insert into lang_mgr (lang_key,zh) select 'hint_67','不支持此币种' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_67') limit 1;
insert into lang_mgr (lang_key,zh) select 'hint_68','发起支付失败' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_68') limit 1;