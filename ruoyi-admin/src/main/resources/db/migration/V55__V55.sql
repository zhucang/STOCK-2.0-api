ALTER TABLE `stock`.`user_info`
    ADD COLUMN `agent_nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理昵称' AFTER `agent_name`;

update `stock`.`user_info`,`stock`.`sys_user`
set agent_nick_name = `stock`.`sys_user`.nick_name
where agent_id = user_id;