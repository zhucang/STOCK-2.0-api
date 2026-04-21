update `stock`.`user_info`,`stock`.`sys_user`
set agent_nick_name = `stock`.`sys_user`.nick_name
where agent_id = user_id;