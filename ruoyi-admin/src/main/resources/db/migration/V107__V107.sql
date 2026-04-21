insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 105,'app禁止国外ip登陆 （港澳台除外）',1,0,31 from switch_set where not exists (select id from switch_set where id = 105)limit 1;

insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 106,'后台禁止国外ip登陆 （港澳台除外）',1,0,33 from switch_set where not exists (select id from switch_set where id = 106)limit 1;
