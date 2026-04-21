insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 114,'用户充值计算VIP等级',0,1,114 from switch_set where not exists (select id from switch_set where id = 114)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 115,'用户上分计算VIP等级',0,1,115 from switch_set where not exists (select id from switch_set where id = 115)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 116,'赠送彩金计算VIP等级',0,1,116 from switch_set where not exists (select id from switch_set where id = 116)limit 1;

CREATE TABLE `stock`.`vip_experience_value`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                     `user_id` bigint NOT NULL COMMENT '用户ID',
                                     `relate_order_code` varchar(255) NULL COMMENT '关联订单号',
                                     `experience_value` decimal(27, 10) NULL DEFAULT 0 COMMENT '本次经验值',
                                     `experience_value_before` decimal(27, 10) NULL DEFAULT 0 COMMENT '经验值更新前',
                                     `experience_value_after` decimal(27, 10) NULL DEFAULT 0 COMMENT '经验值更新后',
                                     `create_time` datetime NULL COMMENT '创建时间',
                                     PRIMARY KEY (`id`),
                                     INDEX(`user_id`) USING BTREE
) ENGINE = InnoDB COMMENT = 'VIP经验值';