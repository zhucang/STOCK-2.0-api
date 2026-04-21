insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 125,'充值自动转化平台默认交易币种',1,1,66 from switch_set where not exists (select id from switch_set where id = 125)limit 1;
