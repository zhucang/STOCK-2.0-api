insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 108,'是否要求高级实名认证名字与初级实名认证名字一致',0,1,77 from switch_set where not exists (select id from switch_set where id = 108)limit 1;
