package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 极速交易控制配置对象 fast_order_control_config
 * 
 * @author ruoyi
 * @date 2023-11-28
 */
public class FastOrderControlConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品类型类型（1：美股 2：加密货币 3：期货 4：外汇） */
    @Excel(name = "产品类型类型", readConverterExp = "1=：美股,2=：加密货币,3=：期货,4=：外汇")
    private Integer productType;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date finishTime;

    /** 交易方向 0：做多 1：做空 */
    @Excel(name = "交易方向 0：做多 1：做空")
    private Integer tradeDirect;

    /** 状态：0：启用 1：禁用 */
    @Excel(name = "状态：0：启用 1：禁用")
    private Integer status;

    /** 输赢 0：输  1：赢 2：平 3:未控 */
    @Excel(name = "输赢 0：输  1：赢 2：平 3:未控")
    private Integer winOrLose;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProductCode(String productCode) 
    {
        this.productCode = productCode;
    }

    public String getProductCode() 
    {
        return productCode;
    }
    public void setProductType(Integer productType) 
    {
        this.productType = productType;
    }

    public Integer getProductType() 
    {
        return productType;
    }
    public void setBeginTime(Date beginTime) 
    {
        this.beginTime = beginTime;
    }

    public Date getBeginTime() 
    {
        return beginTime;
    }
    public void setFinishTime(Date finishTime) 
    {
        this.finishTime = finishTime;
    }

    public Date getFinishTime() 
    {
        return finishTime;
    }
    public void setTradeDirect(Integer tradeDirect) 
    {
        this.tradeDirect = tradeDirect;
    }

    public Integer getTradeDirect() 
    {
        return tradeDirect;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setWinOrLose(Integer winOrLose) 
    {
        this.winOrLose = winOrLose;
    }

    public Integer getWinOrLose() 
    {
        return winOrLose;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productCode", getProductCode())
            .append("productType", getProductType())
            .append("beginTime", getBeginTime())
            .append("finishTime", getFinishTime())
            .append("tradeDirect", getTradeDirect())
            .append("status", getStatus())
            .append("winOrLose", getWinOrLose())
            .toString();
    }
}
