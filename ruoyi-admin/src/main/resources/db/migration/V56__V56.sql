ALTER TABLE `stock`.`loan_product`
    ADD COLUMN `repayment_method` tinyint(1) NULL DEFAULT 0 COMMENT '还款方式 0：联系客服 1：提交还款订单' AFTER `interest_settlement_method`;

ALTER TABLE `stock`.`loan_order`
    ADD COLUMN `repayment_method` tinyint(1) NULL DEFAULT 0 COMMENT '还款方式 0：联系客服 1：提交还款订单' AFTER `interest_settlement_method`;

CREATE TABLE `stock`.`user_loan_repayment_order`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `loan_order_id` bigint NOT NULL COMMENT '贷款订单id',
                                     `user_id` bigint NOT NULL COMMENT '用户id',
                                     `order_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
                                     `order_amount` decimal(22, 6) NOT NULL COMMENT '还款金额',
                                     `order_status` tinyint(1) NULL DEFAULT 0 COMMENT '状态：0：审核中 1:通过 2：驳回 3：待审核',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
                                     `operator_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人名称',
                                     `pay_channel_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '充值通道名称',
                                     `pay_channel_id` bigint NOT NULL COMMENT '支付通道id',
                                     `currency_id` bigint NOT NULL DEFAULT 1 COMMENT '币种id',
                                     `recharge_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付凭证',
                                     `recharge_msg` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回信息',
                                     `recharge_method` tinyint(1) NULL DEFAULT 0 COMMENT '充值方式： 0：线下充值 1：在线支付',
                                     `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                     `sql_version` bigint NULL DEFAULT 0 COMMENT 'sql版本',
                                     PRIMARY KEY (`id`),
                                     INDEX `user_id`(`user_id`) USING BTREE,
                                     INDEX `recharge_method`(`recharge_method`, `user_id`, `order_status`, `currency_id`) USING BTREE
) ENGINE = InnoDB COMMENT = '用户贷款还款订单';

insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_78','此贷款记录正在申请还款中' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_78') limit 1;

ALTER TABLE `stock`.`loan_order`
    ADD COLUMN `statistical_report` tinyint(1) NULL DEFAULT 0 COMMENT '是否统计报表 0：是 1：否' AFTER `update_time`;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改贷款订单免客损状态', 2341, '3',  null, '', 1, 0, 'F', '0', '0', 'system:loanOrder:updateStatisticalReport',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:loanOrder:updateStatisticalReport');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('后台人工结算', 2341, '4',  null, '', 1, 0, 'F', '0', '0', 'system:loanOrder:loanRepayment',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:loanOrder:loanRepayment');