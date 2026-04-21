-- 按钮父菜单ID
SELECT @parentId := (select menu_id from sys_menu where menu_name = '质押订单' and component = 'pledge/order');


insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
('质押订单审核', 2388, '1',  null, '', 1, 0, 'F', '0', '0', 'system:stakingOrder:updateStakingOrderStatus',       '#', 'admin', sysdate(), '', null, '');
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:stakingOrder:updateStakingOrderStatus');
