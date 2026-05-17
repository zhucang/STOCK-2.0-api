ALTER TABLE `copy_trade_order`
    ADD COLUMN `follower_sell_order_price` decimal(22,6) DEFAULT NULL COMMENT '跟单单平仓价格快照' AFTER `follower_order_code`,
    ADD COLUMN `follower_profit_and_lose` decimal(22,6) DEFAULT NULL COMMENT '跟单单收益快照' AFTER `follower_sell_order_price`,
    ADD COLUMN `follower_all_profit_and_lose` decimal(22,6) DEFAULT NULL COMMENT '跟单单总收益快照' AFTER `follower_profit_and_lose`,
    ADD COLUMN `close_source` tinyint DEFAULT NULL COMMENT '平仓来源 0交易员同步 1跟随者手动 2系统强平' AFTER `follower_all_profit_and_lose`,
    ADD COLUMN `close_time` datetime DEFAULT NULL COMMENT '跟单单平仓时间快照' AFTER `close_source`;
