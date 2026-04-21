ALTER TABLE `stock`.`sys_oper_log`
    MODIFY COLUMN `method` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称' AFTER `business_type`,
    MODIFY COLUMN `oper_param_translate` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求参数翻译' AFTER `oper_param`;