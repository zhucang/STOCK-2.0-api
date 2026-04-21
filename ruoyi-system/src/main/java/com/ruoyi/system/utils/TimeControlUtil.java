package com.ruoyi.system.utils;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.FastOrderControlConfig;
import com.ruoyi.system.domain.FastTradeOrder;
import com.ruoyi.system.domain.UserFastTradeControl;
import com.ruoyi.system.mapper.UserFastTradeControlMapper;
import com.ruoyi.system.task.news.CrawlNewsTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class TimeControlUtil {

    private static final Logger log = LoggerFactory.getLogger(CrawlNewsTask.class);

    public static final int CONTROL_TIME_VAL = 10;
    public static final double MUL_RATE = 0.001;

    public static final String TIME_CONTROL_CACHE_PREFIX = "time_control_";

    private static UserFastTradeControlMapper userFastTradeControlMapper = SpringUtils.getBean(UserFastTradeControlMapper.class);

//    /**
//     * 将订单控制价格信息设置缓存
//     * @param order
//     * @param orderControlPriceList
//     */
//    public static void setOrderControlPriceToCache(UserOrderVO order, List<OrderControlPriceVO> orderControlPriceList) {
//        if (orderControlPriceList!=null && !orderControlPriceList.isEmpty()){
//            String orderControlPriceStr = JsonUtil.obj2String(orderControlPriceList);
//            RedisShardedPoolUtils.set(TIME_CONTROL_CACHE_PREFIX+ order.getOrderId(),orderControlPriceStr);
//        }
//    }
//
//    /**
//     * 从缓存中获取订单控制价格信息
//     * @param order
//     * @return
//     */
//    public static List<OrderControlPriceVO> getOrderControlPriceFromCache(UserOrderVO order) {
//        List<OrderControlPriceVO> orderControlPriceList = null;
//        String orderControlPriceStr = RedisShardedPoolUtils.get(TIME_CONTROL_CACHE_PREFIX+ order.getOrderId());
//        if (StringUtils.isNotEmpty(orderControlPriceStr)) {
//            orderControlPriceList = JsonUtil.string2Obj(orderControlPriceStr, List.class, OrderControlPriceVO.class);
//        }
//        return orderControlPriceList;
//    }
//
//    /**
//     * 从缓存中移除订单控制价格信息
//     * @param order 订单
//     * @return
//     */
//    public static Long delOrderControlPriceInCache(UserOrderVO order) {
//        Long result = RedisShardedPoolUtils.del(TIME_CONTROL_CACHE_PREFIX + order.getOrderId());
//        return result;
//    }

    /**
     * 控制订单最后10秒的价格
     * @param order 极速交易订单信息
     * @param nowPrice 当前价格
     * @param userFastTradeControl  极速交易用户单控配置
     * @param fastOrderControlConfigs  极速交易群控配置列表
     * @param randomDiffAmount 随机差金额
     * @param userAmount 用户钱包信息
     * @return
     */
    public  static Map<String,String> generalOrderControlPriceList(FastTradeOrder order, BigDecimal nowPrice, UserFastTradeControl userFastTradeControl, List<FastOrderControlConfig> fastOrderControlConfigs, BigDecimal randomDiffAmount,UserAmount userAmount){
        //控制优先级 极速交易订单单控 -> 极速交易用户单控 -> 极速交易群控 ->随机差大小金额控
        //输赢标志 0：未控  1：赢  2：输 3：平
        Integer winOrLose = 0;
        //极速交易订单单控
        winOrLose = order.getOrderControlFlag();
        //如果极速交易订单单控未控，则进行极速交易用户单控判断
        if (winOrLose.equals(0)){
            //极速交易用户单控
            if (userFastTradeControl != null){
                winOrLose = getUserFastTradeControlWinOrLose(order.getOrderPrice(),order.getOrderDirection(),userFastTradeControl,userAmount);
            }
            //如果极速交易用户单控未控，则进行极速交易群控判断
            if (winOrLose.equals(0)){
                //极速交易群控
                if (fastOrderControlConfigs.size() > 0) {
                    //过滤出符合条件的群控配置
                    List<FastOrderControlConfig> collect = fastOrderControlConfigs.stream().filter(a -> a.getTradeDirect().equals(order.getOrderDirection()) && order.getDeliverTime().after(a.getBeginTime()) && order.getDeliverTime().before(a.getFinishTime())).collect(Collectors.toList());
                    if (collect.size() > 0){
                        //取第一个
                        FastOrderControlConfig fastOrderControlConfig = collect.get(0);
                        //0：输  1：赢 2：平 3:未控
                        Integer flag = fastOrderControlConfig.getWinOrLose();
                        if (flag.equals(0)){
                            winOrLose =  2;
                        }else if (flag.equals(1)){
                            winOrLose =  1;
                        }else if (flag.equals(2)){
                            winOrLose =  3;
                        }else {
                            winOrLose = 0;
                        }
                    }
                }
                //如果极速交易群控未控，则进行极速交易群控判断
                if (winOrLose.equals(0)){
                    //将订单金额小于于等于随机差金额的订单控输
                    if (randomDiffAmount != null && order.getOrderPrice().compareTo(randomDiffAmount) <= 0) {
                        winOrLose = 2;
                    }
                }
            }
        }
        //结果集
        Map<String,String> result = null;
        //如果未控
        if (winOrLose.equals(0)){
            result = new HashMap<>();
            //不允许其他订单控制影响当前订单的结算价格
            result.put(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,order.getDeliverTime()),"notAllowedControl");
            log.info("极速交易订单控制状态未控，订单号：" + order.getOrderCode() + "，订单未受到控制");
            return result;
        }
        //结算时间
        Date deliverTime = order.getDeliverTime();
        //结算时间前N秒的时间
        Date beforeTenSecondTime = new Date(order.getDeliverTime().getTime() - CONTROL_TIME_VAL * 1000);
        //当前时间
        Date nowDateTime = new Date();
        //如果当前时间还在生成数据的时间段内
        if (!nowDateTime.before(beforeTenSecondTime) && !nowDateTime.after(deliverTime)){//开始进行控制
            result = new HashMap<>();
            //小数位数
            int scale = 6;
            //购买价格
            BigDecimal buyPrice = order.getBuyPrice().stripTrailingZeros();
            //计算差价
            BigDecimal mulRate = new BigDecimal(MUL_RATE);//乘积率
            BigDecimal diffPrice = nowPrice.subtract(buyPrice);//现价与买入价差价
            BigDecimal diffNowPrice = nowPrice.multiply(mulRate).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE); //现价乘积差价
            BigDecimal diffOrderPrice = buyPrice.multiply(mulRate).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);//买入价乘积差价
            BigDecimal highPrice = java.math.BigDecimal.ZERO;
            BigDecimal lowPrice = java.math.BigDecimal.ZERO;
            BigDecimal highDiffPrice = java.math.BigDecimal.ZERO;
            BigDecimal lowDiffPrice = java.math.BigDecimal.ZERO;
            if (diffNowPrice.compareTo(java.math.BigDecimal.ZERO) == 0){
                diffNowPrice = new BigDecimal("0.000001");
            }
            if (diffOrderPrice.compareTo(java.math.BigDecimal.ZERO) == 0){
                diffOrderPrice = new BigDecimal("0.000001");
            }
            if(diffPrice.compareTo(java.math.BigDecimal.ZERO)>=0) {
                highPrice = nowPrice.add(diffNowPrice);
                lowPrice = buyPrice.subtract(diffOrderPrice);
                highDiffPrice = diffNowPrice;
                lowDiffPrice = diffOrderPrice;
            } else {
                lowPrice = nowPrice.subtract(diffNowPrice);
                highPrice= buyPrice.add(diffOrderPrice);
                highDiffPrice = diffOrderPrice;
                lowDiffPrice = diffNowPrice;
            }
            //产品的价格在最高价与最低价之间浮动，最后的价格根据控制策略确定是实际价格
            Integer tradeDirect = order.getOrderDirection();//0：涨 1：跌
            BigDecimal targetPrice = null;
            BigDecimal ctlDiffPrice = null;
            Random random = new Random();
            for (int i = 0; i < CONTROL_TIME_VAL; i++) {
                //最后时间
                if(i == (CONTROL_TIME_VAL -1)){
                    if (winOrLose.equals(1)){//控赢
                        if (tradeDirect.equals(0)){//买涨
                            ctlDiffPrice = highDiffPrice.multiply(java.math.BigDecimal.valueOf(random.nextDouble())).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
                            targetPrice = highPrice.subtract(ctlDiffPrice);
                            if (targetPrice.compareTo(buyPrice) == 0){
                                targetPrice = targetPrice.add(new BigDecimal("0.000001"));
                            }
                        } else {//买跌
                            ctlDiffPrice = lowDiffPrice.multiply(java.math.BigDecimal.valueOf(random.nextDouble())).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
                            targetPrice = lowPrice.add(ctlDiffPrice);
                            if (targetPrice.compareTo(buyPrice) == 0){
                                targetPrice = targetPrice.subtract(new BigDecimal("0.000001"));
                            }
                        }
                    }else if (winOrLose.equals(2)){//控输
                        if (tradeDirect.equals(0)){//买涨
                            ctlDiffPrice = lowDiffPrice.multiply(java.math.BigDecimal.valueOf(random.nextDouble())).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
                            targetPrice = lowPrice.add(ctlDiffPrice);
                            if (targetPrice.compareTo(buyPrice) == 0){
                                targetPrice = targetPrice.subtract(new BigDecimal("0.000001"));
                            }
                        } else {//买跌
                            ctlDiffPrice = highDiffPrice.multiply(java.math.BigDecimal.valueOf(random.nextDouble())).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
                            targetPrice = highPrice.subtract(ctlDiffPrice);
                            if (targetPrice.compareTo(buyPrice) == 0){
                                targetPrice = targetPrice.add(new BigDecimal("0.000001"));
                            }
                        }
                    }else if (winOrLose.equals(3)){//控平
                        targetPrice = buyPrice;
                    }
                } else {
                    //需要控制的差价
                    ctlDiffPrice = highPrice.subtract(lowPrice);
                    ctlDiffPrice = ctlDiffPrice.multiply(java.math.BigDecimal.valueOf(random.nextDouble())).setScale(scale, Constants.BIGDECIMAL_ROUNDINGMODE);
                    if (tradeDirect == 0 ){//买涨
                        targetPrice = highPrice.subtract(ctlDiffPrice);
                    } else {//买跌
                        targetPrice = lowPrice.add(ctlDiffPrice);
                    }
                }
                result.put(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,new Date(beforeTenSecondTime.getTime() + 1000 + i * 1000)),targetPrice.toString());
            }
            //如果正常生成控制数据
            log.info("极速交易订单控制信息生成成功，控制状态："+winOrLose+"，订单号：" + order.getOrderCode() + "，时间：" + DateUtils.getTime());
        }else {
            throw new RuntimeException("极速交易订单控制信息生成失败，控制状态："+winOrLose+"，订单号：" + order.getOrderCode() + "，时间：" + DateUtils.getTime() + "，当前时间未在控制时间段内");
        }
        return result;
    }

    /**
     * 用户单控获取最终输赢结果
     * @param orderAmount 订单金额
     * @param tradeDirect 涨跌方向 0：买涨 1：买跌
     * @param userFastTradeControl 用户极速交易控制信息
     * @param userAmount 用户钱包信息
     * @return
     */
    public static Integer getUserFastTradeControlWinOrLose(BigDecimal orderAmount,Integer tradeDirect,UserFastTradeControl userFastTradeControl,UserAmount userAmount){
        //没受控制
        if (userFastTradeControl == null){
            return 0;
        }
        //控制类型：0：全部未控 1：全部控赢 2：全部控输 3：注额概率控（全部） 4：注额概率控（买涨） 5：注额概率控（买跌） 6：账户余额概率控（全部）
        //7：账户余额固定控（全部） 8：交易方向控（买多赢买空输） 9：交易方向控（买多输买空赢）
        Integer controlType = userFastTradeControl.getControlType();
        //全部未控
        if (controlType.equals(0)){
            return 0;
        }
        //全部控赢
        else if (controlType.equals(1)){
            return 1;
        }
        //全部控输
        else if (controlType.equals(2)){
            return 2;
        }
        //注额概率控（全部）
        else if (controlType.equals(3)){
            //注额范围最小值
            BigDecimal minLimitTradeAmount = userFastTradeControl.getMinLimitTradeAmount();
            //注额范围最大值
            BigDecimal maxLimitTradeAmount = userFastTradeControl.getMaxLimitTradeAmount();
            //赢率
            BigDecimal winPercent = null;
            //如果下单金额在注额范围内
            if (orderAmount.compareTo(minLimitTradeAmount) >= 0 && orderAmount.compareTo(maxLimitTradeAmount) <= 0){
                //赢率
                winPercent = userFastTradeControl.getWinPercentWithinTradeAmount().divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            }else {
                //如果下单金额在注额范围外
                //赢率
                winPercent = userFastTradeControl.getWinPercentOutsideTradeAmount().divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            }
            //0~1的随机数
            BigDecimal rand = RandomUtil.randomBigDecimal();
            //如果随机数小于赢率
            if (rand.compareTo(winPercent) < 0){
                return 1;
            }else {
                return 2;
            }
        }
        //注额概率控（买涨）
        else if (controlType.equals(4)){
            if (tradeDirect.equals(0)){
                //注额范围最小值
                BigDecimal minLimitTradeAmount = userFastTradeControl.getMinLimitTradeAmount();
                //注额范围最大值
                BigDecimal maxLimitTradeAmount = userFastTradeControl.getMaxLimitTradeAmount();
                //赢率
                BigDecimal winPercent = null;
                //如果下单金额在注额范围内
                if (orderAmount.compareTo(minLimitTradeAmount) >= 0 && orderAmount.compareTo(maxLimitTradeAmount) <= 0){
                    //赢率
                    winPercent = userFastTradeControl.getWinPercentWithinTradeAmount().divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                }else {
                    //如果下单金额在注额范围外
                    //赢率
                    winPercent = userFastTradeControl.getWinPercentOutsideTradeAmount().divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                }

                //0~1的随机数
                BigDecimal rand = RandomUtil.randomBigDecimal();
                //如果随机数小于赢率
                if (rand.compareTo(winPercent) < 0){
                    return 1;
                }else {
                    return 2;
                }
            }
        }
        //注额概率控（买跌）
        else if (controlType.equals(5)){
            if (tradeDirect.equals(1)){
                //注额范围最小值
                BigDecimal minLimitTradeAmount = userFastTradeControl.getMinLimitTradeAmount();
                //注额范围最大值
                BigDecimal maxLimitTradeAmount = userFastTradeControl.getMaxLimitTradeAmount();
                //赢率
                BigDecimal winPercent = null;
                //如果下单金额在注额范围内
                if (orderAmount.compareTo(minLimitTradeAmount) >= 0 && orderAmount.compareTo(maxLimitTradeAmount) <= 0){
                    //赢率
                    winPercent = userFastTradeControl.getWinPercentWithinTradeAmount().divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                }else {
                    //如果下单金额在注额范围外
                    //赢率
                    winPercent = userFastTradeControl.getWinPercentOutsideTradeAmount().divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                }

                //0~1的随机数
                BigDecimal rand = RandomUtil.randomBigDecimal();
                //如果随机数小于赢率
                if (rand.compareTo(winPercent) < 0){
                    return 1;
                }else {
                    return 2;
                }
            }
        }
        //账户余额概率控（全部）
        else if (controlType.equals(6)){
            //用户余额
            BigDecimal amount = userAmount.getAmount();
            //注额范围最小值
            BigDecimal minLimitUserBalance = userFastTradeControl.getMinLimitUserBalance();
            //注额范围最大值
            BigDecimal maxLimitUserBalance = userFastTradeControl.getMaxLimitUserBalance();
            //赢率
            BigDecimal winPercent = null;
            //如果用户余额在控制范围内
            if (amount.compareTo(minLimitUserBalance) >= 0 && amount.compareTo(maxLimitUserBalance) <= 0){
                //赢率
                winPercent = userFastTradeControl.getWinPercentWithinUserBalance().divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            }else {
                //如果用户余额在控制范围外
                //赢率
                winPercent = userFastTradeControl.getWinPercentOutsideUserBalance().divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            }
            //0~1的随机数
            BigDecimal rand = RandomUtil.randomBigDecimal();
            //如果随机数小于赢率
            if (rand.compareTo(winPercent) < 0){
                return 1;
            }else {
                return 2;
            }
        }
        //账户余额固定控（全部）
        else if (controlType.equals(7)){
            //用户余额
            BigDecimal amount = userAmount.getAmount();
            //注额范围最小值
            BigDecimal minLimitUserBalance = userFastTradeControl.getMinLimitUserBalance();
            //注额范围最大值
            BigDecimal maxLimitUserBalance = userFastTradeControl.getMaxLimitUserBalance();
            //赢率
            BigDecimal winPercent = null;
            //如果用户余额在控制范围内
            if (amount.compareTo(minLimitUserBalance) >= 0 && amount.compareTo(maxLimitUserBalance) <= 0){
                //赢率
                winPercent = userFastTradeControl.getWinPercentWithinUserBalance().divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            }else {
                //如果用户余额在控制范围外
                //赢率
                winPercent = userFastTradeControl.getWinPercentOutsideUserBalance().divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            }
            //0~1的随机数
            BigDecimal rand = RandomUtil.randomBigDecimal();
            //如果随机数小于赢率
            if (rand.compareTo(winPercent) < 0){
                return 1;
            }else {
                return 2;
            }
        }
        //交易方向控（买多赢买空输）
        else if (controlType.equals(8)){
            if (tradeDirect.equals(0)){
                return 1;
            }else {
                return 2;
            }
        }
        //交易方向控（买多输买空赢）
        else if (controlType == 9){
            if (tradeDirect.equals(0)){
                return 2;
            }else {
                return 1;
            }
        }
        return 0;
    }
}
