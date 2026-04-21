ALTER TABLE `stock`.`loan_order`
    MODIFY COLUMN `id_type` tinyint(1) NULL DEFAULT 0 COMMENT '证件类型 0：身份证 1：驾驶证 2：护照 3：CPF' AFTER `residential_address`;