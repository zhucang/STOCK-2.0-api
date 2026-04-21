 -- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公告类型', 2008, '3', 'siteMessageType', 'set/siteMessageType', 1, 0, 'C', '0', '0', 'system:siteMessageType:list', 'logininfor', 'admin', sysdate(), '', null, '公告类型菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('站内信类型查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:siteMessageType:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('站内信类型新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:siteMessageType:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('站内信类型修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:siteMessageType:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('站内信类型删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:siteMessageType:remove',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) values (2, @parentId), (2, @parentId+1), (2, @parentId+2), (2, @parentId+3),(2, @parentId+4);

