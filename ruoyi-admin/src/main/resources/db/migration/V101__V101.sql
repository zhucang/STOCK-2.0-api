insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 103,'app提现是否显示变更余额',0,1,103 from switch_set where not exists (select id from switch_set where id = 103)limit 1;
