ALTER TABLE `stock`.`app_config`
    ADD COLUMN `img_url2` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片路径2' AFTER `img_url`;