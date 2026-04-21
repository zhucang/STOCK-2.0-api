insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 117,'用户实名认证是否填写真实姓名',0,1,63 from switch_set where not exists (select id from switch_set where id = 117)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 118,'用户实名认证是否上传背面身份证',0,1,63 from switch_set where not exists (select id from switch_set where id = 118)limit 1;
