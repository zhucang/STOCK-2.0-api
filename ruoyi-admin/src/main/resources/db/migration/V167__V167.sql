insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 121,'有未结清的贷款禁止继续贷款',0,1,121 from switch_set where not exists (select id from switch_set where id = 121)limit 1;
insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 122,'有申请中的贷款禁止继续贷款',0,1,122 from switch_set where not exists (select id from switch_set where id = 122)limit 1;
