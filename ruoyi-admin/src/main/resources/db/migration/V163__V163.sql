CREATE TABLE `stock`.`staking_order_interest_record`
(
    `id`                bigint          NOT NULL AUTO_INCREMENT COMMENT '质押订单派息记录ID',
    `user_id`           bigint          NOT NULL COMMENT '用户ID',
    `staking_order_id`  bigint          NOT NULL COMMENT '质押订单ID',
    `daily_income_rate` decimal(27, 10) NOT NULL DEFAULT 0.0000000000 COMMENT '日收益率',
    `pay_time`          datetime(0) NOT NULL COMMENT '派息时间',
    `pay_amount`        decimal(27, 10) NOT NULL COMMENT '派息金额',
    PRIMARY KEY (`id`),
    INDEX(`staking_order_id`)
) ENGINE = InnoDB COMMENT = '质押订单派息记录';