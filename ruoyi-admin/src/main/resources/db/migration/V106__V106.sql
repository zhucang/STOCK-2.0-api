delete from other_value where other_key in ('first_recharge_winnings_rate','first_recharge_winnings_amount');

ALTER TABLE `stock`.`recharge_channel_config`
    ADD COLUMN `first_recharge_winnings_amount` decimal(22, 6) NULL DEFAULT 0.000000 COMMENT '首次充值赠送金额' AFTER `customer_url`,
ADD COLUMN `first_recharge_winnings_rate` decimal(22, 6) NULL DEFAULT 0.000000 COMMENT '首次充值赠送比率' AFTER `first_recharge_winnings_amount`,
ADD COLUMN `first_recharge_winnings_method` tinyint(1) NULL DEFAULT 0 COMMENT '首次充值赠送方式 0：按比例赠送 1：按固定金额赠送' AFTER `first_recharge_winnings_rate`,
ADD COLUMN `daily_recharge_winnings_amount` decimal(22, 6) NULL DEFAULT 0.000000 COMMENT '日常充值赠送金额' AFTER `first_recharge_winnings_method`,
ADD COLUMN `daily_recharge_winnings_rate` decimal(22, 6) NULL DEFAULT 0.000000 COMMENT '日常充值赠送比率' AFTER `daily_recharge_winnings_amount`,
ADD COLUMN `daily_recharge_winnings_method` tinyint(1) NULL DEFAULT 0 COMMENT '日常充值赠送方式 0：按比例赠送 1：按固定金额赠送' AFTER `daily_recharge_winnings_rate`;

ALTER TABLE `stock`.`fast_trade_order_options`
    MODIFY COLUMN `duration_label` tinyint(1) NOT NULL COMMENT '时长标签（0:秒 1：分钟 2：小时 3：天）' AFTER `duration_value`;