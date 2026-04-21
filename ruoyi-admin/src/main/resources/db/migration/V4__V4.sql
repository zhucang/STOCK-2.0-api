ALTER TABLE `stock`.`app_config`
    ADD COLUMN `client_version` varchar(3) NULL COMMENT '客户端版本' AFTER `str_right`,
ADD INDEX `client_version`(`client_version`) USING BTREE;

ALTER TABLE `stock`.`user_recharge`
    ADD COLUMN `recharge_msg` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回信息' AFTER `recharge_img`;