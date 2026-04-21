insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 102,'提现是否可以选择地址',0,1,102 from switch_set where not exists (select id from switch_set where id = 102)limit 1;

ALTER TABLE `stock`.`financial_product`
    MODIFY COLUMN `open_quantity` decimal(22, 6) NULL DEFAULT NULL COMMENT '开放数量' AFTER `auto_approve`,
    MODIFY COLUMN `remaining_quantity` decimal(22, 6) NULL DEFAULT NULL COMMENT '剩余数量' AFTER `open_quantity`;