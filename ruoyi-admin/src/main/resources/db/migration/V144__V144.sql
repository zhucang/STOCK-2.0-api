insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改提现收款账户信息', 2110, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:userWithdraw:updateReceiptAccountInfo',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userWithdraw:updateReceiptAccountInfo');
