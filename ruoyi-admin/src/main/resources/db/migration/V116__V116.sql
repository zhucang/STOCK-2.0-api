ALTER TABLE `stock`.`balance_convert_record`
    MODIFY COLUMN `trans_amt` decimal(27, 10) NOT NULL COMMENT '转化金额' AFTER `user_id`,
    MODIFY COLUMN `converted_amount` decimal(27, 10) NOT NULL COMMENT '换算金额' AFTER `trans_amt`,
    MODIFY COLUMN `exchange_rate` decimal(27, 10) NOT NULL COMMENT '汇率' AFTER `converted_amount`,
    MODIFY COLUMN `handing_fee` decimal(27, 10) NULL DEFAULT 0.0000000000 COMMENT '手续费金额' AFTER `exchange_rate`;

ALTER TABLE `stock`.`bibi_trade_order`
    MODIFY COLUMN `order_amount` decimal(27, 10) NOT NULL COMMENT '订单金额' AFTER `product_type`,
    MODIFY COLUMN `turnover_amount` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '成交额' AFTER `order_amount`,
    MODIFY COLUMN `order_volume` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '成交量' AFTER `turnover_amount`,
    MODIFY COLUMN `handing_fee` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '手续费' AFTER `order_volume`,
    MODIFY COLUMN `product_price` decimal(27, 10) NOT NULL COMMENT '产品价格' AFTER `handing_fee`;

ALTER TABLE `stock`.`bonus_config`
    MODIFY COLUMN `bonus_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '赠送金额' AFTER `end_time`;

ALTER TABLE `stock`.`cryptocurrency_everyday_record`
    MODIFY COLUMN `now_price` decimal(27, 10) NOT NULL COMMENT '现价' AFTER `product_code`,
    MODIFY COLUMN `change_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '涨跌幅' AFTER `now_price`,
    MODIFY COLUMN `open_price` decimal(27, 10) NOT NULL COMMENT '开盘价' AFTER `change_rate`,
    MODIFY COLUMN `close_price` decimal(27, 10) NOT NULL COMMENT '收盘价' AFTER `open_price`,
    MODIFY COLUMN `business_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '交易总金额' AFTER `close_price`,
    MODIFY COLUMN `business_volume` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '交易总量' AFTER `business_amount`,
    MODIFY COLUMN `max_price` decimal(27, 10) NOT NULL COMMENT '最高价' AFTER `business_volume`,
    MODIFY COLUMN `min_price` decimal(27, 10) NOT NULL COMMENT '最低价' AFTER `max_price`;

ALTER TABLE `stock`.`currency_exchange_rate`
    MODIFY COLUMN `exchange_rate` decimal(27, 10) NOT NULL COMMENT '汇率' AFTER `currency_id_to`,
    MODIFY COLUMN `opposite_exchange_rate` decimal(27, 10) NOT NULL COMMENT '反汇率' AFTER `exchange_rate`,
    MODIFY COLUMN `fee_percent` decimal(27, 10) NULL DEFAULT 0.00000000 COMMENT '手续费率' AFTER `update_time`;

ALTER TABLE `stock`.`fast_trade_order`
    MODIFY COLUMN `order_price` decimal(27, 10) NOT NULL COMMENT '下单金额' AFTER `order_direction`,
    MODIFY COLUMN `handing_fee` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '手续费' AFTER `order_price`,
    MODIFY COLUMN `win_profit_ratio` decimal(27, 10) NOT NULL COMMENT '赢收益比率' AFTER `duration_label`,
    MODIFY COLUMN `lose_profit_ratio` decimal(27, 10) NOT NULL COMMENT '输扣除比率' AFTER `win_profit_ratio`,
    MODIFY COLUMN `buy_price` decimal(27, 10) NOT NULL COMMENT '买入价格' AFTER `lose_profit_ratio`,
    MODIFY COLUMN `sell_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '结算价格' AFTER `buy_price`,
    MODIFY COLUMN `order_profit` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '订单收益' AFTER `deliver_time`;

