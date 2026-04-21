-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('贷款管理', '0', '9', 'loan', null, 1, 0, 'M', '0', '0', null, 'money', 'admin', sysdate(), '', null, null);

-- 按钮父菜单ID
SELECT @parentId1 := LAST_INSERT_ID();

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('贷款产品', @parentId1, '1', 'list', 'loan/loanList', 1, 0, 'C', '0', '0', 'system:loanProduct:list', 'list', 'admin', sysdate(), '', null, '贷款产品菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('贷款产品配置查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:loanProduct:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('贷款产品配置新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:loanProduct:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('贷款产品配置修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:loanProduct:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('贷款产品配置删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:loanProduct:remove',       '#', 'admin', sysdate(), '', null, '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('贷款订单', @parentId1, '2', 'order', 'loan/loanOrder', 1, 0, 'C', '0', '0', 'system:loanOrder:list', 'swagger', 'admin', sysdate(), '', null, '贷款订单菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('贷款订单查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:loanOrder:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('贷款订单审核', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:loanOrder:updateLoanOrderStatus',        '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where menu_id in (@parentId1);
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:loanProduct:list','system:loanProduct:query','system:loanProduct:add','system:loanProduct:edit','system:loanProduct:remove','system:loanOrder:list','system:loanOrder:query','system:loanOrder:updateLoanOrderStatus');