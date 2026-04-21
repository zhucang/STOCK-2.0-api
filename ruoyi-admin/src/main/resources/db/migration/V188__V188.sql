SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for recharge_reward_tier
-- ----------------------------
DROP TABLE IF EXISTS `recharge_reward_tier`;
CREATE TABLE `recharge_reward_tier`  (
                                         `recharge_reward_tier_id` bigint NOT NULL AUTO_INCREMENT COMMENT '充值奖励层级配置ID',
                                         `tier_amount_min` decimal(27, 10) NOT NULL COMMENT '档位金额范围最小值',
                                         `tier_amount_max` decimal(27, 10) NOT NULL COMMENT '档位金额范围最大值',
                                         `reward_rate` decimal(27, 10) NOT NULL COMMENT '奖励百分比（%）',
                                         `status` tinyint NULL DEFAULT 1 COMMENT '状态 0：禁用 1：启用',
                                         `currency_id` bigint NULL DEFAULT 3 COMMENT '币种ID',
                                         `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
                                         `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                         `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
                                         `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                         `del_flag` tinyint NULL DEFAULT 0 COMMENT '删除标志 0：未删除 1：已删除',
                                         PRIMARY KEY (`recharge_reward_tier_id`) USING BTREE,
                                         INDEX `tier_amount_min`(`tier_amount_min`, `tier_amount_max`, `status`, `currency_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '充值奖励层级配置' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;



SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for recharge_reward_record
-- ----------------------------
DROP TABLE IF EXISTS `recharge_reward_record`;
CREATE TABLE `recharge_reward_record`  (
                                           `recharge_reward_record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '充值奖励领取记录',
                                           `user_id` bigint NOT NULL COMMENT '用户ID',
                                           `user_recharge_id` bigint NOT NULL COMMENT '充值订单ID',
                                           `reward_tier_id` bigint NOT NULL COMMENT '充值奖励层级ID',
                                           `recharge_amount` decimal(27, 10) NOT NULL COMMENT '充值金额',
                                           `reward_rate` decimal(27, 10) NOT NULL COMMENT '奖励比例（%）',
                                           `reward_amount` decimal(27, 10) NOT NULL COMMENT '奖励金额',
                                           `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
                                           `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                           `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
                                           `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                           PRIMARY KEY (`recharge_reward_record_id`) USING BTREE,
                                           UNIQUE INDEX `uk_user_tier`(`user_id`, `reward_tier_id`) USING BTREE,
                                           INDEX `idx_user_id`(`user_id`) USING BTREE,
                                           INDEX `idx_order_id`(`user_recharge_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '充值奖励领取记录表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('充值奖励档位配置', '2163', '6', 'rechargeRewardTier', 'risk/rechargeRewardTier/index', 1, 0, 'C', '0', '0', 'system:rechargeRewardTier:list', 'system', 'admin', sysdate(), '', null, '充值奖励层级配置菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('充值奖励层级配置查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:rechargeRewardTier:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('充值奖励层级配置新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:rechargeRewardTier:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('充值奖励层级配置修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:rechargeRewardTier:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('充值奖励层级配置删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:rechargeRewardTier:remove',       '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:rechargeRewardTier:list','system:rechargeRewardTier:query','system:rechargeRewardTier:add','system:rechargeRewardTier:edit','system:rechargeRewardTier:remove');
