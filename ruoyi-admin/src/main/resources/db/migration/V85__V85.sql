update sys_oper_log set relate_app_user_id = (select user_id from user_auth_record where id = sys_oper_log.relate_app_user_id)
where title = '用户实名审核';