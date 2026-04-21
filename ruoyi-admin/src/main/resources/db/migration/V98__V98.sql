ALTER TABLE `stock`.`new_product_apply_purchase`
    MODIFY COLUMN `listing_quantity` int NULL DEFAULT NULL COMMENT '上市数量' AFTER `listing_price`,
    MODIFY COLUMN `remaining_quantity` int NULL DEFAULT NULL COMMENT '剩余数量' AFTER `apply_purchase_end_date`;