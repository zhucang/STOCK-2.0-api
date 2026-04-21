CREATE TABLE `stock`.`user_point_change_record`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `user_id` bigint NOT NULL COMMENT '用户id',
                                     `order_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
                                     `order_amount` decimal(22, 6) NOT NULL COMMENT '充值金额',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     `operator_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人名称',
                                     `currency_id` bigint NOT NULL DEFAULT 1 COMMENT '币种id',
                                     `user_amount_before` decimal(22, 6) NULL DEFAULT NULL COMMENT '余额变更前',
                                     `user_amount_after` decimal(22, 6) NULL DEFAULT NULL COMMENT '余额变更后',
                                     `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                     PRIMARY KEY (`id`),
                                     INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB COMMENT = '用户上分下分记录';

ALTER TABLE `stock`.`user_point_change_record`
    ADD COLUMN `order_type` tinyint(1) NOT NULL COMMENT '订单类型 0：上分 1：下分' AFTER `order_amount`;

insert into user_point_change_record (user_id,order_code,order_amount,order_type,create_time,currency_id,user_amount_before,user_amount_after)
select user_id,id,order_amount,0,order_time,currency_id,amount_before,amount_after from user_bill_detail where order_class = 2;

insert into user_point_change_record (user_id,order_code,order_amount,order_type,create_time,currency_id,user_amount_before,user_amount_after)
select user_id,id,order_amount,1,order_time,currency_id,amount_before,amount_after from user_bill_detail where order_class = 3;

