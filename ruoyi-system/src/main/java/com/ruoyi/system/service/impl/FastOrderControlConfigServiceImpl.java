package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.FastOrderControlConfig;
import com.ruoyi.system.mapper.FastOrderControlConfigMapper;
import com.ruoyi.system.service.IFastOrderControlConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 极速交易控制配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-25
 */
@Service
public class FastOrderControlConfigServiceImpl implements IFastOrderControlConfigService 
{
    @Resource
    private FastOrderControlConfigMapper fastOrderControlConfigMapper;

    /**
     * 查询极速交易控制配置
     * 
     * @param id 极速交易控制配置主键
     * @return 极速交易控制配置
     */
    @Override
    public FastOrderControlConfig selectFastOrderControlConfigById(Long id)
    {
        return fastOrderControlConfigMapper.selectFastOrderControlConfigById(id);
    }

    /**
     * 查询极速交易控制配置列表
     * 
     * @param fastOrderControlConfig 极速交易控制配置
     * @return 极速交易控制配置
     */
    @Override
    public List<FastOrderControlConfig> selectFastOrderControlConfigList(FastOrderControlConfig fastOrderControlConfig)
    {
        return fastOrderControlConfigMapper.selectFastOrderControlConfigList(fastOrderControlConfig);
    }

    /**
     * 新增极速交易控制配置
     * 
     * @param fastOrderControlConfig 极速交易控制配置
     * @return 结果
     */
    @Override
    public int insertFastOrderControlConfig(FastOrderControlConfig fastOrderControlConfig)
    {
        return fastOrderControlConfigMapper.insertFastOrderControlConfig(fastOrderControlConfig);
    }

    /**
     * 修改极速交易控制配置
     * 
     * @param fastOrderControlConfig 极速交易控制配置
     * @return 结果
     */
    @Override
    public int updateFastOrderControlConfig(FastOrderControlConfig fastOrderControlConfig)
    {
        return fastOrderControlConfigMapper.updateFastOrderControlConfig(fastOrderControlConfig);
    }

    /**
     * 批量删除极速交易控制配置
     * 
     * @param ids 需要删除的极速交易控制配置主键
     * @return 结果
     */
    @Override
    public int deleteFastOrderControlConfigByIds(Long[] ids)
    {
        FastOrderControlConfig search = new FastOrderControlConfig();
        search.getParams().put("ids", Arrays.asList(ids));
        List<FastOrderControlConfig> fastOrderControlConfigs = fastOrderControlConfigMapper.selectFastOrderControlConfigList(search);
        //日志记录极速交易控制配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:fastOrderControlConfigs", JSONObject.toJSONString(fastOrderControlConfigs));
        return fastOrderControlConfigMapper.deleteFastOrderControlConfigByIds(ids);
    }

    /**
     * 删除极速交易控制配置信息
     * 
     * @param id 极速交易控制配置主键
     * @return 结果
     */
    @Override
    public int deleteFastOrderControlConfigById(Long id)
    {
        return fastOrderControlConfigMapper.deleteFastOrderControlConfigById(id);
    }
}
