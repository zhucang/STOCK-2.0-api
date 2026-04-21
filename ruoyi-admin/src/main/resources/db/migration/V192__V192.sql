ALTER TABLE `stock`.`financial_product`
    ADD COLUMN `user_buy_limit` int NULL DEFAULT 0 COMMENT '用户限购数量' AFTER `currency_id`;

insert into `stock`.`lang_mgr` (lang_key,zh,en) select 'hint_98', '该产品用户限购{1}次', 'Each user is limited to purchasing this product {1} time.' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_98') limit 1;
