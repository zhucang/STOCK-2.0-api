-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('nginx管理', '1', '1', 'nginx', 'set/nginx', 1, 0, 'C', '0', '0', 'system:nginxConfig:list', 'color', 'admin', sysdate(), '', null, 'nginx配置转发菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('nginx配置转发查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:nginxConfig:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('nginx配置转发新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:nginxConfig:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('nginx配置转发修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:nginxConfig:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('nginx配置转发删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:nginxConfig:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('重载服务器配置', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:nginxConfig:reloadConfig',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:nginxConfig:list','system:nginxConfig:query','system:nginxConfig:add','system:nginxConfig:edit','system:nginxConfig:remove','system:nginxConfig:reloadConfig');