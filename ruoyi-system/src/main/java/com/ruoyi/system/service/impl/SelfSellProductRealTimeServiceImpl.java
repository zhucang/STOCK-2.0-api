package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.system.domain.SelfSellProductDailyDataConfig;
import com.ruoyi.system.domain.SelfSellProductRealTime;
import com.ruoyi.system.mapper.SelfSellProductDailyDataConfigMapper;
import com.ruoyi.system.mapper.SelfSellProductRealTimeMapper;
import com.ruoyi.system.service.ISelfSellProductRealTimeService;
import com.ruoyi.system.utils.ProductQuoteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 自营产品分时图数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-08-28
 */
@Service
public class SelfSellProductRealTimeServiceImpl implements ISelfSellProductRealTimeService
{

    private static final Logger log = LoggerFactory.getLogger(SelfSellProductRealTimeServiceImpl.class);

    @Resource
    private SelfSellProductRealTimeMapper selfSellProductRealTimeMapper;

    @Resource
    private SelfSellProductDailyDataConfigMapper selfSellProductDailyDataConfigMapper;

    @Autowired
    private ISelfSellProductRealTimeService selfSellProductRealTimeService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询自营产品分时图数据
     * 
     * @param id 自营产品分时图数据主键
     * @return 自营产品分时图数据
     */
    @Override
    public SelfSellProductRealTime selectSelfSellProductRealTimeById(Long id)
    {
        return selfSellProductRealTimeMapper.selectSelfSellProductRealTimeById(id);
    }

    /**
     * 查询自营产品分时图数据列表
     * 
     * @param selfSellProductRealTime 自营产品分时图数据
     * @return 自营产品分时图数据
     */
    @Override
    public List<SelfSellProductRealTime> selectSelfSellProductRealTimeList(SelfSellProductRealTime selfSellProductRealTime)
    {
        return selfSellProductRealTimeMapper.selectSelfSellProductRealTimeList(selfSellProductRealTime);
    }

    /**
     * 查询自营产品K线数据
     *
     * @param selfSellProductRealTime 自营产品分时图数据
     * @return 自营产品分时图数据集合
     */
    @Override
    public List<SelfSellProductRealTime> selectKLine(SelfSellProductRealTime selfSellProductRealTime){
        return selfSellProductRealTimeMapper.selectKLine(selfSellProductRealTime);
    }

    /**
     * 新增自营产品分时图数据
     * 
     * @param selfSellProductRealTime 自营产品分时图数据
     * @return 结果
     */
    @Override
    public int insertSelfSellProductRealTime(SelfSellProductRealTime selfSellProductRealTime)
    {
        selfSellProductRealTime.setCreateTime(DateUtils.getNowDate());
        return selfSellProductRealTimeMapper.insertSelfSellProductRealTime(selfSellProductRealTime);
    }

    /**
     * 修改自营产品分时图数据
     * 
     * @param selfSellProductRealTime 自营产品分时图数据
     * @return 结果
     */
    @Override
    public int updateSelfSellProductRealTime(SelfSellProductRealTime selfSellProductRealTime)
    {
        return selfSellProductRealTimeMapper.updateSelfSellProductRealTime(selfSellProductRealTime);
    }

    /**
     * 批量删除自营产品分时图数据
     * 
     * @param ids 需要删除的自营产品分时图数据主键
     * @return 结果
     */
    @Override
    public int deleteSelfSellProductRealTimeByIds(Long[] ids)
    {
        return selfSellProductRealTimeMapper.deleteSelfSellProductRealTimeByIds(ids);
    }

    /**
     * 删除自营产品分时图数据信息
     * 
     * @param id 自营产品分时图数据主键
     * @return 结果
     */
    @Override
    public int deleteSelfSellProductRealTimeById(Long id)
    {
        return selfSellProductRealTimeMapper.deleteSelfSellProductRealTimeById(id);
    }

