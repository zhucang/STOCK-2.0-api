package com.ruoyi.system.service;


import com.ruoyi.system.domain.SelfSellProductRealTime;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 自营产品分时图数据Service接口
 * 
 * @author ruoyi
 * @date 2024-08-28
 */
public interface ISelfSellProductRealTimeService
{
    /**
     * 查询自营产品分时图数据
     * 
     * @param id 自营产品分时图数据主键
     * @return 自营产品分时图数据
     */
    public SelfSellProductRealTime selectSelfSellProductRealTimeById(Long id);

    /**
     * 查询自营产品分时图数据列表
     * 
     * @param selfSellProductRealTime 自营产品分时图数据
     * @return 自营产品分时图数据集合
     */
    public List<SelfSellProductRealTime> selectSelfSellProductRealTimeList(SelfSellProductRealTime selfSellProductRealTime);

    /**
     * 查询自营产品K线数据
     *
     * @param selfSellProductRealTime 自营产品分时图数据
     * @return 自营产品分时图数据集合
     */
    public List<SelfSellProductRealTime> selectKLine(SelfSellProductRealTime selfSellProductRealTime);

    /**
     * 新增自营产品分时图数据
     * 
     * @param selfSellProductRealTime 自营产品分时图数据
     * @return 结果
     */
    public int insertSelfSellProductRealTime(SelfSellProductRealTime selfSellProductRealTime);

    /**
     * 修改自营产品分时图数据
     * 
     * @param selfSellProductRealTime 自营产品分时图数据
     * @return 结果
     */
    public int updateSelfSellProductRealTime(SelfSellProductRealTime selfSellProductRealTime);

    /**
     * 批量删除自营产品分时图数据
     * 
     * @param ids 需要删除的自营产品分时图数据主键集合
     * @return 结果
     */
    public int deleteSelfSellProductRealTimeByIds(Long[] ids);

    /**
     * 删除自营产品分时图数据信息
     * 
     * @param id 自营产品分时图数据主键
     * @return 结果
     */
    public int deleteSelfSellProductRealTimeById(Long id);

    /**
     * 生成所有自营产品分时图数据
     * @param productType 产品类型 1：股票 2：加密货币
     */
    public void generateAllProductRealTimeData(Integer productType);

    /**
     * 生成自营产品分时图数据
     * @return
     */
    public void generateRealTimeData(String productCode,Integer productType, BigDecimal startPrice, BigDecimal targetPrice,List<Date> timeTemp);

    /**
     * 获取时间模板
     * @param productType 产品类型
     * @return
     */
    public List<Date> getTimeTemp(Integer productType);

    /**
     * 清空相应的分数图数据
     * @param productType 产品类型 1：股票 2：加密货币
     * @param specificTime 对应时间
     * @param productCodes 产品代码
     * @return
     */
    public int cleanProductRealTimeData(Integer productType, Date specificTime, List<String> productCodes);
}