ALTER TABLE `stock`.`fast_trade_order_options`
    MODIFY COLUMN `up_win_profit_ratio` decimal(27, 10) NOT NULL COMMENT '涨赢收益率' AFTER `status`,
    MODIFY COLUMN `up_lose_profit_ratio` decimal(27, 10) NOT NULL COMMENT '涨输扣除率' AFTER `up_win_profit_ratio`,
    MODIFY COLUMN `up_fluctuation_ratio` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '涨波动率' AFTER `up_lose_profit_ratio`,
    MODIFY COLUMN `down_win_profit_ratio` decimal(27, 10) NOT NULL COMMENT '跌赢收益率' AFTER `up_fluctuation_ratio`,
    MODIFY COLUMN `down_lose_profit_ratio` decimal(27, 10) NOT NULL COMMENT '跌输扣除率' AFTER `down_win_profit_ratio`,
    MODIFY COLUMN `down_fluctuation_ratio` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '跌波动率' AFTER `down_lose_profit_ratio`,
    MODIFY COLUMN `min_buy_amount` decimal(27, 10) NOT NULL COMMENT '最小买入金额' AFTER `down_fluctuation_ratio`,
    MODIFY COLUMN `max_buy_amount` decimal(27, 10) NULL DEFAULT NULL COMMENT '最大买入金额' AFTER `min_buy_amount`,
    MODIFY COLUMN `min_user_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '用户金额大于此刻度可下单' AFTER `vip_level_limit`;

ALTER TABLE `stock`.`financial_order`
    MODIFY COLUMN `buy_price` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '理财购买金额' AFTER `financial_time`,
    MODIFY COLUMN `break_contract_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '违约金比率' AFTER `already_interest_count`,
    MODIFY COLUMN `daily_income_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '日收益率' AFTER `break_contract_rate`,
    MODIFY COLUMN `float_daily_income_min_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '浮动日收益最低' AFTER `currency_id`,
    MODIFY COLUMN `float_daily_income_max_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '浮动日收益最高' AFTER `float_daily_income_min_rate`;

ALTER TABLE `stock`.`financial_pay_interest_record`
    MODIFY COLUMN `daily_income_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '日收益率' AFTER `financial_order_id`,
    MODIFY COLUMN `pay_amount` decimal(27, 10) NOT NULL COMMENT '派息金额' AFTER `pay_time`;

ALTER TABLE `stock`.`financial_product`
    MODIFY COLUMN `min_price` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '最低购买' AFTER `financial_img`,
    MODIFY COLUMN `max_price` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '最高购买' AFTER `min_price`,
    MODIFY COLUMN `float_daily_income_min_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '浮动日收益最低' AFTER `financial_time`,
    MODIFY COLUMN `float_daily_income_max_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '浮动日收益最高' AFTER `float_daily_income_min_rate`,
    MODIFY COLUMN `fixed_income_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '固定收益率' AFTER `float_daily_income_max_rate`,
    MODIFY COLUMN `break_contract_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '违约金比率' AFTER `sort`,
    MODIFY COLUMN `user_amount_limit` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '用户余额最低要求' AFTER `break_contract_rate`,
    MODIFY COLUMN `open_quantity` decimal(27, 10) NULL DEFAULT NULL COMMENT '开放数量' AFTER `auto_approve`,
    MODIFY COLUMN `remaining_quantity` decimal(27, 10) NULL DEFAULT NULL COMMENT '剩余数量' AFTER `open_quantity`;

ALTER TABLE `stock`.`forex_everyday_record`
    MODIFY COLUMN `now_price` decimal(27, 10) NOT NULL COMMENT '现价' AFTER `product_code`,
    MODIFY COLUMN `change_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '涨跌幅' AFTER `now_price`,
    MODIFY COLUMN `open_price` decimal(27, 10) NOT NULL COMMENT '开盘价' AFTER `change_rate`,
    MODIFY COLUMN `close_price` decimal(27, 10) NOT NULL COMMENT '收盘价' AFTER `open_price`,
    MODIFY COLUMN `business_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '交易总金额' AFTER `close_price`,
    MODIFY COLUMN `business_volume` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '交易总量' AFTER `business_amount`,
    MODIFY COLUMN `max_price` decimal(27, 10) NOT NULL COMMENT '最高价' AFTER `business_volume`,
    MODIFY COLUMN `min_price` decimal(27, 10) NOT NULL COMMENT '最低价' AFTER `max_price`;

