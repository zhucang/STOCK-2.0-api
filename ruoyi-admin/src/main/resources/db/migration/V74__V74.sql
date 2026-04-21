set @parentId = (select menu_id from sys_menu where path = 'assets' and component = 'bibi/assets' and perms = 'system:userBibiAssets:list');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('正常平仓', @parentId, '1',  null, null, 1, 0, 'F', '0', '0', 'system:bibiTradeOrder:manualSell', '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:bibiTradeOrder:manualSell');
