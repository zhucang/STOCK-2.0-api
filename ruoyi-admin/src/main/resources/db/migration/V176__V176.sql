insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 124,'app用户是否可以绑定邮箱',0,1,124 from switch_set where not exists (select id from switch_set where id = 124)limit 1;