ALTER TABLE `stock`.`futures_everyday_record`
    MODIFY COLUMN `now_price` decimal(27, 10) NOT NULL COMMENT '现价' AFTER `product_code`,
    MODIFY COLUMN `change_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '涨跌幅' AFTER `now_price`,
    MODIFY COLUMN `open_price` decimal(27, 10) NOT NULL COMMENT '开盘价' AFTER `change_rate`,
    MODIFY COLUMN `close_price` decimal(27, 10) NOT NULL COMMENT '收盘价' AFTER `open_price`,
    MODIFY COLUMN `business_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '交易总金额' AFTER `close_price`,
    MODIFY COLUMN `business_volume` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '交易总量' AFTER `business_amount`,
    MODIFY COLUMN `max_price` decimal(27, 10) NOT NULL COMMENT '最高价' AFTER `business_volume`,
    MODIFY COLUMN `min_price` decimal(27, 10) NOT NULL COMMENT '最低价' AFTER `max_price`;

ALTER TABLE `stock`.`loan_order`
    MODIFY COLUMN `order_price` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '订单金额' AFTER `loan_product_id`,
    MODIFY COLUMN `loan_daily_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '贷款每日利息率' AFTER `interest_free_days`,
    MODIFY COLUMN `break_contract_daily_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '违约每日利息率' AFTER `loan_daily_rate`,
    MODIFY COLUMN `need_pay_interest` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '需要支付利息' AFTER `already_loan_days`;

ALTER TABLE `stock`.`loan_order_interest_record`
    MODIFY COLUMN `order_price` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '订单金额' AFTER `loan_order_id`;

ALTER TABLE `stock`.`loan_product`
    MODIFY COLUMN `min_price` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '最低金额' AFTER `product_img`,
    MODIFY COLUMN `max_price` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '最高金额' AFTER `min_price`,
    MODIFY COLUMN `loan_daily_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '贷款每日利息率' AFTER `interest_free_days`,
    MODIFY COLUMN `break_contract_daily_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '违约每日利息率' AFTER `loan_daily_rate`;

ALTER TABLE `stock`.`new_product_apply_purchase`
    MODIFY COLUMN `listing_price` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '上市价格' AFTER `self_sell_product_id`,
    MODIFY COLUMN `listing_day_increase_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '上市当天涨幅率' AFTER `listing_quantity`,
    MODIFY COLUMN `user_amount_limit` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '用户余额最低要求' AFTER `listing_day_increase_rate`,
    MODIFY COLUMN `listed_day_increase_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '上市后每天涨幅率' AFTER `listing_end_date`,
    MODIFY COLUMN `apply_purchase_min` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '单笔申购金额最低' AFTER `listed_day_increase_rate`,
    MODIFY COLUMN `apply_purchase_max` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '单笔申购金额最高' AFTER `apply_purchase_min`;

ALTER TABLE `stock`.`order_fee_setting`
    MODIFY COLUMN `fee_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '手续费费率' AFTER `key`;

ALTER TABLE `stock`.`platform_currency`
    MODIFY COLUMN `balance_convert_min_limit` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '资金互转金额最小限制' AFTER `use_in_fast_trade`,
    MODIFY COLUMN `balance_convert_max_limit` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '资金互转金额最大限制' AFTER `balance_convert_min_limit`;

ALTER TABLE `stock`.`product_quote_control`
    MODIFY COLUMN `preset_price` decimal(27, 10) NOT NULL COMMENT '预设点位' AFTER `product_type`,
    MODIFY COLUMN `start_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '启动点位' AFTER `preset_end_time`;

