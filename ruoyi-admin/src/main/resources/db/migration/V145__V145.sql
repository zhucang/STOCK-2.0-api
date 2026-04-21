update `stock`.`user_info` a, `stock`.`sys_user` b
set agent_nick_name = b.nick_name
where agent_id = b.user_id