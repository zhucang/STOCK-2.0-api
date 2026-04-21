package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.ProductQuoteControl;
import com.ruoyi.system.mapper.ProductQuoteControlMapper;
import com.ruoyi.system.service.IProductQuoteControlService;
import com.ruoyi.system.utils.ProductQuoteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 产品短线行情控制Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-01-11
 */
@Service
public class ProductQuoteControlServiceImpl implements IProductQuoteControlService 
{
    @Resource
    private ProductQuoteControlMapper productQuoteControlMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询产品短线行情控制
     * 
     * @param id 产品短线行情控制主键
     * @return 产品短线行情控制
     */
    @Override
    public ProductQuoteControl selectProductQuoteControlById(Long id)
    {
        return productQuoteControlMapper.selectProductQuoteControlById(id);
    }

    /**
     * 查询产品短线行情控制列表
     * 
     * @param productQuoteControl 产品短线行情控制
     * @return 产品短线行情控制
     */
    @Override
    public List<ProductQuoteControl> selectProductQuoteControlList(ProductQuoteControl productQuoteControl)
    {
        List<ProductQuoteControl> productQuoteControls = productQuoteControlMapper.selectProductQuoteControlList(productQuoteControl);
        //已启用并且未过期的
        String productCodes = productQuoteControls.stream().filter(a->a.getStatus().equals(1)).filter(a->a.getPresetEndTime().after(new Date())).map(ProductQuoteControl::getProductCode).collect(Collectors.joining(","));
        if (StringUtils.isEmpty(productCodes)){
            return productQuoteControls;
        }
        //行情map
        Map<String, TickerInfo> tickerInfoMap = null;
        if (productQuoteControl.getProductType().equals(1)){
            tickerInfoMap = ProductQuoteUtils.getStockQuote(productCodes,false);
        }else if (productQuoteControl.getProductType().equals(2)){
            tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
        }else if (productQuoteControl.getProductType().equals(3)){
            tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCodes);
        }else if (productQuoteControl.getProductType().equals(4)){
            tickerInfoMap = ProductQuoteUtils.getForexQuote(productCodes);
        }else {
            return productQuoteControls;
        }
        for (int i = 0; i < productQuoteControls.size(); i++) {
            ProductQuoteControl control = productQuoteControls.get(i);
            if (control.getPresetEndTime().before(new Date())){
                //已结束
                control.setStatus(2);
                control.setUpwardFluctuation(BigDecimal.ZERO);
                control.setDownWardFluctuation(BigDecimal.ZERO);
            }else {
                //产品代码
                String productCode = control.getProductCode();
                //启动点位
                BigDecimal startPrice = control.getStartPrice();
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null && startPrice != null){
                    //现价
                    BigDecimal nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                    BigDecimal upwardFluctuation = nowPrice.subtract(startPrice);
                    control.setUpwardFluctuation(upwardFluctuation);
                    control.setDownWardFluctuation(upwardFluctuation.negate());
                }else {
                    control.setUpwardFluctuation(BigDecimal.ZERO);
                    control.setDownWardFluctuation(BigDecimal.ZERO);
                }
            }
        }
        return productQuoteControls;
    }

    /**
     * 新增产品短线行情控制
     * 
     * @param productQuoteControl 产品短线行情控制
     * @return 结果
     */
    @Override
    public int insertProductQuoteControl(ProductQuoteControl productQuoteControl)
    {
        //产品代码
        String productCode = productQuoteControl.getProductCode();
        //产品类型
        Integer productType = productQuoteControl.getProductType();

        ProductQuoteControl productQuoteControlVo = new ProductQuoteControl();
        productQuoteControlVo.setProductCode(productCode);
        productQuoteControlVo.setProductType(productType);
        List<ProductQuoteControl> productQuoteControls = productQuoteControlMapper.selectProductQuoteControlList(productQuoteControlVo);
        productQuoteControls = productQuoteControls.stream().filter(a->a.getPresetEndTime().after(new Date())).collect(Collectors.toList());
        if (productQuoteControls.size() > 0){
            throw new ServiceException("此产品存在未结束的行情");
        }

        //现价
        BigDecimal price = BigDecimal.ZERO;
        //股票
        if (productType.equals(1)){
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCode,false);
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                price = new BigDecimal(tickerInfo.getNowPrice());
            }
        }else if (productType.equals(2)){
            //加密货币
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                price = new BigDecimal(tickerInfo.getNowPrice());
            }
        }else if (productType.equals(3)){
            //加密货币
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCode);
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                price = new BigDecimal(tickerInfo.getNowPrice());
            }
        }else if (productType.equals(4)){
            //加密货币
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCode);
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                price = new BigDecimal(tickerInfo.getNowPrice());
            }
        }else {
            throw new ServiceException("产品类型错误");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("未获取到该产品行情信息");
        }

        //实时时间
        Date nowDateTime = new Date();
        //启动延时时间
        Integer startDelayTime = productQuoteControl.getStartDelayTime();
        //启动时间
        Date presetStartTime = new Date(nowDateTime.getTime() + startDelayTime * 1000);
        //预设点位
        BigDecimal presetPrice = productQuoteControl.getPresetPrice();
        //预设时间
        Integer presetDuration = productQuoteControl.getPresetDuration();
        //回归时间
        Integer returnDuration = productQuoteControl.getReturnDuration();
        //到达预设点位时间
        Date arrivePresetPriceTime = new Date(presetStartTime.getTime() + presetDuration*1000 - returnDuration*1000);
        //结束时间
        Date endTime = new Date(arrivePresetPriceTime.getTime() + returnDuration*1000);

        productQuoteControl.setProductCode(productCode);
        productQuoteControl.setProductType(productType);
        productQuoteControl.setPresetPrice(presetPrice);
        productQuoteControl.setPresetStartTime(presetStartTime);
        productQuoteControl.setPresetDuration(presetDuration);
        productQuoteControl.setReturnDuration(returnDuration);
        productQuoteControl.setArrivePresetPriceTime(arrivePresetPriceTime);
        productQuoteControl.setPresetEndTime(endTime);
        productQuoteControl.setStartPrice(null);

        int count = productQuoteControlMapper.insertProductQuoteControl(productQuoteControl);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }

        //控制配置id
        Long id = productQuoteControl.getId();

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(()->{
            //控制
            ProductQuoteControl control = productQuoteControlMapper.selectProductQuoteControlById(id);
            //如果控制已删除，则返回
            if (control == null){
                return;
            }
            //预设点位
            BigDecimal presetPriceControl = control.getPresetPrice();
            //预设时间
            Integer presetDurationControl = control.getPresetDuration();
            //回归时间
            Integer returnDurationControl = control.getReturnDuration();
            //启动时间
            Date presetStartTimeControl = control.getPresetStartTime();
            //产品代码
            String productCodeControl = control.getProductCode();
            //到达预设点位时长
            Integer arrivePresetPriceDuration = presetDurationControl - returnDurationControl;

            //产品现价
            BigDecimal nowPrice = BigDecimal.ZERO;
            BigDecimal changeRate = BigDecimal.ZERO;
            //股票
            if (control.getProductType().equals(1)){
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCodeControl,false);
                TickerInfo tickerInfo = tickerInfoMap.get(productCodeControl);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                    changeRate = new BigDecimal(tickerInfo.getChangeRate());
                }
            }else if (control.getProductType().equals(2)){
                //加密货币
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCodeControl,false);
                TickerInfo tickerInfo = tickerInfoMap.get(productCodeControl);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                    changeRate = new BigDecimal(tickerInfo.getChangeRate());
                }
            }else if (control.getProductType().equals(3)){
                //期货
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCodeControl);
                TickerInfo tickerInfo = tickerInfoMap.get(productCodeControl);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                    changeRate = new BigDecimal(tickerInfo.getChangeRate());
                }
            }else if (control.getProductType().equals(4)){
                //外汇
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCodeControl);
                TickerInfo tickerInfo = tickerInfoMap.get(productCodeControl);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                    changeRate = new BigDecimal(tickerInfo.getChangeRate());
                }
            }else {
                return;
            }
            //如果获取现价异常，则返回
            if (nowPrice.compareTo(BigDecimal.ZERO) == 0){
                return;
            }

            //昨日收盘价
            BigDecimal closePricePrevDay = nowPrice.divide((changeRate.divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE).add(new BigDecimal(1))),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //到达预设点位价格数据
            List<BigDecimal> priceDataArrivePresetPrice = this.getData(nowPrice, presetPriceControl, arrivePresetPriceDuration);
            //回归启动点位价格数据
            List<BigDecimal> priceDataReturn = this.getData(presetPriceControl, nowPrice, returnDurationControl);
            priceDataArrivePresetPrice.addAll(priceDataReturn);

            Map<String, String> map = new HashMap<>();
            long time = presetStartTimeControl.getTime();
            for (int i = 0; i < priceDataArrivePresetPrice.size(); i++) {
                //当前价格
                nowPrice = priceDataArrivePresetPrice.get(i);
                //涨跌幅
                changeRate = nowPrice.subtract(closePricePrevDay).divide(closePricePrevDay,6, 4).multiply(new BigDecimal(100)).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                //加一秒
                time = time + 1000;
                //时间
                Date dateTime = new Date(time);
                map.put(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,dateTime),nowPrice+"/"+changeRate);
            }
            redisCache.setCacheObject("productQuoteControlCache:"+productCodeControl+"/"+control.getProductType(), map,presetDurationControl,TimeUnit.SECONDS);
            //更新启动点位
            control.setStartPrice(nowPrice);
            control.setStatus(1);
            productQuoteControlMapper.updateProductQuoteControl(control);
        },startDelayTime, TimeUnit.SECONDS);
        service.shutdown();
        return 1;
    }

    /**
     * 获取生成的现价数据
     * @param startPrice 开始价格
     * @param endPrice 结束价格
     * @param num 需要生成的数目
     * @return
     */
    public static List<BigDecimal> getData(BigDecimal startPrice,BigDecimal endPrice,Integer num){
        Random random = new Random();
        //方向（0是涨 1是跌）
        int randDirect = random.nextInt(2);
        //变动比率
        double randRate = random.nextDouble();

        ArrayList<BigDecimal> list = new ArrayList<>();
        list.add(startPrice);
        BigDecimal nowPrice = startPrice;

        for (int i = 0; i < num-1; i++) {
            //目前差价
            BigDecimal subPrice = endPrice.subtract(nowPrice);
            //目前平均
            BigDecimal averagePrice = subPrice.divide(new BigDecimal(num - i + 1),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            if (averagePrice.compareTo(BigDecimal.ZERO)==0){
                averagePrice = new BigDecimal(randRate).divide(new BigDecimal(10),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            }
            if (i >= num-30){
                //最后30分钟
                if (subPrice.compareTo(BigDecimal.ZERO) == -1){
                    randDirect = 1;
                }else {
                    randDirect = 0;
                }
            }else {
                randDirect = random.nextInt(2);
                if (startPrice.compareTo(BigDecimal.ZERO) <= 0){
                    randDirect = 0;
                }
            }
            randRate = random.nextDouble()+0.5;
            //是涨
            if (randDirect == 0){
                nowPrice = nowPrice.add(averagePrice.multiply(new BigDecimal(randRate))).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            }else {
                if (averagePrice.compareTo(BigDecimal.ZERO)==-1){
                    averagePrice = averagePrice.negate();
                }
                //是跌
                nowPrice = nowPrice.subtract(averagePrice.multiply(new BigDecimal(randRate))).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            }
            list.add(nowPrice);
        }
        return list;
    }

    /**
     * 修改产品短线行情控制
     * 
     * @param productQuoteControl 产品短线行情控制
     * @return 结果
     */
    @Override
    public int updateProductQuoteControl(ProductQuoteControl productQuoteControl)
    {
        return productQuoteControlMapper.updateProductQuoteControl(productQuoteControl);
    }

    /**
     * 批量删除产品短线行情控制
     * 
     * @param ids 需要删除的产品短线行情控制主键
     * @return 结果
     */
    @Override
    public int deleteProductQuoteControlByIds(Long[] ids)
    {
        ProductQuoteControl search = new ProductQuoteControl();
        search.getParams().put("ids", Arrays.asList(ids));
        List<ProductQuoteControl> productQuoteControls = productQuoteControlMapper.selectProductQuoteControlList(search);
        //日志记录产品短线行情控制配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:productQuoteControls", JSONObject.toJSONString(productQuoteControls));
        return productQuoteControlMapper.deleteProductQuoteControlByIds(ids);
    }

    /**
     * 删除产品短线行情控制信息
     * 
     * @param id 产品短线行情控制主键
     * @return 结果
     */
    @Override
    public int deleteProductQuoteControlById(Long id)
    {
        return productQuoteControlMapper.deleteProductQuoteControlById(id);
    }
}