ALTER TABLE `stock`.`product_setting`
    MODIFY COLUMN `duty_fee` decimal(27, 10) NULL DEFAULT 0.001000 COMMENT '印花税率' AFTER `id`,
    MODIFY COLUMN `stay_fee` decimal(27, 10) NULL DEFAULT 0.001000 COMMENT '留仓费率' AFTER `duty_fee`,
    MODIFY COLUMN `buy_min_amt` decimal(27, 10) NULL DEFAULT 1.000000 COMMENT '最小购买金额' AFTER `stay_max_days`,
    MODIFY COLUMN `buy_min_num` decimal(27, 10) NULL DEFAULT 1.000000 COMMENT '最小购买数量' AFTER `buy_min_amt`,
    MODIFY COLUMN `buy_max_num` decimal(27, 10) NULL DEFAULT 1000000.000000 COMMENT '最大购买数量' AFTER `buy_min_num`,
    MODIFY COLUMN `buy_max_amt_percent` decimal(27, 10) NULL DEFAULT 1.000000 COMMENT '最大买入比例' AFTER `buy_max_num`,
    MODIFY COLUMN `force_stop_percent` decimal(27, 10) NULL DEFAULT 0.990000 COMMENT '强制平仓比例' AFTER `buy_max_amt_percent`,
    MODIFY COLUMN `buy_num_lots` decimal(27, 10) NULL DEFAULT NULL COMMENT 'N分钟内交易手数不能超过M手（M）' AFTER `buy_num_times`,
    MODIFY COLUMN `hand_correspond_num` decimal(27, 10) NULL DEFAULT 100.000000 COMMENT '一手对应的数量' AFTER `cant_sell_times`;

ALTER TABLE `stock`.`recharge_channel_config`
    MODIFY COLUMN `channel_min_limit` decimal(27, 10) NOT NULL COMMENT '最小入金' AFTER `account_type`,
    MODIFY COLUMN `channel_max_limit` decimal(27, 10) NOT NULL COMMENT '最大入金' AFTER `channel_min_limit`,
    MODIFY COLUMN `first_recharge_winnings_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '首次充值赠送金额' AFTER `customer_url`,
    MODIFY COLUMN `first_recharge_winnings_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '首次充值赠送比率' AFTER `first_recharge_winnings_amount`,
    MODIFY COLUMN `daily_recharge_winnings_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '日常充值赠送金额' AFTER `first_recharge_winnings_method`,
    MODIFY COLUMN `daily_recharge_winnings_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '日常充值赠送比率' AFTER `daily_recharge_winnings_amount`,
    MODIFY COLUMN `channel_name_vi` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '通道名称-越南' AFTER `channel_name_th`;

ALTER TABLE `stock`.`self_sell_product`
    MODIFY COLUMN `initial_price` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '初始价格' AFTER `product_img`;

