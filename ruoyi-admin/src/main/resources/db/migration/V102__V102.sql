update `stock`.`user_amount` a,
    (select sum(received_amount + withdraw_fee) as order_amount,user_id,currency_id from `stock`.`user_withdraw` where `withdraw_status` = 0 group by user_id,currency_id) as b
set frozen_amount = frozen_amount + order_amount
where a.user_id = b.user_id and a.currency_id = b.currency_id;