    /**
     * 生成所有自营产品分时图数据
     * @return
     */
    @Override
    public void generateAllProductRealTimeData(Integer productType){
        //获取需要生成行情数据的自营产品配置列表
        SelfSellProductDailyDataConfig selfSellProductDailyDataConfig = new SelfSellProductDailyDataConfig();
        selfSellProductDailyDataConfig.setIsDefault(0);
        selfSellProductDailyDataConfig.setProductType(productType);
        List<SelfSellProductDailyDataConfig> configs = selfSellProductDailyDataConfigMapper.selectSelfSellProductDailyDataConfigList(selfSellProductDailyDataConfig);
        //如果没有需要生成行情数据的配置
        if (configs.size() == 0){
            return ;
        }
        //productCodes
        String productCodes = configs.stream().map(SelfSellProductDailyDataConfig::getProductCode).collect(Collectors.joining(","));
        //行情map
        Map<String, TickerInfo> tickerInfoMap;
        if (productType.equals(1)){
            tickerInfoMap = ProductQuoteUtils.getStockQuote(productCodes,false);
        }else if (productType.equals(2)){
            tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
        }else {
            throw new ServiceException("产品代码错误");
        }
        //行情时间模板
        List<Date> timeTemp = selfSellProductRealTimeService.getTimeTemp(productType);
        ExecutorService executorService = Executors.newFixedThreadPool(configs.size());
        try {
            for (int i = 0; i < configs.size(); i++) {
                SelfSellProductDailyDataConfig config = configs.get(i);
                executorService.execute(()->{
                    try{
                        //产品代码
                        String productCode = config.getProductCode();
                        //验证是否今日上市的产品
                        String cacheKey = "listingProduct::"+productType+"::"+productCode+DateUtils.getDate();
                        //如果是今日上市，无需生成行情
                        if (redisCache.getCacheObject(cacheKey) != null){
                            return;
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
                        BigDecimal targetPrice = config.getFinallyPrice();
                        //如果没有设置最终价格
                        if (targetPrice.compareTo(BigDecimal.ZERO) == 0){
                            //最终涨跌幅
                            BigDecimal finallyChangeRate = config.getFinallyChangeRate();
                            //昨日收盘价格（当前价格）
                            BigDecimal closePricePrevDay = nowPrice;
                            //最终价格以涨跌幅计算
                            targetPrice = finallyChangeRate.multiply(new BigDecimal(0.01)).multiply(closePricePrevDay).add(closePricePrevDay).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                        }
                        selfSellProductRealTimeService.generateRealTimeData(productCode,productType,nowPrice,targetPrice,timeTemp);
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                });
            }
        }catch (Exception e){
            throw new RuntimeException("创建定时任务异常，异常原因"+e.getStackTrace());
        }finally {
            executorService.shutdown();
        }
    }

    /**
     * 生成自营产品分时图数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateRealTimeData(String productCode,Integer productType,BigDecimal startPrice,BigDecimal targetPrice,List<Date> timeTemp){
        //行情时间模板
        if (timeTemp == null){
            timeTemp = selfSellProductRealTimeService.getTimeTemp(productType);
        }
        //小数点数量
        int scale = 6;
        //分钟数量
        int timeNum = timeTemp.size();
        //random
        Random random = new Random();
        //涨的次数（随机有三分之一到三分之二的时间涨）
        int upNum = random.nextInt(timeNum/3)+timeNum/3;
        //跌的次数(其余的时间跌)
        int downNum = timeNum - upNum;
        //差价
        BigDecimal differencePrice = targetPrice.subtract(startPrice).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
        //取百分之一为基础波动值
        BigDecimal per = startPrice.divide(new BigDecimal(100), scale, Constants.BIGDECIMAL_ROUNDINGMODE).abs();
        //如果波动值没到1%，取1%
        if (differencePrice.abs().compareTo(per) > 0){
            per = differencePrice.abs();
        }
        //涨总额
        BigDecimal allUpPrice = null;
        //跌总额
        BigDecimal allDownPrice = null;
        //如果是涨
        if (differencePrice.compareTo(BigDecimal.ZERO) > 0){
            //涨总额大于跌总额
            allUpPrice = RandomUtil.randomBigDecimal(per.multiply(new BigDecimal(5)), per.multiply(new BigDecimal(10))).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
            allDownPrice = allUpPrice.subtract(differencePrice);
        }else if (differencePrice.compareTo(BigDecimal.ZERO) < 0){
            //如果是跌
            differencePrice = differencePrice.negate();
            //跌总额大于涨总额
            allDownPrice = RandomUtil.randomBigDecimal(per.multiply(new BigDecimal(5)), per.multiply(new BigDecimal(10))).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
            allUpPrice = allDownPrice.subtract(differencePrice);
        }else {
            //如果持平
            differencePrice = per;
            allUpPrice = RandomUtil.randomBigDecimal(per.multiply(new BigDecimal(5)), per.multiply(new BigDecimal(10))).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
            allDownPrice = allUpPrice;
        }

        //涨均价
        BigDecimal upAverage = allUpPrice.divide(new BigDecimal(upNum),scale, Constants.BIGDECIMAL_ROUNDINGMODE);
        //跌均价
        BigDecimal downAverage = allDownPrice.divide(new BigDecimal(downNum),scale, Constants.BIGDECIMAL_ROUNDINGMODE);
        //涨均价波动
        BigDecimal upAverageFluctuation = upAverage.divide(new BigDecimal(1000),scale, Constants.BIGDECIMAL_ROUNDINGMODE);
        //跌均价波动
        BigDecimal downAverageFluctuation = downAverage.divide(new BigDecimal(1000),scale, Constants.BIGDECIMAL_ROUNDINGMODE);
        //打散涨跌顺序
        //总时间列表
        List<Date> allTime = new ArrayList<>();
        allTime.addAll(timeTemp);
        //涨的时间map
        Map<Date, Object> upTimeMap = new HashMap<>();
        //遍历
        for (int i = 0; i < upNum; i++) {
            //随机取数组中的一个
            int index = random.nextInt(allTime.size());
            //加入涨的时间map
            upTimeMap.put(allTime.get(index),"0");
            //去掉
            allTime.remove(index);
        }
        //现价
        BigDecimal nowPrice = startPrice;
        //昨日收盘价
        BigDecimal closePricePrev = startPrice;
        //涨跌幅
        BigDecimal changeRate;
        //成交额随机范围最小
        BigDecimal amountMin = startPrice.multiply(new BigDecimal(10000)).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
        //成交额随机范围最大
        BigDecimal amountMax = startPrice.multiply(new BigDecimal(100000)).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
        //实时时间
        Date nowDateTime = new Date();
        //result
        List<SelfSellProductRealTime> result = new ArrayList<>();
        //遍历生成数据
        for (int i = 0; i < timeTemp.size(); i++) {
            //时间
            Date date = timeTemp.get(i);
            //如果是第一条数据，开盘价等于开始价格

            //本分钟涨跌浮动金额
            BigDecimal fluctuationAmount;
            //涨
            if (upTimeMap.get(date) != null){
                //本分钟涨额
                fluctuationAmount = RandomUtil.randomBigDecimal(upAverage.subtract(upAverageFluctuation), upAverage.add(upAverageFluctuation)).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
                //现价
                nowPrice = nowPrice.add(fluctuationAmount);
            }else {
                //跌
                //本分钟跌额
                fluctuationAmount = RandomUtil.randomBigDecimal(downAverage.subtract(downAverageFluctuation), downAverage.add(downAverageFluctuation)).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
                //跌
                BigDecimal price = nowPrice.subtract(fluctuationAmount);
                //如果跌完后的价格正常大于0
                if (price.compareTo(BigDecimal.ZERO) > 0){
                    nowPrice = price;
                }else {
                    //如果跌完价格小于0，则换成涨
                    //本分钟涨额
                    fluctuationAmount = RandomUtil.randomBigDecimal(upAverage.subtract(upAverageFluctuation), upAverage.add(upAverageFluctuation)).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
                    //现价
                    nowPrice = nowPrice.add(fluctuationAmount);
                    //从之后的时间里随机删掉一个涨
                    if (upTimeMap.size() > 0){
                        Date[] dates = upTimeMap.keySet().toArray(new Date[0]);
                        upTimeMap.remove(dates[random.nextInt(dates.length)]);
                    }
                    //把此次记录成涨
                    upTimeMap.put(date,"0");
                }
            }
            //现价-昨日收盘/昨日收盘 = 涨跌幅
            changeRate =  nowPrice.subtract(closePricePrev).divide(closePricePrev,scale, Constants.BIGDECIMAL_ROUNDINGMODE).multiply(new BigDecimal(100));
            //最高价格
            BigDecimal highPrice = RandomUtil.randomBigDecimal(nowPrice.add(fluctuationAmount.divide(new BigDecimal(2),scale, Constants.BIGDECIMAL_ROUNDINGMODE)),nowPrice.add(fluctuationAmount)).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
            //最低价格
            BigDecimal lowPrice = RandomUtil.randomBigDecimal(nowPrice.subtract(fluctuationAmount),nowPrice.subtract(fluctuationAmount.divide(new BigDecimal(2),scale, Constants.BIGDECIMAL_ROUNDINGMODE))).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
            //如果最低价格小于0，则最低价格取现价
            if (lowPrice.compareTo(BigDecimal.ZERO) <= 0){
                lowPrice = nowPrice;
            }
            //开盘价格
            BigDecimal openPrice;
            //收盘价格
            BigDecimal closePrice;
            //涨
            if (upTimeMap.get(date) != null){
                //开盘价格
                openPrice = RandomUtil.randomBigDecimal(lowPrice,nowPrice).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
                //收盘价格
                closePrice = RandomUtil.randomBigDecimal(nowPrice,highPrice).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
            }else {
                //跌
                //开盘价格
                openPrice = RandomUtil.randomBigDecimal(nowPrice,highPrice).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
                //收盘价格
                closePrice = RandomUtil.randomBigDecimal(lowPrice,nowPrice).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
            }
            upTimeMap.remove(date);
            //均价
            BigDecimal averagePrice = RandomUtil.randomBigDecimal(lowPrice,highPrice).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
            //成交额
            BigDecimal amount = RandomUtil.randomBigDecimal(amountMin,amountMax).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
            //成交量
            BigDecimal volumes = amount.divide(averagePrice,scale, Constants.BIGDECIMAL_ROUNDINGMODE);
            //selfSellProductRealTime
            SelfSellProductRealTime selfSellProductRealTime = new SelfSellProductRealTime();
            selfSellProductRealTime.setProductCode(productCode);
            selfSellProductRealTime.setProductType(productType);
            selfSellProductRealTime.setReferencePrice(nowPrice);
            selfSellProductRealTime.setReferenceChangeRate(changeRate);
            selfSellProductRealTime.setOpenPrice(openPrice);
            selfSellProductRealTime.setClosePrice(closePrice);
            selfSellProductRealTime.setHighPrice(highPrice);
            selfSellProductRealTime.setLowPrice(lowPrice);
            selfSellProductRealTime.setAveragePrice(averagePrice);
            selfSellProductRealTime.setVolumes(volumes);
            selfSellProductRealTime.setAmount(amount);
            selfSellProductRealTime.setSpecificTime(date);
            selfSellProductRealTime.setCreateTime(nowDateTime);
            result.add(selfSellProductRealTime);
        }
        if (result.size() > 0){
            selfSellProductRealTimeMapper.insertSelfSellProductRealTimes(result);
        }
    }

    /**
     * 获取时间模板
     * @param productType 产品类型
     * @return
     */
    @Override
    public List<Date> getTimeTemp(Integer productType){
        //时间模板
        List<String> timeTemp = selfSellProductRealTimeMapper.getTimeTemp(productType);
        //日期
        String date = DateUtils.getDate();
        //时间格式
        String dateFormat = "yyyy-MM-dd HH:mm";
        //结果
        List<Date> result = timeTemp.stream().map(a -> DateUtils.dateTime(dateFormat, date + " " + a)).collect(Collectors.toList());
        return result;
    }

    /**
     * 清空相应的分数图数据
     * @param productType 产品类型 1：股票 2：加密货币
     * @param specificTime 对应时间
     * @param productCodes 产品代码
     * @return
     */
    @Override
    public int cleanProductRealTimeData(Integer productType, Date specificTime, List<String> productCodes){
        return selfSellProductRealTimeMapper.cleanProductRealTimeData(productType,specificTime,productCodes);
    }
}
