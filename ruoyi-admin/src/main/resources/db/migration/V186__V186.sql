insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 136,'用户贷款计算VIP等级',1,1,116 from switch_set where not exists (select id from switch_set where id = 136)limit 1;
