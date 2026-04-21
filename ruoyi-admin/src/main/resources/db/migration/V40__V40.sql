ALTER TABLE `stock`.`balance_convert_record`
    ADD COLUMN `exchange_rate` decimal(22, 6) NOT NULL COMMENT '汇率' AFTER `converted_amount`;

insert into `stock`.`sys_menu` (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
    ('重新生成行情', 2162, '5',  null, null, 1, 0, 'F', '0', '0', 'system:selfSellProductDailyDataConfig:regenerateRealtimeTempData',       '#', 'admin', sysdate(), '', null, '');
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:selfSellProductDailyDataConfig:regenerateRealtimeTempData');

insert into `stock`.`sys_menu` (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
    ('查看用户实名信息', 2001, '16',  null, null, 1, 0, 'F', '0', '0', 'system:userAuthRecord:getInfoByUserId',       '#', 'admin', sysdate(), '', null, ''),
    ('查看用户实名信息', 2073, '16',  null, null, 1, 0, 'F', '0', '0', 'system:touristsAuthRecord:getInfoByUserId',       '#', 'admin', sysdate(), '', null, ''),
    ('修改用户实名信息', 2001, '17',  null, null, 1, 0, 'F', '0', '0', 'system:userInfo:authApprove',       '#', 'admin', sysdate(), '', null, ''),
    ('修改用户实名信息', 2073, '17',  null, null, 1, 0, 'F', '0', '0', 'system:tourists:authApprove',       '#', 'admin', sysdate(), '', null, '');
insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userAuthRecord:getInfoByUserId','system:touristsAuthRecord:getInfoByUserId','system:userInfo:authApprove','system:tourists:authApprove');
