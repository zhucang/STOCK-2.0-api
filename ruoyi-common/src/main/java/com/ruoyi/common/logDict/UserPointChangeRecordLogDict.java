package com.ruoyi.common.logDict;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户上分下分记录对象 user_point_change_record
 * 
 * @author ruoyi
 * @date 2024-03-30
 */
public class UserPointChangeRecordLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","上下分订单ID");
        this.put("orderCode","订单号");
        this.put("orderAmount","订单金额");
        this.put("orderType","订单类型");
        this.put("orderType","0","上分");
        this.put("orderType","1","下分");
        this.put("operatorName","操作人名称");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("userAmountBefore","余额变更前");
        this.put("userAmountAfter","余额变更后");
    }
}
