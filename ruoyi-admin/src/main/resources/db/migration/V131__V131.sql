update other_value set other_value = 10 where other_key = 'bigDecimal_scale';

ALTER TABLE `stock`.`stock_product`
    ADD COLUMN `product_name_tr` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品名称-土耳其' AFTER `product_name_it`;

ALTER TABLE `stock`.`cryptocurrency_product`
    ADD COLUMN `product_name_tr` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品名称-土耳其' AFTER `product_name_it`;

ALTER TABLE `stock`.`futures_product`
    ADD COLUMN `product_name_tr` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品名称-土耳其' AFTER `product_name_it`;

ALTER TABLE `stock`.`forex_product`
    ADD COLUMN `product_name_tr` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品名称-土耳其' AFTER `product_name_it`;


ALTER TABLE `stock`.`customer_service`
    ADD COLUMN `customer_service_name_tr` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '客服名称-土耳其' AFTER `customer_service_name_it`;

ALTER TABLE `stock`.`financial_product`
    ADD COLUMN `financial_name_tr` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '理财名称-土耳其' AFTER `financial_name_it`;

ALTER TABLE `stock`.`help_center`
    ADD COLUMN `question_tr` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '问题-土耳其' AFTER `question_it`,
ADD COLUMN `answer_tr` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '答案-土耳其' AFTER `answer_it`;

ALTER TABLE `stock`.`lang_mgr`
    ADD COLUMN `tr` varchar(400) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '土耳其' AFTER `it`;

insert into `stock`.`language` (language,abbreviations,nationalFlag,value,status,sort) select '土耳其','tr',null,'Türkiye',0,20 from `stock`.`language` where not exists (select `language` from `stock`.`language` where `language` = '土耳其') limit 1;

ALTER TABLE `stock`.`loan_product`
    ADD COLUMN `product_name_tr` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品名称-土耳其' AFTER `product_name_it`;

ALTER TABLE `stock`.`mail_config`
    ADD COLUMN `email_title_tr` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '邮件标题-土耳其' AFTER `email_title_it`,
ADD COLUMN `email_content_tr` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '邮件内容-土耳其' AFTER `email_content_it`;

ALTER TABLE `stock`.`platform_currency`
    ADD COLUMN `currency_name_tr` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '币种名称-土耳其' AFTER `currency_name_it`,
ADD COLUMN `currency_desc_tr` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '币种描述-土耳其' AFTER `currency_desc_it`;

ALTER TABLE `stock`.`pop_up`
    ADD COLUMN `pop_up_title_tr` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '弹窗标题-土耳其' AFTER `pop_up_title_it`,
ADD COLUMN `pop_up_content_tr` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '弹窗内容-土耳其' AFTER `pop_up_content_it`;

ALTER TABLE `stock`.`recharge_channel_config`
    ADD COLUMN `channel_name_tr` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '通道名称-土耳其' AFTER `channel_name_it`;

ALTER TABLE `stock`.`self_sell_product`
    ADD COLUMN `product_name_tr` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品名称-土耳其' AFTER `product_name_it`;

ALTER TABLE `stock`.`site_banner`
    ADD COLUMN `banner_img_tr` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '横幅图片-土耳其' AFTER `banner_img_it`,
    ADD COLUMN `banner_title_tr` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '横幅标题-土耳其' AFTER `banner_title_it`,
ADD COLUMN `banner_content_tr` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '横幅内容-土耳其' AFTER `banner_content_it`;

ALTER TABLE `stock`.`site_message`
    ADD COLUMN `msg_title_tr` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '信息标题-土耳其' AFTER `msg_title_it`,
ADD COLUMN `msg_content_tr` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '信息内容-土耳其' AFTER `msg_content_it`;

ALTER TABLE `stock`.`text_lang`
    ADD COLUMN `content_tr` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '文本内容-土耳其' AFTER `content_it`;

ALTER TABLE `stock`.`third_party_recharge_channel`
    ADD COLUMN `channel_name_tr` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '通道名称-土耳其' AFTER `channel_name_it`;

ALTER TABLE `stock`.`trumpet_info`
    ADD COLUMN `trumpet_title_tr` varchar(255) NULL COMMENT '喇叭标题-土耳其' AFTER `trumpet_title_it`,
ADD COLUMN `trumpet_content_tr` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '喇叭-土耳其' AFTER `trumpet_content_it`;

ALTER TABLE `stock`.`website_lang`
    ADD COLUMN `tr` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '土耳其' AFTER `it`;

ALTER TABLE `stock`.`withdraw_channel_config`
    ADD COLUMN `channel_name_tr` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '通道名称-土耳其' AFTER `channel_name_it`;
