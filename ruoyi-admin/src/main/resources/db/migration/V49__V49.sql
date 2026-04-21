ALTER TABLE `stock`.`loan_order`
    ADD COLUMN `loan_start_time` datetime NULL COMMENT '贷款开始时间' AFTER `break_contract_daily_rate`,
ADD COLUMN `loan_end_time` datetime NULL COMMENT '贷款结束时间' AFTER `loan_start_time`;

CREATE TABLE `stock`.`loan_order_interest_record`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `loan_order_id` bigint NOT NULL COMMENT '贷款订单id',
                                     `order_price` decimal(22, 6) NOT NULL DEFAULT 0.000000 COMMENT '订单金额',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     `already_settle_flag` tinyint(1) NULL DEFAULT 0 COMMENT '是否结算 0：是 1：否',
                                     PRIMARY KEY (`id`, `loan_order_id`)
) ENGINE = InnoDB COMMENT = '贷款订单利息生成记录';

insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_73','贷款金额范围{1}~{2}' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_73') limit 1;

ALTER TABLE `stock`.`loan_order`
    MODIFY COLUMN `img4_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行流水清单' AFTER `img3_key`,
    MODIFY COLUMN `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者' AFTER `img4_key`,
    MODIFY COLUMN `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间' AFTER `update_by`,
    MODIFY COLUMN `sql_version` bigint NULL DEFAULT 0 COMMENT 'sql版本' AFTER `update_time`;