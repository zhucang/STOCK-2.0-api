ALTER TABLE `stock`.`user_recharge`
    MODIFY COLUMN `order_status` tinyint(1) NULL DEFAULT 0 COMMENT '状态：0：审核中 1:通过 2：驳回 3：待审核' AFTER `recharge_amount`;

insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 76,'交易是否显示确认订单',0,1,76 from switch_set where not exists (select id from switch_set where id = 76)limit 1;

ALTER TABLE `stock`.`user_withdraw`
    ADD COLUMN `statistical_report` tinyint(1) NULL DEFAULT 0 COMMENT '是否统计报表 0：是 1：否' AFTER `sql_version`,
DROP INDEX `user_id`,
ADD INDEX `user_id`(`user_id`, `currency_id`, `withdraw_status`, `statistical_report`) USING BTREE;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改提现订单免客损状态', 2110, '2',  null, '', 1, 0, 'F', '0', '0', 'system:userWithdraw:updateStatisticalReport',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userAuthRecord:list','system:userAuthRecord:query','system:userAuthRecord:userAuthReview','system:userAuthRecord:remove');