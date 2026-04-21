package com.ruoyi.common.logDict;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户彩金出入记录对象 user_winnings_change_record
 *
 * @author ruoyi
 * @date 2024-04-04
 */
public class UserWinningsChangeRecordLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","彩金出入订单ID");
        this.put("orderCode","订单号");
        this.put("orderAmount","订单金额");
        this.put("orderType","订单类型");
        this.put("orderType","0","彩金赠送（系统充值）");
        this.put("orderType","1","彩金赠送（福利彩金）");
        this.put("orderType","2","彩金回收");
        this.put("orderType","3","充值彩金");
        this.put("orderType","4","注册彩金");
        this.put("operatorName","操作人名称");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("userAmountBefore","余额变更前");
        this.put("userAmountAfter","余额变更后");
    }
}