ALTER TABLE `stock`.`self_sell_product_daily_data_config`
    MODIFY COLUMN `finally_change_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '设置的最终涨跌幅' AFTER `is_default`,
    MODIFY COLUMN `finally_price` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '设置的最终价格' AFTER `finally_change_rate`;

ALTER TABLE `stock`.`self_sell_product_real_time`
    MODIFY COLUMN `reference_price` decimal(27, 10) NOT NULL COMMENT '参考价格' AFTER `product_type`,
    MODIFY COLUMN `reference_change_rate` decimal(27, 10) NOT NULL COMMENT '参考涨跌幅' AFTER `reference_price`,
    MODIFY COLUMN `open_price` decimal(27, 10) NOT NULL COMMENT '开盘价格' AFTER `reference_change_rate`,
    MODIFY COLUMN `close_price` decimal(27, 10) NOT NULL COMMENT '收盘价格' AFTER `open_price`,
    MODIFY COLUMN `high_price` decimal(27, 10) NOT NULL COMMENT '最高价格' AFTER `close_price`,
    MODIFY COLUMN `low_price` decimal(27, 10) NOT NULL COMMENT '最低价格' AFTER `high_price`,
    MODIFY COLUMN `average_price` decimal(27, 10) NOT NULL DEFAULT 0.0000000000 COMMENT '平均价格' AFTER `low_price`,
    MODIFY COLUMN `volumes` decimal(27, 10) NOT NULL COMMENT '成交量' AFTER `average_price`,
    MODIFY COLUMN `amount` decimal(27, 10) NOT NULL COMMENT '成交额' AFTER `volumes`;

ALTER TABLE `stock`.`spot_trade_order`
    MODIFY COLUMN `buy_order_price` decimal(27, 10) NOT NULL COMMENT '购买价格' AFTER `buy_order_time`,
    MODIFY COLUMN `sell_order_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '卖出价格' AFTER `sell_order_time`,
    MODIFY COLUMN `order_num` decimal(27, 10) NOT NULL COMMENT '订单数量' AFTER `sell_order_price`,
    MODIFY COLUMN `order_total_price` decimal(27, 10) NOT NULL COMMENT '订单总金额' AFTER `order_num`,
    MODIFY COLUMN `order_fee` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '手续费' AFTER `order_total_price`,
    MODIFY COLUMN `profit_and_lose` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '浮动盈亏' AFTER `order_fee`,
    MODIFY COLUMN `all_profit_and_lose` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '总盈亏' AFTER `profit_and_lose`;

ALTER TABLE `stock`.`stock_everyday_record`
    MODIFY COLUMN `now_price` decimal(27, 10) NOT NULL COMMENT '现价' AFTER `product_code`,
    MODIFY COLUMN `change_rate` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '涨跌幅' AFTER `now_price`,
    MODIFY COLUMN `open_price` decimal(27, 10) NOT NULL COMMENT '开盘价' AFTER `change_rate`,
    MODIFY COLUMN `close_price` decimal(27, 10) NOT NULL COMMENT '收盘价' AFTER `open_price`,
    MODIFY COLUMN `business_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '交易总金额' AFTER `close_price`,
    MODIFY COLUMN `business_volume` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '交易总量' AFTER `business_amount`,
    MODIFY COLUMN `max_price` decimal(27, 10) NOT NULL COMMENT '最高价' AFTER `business_volume`,
    MODIFY COLUMN `min_price` decimal(27, 10) NOT NULL COMMENT '最低价' AFTER `max_price`;

ALTER TABLE `stock`.`user_amount`
    MODIFY COLUMN `amount` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '余额' AFTER `id`,
    MODIFY COLUMN `frozen_amount` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '冻结金额' AFTER `user_id`;

ALTER TABLE `stock`.`user_apply_purchase_order`
    MODIFY COLUMN `apply_purchase_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '申购金额' AFTER `apply_purchase_time`,
    MODIFY COLUMN `apply_purchase_single_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '申购单价' AFTER `apply_purchase_quantity`,
    MODIFY COLUMN `winning_rate` decimal(27, 10) NULL DEFAULT NULL COMMENT '中签率' AFTER `listing_start_date`,
    MODIFY COLUMN `winning_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '中签金额' AFTER `winning_rate`,
    MODIFY COLUMN `winning_quantity` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '中签数量' AFTER `winning_amount`,
    MODIFY COLUMN `sell_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '平仓价' AFTER `winning_quantity`,
    MODIFY COLUMN `profit_and_lose` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '盈亏' AFTER `sell_time`;

ALTER TABLE `stock`.`user_bibi_assets`
    MODIFY COLUMN `bibi_amount` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '持有数量' AFTER `product_type`,
    MODIFY COLUMN `bibi_frozen_amount` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '冻结数量' AFTER `bibi_amount`,
    MODIFY COLUMN `buy_amount_all` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '买入总金额' AFTER `bibi_frozen_amount`,
    MODIFY COLUMN `sell_amount_all` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '卖出总金额' AFTER `buy_amount_all`,
    MODIFY COLUMN `buy_and_sell_amount_difference` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '买入与卖出金额的差值' AFTER `sell_amount_all`;

ALTER TABLE `stock`.`user_bill_detail`
    MODIFY COLUMN `order_amount` decimal(27, 10) NOT NULL COMMENT '流水金额' AFTER `user_id`,
    MODIFY COLUMN `amount_before` decimal(27, 10) NOT NULL COMMENT '变更前总资金' AFTER `order_time`,
    MODIFY COLUMN `amount_after` decimal(27, 10) NOT NULL COMMENT '变更后总资金' AFTER `amount_before`;

ALTER TABLE `stock`.`user_commission_record`
    MODIFY COLUMN `commission_amount` decimal(27, 10) NOT NULL COMMENT '返佣金额' AFTER `commission_level`,
    MODIFY COLUMN `commission_profit` decimal(27, 10) NOT NULL COMMENT '返佣比例' AFTER `commission_amount`;

ALTER TABLE `stock`.`user_cryptocurrency_position`
    MODIFY COLUMN `buy_order_price` decimal(27, 10) NOT NULL COMMENT '购买价格' AFTER `buy_order_time`,
    MODIFY COLUMN `sell_order_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '卖出价格' AFTER `sell_order_time`,
    MODIFY COLUMN `order_total_price` decimal(27, 10) NOT NULL COMMENT '订单买入花费' AFTER `order_lever`,
    MODIFY COLUMN `order_fee` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '手续费' AFTER `order_total_price`,
    MODIFY COLUMN `order_yhs_fee` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '印花税' AFTER `order_fee`,
    MODIFY COLUMN `profit_and_lose` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '浮动盈亏' AFTER `order_yhs_fee`,
    MODIFY COLUMN `all_profit_and_lose` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '总盈亏' AFTER `profit_and_lose`,
    MODIFY COLUMN `stop_profit_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '止盈线' AFTER `currency_id`,
    MODIFY COLUMN `stop_loss_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '止损线' AFTER `stop_profit_price`;

