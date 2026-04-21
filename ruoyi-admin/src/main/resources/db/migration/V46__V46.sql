ALTER TABLE `stock`.`help_center`
    ADD COLUMN `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态 0：启用 1：禁用' AFTER `answer`,
ADD COLUMN `sort` int NULL COMMENT '排序' AFTER `status`;