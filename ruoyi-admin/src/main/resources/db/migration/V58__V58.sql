insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('还款订单', 2335, '3',  'rePay', 'loan/rePay', 1, 1, 'C', '0', '0', 'system:userLoanRepaymentOrder:list',       'monitor', 'admin', sysdate(), '', null, '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('还款订单查询', @parentId, '1',  null, '', 1, 0, 'F', '0', '0', 'system:userLoanRepaymentOrder:query',       '#', 'admin', sysdate(), '', null, ''),
('还款订单审核', @parentId, '2',  null, '', 1, 0, 'F', '0', '0', 'system:userLoanRepaymentOrder:updateLoanRepaymentOrderStatus',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userLoanRepaymentOrder:list','system:userLoanRepaymentOrder:query','system:userLoanRepaymentOrder:updateLoanRepaymentOrderStatus');