ALTER TABLE `stock`.`user_dm_amount_change_record`
    MODIFY COLUMN `order_amount` decimal(27, 10) NULL DEFAULT NULL COMMENT '订单金额' AFTER `user_id`,
    MODIFY COLUMN `dm_multiples` decimal(27, 10) NULL DEFAULT NULL COMMENT '打码倍数' AFTER `order_amount`,
    MODIFY COLUMN `dm_amount` decimal(27, 10) NULL DEFAULT NULL COMMENT '打码量' AFTER `dm_multiples`,
    MODIFY COLUMN `dm_amount_before` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '打码量变更前' AFTER `dm_amount`,
    MODIFY COLUMN `dm_amount_after` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '打码量变更后' AFTER `dm_amount_before`;

ALTER TABLE `stock`.`user_fast_trade_control`
    MODIFY COLUMN `min_limit_trade_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '最小界限注额' AFTER `control_type`,
    MODIFY COLUMN `max_limit_trade_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '最大界限注额' AFTER `min_limit_trade_amount`,
    MODIFY COLUMN `win_percent_within_trade_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '注额界限范围之内赢率' AFTER `max_limit_trade_amount`,
    MODIFY COLUMN `win_percent_outside_trade_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '注额界限范围之外赢率' AFTER `win_percent_within_trade_amount`,
    MODIFY COLUMN `min_limit_user_balance` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '最小界限用户余额' AFTER `win_percent_outside_trade_amount`,
    MODIFY COLUMN `max_limit_user_balance` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '最大界限用户余额' AFTER `min_limit_user_balance`,
    MODIFY COLUMN `win_percent_within_user_balance` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '用户余额界限范围内赢率' AFTER `max_limit_user_balance`,
    MODIFY COLUMN `win_percent_outside_user_balance` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '用户余额界限范围之外赢率' AFTER `win_percent_within_user_balance`;

