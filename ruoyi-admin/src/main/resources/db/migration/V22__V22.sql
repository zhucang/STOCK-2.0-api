ALTER TABLE `stock`.`platform_currency`
    CHANGE COLUMN `withdraw_type` `currency_type` tinyint(1) NULL DEFAULT 0 COMMENT '币种类型 0：法币 1：虚拟币' AFTER `currency_desc`;

ALTER TABLE `stock`.`withdraw_channel_config`
    ADD COLUMN `withdraw_type` tinyint(1) NULL DEFAULT 0 COMMENT '提现类型 0：银行卡 1：虚拟钱包' AFTER `currency_id`;

update `stock`.`withdraw_channel_config`,`stock`.`platform_currency`
set withdraw_type = currency_type
where withdraw_channel_config.currency_id = platform_currency.id;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_auth_first','请先完成实名认证' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_auth_first') limit 1;

ALTER TABLE `stock`.`withdraw_channel_config`
    ADD COLUMN `arrival_currency_id` bigint NULL COMMENT '提现到账币种id' AFTER `currency_id`;

update `stock`.`withdraw_channel_config`
set arrival_currency_id = currency_id;

