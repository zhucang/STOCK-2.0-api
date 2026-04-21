ALTER TABLE `stock`.`fast_trade_order_options`
    MODIFY COLUMN `max_buy_amount` decimal(22, 6) NULL COMMENT '最大买入金额' AFTER `min_buy_amount`;

insert into lang_mgr (lang_key,zh) select 'hint_65','此选项下单金额最小为{1}' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_65') limit 1;
insert into lang_mgr (lang_key,zh) select 'hint_66','此选项下单金额最大为{1}' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_66') limit 1;