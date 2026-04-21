package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.CryptocurrencyProduct;
import com.ruoyi.system.domain.SelfSellProduct;
import com.ruoyi.system.domain.SelfSellProductDailyDataConfig;
import com.ruoyi.system.domain.StockProduct;
import com.ruoyi.system.mapper.CryptocurrencyProductMapper;
import com.ruoyi.system.mapper.SelfSellProductDailyDataConfigMapper;
import com.ruoyi.system.mapper.SelfSellProductMapper;
import com.ruoyi.system.mapper.StockProductMapper;
import com.ruoyi.system.service.ISelfSellProductDailyDataConfigService;
import com.ruoyi.system.service.ISelfSellProductRealTimeService;
import com.ruoyi.system.utils.ProductQuoteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 自营产品每日行情数据配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
@Service
public class SelfSellProductDailyDataConfigServiceImpl implements ISelfSellProductDailyDataConfigService 
{
    @Resource
    private SelfSellProductDailyDataConfigMapper selfSellProductDailyDataConfigMapper;

    @Resource
    private SelfSellProductMapper selfSellProductMapper;

    @Resource
    private StockProductMapper stockProductMapper;

    @Resource
    private CryptocurrencyProductMapper cryptocurrencyProductMapper;

    @Autowired
    private ISelfSellProductRealTimeService selfSellProductRealTimeService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询自营产品每日行情数据配置
     * 
     * @param id 自营产品每日行情数据配置主键
     * @return 自营产品每日行情数据配置
     */
    @Override
    public SelfSellProductDailyDataConfig selectSelfSellProductDailyDataConfigById(Long id)
    {
        return selfSellProductDailyDataConfigMapper.selectSelfSellProductDailyDataConfigById(id);
    }

    /**
     * 查询自营产品每日行情数据配置列表
     * 
     * @param selfSellProductDailyDataConfig 自营产品每日行情数据配置
     * @return 自营产品每日行情数据配置
     */
    @Override
    public List<SelfSellProductDailyDataConfig> selectSelfSellProductDailyDataConfigList(SelfSellProductDailyDataConfig selfSellProductDailyDataConfig)
    {
        return selfSellProductDailyDataConfigMapper.selectSelfSellProductDailyDataConfigList(selfSellProductDailyDataConfig);
    }

    /**
     * 新增自营产品每日行情数据配置
     * 
     * @param selfSellProductDailyDataConfig 自营产品每日行情数据配置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertSelfSellProductDailyDataConfig(SelfSellProductDailyDataConfig selfSellProductDailyDataConfig)
    {
        //自营产品信息
        SelfSellProduct selfSellProduct = selfSellProductMapper.selectSelfSellProductById(selfSellProductDailyDataConfig.getSelfSellProductId());
        if (selfSellProduct == null){
            return AjaxResult.error("获取自营产品信息异常");
        }
        //如果是新股新币
        if (selfSellProduct.getIsDirectListing().equals(1) && selfSellProduct.getRelateProductId().equals(0L)){
            throw new ServiceException("此产品还未上市");
        }
        selfSellProductDailyDataConfig.setProductCode(selfSellProduct.getProductCode());
        selfSellProductDailyDataConfig.setProductType(selfSellProduct.getProductType());
        //如果是默认选项
        if (selfSellProductDailyDataConfig.getIsDefault().equals(0)){
            //取消其他模板的默认
            selfSellProductDailyDataConfigMapper.cancelAllDefault(selfSellProductDailyDataConfig.getProductCode());
        }
        //如果选择了行情模板
        if (selfSellProductDailyDataConfig.getIsTemp().equals(0)){
            //模板产品代码
            String tempProductCode = selfSellProductDailyDataConfig.getTempProductCode();
            if (StringUtils.isEmpty(tempProductCode)){
                return AjaxResult.error("请选择模板产品");
            }
            //产品类型
            Integer productType = selfSellProductDailyDataConfig.getProductType();
            //股票
            if (productType.equals(1)){
                StockProduct product = stockProductMapper.selectStockProductByCode(tempProductCode);
                if (product == null){
                    return AjaxResult.error("获取模板股票信息异常");
                }
                selfSellProductDailyDataConfig.setTempProductCode(product.getProductCode());
            }else if (productType.equals(2)){
                CryptocurrencyProduct product = cryptocurrencyProductMapper.selectCryptocurrencyProductByCode(tempProductCode);
                if (product == null){
                    return AjaxResult.error("获取模板数字货币信息异常");
                }
                selfSellProductDailyDataConfig.setTempProductCode(product.getProductCode());
            }
        }
        selfSellProductDailyDataConfig.setCreateBy(SecurityUtils.getUsername());
        selfSellProductDailyDataConfig.setCreateTime(new Date());
        int count = selfSellProductDailyDataConfigMapper.insertSelfSellProductDailyDataConfig(selfSellProductDailyDataConfig);
        if (count <= 0){
            throw new RuntimeException("系统繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 修改自营产品每日行情数据配置
     * 
     * @param selfSellProductDailyDataConfig 自营产品每日行情数据配置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateSelfSellProductDailyDataConfig(SelfSellProductDailyDataConfig selfSellProductDailyDataConfig)
    {
        //旧自营产品每日行情数据配置
        SelfSellProductDailyDataConfig selfSellProductDailyDataConfigVo = selfSellProductDailyDataConfigMapper.selectSelfSellProductDailyDataConfigById(selfSellProductDailyDataConfig.getId());
        if (!selfSellProductDailyDataConfigVo.getSelfSellProductId().equals(selfSellProductDailyDataConfig.getSelfSellProductId())){
            return AjaxResult.error("不允许修改对应产品");
        }
        //如果变更为默认选项
        if (selfSellProductDailyDataConfig.getIsDefault().equals(0) && selfSellProductDailyDataConfigVo.getIsDefault().equals(1)){
            //取消其他模板的默认
            selfSellProductDailyDataConfigMapper.cancelAllDefault(selfSellProductDailyDataConfigVo.getProductCode());
        }
        //模板产品代码
        String tempProductCode = selfSellProductDailyDataConfig.getTempProductCode();
        //产品类型
        Integer productType = selfSellProductDailyDataConfigVo.getProductType();
        //如果模板发生了变更
        if (StringUtils.isNotEmpty(tempProductCode) && tempProductCode.equals(selfSellProductDailyDataConfigVo.getTempProductCode())){
            //股票
            if (productType.equals(1)){
                StockProduct product = stockProductMapper.selectStockProductByCode(tempProductCode);
                if (product == null){
                    return AjaxResult.error("获取模板股票信息异常");
                }
                selfSellProductDailyDataConfig.setTempProductCode(product.getProductCode());
            }else if (productType.equals(2)){
                CryptocurrencyProduct product = cryptocurrencyProductMapper.selectCryptocurrencyProductByCode(tempProductCode);
                if (product == null){
                    return AjaxResult.error("获取模板数字货币信息异常");
                }
                selfSellProductDailyDataConfig.setTempProductCode(product.getProductCode());
            }
        }
        selfSellProductDailyDataConfig.setUpdateBy(SecurityUtils.getUsername());
        selfSellProductDailyDataConfig.setUpdateTime(new Date());
        int count = selfSellProductDailyDataConfigMapper.updateSelfSellProductDailyDataConfig(selfSellProductDailyDataConfig);
        if (count <= 0){
            throw new RuntimeException("系统繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 批量删除自营产品每日行情数据配置
     * 
     * @param ids 需要删除的自营产品每日行情数据配置主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSelfSellProductDailyDataConfigByIds(Long[] ids)
    {
        SelfSellProductDailyDataConfig search = new SelfSellProductDailyDataConfig();
        search.getParams().put("ids", Arrays.asList(ids));
        List<SelfSellProductDailyDataConfig> selfSellProductDailyDataConfigs = selfSellProductDailyDataConfigMapper.selectSelfSellProductDailyDataConfigList(search);
        //日志记录自营产品每日行情数据配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:selfSellProductDailyDataConfigs", JSONObject.toJSONString(selfSellProductDailyDataConfigs));

        if (selfSellProductDailyDataConfigs.stream().filter(a->a.getIsDefault().equals(0)).count() > 0){
            throw new ServiceException("默认模板不允许删除");
        }
        return selfSellProductDailyDataConfigMapper.deleteSelfSellProductDailyDataConfigByIds(ids);
    }

    /**
     * 删除自营产品每日行情数据配置信息
     * 
     * @param id 自营产品每日行情数据配置主键
     * @return 结果
     */
    @Override
    public int deleteSelfSellProductDailyDataConfigById(Long id)
    {
        return selfSellProductDailyDataConfigMapper.deleteSelfSellProductDailyDataConfigById(id);
    }

