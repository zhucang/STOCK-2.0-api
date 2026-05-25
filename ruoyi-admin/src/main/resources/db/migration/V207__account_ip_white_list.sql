CREATE TABLE IF NOT EXISTS `account_ip_white_list` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account_type` int NULL DEFAULT NULL COMMENT '账号类型：0真实用户 1游客；为空时表示不限制账号类型',
  `account_id` bigint NULL DEFAULT NULL COMMENT '账号ID；为空时表示不限制账号ID',
  `ip_address` varchar(70) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'ip地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_account_ip_white_list_type_ip` (`account_type`, `ip_address`) USING BTREE,
  INDEX `idx_account_ip_white_list_id_ip` (`account_id`, `ip_address`) USING BTREE,
  INDEX `idx_account_ip_white_list_account_ip` (`account_type`, `account_id`, `ip_address`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '账号ip白名单' ROW_FORMAT = DYNAMIC;
