insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('贷款利息明细', 2335, '4',  'detail', 'loan/detail', 1, 1, 'C', '1', '0', null,       'build', 'admin', sysdate(), '', null, '');

SELECT @parentId := LAST_INSERT_ID();
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where menu_id in (@parentId);