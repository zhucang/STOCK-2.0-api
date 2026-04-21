ALTER TABLE `stock`.`user_wallet_address`
    ADD COLUMN `wallet_tag` varchar(255) NULL COMMENT '钱包标签' AFTER `wallet_receipt_qr_code`;