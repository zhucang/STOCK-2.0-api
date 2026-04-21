CREATE TABLE `stock`.`backend_reminder_config`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `reminder_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
                                     `reminder_type` tinyint(1) NULL DEFAULT 0 COMMENT '提醒类型 0：不提示 1：提醒一次 :2：循环提醒',
                                     `jump_type` tinyint(1) NOT NULL COMMENT '跳转类型 0：初级实名认证:1：高级实名认证 2：充值 3：提现 4：贷款申请 5：贷款还款',
                                     `music_source_number` tinyint(1) NULL DEFAULT 1 COMMENT '音源编号',
                                     `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态 0：启用 1：禁用',
                                     PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '后台提醒配置';

insert into `stock`.`backend_reminder_config` (id, reminder_name, reminder_type, jump_type, music_source_number, status)
values
(1,'初级实名认证',1,0,1,0),
(2,'高级实名认证',1,1,1,0),
(3,'充值',1,2,1,0),
(4,'提现',1,3,1,0),
(5,'贷款申请',1,4,1,0),
(6,'贷款还款',1,5,1,0);

ALTER TABLE `stock`.`backend_reminder_config`
    ADD COLUMN `jump_url` varchar(255) NULL COMMENT '跳转路径' AFTER `jump_type`;

ALTER TABLE `stock`.`backend_reminder_config`
    ADD COLUMN `search_status` tinyint(1) NULL DEFAULT 0 COMMENT '筛选状态 ' AFTER `music_source_number`;

update `stock`.`backend_reminder_config` set search_status = 3 where id = 3;

update `stock`.`backend_reminder_config` set jump_url = '/user/realIndex' where id = 1;
update `stock`.`backend_reminder_config` set jump_url = '/user/realIndex' where id = 2;
update `stock`.`backend_reminder_config` set jump_url = '/userOrder/recharge' where id = 3;
update `stock`.`backend_reminder_config` set jump_url = '/userOrder/withdraw' where id = 4;
update `stock`.`backend_reminder_config` set jump_url = '/loan/order' where id = 5;
update `stock`.`backend_reminder_config` set jump_url = '/loan/rePay' where id = 6;


insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提示音管理', 2008, '17',  'tips', 'set/tips', 1, 0, 'C', '0', '0', 'system:backendReminderConfig:list', 'monitor', 'admin', sysdate(), '', null, '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('后台提醒配置查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:backendReminderConfig:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('后台提醒配置修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:backendReminderConfig:edit',         '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:backendReminderConfig:list','system:backendReminderConfig:query','system:backendReminderConfig:edit');