    /**
     * 重新生成自营产品行情模板数据
     * @param configId 每日行情数据配置id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int regenerateRealtimeTempData(Long configId){
        //每日行情数据配置信息
        SelfSellProductDailyDataConfig config = selfSellProductDailyDataConfigMapper.selectSelfSellProductDailyDataConfigById(configId);
        if (config == null){
            throw new ServiceException("获取每日行情数据配置信息异常");
        }
        //日志记录配置信息
        HttpUtils.getRequestLogParams().put("config",JSONObject.toJSONString(config));
        if (!config.getIsDefault().equals(0)){
            throw new ServiceException("请先将该配置设置为默认配置");
        }
        //产品代码
        Integer productType = config.getProductType();
        //产品代码
        String productCode = config.getProductCode();
        //productCodes
        List<String> productCodes = new ArrayList<>();
        productCodes.add(productCode);
        //删除今日行情模板
        selfSellProductRealTimeService.cleanProductRealTimeData(productType,new Date(),productCodes);
        //行情map
        Map<String, TickerInfo> tickerInfoMap;
        if (productType.equals(1)){
            tickerInfoMap = ProductQuoteUtils.getStockQuote(productCode,false);
        }else if (productType.equals(2)){
            tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
        }else {
            throw new ServiceException("产品代码错误");
        }
        //现价
        BigDecimal nowPrice = BigDecimal.ZERO;
        //行情信息
        TickerInfo tickerInfo = tickerInfoMap.get(productCode);
        if (tickerInfo != null){
            nowPrice = new BigDecimal(tickerInfo.getNowPrice());
        }
        if (nowPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("获取行情异常");
        }
        //最终价格
        BigDecimal targetPrice;
        //缓存今日上市的产品
        String cacheKey = "listingProduct::"+productType+"::"+productCode+DateUtils.getDate();
        //上市当天最终价格
        BigDecimal price = redisCache.getCacheObject(cacheKey);
        if (price != null){
            targetPrice = price;
        }else {
            targetPrice = config.getFinallyPrice();
            //如果没有设置最终价格
            if (targetPrice.compareTo(BigDecimal.ZERO) == 0){
                //最终涨跌幅
                BigDecimal finallyChangeRate = config.getFinallyChangeRate();
                //昨日收盘价格（当前价格）
                BigDecimal closePricePrevDay = nowPrice;
                //最终价格以涨跌幅计算
                targetPrice = finallyChangeRate.multiply(new BigDecimal(0.01)).multiply(closePricePrevDay).add(closePricePrevDay).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            }
        }
        //生成行情
        selfSellProductRealTimeService.generateRealTimeData(productCode,productType,nowPrice,targetPrice,null);
        return 1;

    }
}
