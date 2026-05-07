ALTER TABLE `copy_trade_trader`
    ADD COLUMN `monthly_profit_min_rate` decimal(18,6) DEFAULT NULL COMMENT 'APP展示月收益率范围最低' AFTER `default_follow_ratio`,
    ADD COLUMN `monthly_profit_max_rate` decimal(18,6) DEFAULT NULL COMMENT 'APP展示月收益率范围最高' AFTER `monthly_profit_min_rate`,
    ADD COLUMN `annual_profit_min_rate` decimal(18,6) DEFAULT NULL COMMENT 'APP展示年收益率范围最低' AFTER `monthly_profit_max_rate`,
    ADD COLUMN `annual_profit_max_rate` decimal(18,6) DEFAULT NULL COMMENT 'APP展示年收益率范围最高' AFTER `annual_profit_min_rate`;
