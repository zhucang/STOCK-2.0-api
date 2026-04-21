ALTER TABLE `stock`.`user_withdraw`
    MODIFY COLUMN `withdraw_fee` decimal(22, 6) NULL DEFAULT 0.000000 COMMENT '提现手续费' AFTER `withdraw_amount`,
    ADD COLUMN `received_amount` decimal(22, 6) NOT NULL COMMENT '到账金额' AFTER `withdraw_fee`;

update `stock`.`user_withdraw` set received_amount = withdraw_amount;

ALTER TABLE `stock`.`user_withdraw`
    ADD INDEX(`withdraw_status`, `currency_id`, `statistical_report`);