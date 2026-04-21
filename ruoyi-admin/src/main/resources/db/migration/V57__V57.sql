insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 92,'贷款是否计入总客损',1,1,92 from switch_set where not exists (select id from switch_set where id = 92)limit 1;
