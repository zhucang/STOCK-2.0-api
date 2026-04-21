insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 87,'app行情图是否显示在交易页面',1,1,87 from switch_set where not exists (select id from switch_set where id = 87)limit 1;

insert into `stock`.`sys_menu` (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
    ('变更上级', 2001, '15',  null, null, 1, 0, 'F', '0', '0', 'system:userInfo:changeSuperior',       '#', 'admin', sysdate(), '', null, ''),
    ('变更上级', 2073, '15',  null, null, 1, 0, 'F', '0', '0', 'system:tourists:changeSuperior',       '#', 'admin', sysdate(), '', null, '');
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userInfo:changeSuperior','system:tourists:changeSuperior');
