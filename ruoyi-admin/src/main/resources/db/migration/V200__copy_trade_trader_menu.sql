set @pId = (
  SELECT menu_id FROM `sys_menu` WHERE `menu_name` = '会员管理' and path = 'user' limit 1
);

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('大神交易员', @pId, '1', 'copyTradeTrader', 'copyTrade/copyTradeTrader/index', 1, 0, 'C', '0', '0', 'system:copyTradeTrader:list', '#', 'admin', sysdate(), '', null, '大神交易员菜单');
SELECT @copyTradeTraderId := LAST_INSERT_ID();
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('大神交易员查询', @copyTradeTraderId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeTrader:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('大神交易员新增', @copyTradeTraderId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeTrader:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('大神交易员修改', @copyTradeTraderId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeTrader:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('启用停用', @copyTradeTraderId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeTrader:edit',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('跟单人员', @copyTradeTraderId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeRelation:list',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('跟单列表', @copyTradeTraderId, '6',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeOrder:list',       '#', 'admin', sysdate(), '', null, '');

insert into sys_role_menu (role_id, menu_id)
select 2, menu_id from sys_menu
where perms in (
  'system:copyTradeTrader:list',
  'system:copyTradeTrader:query',
  'system:copyTradeTrader:add',
  'system:copyTradeTrader:edit'
);

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('跟单人员', @pId, '6', 'copyTradeRelation', 'copyTrade/copyTradeRelation/index', 1, 0, 'C', '1', '0', 'system:copyTradeRelation:list', '#', 'admin', sysdate(), '', null, '跟单人员菜单');

SELECT @copyTradeRelationId := LAST_INSERT_ID();

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('跟单人员查询', @copyTradeRelationId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeRelation:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('跟单人员新增', @copyTradeRelationId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeRelation:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('跟单人员修改', @copyTradeRelationId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeRelation:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('跟单人员删除', @copyTradeRelationId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeRelation:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('跟单人员跟随', @copyTradeRelationId, '6',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeRelation:follow',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('跟单人员停止跟随', @copyTradeRelationId, '7',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeRelation:unfollow',       '#', 'admin', sysdate(), '', null, '');

insert into sys_role_menu (role_id, menu_id)
select 2, menu_id from sys_menu
where perms in (
    'system:copyTradeRelation:list',
    'system:copyTradeRelation:query',
    'system:copyTradeRelation:add',
    'system:copyTradeRelation:edit',
    'system:copyTradeRelation:remove',
    'system:copyTradeRelation:follow',
    'system:copyTradeRelation:unfollow'
);


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('跟单列表', @pId, '1', 'copyTradeOrder', 'copyTrade/copyTradeOrder/index', 1, 0, 'C', '1', '0', 'system:copyTradeOrder:list', '#', 'admin', sysdate(), '', null, '跟单列表菜单');
SELECT @copyTradeOrderId := LAST_INSERT_ID();
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('跟单列表查询', @copyTradeOrderId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:copyTradeOrder:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_role_menu (role_id, menu_id)
select 2, menu_id from sys_menu
where perms in (
    'system:copyTradeOrder:list',
    'system:copyTradeOrder:query'
);
