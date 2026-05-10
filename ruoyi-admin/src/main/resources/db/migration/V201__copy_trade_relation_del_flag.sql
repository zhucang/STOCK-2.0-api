ALTER TABLE `copy_trade_relation`
    ADD COLUMN `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志 0未删除 1已删除' AFTER `status`,
    ADD COLUMN `delete_time` datetime DEFAULT NULL COMMENT '删除时间' AFTER `del_flag`;
