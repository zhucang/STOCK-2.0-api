ALTER TABLE `stock`.`bank_property_config`
    ADD COLUMN `config_type` tinyint(1) NULL DEFAULT 0 COMMENT '配置类型 0：用户银行卡 1：平台收款银行卡' AFTER `sort`,
ADD INDEX(`config_type`) USING BTREE;

ALTER TABLE `stock`.`bank_property_config`
DROP INDEX `config_type`,
ADD INDEX `config_type`(`config_type`, `is_visible`) USING BTREE;

ALTER TABLE `stock`.`bank_property_config`
DROP INDEX `property_name`;

insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 93,'是否展示用户充值上传凭证窗口',0,1,80 from switch_set where not exists (select id from switch_set where id = 93)limit 1;

insert into `stock`.`bank_property_config` (property_name, property_desc, lang_key, is_visible, is_require, sort,config_type)
values
    ('bankName','银行名称','bank.name',0,0,null,1),
    ('channelAccount','银行卡号','bank.no',0,0,null,1),
    ('accountOpenBank','开户行','bank.address',0,0,null,1),
    ('holder','开户人姓名','bank.people',0,0,null,1),
    ('bankPhone','绑定手机号','reg_phone',0,1,null,1),
    ('bankCountry','银行国家','bank_bankCountry',0,1,null,1),
    ('abaCode','ABA代码','bank_abaCode',0,1,null,1),
    ('swift','SWIFT','bank_SWIFT',0,1,null,1),
    ('postCode','邮编号码','bank_postCode',0,1,null,1),
    ('userRealAddress','用户地址','bank_userRealAddress',0,1,null,1),
    ('accountType','账户类型','bank_accountType',0,1,null,1);