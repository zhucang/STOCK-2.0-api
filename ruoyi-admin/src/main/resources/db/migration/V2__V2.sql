ALTER TABLE `stock`.`user_info`
    MODIFY COLUMN `img1_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件图片（正面）' AFTER `id_card`,
    MODIFY COLUMN `img2_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件图片（背面）' AFTER `img1_key`,
    MODIFY COLUMN `img3_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件图片（手持）' AFTER `img2_key`,
    ADD COLUMN `auth_method` tinyint(1) NULL DEFAULT 0 COMMENT '实名审核方式 0：身份证 1：驾驶证 2：护照' AFTER `is_active`;

