ALTER TABLE `stock`.`financial_product`
    ADD COLUMN `is_del` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志 0：未删除 1：已删除' AFTER `financial_name_tr`;