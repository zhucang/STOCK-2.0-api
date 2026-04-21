CREATE TABLE `stock`.`activity_center`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `activity_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动图片',
                                     `activity_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动标题',
                                     `activity_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '活动内容',
                                     `sort` int NULL DEFAULT 0 COMMENT '排序',
                                     `show_time` datetime(0) NULL DEFAULT NULL COMMENT '展示时间',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     `activity_img_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-英文',
                                     `activity_img_tc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-繁体',
                                     `activity_img_de` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-德国',
                                     `activity_img_es` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-西班牙',
                                     `activity_img_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-法国',
                                     `activity_img_idn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-印度尼西亚',
                                     `activity_img_jp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-日本',
                                     `activity_img_ko` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-韩国',
                                     `activity_img_my` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-马来西亚',
                                     `activity_img_th` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-泰国',
                                     `activity_img_vi` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-越南',
                                     `activity_img_pt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-葡萄牙',
                                     `activity_img_rus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-俄语',
                                     `activity_img_blr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-白俄罗斯',
                                     `activity_img_ida` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-印度',
                                     `activity_img_sa` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-沙特阿拉伯',
                                     `activity_img_ar` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-阿拉伯',
                                     `activity_img_it` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-意大利',
                                     `activity_img_tr` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '活动图片-土耳其',
                                     `activity_title_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-英文',
                                     `activity_title_tc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-繁体',
                                     `activity_title_de` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-德国',
                                     `activity_title_es` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-西班牙',
                                     `activity_title_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-法国',
                                     `activity_title_idn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-印度尼西亚',
                                     `activity_title_jp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-日本',
                                     `activity_title_ko` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-韩国',
                                     `activity_title_my` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-马来西亚',
                                     `activity_title_th` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-泰国',
                                     `activity_title_vi` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-越南',
                                     `activity_title_pt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-葡萄牙',
                                     `activity_title_rus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-俄语',
                                     `activity_title_blr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-白俄罗斯',
                                     `activity_title_ida` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-印度',
                                     `activity_title_sa` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-沙特阿拉伯',
                                     `activity_title_ar` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-阿拉伯',
                                     `activity_title_it` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-意大利',
                                     `activity_title_tr` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '活动标题-土耳其',
                                     `activity_content_en` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-英文',
                                     `activity_content_tc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-繁体',
                                     `activity_content_de` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-德国',
                                     `activity_content_es` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-西班牙',
                                     `activity_content_fr` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-法国',
                                     `activity_content_idn` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-印度尼西亚',
                                     `activity_content_jp` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-日本',
                                     `activity_content_ko` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-韩国',
                                     `activity_content_my` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-马来西亚',
                                     `activity_content_th` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-泰国',
                                     `activity_content_vi` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-越南',
                                     `activity_content_pt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-葡萄牙',
                                     `activity_content_rus` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-俄语',
                                     `activity_content_blr` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-白俄罗斯',
                                     `activity_content_ida` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-印度',
                                     `activity_content_sa` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容-沙特阿拉伯',
                                     `activity_content_ar` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '活动内容-阿拉伯',
                                     `activity_content_it` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '活动内容-意大利',
                                     `activity_content_tr` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '活动内容-土耳其',
                                     PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '活动中心配置';


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('活动中心配置', '2008', '19', 'activity', 'set/activity', 1, 0, 'C', '0', '0', 'system:activityCenter:list', 'example', 'admin', sysdate(), '', null, '活动中心配置菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('活动中心配置查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:activityCenter:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('活动中心配置新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:activityCenter:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('活动中心配置修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:activityCenter:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('活动中心配置删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:activityCenter:remove',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:activityCenter:list','system:activityCenter:query','system:activityCenter:add','system:activityCenter:edit','system:activityCenter:remove');
