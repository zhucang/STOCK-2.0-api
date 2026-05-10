ALTER TABLE `copy_trade_order`
    ADD UNIQUE KEY `uk_copy_trade_order_relation_leader` (`product_type`, `relation_id`, `leader_position_id`);
