package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.CurrencyExchangeRate;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.mapper.CurrencyExchangeRateMapper;
import com.ruoyi.system.service.ICurrencyExchangeRateService;
import com.ruoyi.system.service.IPlatformCurrencyService;
import com.ruoyi.system.utils.currencyExchangeRate.ExchangeRateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 货币兑换汇率Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-25
 */
@Service
public class CurrencyExchangeRateServiceImpl implements ICurrencyExchangeRateService 
{
    @Resource
    private CurrencyExchangeRateMapper currencyExchangeRateMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    /**
     * 查询货币兑换汇率
     * 
     * @param id 货币兑换汇率主键
     * @return 货币兑换汇率
     */
    @Override
    public CurrencyExchangeRate selectCurrencyExchangeRateById(Long id)
    {
        return currencyExchangeRateMapper.selectCurrencyExchangeRateById(id);
    }

    /**
     * 查询货币兑换汇率列表
     * 
     * @param currencyExchangeRate 货币兑换汇率
     * @return 货币兑换汇率
     */
    @Override
    public List<CurrencyExchangeRate> selectCurrencyExchangeRateList(CurrencyExchangeRate currencyExchangeRate)
    {
        return currencyExchangeRateMapper.selectCurrencyExchangeRateList(currencyExchangeRate);
    }

    /**
     * 新增货币兑换汇率
     * 
     * @param currencyExchangeRate 货币兑换汇率
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCurrencyExchangeRate(CurrencyExchangeRate currencyExchangeRate)
    {
        //转化者币种id
        Long currencyIdFrom = currencyExchangeRate.getCurrencyIdFrom();
        //转化者币种信息
        PlatformCurrency platformCurrencyFrom = platformCurrencyService.selectPlatformCurrencyById(currencyIdFrom);
        if (platformCurrencyFrom == null){
            throw new ServiceException("获取转化者币种信息异常");
        }
        //日志记录转化者币种名称
        HttpUtils.getRequestLogParams().put("currencyNameFrom",platformCurrencyFrom.getCurrencyName());
        //被转化者币种id
        Long currencyIdTo = currencyExchangeRate.getCurrencyIdTo();
        //被转化者币种信息
        PlatformCurrency platformCurrencyTo = platformCurrencyService.selectPlatformCurrencyById(currencyIdTo);
        if (platformCurrencyTo == null){
            throw new ServiceException("获取被转化者币种信息异常");
        }
        //日志记录被转化者币种名称
        HttpUtils.getRequestLogParams().put("currencyNameTo",platformCurrencyTo.getCurrencyName());
        //获取两币种的兑换汇率配置
        CurrencyExchangeRate currencyExchangeRateVo = new CurrencyExchangeRate();
        currencyExchangeRateVo.setCurrencyIdFrom(currencyExchangeRate.getCurrencyIdFrom());
        currencyExchangeRateVo.setCurrencyIdTo(currencyExchangeRate.getCurrencyIdTo());
        currencyExchangeRateVo = currencyExchangeRateMapper.selectCurrencyExchangeRate(currencyExchangeRateVo);
        //如果该兑换汇率配置已经存在
        if (currencyExchangeRateVo != null){
            throw new ServiceException("兑换汇率配置已经存在");
        }
        //反汇率
        currencyExchangeRate.setOppositeExchangeRate(new BigDecimal(1).divide(currencyExchangeRate.getExchangeRate(),10,1));
        currencyExchangeRate.setCreateBy(SecurityUtils.getUsername());
        currencyExchangeRate.setCreateTime(new Date());
        //新增货币兑换汇率配置
        int count = currencyExchangeRateMapper.insertCurrencyExchangeRate(currencyExchangeRate);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        reloadLangCurrencyExchangeRates();
        return 1;
    }

    /**
     * 修改货币兑换汇率
     * 
     * @param currencyExchangeRate 货币兑换汇率
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCurrencyExchangeRate(CurrencyExchangeRate currencyExchangeRate)
    {
        //转化者币种id
        Long currencyIdFrom = currencyExchangeRate.getCurrencyIdFrom();
        //转化者币种信息
        PlatformCurrency platformCurrencyFrom = platformCurrencyService.selectPlatformCurrencyById(currencyIdFrom);
        if (platformCurrencyFrom == null){
            throw new ServiceException("获取转化者币种信息异常");
        }
        //日志记录转化者币种名称
        HttpUtils.getRequestLogParams().put("currencyNameFrom",platformCurrencyFrom.getCurrencyName());
        //被转化者币种id
        Long currencyIdTo = currencyExchangeRate.getCurrencyIdTo();
        //被转化者币种信息
        PlatformCurrency platformCurrencyTo = platformCurrencyService.selectPlatformCurrencyById(currencyIdTo);
        if (platformCurrencyTo == null){
            throw new ServiceException("获取被转化者币种信息异常");
        }
        //日志记录被转化者币种名称
        HttpUtils.getRequestLogParams().put("currencyNameTo",platformCurrencyTo.getCurrencyName());
        //获取两币种的兑换汇率配置
        CurrencyExchangeRate currencyExchangeRateVo = new CurrencyExchangeRate();
        currencyExchangeRateVo.setCurrencyIdFrom(currencyExchangeRate.getCurrencyIdFrom());
        currencyExchangeRateVo.setCurrencyIdTo(currencyExchangeRate.getCurrencyIdTo());
        currencyExchangeRateVo = currencyExchangeRateMapper.selectCurrencyExchangeRate(currencyExchangeRateVo);
        //如果该兑换汇率配置已经存在
        if (currencyExchangeRateVo != null && !currencyExchangeRateVo.getId().equals(currencyExchangeRateVo.getId())){
            throw new ServiceException("兑换汇率配置已经存在");
        }
        //反汇率
        currencyExchangeRate.setOppositeExchangeRate(new BigDecimal(1).divide(currencyExchangeRate.getExchangeRate(),10,1));
        currencyExchangeRate.setUpdateTime(DateUtils.getNowDate());
        currencyExchangeRate.setUpdateBy(SecurityUtils.getUsername());
        //修改货币兑换汇率配置
        int count = currencyExchangeRateMapper.updateCurrencyExchangeRate(currencyExchangeRate);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        reloadLangCurrencyExchangeRates();
        return 1;
    }

    /**
     * 批量删除货币兑换汇率
     * 
     * @param ids 需要删除的货币兑换汇率主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCurrencyExchangeRateByIds(Long[] ids)
    {
        CurrencyExchangeRate search = new CurrencyExchangeRate();
        search.getParams().put("ids", Arrays.asList(ids));
        List<CurrencyExchangeRate> currencyExchangeRates = currencyExchangeRateMapper.selectCurrencyExchangeRateList(search);
        //日志记录货币兑换汇率配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:currencyExchangeRates", JSONObject.toJSONString(currencyExchangeRates));
        int count = currencyExchangeRateMapper.deleteCurrencyExchangeRateByIds(ids);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        reloadLangCurrencyExchangeRates();
        return 1;
    }

    /**
     * 重载缓存
     */
    @Override
    public void reloadLangCurrencyExchangeRates() {
        List<CurrencyExchangeRate> currencyExchangeRates = currencyExchangeRateMapper.selectCurrencyExchangeRateList(new CurrencyExchangeRate());
        Map<String, CurrencyExchangeRate> map1 = currencyExchangeRates.stream().collect(Collectors.toMap(a -> a.getCurrencyIdFrom() + "/" + a.getCurrencyIdTo(), a -> a));
        Map<String, CurrencyExchangeRate> map2 = currencyExchangeRates.stream().collect(Collectors.toMap(a -> a.getCurrencyIdTo() + "/" + a.getCurrencyIdFrom(),a->{
            CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate();
            BeanUtils.copyProperties(a,currencyExchangeRate);
            currencyExchangeRate.setCurrencyIdFrom(a.getCurrencyIdTo());
            currencyExchangeRate.setCurrencyIdTo(a.getCurrencyIdFrom());
            currencyExchangeRate.setExchangeRate(a.getOppositeExchangeRate());
            currencyExchangeRate.setOppositeExchangeRate(a.getExchangeRate());
            return currencyExchangeRate;
        }));
        map1.putAll(map2);
        //清空之前的缓存
        String mapKey = "currencyExchangeRate:";
        redisCache.deleteObject(mapKey);
        //新缓存
        redisCache.setCacheMap(mapKey,map1);
    }

