insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
    ('用户报表', 2290, '2',  'userCustome', 'custome/userCustome', 1, 0, 'C', '0', '0', 'system:customerLossReport:userCustomerLossReport',       'edit', 'admin', sysdate(), '', null, '');
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:customerLossReport:userCustomerLossReport');

insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 85,'app银行卡是否*号显示',1,1,85 from switch_set where not exists (select id from switch_set where id = 85)limit 1;