ALTER TABLE `stock`.`user_forex_position`
    MODIFY COLUMN `buy_order_price` decimal(27, 10) NOT NULL COMMENT '购买价格' AFTER `buy_order_time`,
    MODIFY COLUMN `sell_order_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '卖出价格' AFTER `sell_order_time`,
    MODIFY COLUMN `order_total_price` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '订单买入花费' AFTER `order_lever`,
    MODIFY COLUMN `order_fee` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '手续费' AFTER `order_total_price`,
    MODIFY COLUMN `order_yhs_fee` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '印花税' AFTER `order_fee`,
    MODIFY COLUMN `profit_and_lose` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '浮动盈亏' AFTER `order_yhs_fee`,
    MODIFY COLUMN `all_profit_and_lose` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '总盈亏' AFTER `profit_and_lose`,
    MODIFY COLUMN `stop_profit_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '止盈线' AFTER `currency_id`,
    MODIFY COLUMN `stop_loss_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '止损线' AFTER `stop_profit_price`;

ALTER TABLE `stock`.`user_futures_position`
    MODIFY COLUMN `buy_order_price` decimal(27, 10) NOT NULL COMMENT '购买价格' AFTER `buy_order_time`,
    MODIFY COLUMN `sell_order_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '卖出价格' AFTER `sell_order_time`,
    MODIFY COLUMN `order_total_price` decimal(27, 10) NOT NULL COMMENT '订单买入花费' AFTER `order_lever`,
    MODIFY COLUMN `order_fee` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '手续费' AFTER `order_total_price`,
    MODIFY COLUMN `order_yhs_fee` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '印花税' AFTER `order_fee`,
    MODIFY COLUMN `profit_and_lose` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '浮动盈亏' AFTER `order_yhs_fee`,
    MODIFY COLUMN `all_profit_and_lose` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '总盈亏' AFTER `profit_and_lose`,
    MODIFY COLUMN `stop_profit_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '止盈线' AFTER `currency_id`,
    MODIFY COLUMN `stop_loss_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '止损线' AFTER `stop_profit_price`;

ALTER TABLE `stock`.`user_info`
    MODIFY COLUMN `overflow_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '单控溢出数值' AFTER `start_delay_time`,
    MODIFY COLUMN `need_order_amount` decimal(27, 0) NULL DEFAULT 0.000000 COMMENT '打码量' AFTER `status`,
    MODIFY COLUMN `fast_trade_win_profit_ratio` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '极速交易赢收益率' AFTER `is_can_withdraw`,
    MODIFY COLUMN `fast_trade_lose_profit_ratio` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '极速交易输收益率' AFTER `fast_trade_win_profit_ratio`;

ALTER TABLE `stock`.`user_loan_repayment_order`
    MODIFY COLUMN `order_amount` decimal(27, 10) NOT NULL COMMENT '还款金额' AFTER `order_code`;

ALTER TABLE `stock`.`user_point_change_record`
    MODIFY COLUMN `order_amount` decimal(27, 10) NOT NULL COMMENT '充值金额' AFTER `order_code`,
    MODIFY COLUMN `user_amount_before` decimal(27, 10) NULL DEFAULT NULL COMMENT '余额变更前' AFTER `currency_id`,
    MODIFY COLUMN `user_amount_after` decimal(27, 10) NULL DEFAULT NULL COMMENT '余额变更后' AFTER `user_amount_before`;

ALTER TABLE `stock`.`user_realtime_control`
    MODIFY COLUMN `volumes` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '成交量' AFTER `time`,
    MODIFY COLUMN `price` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '现价' AFTER `volumes`,
    MODIFY COLUMN `rates` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '涨跌幅' AFTER `price`,
    MODIFY COLUMN `amounts` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '手数' AFTER `rates`,
    MODIFY COLUMN `average_price` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '均价' AFTER `amounts`;

ALTER TABLE `stock`.`user_rebate_rate`
    MODIFY COLUMN `rebate_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '返佣比率' AFTER `rebate_name`;

