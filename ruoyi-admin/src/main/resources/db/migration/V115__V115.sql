insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 109,'是否设置钱包地址唯一性',0,1,109 from switch_set where not exists (select id from switch_set where id = 109)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 110,'是否设置银行卡号唯一性',0,1,110 from switch_set where not exists (select id from switch_set where id = 110)limit 1;
