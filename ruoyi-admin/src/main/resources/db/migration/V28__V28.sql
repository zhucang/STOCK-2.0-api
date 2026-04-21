ALTER TABLE `stock`.`user_winnings_change_record`
    MODIFY COLUMN `order_type` tinyint(1) NOT NULL COMMENT '订单类型： 0：彩金赠送（系统充值） 1：彩金赠送（福利彩金） 2：彩金回收 3:充值彩金 4：注册彩金' AFTER `order_amount`;

insert into user_winnings_change_record(user_id, order_code, order_amount, order_type, create_time, operator_name, currency_id, user_amount_before, user_amount_after, remark)
select user_id,id,order_amount,3,order_time,null,currency_id,amount_before,amount_after,null from user_bill_detail where order_class =55;

insert into user_winnings_change_record(user_id, order_code, order_amount, order_type, create_time, operator_name, currency_id, user_amount_before, user_amount_after, remark)
select user_id,id,order_amount,4,order_time,null,currency_id,amount_before,amount_after,null from user_bill_detail where order_class =56;