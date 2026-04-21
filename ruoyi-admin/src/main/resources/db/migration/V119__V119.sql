insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 111,'app开启资金兑换功能',0,1,111 from switch_set where not exists (select id from switch_set where id = 111)limit 1;
