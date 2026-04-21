CREATE TABLE `stock`.`site_message_type`  (
                                     `site_message_type_id` bigint NOT NULL AUTO_INCREMENT COMMENT '站内信类型',
                                     `site_message_type_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站内信类型名称',
                                     `site_message_type_name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-英文',
                                     `site_message_type_name_tc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-繁体',
                                     `site_message_type_name_de` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-德国',
                                     `site_message_type_name_es` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-西班牙',
                                     `site_message_type_name_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-法国',
                                     `site_message_type_name_idn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-印度尼西亚',
                                     `site_message_type_name_jp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-日本',
                                     `site_message_type_name_ko` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-韩国',
                                     `site_message_type_name_my` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-马来西亚',
                                     `site_message_type_name_th` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-泰国',
                                     `site_message_type_name_vi` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-越南',
                                     `site_message_type_name_pt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-葡萄牙',
                                     `site_message_type_name_rus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-俄语',
                                     `site_message_type_name_blr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-白俄罗斯',
                                     `site_message_type_name_ida` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-印度',
                                     `site_message_type_name_sa` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-沙特阿拉伯',
                                     `site_message_type_name_ar` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-阿拉伯',
                                     `site_message_type_name_it` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-意大利',
                                     `site_message_type_name_tr` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '站内信类型名称-土耳其',
                                     PRIMARY KEY (`site_message_type_id`)
) COMMENT = '站内信类型';

ALTER TABLE `stock`.`site_message`
    ADD COLUMN `site_message_type_id` bigint NULL COMMENT '站内信类型ID' AFTER `is_private`,
DROP INDEX `user_id`,
ADD INDEX `user_id`(`user_id`, `is_visible`, `site_message_type_id`) USING BTREE;
