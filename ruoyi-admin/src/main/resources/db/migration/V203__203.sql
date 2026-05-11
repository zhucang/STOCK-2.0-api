ALTER TABLE `stock`.`user_recharge`
    MODIFY COLUMN `recharge_img` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付凭证' AFTER `currency_id`;

update nginx_config
set config_content = replace(
        config_content,
        'server_name  \${domain};',
        'server_name  \${domain}; client_max_body_size 30m;'
    )
where config_content not like '%client_max_body_size 30m;%';