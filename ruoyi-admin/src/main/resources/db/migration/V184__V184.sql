-- 按钮父菜单ID
SELECT @parentId := (select menu_id from sys_menu where menu_name = '充值订单' and component = 'userOrder/rechIndex');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
    ('修改充值订单备注', @parentId, '4',  null, '', 1, 0, 'F', '0', '0', 'system:userRecharge:updateOrderRemark',       '#', 'admin', sysdate(), '', null, '');
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userRecharge:updateOrderRemark');

-- 按钮父菜单ID
SELECT @parentId := (select menu_id from sys_menu where menu_name = '提现订单' and component = 'userOrder/withdraw');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
    ('修改提现订单备注', @parentId, '4',  null, '', 1, 0, 'F', '0', '0', 'system:userWithdraw:updateOrderRemark',       '#', 'admin', sysdate(), '', null, '');
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userWithdraw:updateOrderRemark');

-- 按钮父菜单ID
SELECT @parentId := (select menu_id from sys_menu where menu_name = '用户管理' and component = 'system/user/index');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
    ('禁用所有客户管理专用账号', @parentId, '8',  null, '', 1, 0, 'F', '0', '0', 'system:user:disableAllCustomerAccounts',       '#', 'admin', sysdate(), '', null, '');
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:user:disableAllCustomerAccounts');
