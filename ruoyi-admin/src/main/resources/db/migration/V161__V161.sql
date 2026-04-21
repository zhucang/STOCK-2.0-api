SELECT @parent_id := (select menu_id from sys_menu where component = 'holdOrder/hy/stockHold');


insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values ('锁仓、解仓操作', @parent_id, '1', null, null, 1, 0, 'F', '0', '0', 'system:userProductPosition:lockUserPosition', '#', 'admin', sysdate(), '', null, ''),('强制平仓操作', @parent_id, '2', null, null, 1, 0, 'F', '0', '0', 'system:userProductPosition:forceSell', '#', 'admin', sysdate(), '', null, '');


insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userProductPosition:lockUserPosition','system:userProductPosition:forceSell');
