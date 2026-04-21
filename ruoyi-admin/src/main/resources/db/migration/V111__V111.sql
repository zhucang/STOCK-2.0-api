CREATE TABLE `stock`.`user_udun_wallet_address`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `user_id` bigint NOT NULL COMMENT '用户id',
                                     `main_coin_type` varchar(50) NOT NULL COMMENT '主币种编号',
                                     `wallet_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '钱包地址',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     PRIMARY KEY (`id`),
                                     INDEX(`main_coin_type`, `wallet_address`, `user_id`) USING BTREE
) ENGINE = InnoDB COMMENT = '优盾加密货币钱包信息';