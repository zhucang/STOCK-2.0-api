
set @pAccountIpWhiteListId = (
  SELECT menu_id FROM `sys_menu` WHERE `menu_name` = '游客管理' and path = 'user1' limit 1
);

-- 菜单 SQL
insert IGNORE into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账号ip白名单', @pAccountIpWhiteListId, '1', 'accountIpWhiteList', 'user/accountIpWhiteList', 1, 0, 'C', '0', '0', 'system:accountIpWhiteList:list', '#', 'admin', sysdate(), '', null, '账号ip白名单菜单');

-- 按钮父菜单ID
SELECT @accountIpWhiteListId := LAST_INSERT_ID();

-- 按钮 SQL
insert IGNORE into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账号ip白名单查询', @accountIpWhiteListId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:accountIpWhiteList:query',        '#', 'admin', sysdate(), '', null, '');

insert IGNORE into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账号ip白名单新增', @accountIpWhiteListId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:accountIpWhiteList:add',          '#', 'admin', sysdate(), '', null, '');

insert IGNORE into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账号ip白名单修改', @accountIpWhiteListId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:accountIpWhiteList:edit',         '#', 'admin', sysdate(), '', null, '');

insert IGNORE into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账号ip白名单删除', @accountIpWhiteListId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:accountIpWhiteList:remove',       '#', 'admin', sysdate(), '', null, '');

insert IGNORE into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账号ip白名单导出', @accountIpWhiteListId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:accountIpWhiteList:export',       '#', 'admin', sysdate(), '', null, '');


insert IGNORE into sys_role_menu (role_id, menu_id)
select 2, menu_id from sys_menu
where perms in (
    'system:accountIpWhiteList:list',
    'system:accountIpWhiteList:query',
    'system:accountIpWhiteList:add',
    'system:accountIpWhiteList:edit',
    'system:accountIpWhiteList:remove',
    'system:accountIpWhiteList:export'
);