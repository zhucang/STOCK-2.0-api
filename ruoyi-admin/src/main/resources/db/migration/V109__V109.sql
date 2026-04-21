insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_89','请检查用户名是否仅由大小写字母、数字和非特殊符号组成' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_89') limit 1;

ALTER TABLE `stock`.`self_sell_product`
    ADD COLUMN `product_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品图标' AFTER `product_code`;