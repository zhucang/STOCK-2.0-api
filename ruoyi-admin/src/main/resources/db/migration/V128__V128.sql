ALTER TABLE `stock`.`new_product_apply_purchase`
    MODIFY COLUMN `apply_purchase_min` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '单笔申购数量最低' AFTER `listed_day_increase_rate`,
    MODIFY COLUMN `apply_purchase_max` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '单笔申购数量最高' AFTER `apply_purchase_min`;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_90','单笔申购数量范围为{1}~{2}' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_90') limit 1;
