package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Realtime;
import com.ruoyi.system.domain.SelfSellProductDailyDataConfig;

import java.util.List;

/**
 * 自营产品每日行情数据配置Service接口
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public interface ISelfSellProductDailyDataConfigService 
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
    public AjaxResult insertSelfSellProductDailyDataConfig(SelfSellProductDailyDataConfig selfSellProductDailyDataConfig);

    /**
     * 修改自营产品每日行情数据配置
     * 
     * @param selfSellProductDailyDataConfig 自营产品每日行情数据配置
     * @return 结果
     */
    public AjaxResult updateSelfSellProductDailyDataConfig(SelfSellProductDailyDataConfig selfSellProductDailyDataConfig);

    /**
     * 批量删除自营产品每日行情数据配置
     * 
     * @param ids 需要删除的自营产品每日行情数据配置主键集合
     * @return 结果
     */
    public int deleteSelfSellProductDailyDataConfigByIds(Long[] ids);

    /**
     * 删除自营产品每日行情数据配置信息
     * 
     * @param id 自营产品每日行情数据配置主键
     * @return 结果
     */
    public int deleteSelfSellProductDailyDataConfigById(Long id);

    /**
     * 重新生成自营产品行情模板数据
     * @param configId 每日行情数据配置id
     * @return
     */
    public int regenerateRealtimeTempData(Long configId);
}
