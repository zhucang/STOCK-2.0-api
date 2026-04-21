ALTER TABLE `stock`.`user_udun_wallet_address`
    ADD COLUMN `coin_name` varchar(255) NULL COMMENT '币种名称' AFTER `main_coin_type`,
DROP INDEX `main_coin_type`,
ADD UNIQUE INDEX `main_coin_type`(`main_coin_type`, `wallet_address`, `user_id`, `coin_name`) USING BTREE;

truncate table `stock`.`user_udun_wallet_address`;

ALTER TABLE `stock`.`user_udun_wallet_address`
DROP INDEX `main_coin_type`,
ADD UNIQUE INDEX(`main_coin_type`, `coin_name`, `wallet_address`) USING BTREE,
ADD UNIQUE INDEX(`user_id`, `main_coin_type`, `coin_name`) USING BTREE;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('优盾钱包', '2000', '5', 'uDun', 'set/uDun', 1, 0, 'C', '0', '0', 'system:userUdunWalletAddress:list', 'monitor', 'admin', sysdate(), '', null, '优盾钱包菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('优盾钱包查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:userUdunWalletAddress:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('优盾钱包新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:userUdunWalletAddress:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('优盾钱包修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:userUdunWalletAddress:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('优盾钱包删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:userUdunWalletAddress:remove',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userUdunWalletAddress:list','system:userUdunWalletAddress:query','system:userUdunWalletAddress:add','system:userUdunWalletAddress:edit','system:userUdunWalletAddress:remove');
