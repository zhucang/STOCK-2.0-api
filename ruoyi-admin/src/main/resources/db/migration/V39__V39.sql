ALTER TABLE `stock`.`text_lang`
    MODIFY COLUMN `content_type` int NULL DEFAULT NULL COMMENT '类型：0：App实名认证页面规格 1：App银行卡页面规则 2：App提现页面规格 3:App按天配资规则 4:App挖矿收益计算 5:App挖矿关于违约金 6:公司简介 7:开户须知 8:操盘须知 9:投资风险 10：产品细节 11:隐私协议 12：注册协议 13：推广规则 14：福利中心 15：借贷规则' AFTER `remark`;

insert into text_lang (content, remark, content_type, website_class)
select '借贷规则','借贷规则',15,null from text_lang where not exists (select content_type from text_lang where content_type = 15)limit 1;