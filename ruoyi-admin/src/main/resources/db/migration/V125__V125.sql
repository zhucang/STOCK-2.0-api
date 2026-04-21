ALTER TABLE `stock`.`platform_currency`
    MODIFY COLUMN `quote_currency` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '行情报价币种' AFTER `sort`;

update platform_currency set real_time_exchange_rate_product = CONCAT('C:',quote_currency,'USD'),real_time_exchange_rate_flag = 1 where currency_type = 0 and (quote_currency != 'USD' and quote_currency != 'USDT' and quote_currency != 'USDC');

update platform_currency set real_time_exchange_rate_product = CONCAT('X:',quote_currency,'USD'),real_time_exchange_rate_flag = 1 where currency_type = 1 and (quote_currency != 'USD' and quote_currency != 'USDT' and quote_currency != 'USDC');

update platform_currency set fixed_exchange_rate = 1,real_time_exchange_rate_flag = 0 where quote_currency = 'USD' or quote_currency = 'USDT' or quote_currency = 'USDC';