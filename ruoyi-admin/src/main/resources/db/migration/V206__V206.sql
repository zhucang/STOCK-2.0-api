ALTER TABLE `stock`.`user_info`
    ADD COLUMN `auth_phone_number` varchar(255) NULL COMMENT '实名电话' AFTER `img3_key`;

ALTER TABLE `stock`.`user_auth_record`
    ADD COLUMN `auth_phone_number` varchar(255) NULL COMMENT '实名电话' AFTER `img3_key`;