insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 123,'telegram通知是否只推送真实用户通知',0,1,123 from switch_set where not exists (select id from switch_set where id = 123)limit 1;
