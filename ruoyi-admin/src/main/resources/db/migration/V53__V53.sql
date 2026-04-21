CREATE TABLE `stock`.`user_dm_amount_change_record`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `user_id` bigint NOT NULL COMMENT '用户id',
                                     `order_amount` decimal(22, 6) NULL DEFAULT NULL COMMENT '订单金额',
                                     `dm_multiples` decimal(22, 6) NULL DEFAULT NULL COMMENT '打码倍数',
                                     `dm_amount` decimal(22, 6) NULL DEFAULT NULL COMMENT '打码量',
                                     `dm_amount_before` decimal(22, 6) NOT NULL DEFAULT 0.000000 COMMENT '打码量变更前',
                                     `dm_amount_after` decimal(22, 6) NOT NULL DEFAULT 0.000000 COMMENT '打码量变更后',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
                                     `order_type` tinyint(1) NOT NULL COMMENT '变更类型 0：后台上分 1：用户充值 2：后台赠送彩金 3：后台修改用户打码量 4:用户贷款',
                                     PRIMARY KEY (`id`),
                                     INDEX `user_id`(`user_id`, `order_type`) USING BTREE
) ENGINE = InnoDB COMMENT = '用户打码量变更记录';



insert into `stock`.`other_value` (other_key,`name`,other_value,remark,visible_flag) select 'jackson_time_zone','展示时区','America/Toronto','北京时间：Asia/Shanghai，美东时间：America/Toronto',0 from other_value where not exists (select other_key from other_value where other_key = 'jackson_time_zone')limit 1;
