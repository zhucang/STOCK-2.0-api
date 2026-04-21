ALTER TABLE `stock`.`user_apply_purchase_order`
    MODIFY COLUMN `product_type` tinyint(1) NULL DEFAULT 1 COMMENT '产品类型 1：股票 2：加密货币' AFTER `new_product_apply_purchase_id`,
    ADD COLUMN `lock_flag` tinyint(1) NULL DEFAULT 0 COMMENT '是否锁仓 0：是 1：否' AFTER `status`,
    ADD COLUMN `un_lock_time` datetime(0) NULL COMMENT '锁仓解锁时间' AFTER `lock_flag`;

ALTER TABLE `stock`.`user_bibi_assets`
    ADD COLUMN `bibi_frozen_amount` decimal(22, 6) NOT NULL DEFAULT 0.000000 COMMENT '冻结数量' AFTER `bibi_amount`;

insert into `stock`.`sys_menu` (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('解锁用户申购订单', 2170, '2',  null, null, 1, 0, 'F', '0', '0', 'system:userApplyPurchaseOrder:unLockOrder',        '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userApplyPurchaseOrder:unLockOrder');

ALTER TABLE `stock`.`user_bibi_assets`
DROP INDEX `user_id`,
ADD UNIQUE INDEX `user_id`(`user_id`, `product_code`, `product_type`) USING BTREE;