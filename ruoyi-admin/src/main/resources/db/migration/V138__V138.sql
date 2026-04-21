insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 119,'合约交易是否可以设置止盈止损',0,1,119 from switch_set where not exists (select id from switch_set where id = 119)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 120,'客服是否显示备注',0,1,120 from switch_set where not exists (select id from switch_set where id = 120)limit 1;
