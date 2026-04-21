insert into `stock`.`switch_set` (id,switch_name,status,`type`,sort) select 112,'app登录页是否开启客服功能',0,1,112 from switch_set where not exists (select id from switch_set where id = 112)limit 1;

ALTER TABLE `stock`.`self_sell_product`
    ADD COLUMN `relate_product_sort` int NULL COMMENT '关联产品列表的排序' AFTER `relate_product_id`,
ADD COLUMN `home_recommend_product_flag` tinyint(1) NULL DEFAULT 0 COMMENT '首页推荐标志 0：不添加首页推荐 1：添加首页推荐' AFTER `relate_product_sort`,
ADD COLUMN `home_recommend_product_sort` int UNSIGNED NULL COMMENT '首页推荐排序' AFTER `home_recommend_product_flag`;

ALTER TABLE `stock`.`app_config`
    MODIFY COLUMN `obj_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示文本及输入用' AFTER `call_fun`;