ALTER TABLE `stock`.`self_sell_product_realtime`
DROP INDEX `type`,
DROP INDEX `stockCode`,
ADD INDEX(`product_type`, `product_code`, `time`, `create_time`) USING BTREE;

insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 104,'用户是否可以修改昵称',0,1,104 from switch_set where not exists (select id from switch_set where id = 104)limit 1;
