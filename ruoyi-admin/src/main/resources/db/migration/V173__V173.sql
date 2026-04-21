insert into backend_reminder_config (reminder_name, reminder_type, jump_type, jump_url, music_source_number, search_status, status)
values ('质押',1,7,'/pledge/order',1,0,1),('极速订单',1,8,'/holdOrder/fast',1,0,1);

-- 按钮父菜单ID
SELECT @parentId := (select menu_id from sys_menu where menu_name = '极速交易' and component = 'holdOrder/fast/index');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
    ('修改极速交易订单信息', @parentId, '3',  null, '', 1, 0, 'F', '0', '0', 'system:fastTradeOrder:edit',       '#', 'admin', sysdate(), '', null, '');
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:fastTradeOrder:edit');