ALTER TABLE `stock`.`user_recharge`
    MODIFY COLUMN `recharge_amount` decimal(27, 10) NOT NULL COMMENT '充值金额' AFTER `order_code`,
    MODIFY COLUMN `user_amount_before` decimal(27, 10) NULL DEFAULT NULL COMMENT '余额变更前' AFTER `recharge_msg`,
    MODIFY COLUMN `user_amount_after` decimal(27, 10) NULL DEFAULT NULL COMMENT '余额变更后' AFTER `user_amount_before`;

ALTER TABLE `stock`.`user_stock_position`
    MODIFY COLUMN `buy_order_price` decimal(27, 10) NOT NULL COMMENT '购买价格' AFTER `buy_order_time`,
    MODIFY COLUMN `sell_order_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '卖出价格' AFTER `sell_order_time`,
    MODIFY COLUMN `order_total_price` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '订单买入花费' AFTER `order_lever`,
    MODIFY COLUMN `order_fee` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '手续费' AFTER `order_total_price`,
    MODIFY COLUMN `order_yhs_fee` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '印花税' AFTER `order_fee`,
    MODIFY COLUMN `profit_and_lose` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '浮动盈亏' AFTER `order_yhs_fee`,
    MODIFY COLUMN `all_profit_and_lose` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '总盈亏' AFTER `profit_and_lose`,
    MODIFY COLUMN `stop_profit_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '止盈线' AFTER `currency_id`,
    MODIFY COLUMN `stop_loss_price` decimal(27, 10) NULL DEFAULT NULL COMMENT '止损线' AFTER `stop_profit_price`;

ALTER TABLE `stock`.`user_vip_level_config`
    MODIFY COLUMN `recharge_amount_min` decimal(27, 10) NOT NULL COMMENT '最小充值金额' AFTER `vip_level`,
    MODIFY COLUMN `recharge_amount_max` decimal(27, 10) NOT NULL COMMENT '最大充值金额' AFTER `recharge_amount_min`,
    MODIFY COLUMN `recharge_bonus_ratio` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '充值赠送比率' AFTER `recharge_amount_max`,
    MODIFY COLUMN `recharge_bonus_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '充值赠送金额' AFTER `recharge_bonus_ratio`;

ALTER TABLE `stock`.`user_winnings_change_record`
    MODIFY COLUMN `order_amount` decimal(27, 10) NOT NULL COMMENT '充值金额' AFTER `order_code`,
    MODIFY COLUMN `user_amount_before` decimal(27, 10) NULL DEFAULT NULL COMMENT '余额变更前' AFTER `currency_id`,
    MODIFY COLUMN `user_amount_after` decimal(27, 10) NULL DEFAULT NULL COMMENT '余额变更后' AFTER `user_amount_before`;

ALTER TABLE `stock`.`user_withdraw`
    MODIFY COLUMN `withdraw_amount` decimal(27, 10) NOT NULL COMMENT '提现金额' AFTER `order_code`,
    MODIFY COLUMN `withdraw_fee` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '提现手续费' AFTER `withdraw_amount`,
    MODIFY COLUMN `received_amount` decimal(27, 10) NOT NULL COMMENT '到账金额' AFTER `withdraw_fee`,
    MODIFY COLUMN `user_amt_before` decimal(27, 10) NOT NULL COMMENT '余额变更前' AFTER `withdraw_type`,
    MODIFY COLUMN `user_amt_after` decimal(27, 10) NOT NULL COMMENT '余额变更后' AFTER `user_amt_before`;

ALTER TABLE `stock`.`withdraw_channel_config`
    MODIFY COLUMN `withdraw_min_amount` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '提现最小金额' AFTER `channel_name`,
    MODIFY COLUMN `withdraw_max_amount` decimal(27, 10) NOT NULL DEFAULT 0.000000 COMMENT '提现最大金额' AFTER `withdraw_min_amount`,
    MODIFY COLUMN `handing_fee_fixed_amount` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '固定手续费金额' AFTER `handing_fee_method`,
    MODIFY COLUMN `handing_fee_rate` decimal(27, 10) NULL DEFAULT 0.000000 COMMENT '百分比手续费率' AFTER `handing_fee_fixed_amount`;