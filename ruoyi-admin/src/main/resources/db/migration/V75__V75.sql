ALTER TABLE `stock`.`financial_product`
    ADD COLUMN `open_quantity` decimal(22, 6) NULL DEFAULT 100000 COMMENT '开放数量' AFTER `auto_approve`,
ADD COLUMN `remaining_quantity` decimal(22, 6) NULL DEFAULT 100000 COMMENT '剩余数量' AFTER `open_quantity`,
ADD COLUMN `sql_version` bigint NULL DEFAULT 0 COMMENT 'sql版本' AFTER `vip_level_limit`;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_84','产品余量不足' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_84') limit 1;

update financial_product set remaining_quantity = (select financial_product.open_quantity-IFNULL(sum(buy_price),0) from financial_order where financial_product_id = financial_product.id);