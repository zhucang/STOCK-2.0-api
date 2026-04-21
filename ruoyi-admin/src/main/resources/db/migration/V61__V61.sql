ALTER TABLE `stock`.`loan_order_interest_record`
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`) USING BTREE,
ADD INDEX(`loan_order_id`) USING BTREE;