insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 101,'虚拟钱包是否必须上传钱包收款码',1,1,101 from switch_set where not exists (select id from switch_set where id = 101)limit 1;

insert into `stock`.`lang_mgr` (lang_key,zh) select 'hint_87','请上传钱包收款码' from lang_mgr where not exists (select lang_key from lang_mgr where lang_key = 'hint_87') limit 1;

insert into `stock`.`sys_menu` (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', 2170, '0',  null, null, 1, 0, 'F', '0', '0', 'system:userApplyPurchaseOrder:query',        '#', 'admin', sysdate(), '', null, '');

insert into `stock`.`sys_role_menu` (role_id,menu_id) select 2,menu_id from stock.sys_menu where perms in ('system:userApplyPurchaseOrder:query');
