insert into switch_set (id,switch_name,status,`type`) values (60,'是否展示第三方充值通道',0,1),(61,'app是否展示总收益',0,1),(62,'app是否展示今日收益',0,1);

ALTER TABLE `stock`.`user_withdraw`
    ADD COLUMN `remark` varchar(255) NULL COMMENT '备注' AFTER `user_amt_after`;

ALTER TABLE `stock`.`user_recharge`
    ADD COLUMN `remark` varchar(255) NULL COMMENT '备注' AFTER `user_amount_after`;

ALTER TABLE `stock`.`other_value`
    ADD COLUMN `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备注' AFTER `other_value`,
ADD COLUMN `visible_flag` tinyint(1) NULL DEFAULT 0 COMMENT '是否显示 ： 0：是 1：否' AFTER `remark`;

