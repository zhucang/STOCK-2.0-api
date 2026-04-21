ALTER TABLE `stock`.`self_sell_product`
    ADD COLUMN `product_desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品描述' AFTER `product_code`;

ALTER TABLE `stock`.`user_info`
    ADD COLUMN `delete_version` bigint NULL DEFAULT 0 COMMENT '逻辑删除version' AFTER `remark`;

update user_info set delete_version = id where is_del = 1;

ALTER TABLE `stock`.`user_info`
DROP INDEX `invite_code`,
DROP INDEX `user_account`,
DROP INDEX `phone`,
DROP INDEX `email`,
ADD UNIQUE INDEX `invite_code`(`invite_code`, `delete_version`) USING BTREE,
ADD UNIQUE INDEX `user_account`(`user_account`, `delete_version`) USING BTREE,
ADD UNIQUE INDEX `phone`(`phone`, `delete_version`) USING BTREE,
ADD UNIQUE INDEX `email`(`email`, `delete_version`) USING BTREE;


