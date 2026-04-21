ALTER TABLE `stock`.`staking_order_interest_record`
    ADD COLUMN `staking_pool_amount` decimal(27, 10) NOT NULL COMMENT '质押池金额' AFTER `staking_order_id`;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('质押管理', 0, '9', 'pledge', null, 1, 0, 'M', '0', '0', null, 'money', 'admin', sysdate(), '', null, '');

SELECT @parentId := LAST_INSERT_ID();

insert into `stock`.`sys_role_menu` (role_id,menu_id) values (2,@parentId);

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
    ('质押列表', @parentId, '1', 'list', 'pledge/list', 1, 0, 'C', '0', '0', 'system:stakingProduct:list', 'list', 'admin', sysdate(), '', null, ''),
    ('质押订单', @parentId, '2', 'order', 'pledge/order', 1, 0, 'C', '0', '0', 'system:stakingOrder:list', 'documentation', 'admin', sysdate(), '', null, ''),
    ('派息记录', @parentId, '3', 'interest', 'pledge/interest', 1, 0, 'C', '1', '0', null, '#', 'admin', sysdate(), '', null, '');

SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('质押产品配置查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:stakingProduct:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('质押产品配置新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:stakingProduct:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('质押产品配置修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:stakingProduct:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('质押产品配置删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:stakingProduct:remove',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) values (2,@parentId),(2,@parentId+1),(2,@parentId+2),(2,@parentId+3),(2,@parentId+4),(2,@parentId+5),(2,@parentId+6);
