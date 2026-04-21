CREATE TABLE `stock`.`user_api_key`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                     `user_id` bigint NOT NULL COMMENT '用户ID',
                                     `api_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'api秘钥',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     PRIMARY KEY (`id`),
                                     UNIQUE INDEX `api_key`(`api_key`) USING BTREE,
                                     INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '应用秘钥apiKey';