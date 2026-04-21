ALTER TABLE `stock`.`site_info`
    ADD COLUMN `android_apk_download_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '安卓apk下载链接' AFTER `android_download_url`;