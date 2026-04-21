package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.BonusConfig;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.mapper.BonusConfigMapper;
import com.ruoyi.system.service.IBonusConfigService;
import com.ruoyi.system.service.IPlatformCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 赠送彩金配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-29
 */
@Service
public class BonusConfigServiceImpl implements IBonusConfigService 
{
    @Resource
    private BonusConfigMapper bonusConfigMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    /**
     * 查询赠送彩金配置
     * 
     * @param id 赠送彩金配置主键
     * @return 赠送彩金配置
     */
    @Override
    public BonusConfig selectBonusConfigById(Long id)
    {
        return bonusConfigMapper.selectBonusConfigById(id);
    }

    /**
     * 查询赠送彩金配置列表
     * 
     * @param bonusConfig 赠送彩金配置
     * @return 赠送彩金配置
     */
    @Override
    public List<BonusConfig> selectBonusConfigList(BonusConfig bonusConfig)
    {
        return bonusConfigMapper.selectBonusConfigList(bonusConfig);
    }

    /**
     * 新增赠送彩金配置
     * 
     * @param bonusConfig 赠送彩金配置
     * @return 结果
     */
    @Override
    public int insertBonusConfig(BonusConfig bonusConfig)
    {
        //币种id
        Long currencyId = bonusConfig.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        return bonusConfigMapper.insertBonusConfig(bonusConfig);
    }

    /**
     * 修改赠送彩金配置
     * 
     * @param bonusConfig 赠送彩金配置
     * @return 结果
     */
    @Override
    public int updateBonusConfig(BonusConfig bonusConfig)
    {
        //币种id
        Long currencyId = bonusConfig.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        return bonusConfigMapper.updateBonusConfig(bonusConfig);
    }

    /**
     * 批量删除赠送彩金配置
     * 
     * @param ids 需要删除的赠送彩金配置主键
     * @return 结果
     */
    @Override
    public int deleteBonusConfigByIds(Long[] ids)
    {
        BonusConfig search = new BonusConfig();
        search.getParams().put("ids", Arrays.asList(ids));
        List<BonusConfig> bonusConfigs = bonusConfigMapper.selectBonusConfigList(search);
        //日志记录赠送彩金配置信息列表
        HttpUtils.getRequestLogParams().put("JSONArray:bonusConfigs", JSONObject.toJSONString(bonusConfigs));
        return bonusConfigMapper.deleteBonusConfigByIds(ids);
    }

    /**
     * 删除赠送彩金配置信息
     * 
     * @param id 赠送彩金配置主键
     * @return 结果
     */
    @Override
    public int deleteBonusConfigById(Long id)
    {
        return bonusConfigMapper.deleteBonusConfigById(id);
    }
}
