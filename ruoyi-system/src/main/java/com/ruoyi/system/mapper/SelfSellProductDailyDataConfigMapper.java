package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SelfSellProductDailyDataConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自营产品每日行情数据配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public interface SelfSellProductDailyDataConfigMapper 
{
    /**
     * 查询自营产品每日行情数据配置
     * 
     * @param id 自营产品每日行情数据配置主键
     * @return 自营产品每日行情数据配置
     */
    public SelfSellProductDailyDataConfig selectSelfSellProductDailyDataConfigById(Long id);

    /**
     * 查询自营产品每日行情数据配置列表
     * 
     * @param selfSellProductDailyDataConfig 自营产品每日行情数据配置
     * @return 自营产品每日行情数据配置集合
     */
    public List<SelfSellProductDailyDataConfig> selectSelfSellProductDailyDataConfigList(SelfSellProductDailyDataConfig selfSellProductDailyDataConfig);

    /**
     * 新增自营产品每日行情数据配置
     * 
     * @param selfSellProductDailyDataConfig 自营产品每日行情数据配置
     * @return 结果
     */
    public int insertSelfSellProductDailyDataConfig(SelfSellProductDailyDataConfig selfSellProductDailyDataConfig);

    /**
     * 修改自营产品每日行情数据配置
     * 
     * @param selfSellProductDailyDataConfig 自营产品每日行情数据配置
     * @return 结果
     */
    public int updateSelfSellProductDailyDataConfig(SelfSellProductDailyDataConfig selfSellProductDailyDataConfig);

    /**
     * 删除自营产品每日行情数据配置
     * 
     * @param id 自营产品每日行情数据配置主键
     * @return 结果
     */
    public int deleteSelfSellProductDailyDataConfigById(Long id);

    /**
     * 批量删除自营产品每日行情数据配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSelfSellProductDailyDataConfigByIds(Long[] ids);

    /**
     * 取消所有默认
     * @param productCode 产品代码
     * @return
     */
    public int cancelAllDefault(@Param("productCode") String productCode);

    /**
     * 批量删除自营产品每日行情数据配置
     * @param selfSellProductIds 自营产品信息ids
     * @return
     */
    public int deleteSelfSellProductDailyDataConfigBySelfSellProductIds(@Param("selfSellProductIds") List<Long> selfSellProductIds);
}
