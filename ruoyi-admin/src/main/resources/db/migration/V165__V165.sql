insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新合约交易', 2092, '4',  'hy', 'holdOrder/hy/stockHold', 1, 0, 'C', '0', '0', 'system:userProductPosition:list', 'excel', 'admin', sysdate(), '', null, '');

update sys_menu set visible = 1,status = 1 where component = 'holdOrder/hy/stockHold' and perms = 'system:userProductPosition:list';