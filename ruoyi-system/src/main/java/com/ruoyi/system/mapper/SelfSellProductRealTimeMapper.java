package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SelfSellProductRealTime;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 自营产品分时图数据Mapper接口
 * 
 * @author ruoyi
 * @date 2024-08-28
 */
public interface SelfSellProductRealTimeMapper
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
     * 新增自营产品分时图数据
     *
     * @param selfSellProductRealTimes 自营产品分时图数据
     * @return 结果
     */
    public int insertSelfSellProductRealTimes(@Param("selfSellProductRealTimes") List<SelfSellProductRealTime> selfSellProductRealTimes);

    /**
     * 修改自营产品分时图数据
     * 
     * @param selfSellProductRealTime 自营产品分时图数据
     * @return 结果
     */
    public int updateSelfSellProductRealTime(SelfSellProductRealTime selfSellProductRealTime);

    /**
     * 删除自营产品分时图数据
     * 
     * @param id 自营产品分时图数据主键
     * @return 结果
     */
    public int deleteSelfSellProductRealTimeById(Long id);

    /**
     * 批量删除自营产品分时图数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSelfSellProductRealTimeByIds(Long[] ids);

    /**
     * 获取时间模板
     * @param productType 产品类型
     * @return
     */
    public List<String> getTimeTemp(Integer productType);

    /**
     * 根据产品代码清空相应分时图数据
     * @param productType 产品类型
     * @param productCodes 产品代码列表
     * @return
     */
    int cleanProductRealTimeData(@Param("productType") Integer productType, @Param("specificTime") Date specificTime, @Param("productCodes") List<String> productCodes);

    /**
     * 根据产品代码获取交易详情
     * @param productCode 产品代码
     * @param productType 产品类型 1：股票 2：加密货币
     * @param date 时间
     * @param isToday 是否今日 0:是 1：否
     * @return
     */
    @MapKey("productCode")
    Map<String, Map> selectRealTimeTradeDetail(@Param("productCode") String productCode,
                                                    @Param("productType") Integer productType,
                                                    @Param("date") Date date,
                                                    @Param("isToday") Integer isToday);

    /**
     * 根据产品代码获取交易详情
     * @param productCode 产品代码
     * @param productType 产品类型 1：股票 2：加密货币
     * @param date 时间
     * @return
     */
    @MapKey("productCode")
    Map<String, Map> selectRealTimeTradeFinalDetail(@Param("productCode") String productCode,
                                               @Param("productType") Integer productType,
                                               @Param("date") Date date);
}
