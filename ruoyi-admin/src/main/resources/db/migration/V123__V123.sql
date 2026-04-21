ALTER TABLE `stock`.`platform_currency`
    ADD COLUMN `exchange_handling_fee_rate` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '资金兑换手续费率' AFTER `balance_convert_max_limit`,
ADD COLUMN `real_time_exchange_rate_flag` tinyint(1) NULL DEFAULT 0 COMMENT '实时汇率标志 0：固定汇率 1：实时汇率' AFTER `exchange_handling_fee_rate`,
ADD COLUMN `fixed_exchange_rate` decimal(27, 10) NULL DEFAULT 0 COMMENT '固定汇率' AFTER `real_time_exchange_rate_flag`,
ADD COLUMN `real_time_exchange_rate_product` varchar(50) NULL COMMENT '实时汇率品种' AFTER `fixed_exchange_rate`;


insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 113,' app钱包地址是否*号显示',0,1,113 from switch_set where not exists (select id from switch_set where id = 113)limit 1;

