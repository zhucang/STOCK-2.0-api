ALTER TABLE `stock`.`loan_order`
    ADD COLUMN `guarantor_id` bigint NULL COMMENT '担保人ID' AFTER `img4_key`,
ADD COLUMN `guarantor_name` varchar(255) NULL COMMENT '担保人名称' AFTER `guarantor_id`;