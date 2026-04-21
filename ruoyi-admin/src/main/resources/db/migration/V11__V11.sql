ALTER TABLE `stock`.`switch_set`
    ADD COLUMN `sort` int NULL COMMENT '排序' AFTER `type`;

update `stock`.`switch_set` set sort = id;
update `stock`.`switch_set` set switch_name = '用户实名审核自动通过' where id = 57;

insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) values (65,'游客实名审核自动通过',1,1,57);

ALTER TABLE `stock`.`user_info`
    ADD COLUMN `remark` varchar(255) NULL COMMENT '备注' AFTER `is_del`;