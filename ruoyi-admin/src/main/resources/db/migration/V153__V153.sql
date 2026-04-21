ALTER TABLE `stock`.`stock_product`
    ADD INDEX(`is_self_sell`);
ALTER TABLE `stock`.`cryptocurrency_product`
    ADD INDEX(`is_self_sell`);