    /**
     * 获取汇率详情 （汇率和手续费）
     * @param currencyIdFrom 转出币种id
     * @param currencyIdTo 转入币种id
     * @return
     */
    @Override
    public Map<String,BigDecimal> getExchangeInfo(Long currencyIdFrom, Long currencyIdTo){
        //如果相同
        if (currencyIdFrom.equals(currencyIdTo)){
            Map<String, BigDecimal> result = new HashMap<>();
            result.put("feeRatio",BigDecimal.ZERO);
            result.put("exchangeRate",BigDecimal.ONE);
            return result;
        }
        //转出币种信息
        PlatformCurrency platformCurrencyFrom = platformCurrencyService.selectPlatformCurrencyById(currencyIdFrom);
        if (platformCurrencyFrom == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取转出币种信息异常");
        }
        //转入币种信息
        PlatformCurrency platformCurrencyTo = platformCurrencyService.selectPlatformCurrencyById(currencyIdTo);
        if (platformCurrencyTo == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取转入币种信息异常");
        }
        //币种信息列表
        List<PlatformCurrency> platformCurrencies = new ArrayList<>();
        platformCurrencies.add(platformCurrencyFrom);
        platformCurrencies.add(platformCurrencyTo);
        //填充实时汇率
        ExchangeRateUtil.fillExchangeRate(platformCurrencies);
        return ExchangeRateUtil.getExchangeInfo(currencyIdFrom, currencyIdTo, platformCurrencies.stream().collect(Collectors.toMap(a->a.getId(),a->a)));
    }
}
