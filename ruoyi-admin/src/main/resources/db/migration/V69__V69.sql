ALTER TABLE `stock`.`app_config`
    MODIFY COLUMN `obj_property_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'obj_字段名' AFTER `obj_name`;