insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 126,'用户提现自动删除提现地址',1,1,126 from switch_set where not exists (select id from switch_set where id = 126)limit 1;
