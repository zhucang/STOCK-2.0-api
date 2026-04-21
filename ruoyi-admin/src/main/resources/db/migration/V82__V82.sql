ALTER TABLE `stock`.`currency_exchange_rate`
    MODIFY COLUMN `exchange_rate` decimal(22, 8) NOT NULL COMMENT '汇率' AFTER `currency_id_to`,
    MODIFY COLUMN `opposite_exchange_rate` decimal(22, 8) NOT NULL COMMENT '反汇率' AFTER `exchange_rate`,
    MODIFY COLUMN `fee_percent` decimal(22, 8) NULL DEFAULT 0.000000 COMMENT '手续费率' AFTER `update_time`;