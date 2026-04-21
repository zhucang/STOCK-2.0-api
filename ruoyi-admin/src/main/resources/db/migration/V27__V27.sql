ALTER TABLE `stock`.`user_bank`
    MODIFY COLUMN `bank_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '银行名称' AFTER `user_id`,
    MODIFY COLUMN `bank_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '银行卡号' AFTER `bank_name`,
    MODIFY COLUMN `bank_open_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '开户行' AFTER `bank_no`,
    MODIFY COLUMN `holder` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '开户人姓名' AFTER `bank_open_address`;

insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 83,'app金额是否使用千分位展示格式',1,1,83 from switch_set where not exists (select id from switch_set where id = 83)limit 1;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
    ('彩金订单', 2108, '6',  'userOrder/winning', 'userOrder/winning', 1, 0, 'C', '0', '0', 'system:userWinningsChangeRecord:list',       'druid', 'admin', sysdate(), '', null, '');
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userWinningsChangeRecord:list');