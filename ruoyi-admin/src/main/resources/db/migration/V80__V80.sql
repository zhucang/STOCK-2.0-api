update `stock`.`sys_menu` set menu_name='行情控制',perms='system:selfSellProductQuoteControl:list',visible='0' where menu_id = 2201;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('行情控制查询', 2201, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:selfSellProductQuoteControl:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('行情控制新增', 2201, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:selfSellProductQuoteControl:add',         '#', 'admin', sysdate(), '', null, '');


insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('行情控制删除', 2201, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:selfSellProductQuoteControl:remove',         '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:selfSellProductQuoteControl:add','system:selfSellProductQuoteControl:query','system:selfSellProductQuoteControl:remove');
