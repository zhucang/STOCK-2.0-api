-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('币币交易', '0', '7', 'bibi', null, 1, 0, 'M', '0', '0', null, 'example', 'admin', sysdate(), '', null, null);

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单管理', @parentId, '1',  'order', 'bibi/order', 1, 1, 'C', '0', '0', 'system:bibiTradeOrder:list',        'list', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('资产管理', @parentId, '2',  'assets', 'bibi/assets', 1, 0, 'C', '0', '0', 'system:userBibiAssets:list',          'money', 'admin', sysdate(), '', null, '');


insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where menu_id in (@parentId,@parentId+1,@parentId+2);

ALTER TABLE `stock`.`scheduled_task_exception_log`
    MODIFY COLUMN `type` int NOT NULL COMMENT '类型：1：理财派发利息任务 2：股票止盈止损触发定时任务 3：加密货币止盈止损触发定时任务 4：期货止盈止损触发定时任务 5:新股新币上市定时器 6:新股新币开始申购定时器 7:留仓费收取定时任务 8:股票留仓到期强制平仓任务 9:同步节假日开关定时任务 10:每日16点收盘时保存每日数据定时任务  11:删除分时图数据定时任务\r\n12：外汇止盈止损定时任务 13:股票强制平仓定时任务 14：加密货币强制平仓定时任务 15：期货强制平仓定时任务 16：外汇强制平仓定时任务 17：贷款收取利息定时任务 18：币币交易委托订单自动通过定时任务' AFTER `exception_info_detail`;