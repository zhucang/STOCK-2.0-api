insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 67,'是否支持在线支付',1,1,67 from switch_set where not exists (select id from switch_set where id = 67)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 68,'是否开启初级实名认证',1,1,68 from switch_set where not exists (select id from switch_set where id = 68)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 69,'完成初级实名认证才能提现',1,1,69 from switch_set where not exists (select id from switch_set where id = 69)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 70,'完成初级实名认证才能交易',1,1,70 from switch_set where not exists (select id from switch_set where id = 70)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 71,'完成高级实名认证才能提现',0,1,71 from switch_set where not exists (select id from switch_set where id = 71)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 72,'完成高级实名认证才能交易',0,1,72 from switch_set where not exists (select id from switch_set where id = 72)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 73,'完成初级实名认证才能充值',1,1,73 from switch_set where not exists (select id from switch_set where id = 73)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 74,'完成高级实名认证才能充值',0,1,74 from switch_set where not exists (select id from switch_set where id = 74)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 75,'是否开启高级实名认证',0,1,68 from switch_set where not exists (select id from switch_set where id = 75)limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_69','单笔充值金额范围{1}~{2}' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_69') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_auth_junior','请先完成初级实名认证' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_auth_junior') limit 1;
insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_auth_senior','请先完成高级实名认证' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_auth_senior') limit 1;

CREATE TABLE `stock`.`user_auth_record`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `user_id` bigint NOT NULL COMMENT '用户id',
                                     `id_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件号码',
                                     `real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实名称',
                                     `img1_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件图片（正面）',
                                     `img2_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件图片（背面）',
                                     `img3_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件图片（手持）',
                                     `auth_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实名审核反馈信息',
                                     `auth_status` tinyint(1) NULL DEFAULT 0 COMMENT '实名状态 0：审核中 1：审核通过 2：审核驳回 ',
                                     `auth_method` tinyint(1) NULL DEFAULT 0 COMMENT '证件类型 0：身份证 1：驾驶证 2：护照',
                                     `auth_level` tinyint(1) NULL DEFAULT 0 COMMENT '实名等级 0:初级 1：高级',
                                     `create_time` datetime(0) NULL COMMENT '创建时间',
                                     `approve_time` datetime(0) NULL COMMENT '审核时间',
                                     `approve_name` varchar(30) NULL COMMENT '审核人',
                                     PRIMARY KEY (`id`),
                                     UNIQUE INDEX `user_id`(`user_id`, `auth_level`, `auth_status`) USING BTREE
) ENGINE = InnoDB COMMENT = '用户实名认证信息';

ALTER TABLE `stock`.`user_auth_record`
DROP INDEX `user_id`,
ADD INDEX `user_id`(`user_id`, `auth_level`, `auth_status`) USING BTREE;

insert into `stock`.`user_auth_record`(user_id,id_number,real_name,img1_key,img2_key,img3_key,auth_msg,auth_status,auth_method,auth_level,create_time,approve_time,approve_name)
select id,id_card,real_name,null,null,null,auth_msg,1,auth_method,0,now(),now(),'未知' from user_info where is_active > 0
union
select id,id_card,real_name,img1_key,img2_key,img3_key,auth_msg,is_active-1,auth_method,1,now(),now(),'未知' from user_info where is_active > 0;

ALTER TABLE `stock`.`user_info`
    ADD COLUMN `auth_status_junior` tinyint(1) NULL DEFAULT 0 COMMENT '初级实名认证状态：  0：未实名 1：审核中 2：审核通过 3：审核驳回' AFTER `is_active`,
ADD COLUMN `auth_status_senior` tinyint(1) NULL DEFAULT 0 COMMENT '高级实名认证状态： 0：未实名 1：审核中 2：审核通过 3：审核驳回' AFTER `auth_status_junior`;

update `stock`.`user_info` set `auth_status_junior` = 2,`auth_status_senior` = is_active where is_active > 0;

delete from `stock`.`switch_set` where id in (52,34);

delete from `stock`.`sys_role_menu` where menu_id in (select menu_id from `stock`.`sys_menu` where menu_name = '用户实名审核');
delete from `stock`.`sys_menu` where menu_name = '用户实名审核';
-- update `stock`.`sys_menu` set menu_name = '查看用户实名信息',perms = 'system:userAuthRecord:getInfoByUserId' where menu_name = '用户实名审核' and perms = 'system:userInfo:authApprove';
-- update `stock`.`sys_menu` set menu_name = '查看用户实名信息',perms = 'system:touristsAuthRecord:getInfoByUserId' where menu_name = '用户实名审核' and perms = 'system:tourists:authApprove';

insert into `stock`.`sys_menu` (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户踢下线', 2001, '14',  '#', '', 1, 0, 'F', '0', '0', 'system:userInfo:kickOffline',       '#', 'admin', sysdate(), '', null, ''),
      ('用户踢下线', 2073, '14',  '#', '', 1, 0, 'F', '0', '0', 'system:tourists:kickOffline',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where menu_name = '用户踢下线';

-- update `stock`.`other_value` set other_key = 'onlineRechargeSupportFiatsCurrency_channel_A',`name` = '通道A在线支付支持的法币' where other_key = 'onlineRechargeSupportFiatsCurrency';
-- insert into `stock`.`other_value` (other_key,`name`,other_value,remark,visible_flag) values ('onlineRechargeSupportFiatsCurrency_channel_B','通道B在线支付支持的法币','EUR',null,0);

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('实名审核', '2000', '4', 'realIndex', 'user/realIndex', 1, 0, 'C', '0', '0', 'system:userAuthRecord:list', 'dict', 'admin', sysdate(), '', null, '用户实名认证信息菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户实名认证信息查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:userAuthRecord:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户实名认证审核', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:userAuthRecord:userAuthReview',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户实名认证信息删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:userAuthRecord:remove',       '#', 'admin', sysdate(), '', null, '');