ALTER TABLE `stock`.`other_value`
    ADD COLUMN `permission_type` tinyint(1) NULL DEFAULT 0 COMMENT '权限类型' AFTER `visible_flag`;

update `stock`.`other_value` set permission_type = 1 where other_key = 'allDemoAccount_fastTrade_control';