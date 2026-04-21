package com.ruoyi.common.logDict;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 首页推荐产品对象 home_recommend_products
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
public class HomeRecommendProductsLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","首页推荐产品ID");
        this.put("productId","产品ID");
        this.put("productCode","产品代码");
        this.put("productType","产品类型");
        this.put("productType","1","股票");
        this.put("productType","2","加密货币");
        this.put("productType","3","期货");
        this.put("productType","4","外汇");
        this.put("isVisible","是否展示");
        this.put("isVisible","0","是");
        this.put("isVisible","1","否");
        this.put("sort","排序");
    }
}
