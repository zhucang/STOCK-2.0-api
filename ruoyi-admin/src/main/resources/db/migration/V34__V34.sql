insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 86,'app极速交易是否显示收益率',1,1,86 from switch_set where not exists (select id from switch_set where id = 86)limit 1;
