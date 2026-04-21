-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('应用秘钥', '2008', '18', 'apiKey', 'set/apiKey', 1, 0, 'C', '0', '0', 'system:userApiKey:list', 'radio', 'admin', sysdate(), '', null, '应用秘钥apiKey菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('应用秘钥apiKey查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:userApiKey:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('应用秘钥apiKey新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:userApiKey:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('应用秘钥apiKey删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:userApiKey:remove',       '#', 'admin', sysdate(), '', null, '');
