ALTER TABLE `stock`.`user_withdraw`
    MODIFY COLUMN `withdraw_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '提现类型 0：银行卡 1：虚拟钱包 2：客服' AFTER `currency_id`;

insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 82,'提现是否要求打码量达到要求',0,1,82 from switch_set where not exists (select id from switch_set where id = 82)limit 1;

ALTER TABLE `stock`.`fast_trade_order_options`
    CHANGE COLUMN `up_profit_ratio` `up_win_profit_ratio` decimal(22, 6) NOT NULL COMMENT '涨赢收益率' AFTER `status`,
    CHANGE COLUMN `down_profit_ratio` `down_win_profit_ratio` decimal(22, 6) NOT NULL COMMENT '跌赢收益率' AFTER `up_fluctuation_ratio`,
    ADD COLUMN `up_lose_profit_ratio` decimal(22, 6) NOT NULL COMMENT '涨输扣除率' AFTER `up_win_profit_ratio`,
    ADD COLUMN `down_lose_profit_ratio` decimal(22, 6) NOT NULL COMMENT '跌输扣除率' AFTER `down_win_profit_ratio`;

update `stock`.`fast_trade_order_options` set up_lose_profit_ratio = up_win_profit_ratio,down_lose_profit_ratio = up_lose_profit_ratio;

ALTER TABLE `stock`.`fast_trade_order`
    CHANGE COLUMN `profit_ratio` `win_profit_ratio` decimal(22, 6) NOT NULL COMMENT '赢收益比率' AFTER `duration_label`,
    ADD COLUMN `lose_profit_ratio` decimal(22, 6) NOT NULL COMMENT '输扣除比率' AFTER `win_profit_ratio`;

ALTER TABLE `stock`.`text_lang`
    ADD COLUMN `content_visible_flag` tinyint(1) NULL DEFAULT 0 COMMENT '内容是否显示 0：是 1：否' AFTER `website_class`;