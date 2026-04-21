insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 107,'是否开启优盾支付',1,1,107 from switch_set where not exists (select id from switch_set where id = 107)limit 1;
