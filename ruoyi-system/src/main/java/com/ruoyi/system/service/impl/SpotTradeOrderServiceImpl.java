package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SpotTradeOrderMapper;
import com.ruoyi.system.mapper.UserApiKeyMapper;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.BuyAndSellUtils;
import com.ruoyi.system.utils.ProductQuoteUtils;
import com.ruoyi.system.utils.UserApiKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 现货交易订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
@Service
public class SpotTradeOrderServiceImpl implements ISpotTradeOrderService 
{
    @Resource
    private SpotTradeOrderMapper spotTradeOrderMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IProductTradeTimeSettingService productTradeTimeSettingService;

    @Autowired
    private IStockProductService stockProductService;

    @Autowired
    private ICryptocurrencyProductService cryptocurrencyProductService;

    @Autowired
    private IFuturesProductService futuresProductService;

    @Autowired
    private IForexProductService forexProductService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Autowired
    private IOrderFeeSettingService orderFeeSettingService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private UserApiKeyMapper userApiKeyMapper;

    /**
     * 查询现货交易订单
     * 
     * @param id 现货交易订单主键
     * @return 现货交易订单
     */
    @Override
    public SpotTradeOrder selectSpotTradeOrderById(Long id)
    {
        return spotTradeOrderMapper.selectSpotTradeOrderById(id);
    }

    /**
     * 查询现货交易订单列表
     * 
     * @param spotTradeOrder 现货交易订单
     * @return 现货交易订单
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<SpotTradeOrder> selectSpotTradeOrderList(SpotTradeOrder spotTradeOrder)
    {
        return spotTradeOrderMapper.selectSpotTradeOrderList(spotTradeOrder);
    }

    /**
     * 填充其他信息
     * @param spotTradeOrders 现货交易订单列表
     */
    @Override
    public void fillOtherInfo(List<SpotTradeOrder> spotTradeOrders){
        fillProductQuote(spotTradeOrders);
    }

    /**
     * 填充行情信息
     * @param spotTradeOrders 现货交易订单列表
     */
    void fillProductQuote(List<SpotTradeOrder> spotTradeOrders){
        if (spotTradeOrders.size() == 0){
            return;
        }
        //需要获取行情的订单map
        Map<Integer, List<SpotTradeOrder>> map = spotTradeOrders.stream().filter(a->a.getOrderStatus().equals(0)).collect(Collectors.groupingBy(a -> a.getProductType()));
        //行情map
        Map<String, TickerInfo> tickerInfoMap = new HashMap<>();
        //股票订单列表
        List<SpotTradeOrder> stockSpotTradeOrders = map.get(1);
        //如果有股票订单列表
        if (stockSpotTradeOrders != null && stockSpotTradeOrders.size() > 0){
            //productCodes
            String productCodes = stockSpotTradeOrders.stream().map(SpotTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            Map<String, TickerInfo> stockQuote = ProductQuoteUtils.getStockQuote(productCodes, false);
            tickerInfoMap.putAll(stockQuote);
        }
        //加密货币订单列表
        List<SpotTradeOrder> cryptocurrencySpotTradeOrders = map.get(2);
        //如果有加密货币订单列表
        if (cryptocurrencySpotTradeOrders != null && cryptocurrencySpotTradeOrders.size() > 0){
            //productCodes
            String productCodes = cryptocurrencySpotTradeOrders.stream().map(SpotTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            Map<String, TickerInfo> stockQuote = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes, false);
            tickerInfoMap.putAll(stockQuote);
        }
        //期货订单列表
        List<SpotTradeOrder> futuresSpotTradeOrders = map.get(3);
        //如果有期货订单列表
        if (futuresSpotTradeOrders != null && futuresSpotTradeOrders.size() > 0){
            //productCodes
            String productCodes = futuresSpotTradeOrders.stream().map(SpotTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            Map<String, TickerInfo> stockQuote = ProductQuoteUtils.getFuturesQuote(productCodes);
            tickerInfoMap.putAll(stockQuote);
        }
        //外汇订单列表
        List<SpotTradeOrder> forexSpotTradeOrders = map.get(4);
        //如果有外汇订单列表
        if (forexSpotTradeOrders != null && forexSpotTradeOrders.size() > 0){
            //productCodes
            String productCodes = forexSpotTradeOrders.stream().map(SpotTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            Map<String, TickerInfo> stockQuote = ProductQuoteUtils.getForexQuote(productCodes);
            tickerInfoMap.putAll(stockQuote);
        }
        //遍历塞入行情信息
        for (int i = 0; i < spotTradeOrders.size(); i++) {
            //订单信息
            SpotTradeOrder spotTradeOrder = spotTradeOrders.get(i);
            //如果订单持仓中
            if (spotTradeOrder.getOrderStatus().equals(0)){
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(spotTradeOrder.getProductCode());
                if (tickerInfo != null){
                    //现价
                    BigDecimal nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                    spotTradeOrder.setNowPrice(nowPrice);
                    //购买时候的市值
                    BigDecimal orderTotalPrice = spotTradeOrder.getOrderTotalPrice();
                    //现在的市值
                    BigDecimal orderTotalPriceNow = nowPrice.multiply(spotTradeOrder.getOrderNum()).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    //浮动盈亏
                    BigDecimal profitAndLose = orderTotalPriceNow.subtract(orderTotalPrice);
                    spotTradeOrder.setProfitAndLose(profitAndLose);
                    //总盈亏 = 浮动盈亏 - 手续费
                    spotTradeOrder.setAllProfitAndLose(profitAndLose.subtract(spotTradeOrder.getOrderFee()));
                }
            }
        }
    }

    /**
     * 新增现货交易订单
     * 
     * @param spotTradeOrder 现货交易订单
     * @return 结果
     */
    @Override
    public int insertSpotTradeOrder(SpotTradeOrder spotTradeOrder)
    {
        return spotTradeOrderMapper.insertSpotTradeOrder(spotTradeOrder);
    }

    /**
     * 修改现货交易订单
     * 
     * @param spotTradeOrder 现货交易订单
     * @return 结果
     */
    @Override
    public int updateSpotTradeOrder(SpotTradeOrder spotTradeOrder)
    {
        return spotTradeOrderMapper.updateSpotTradeOrder(spotTradeOrder);
    }

    /**
     * 批量删除现货交易订单
     * 
     * @param ids 需要删除的现货交易订单主键
     * @return 结果
     */
    @Override
    public int deleteSpotTradeOrderByIds(Long[] ids)
    {
        return spotTradeOrderMapper.deleteSpotTradeOrderByIds(ids);
    }

    /**
     * 删除现货交易订单信息
     * 
     * @param id 现货交易订单主键
     * @return 结果
     */
    @Override
    public int deleteSpotTradeOrderById(Long id)
    {
        return spotTradeOrderMapper.deleteSpotTradeOrderById(id);
    }

    /**
     * 用户现货交易下单
     * @param productType 产品类型
     * @param productCode 产品代码
     * @param orderNum 订单数量
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSpotTradeOrder(Integer productType, String productCode, BigDecimal orderNum){
        //用户id
        Long userId = UserApiKeyUtils.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("userId",userId);
        //验证用户交易状态
        if (!userInfo.getIsLock().equals(0)) {
            throw new LangException("hint_dealErrorAccountLocked","下单失败，用户已被锁定");
        }
        //如果是模拟账号不做验证
        if (!userInfo.getAccountType().equals(2)) {
            //是否开启初级实名认证
            Integer selectSwitchStatusById68 = switchSetService.selectSwitchStatusById(68L);
            //如果初级认证开启
            if (selectSwitchStatusById68.equals(0)){
                //完成初级实名认证才能交易
                Integer selectSwitchStatusById70 = switchSetService.selectSwitchStatusById(70L);
                if (selectSwitchStatusById70.equals(0) && !userInfo.getAuthStatusJunior().equals(2)){
                    throw new LangException(HintConstants.AUTH_FIRST_JUNIOR,"请先完成初级实名认证");
                }
            }
            //是否开启高级实名认证
            Integer selectSwitchStatusById75 = switchSetService.selectSwitchStatusById(75L);
            //如果高级认证开启
            if (selectSwitchStatusById75.equals(0)) {
                //完成高级实名认证才能交易
                Integer selectSwitchStatusById72 = switchSetService.selectSwitchStatusById(72L);
                if (selectSwitchStatusById72.equals(0) && !userInfo.getAuthStatusSenior().equals(2)){
                    throw new LangException(HintConstants.AUTH_FIRST_SENIOR,"请先完成高级实名认证");
                }
            }
        }

        //是否在交易时间
        ProductTradeTimeSetting productTradeTimeSetting = productTradeTimeSettingService.selectProductTradeTimeSettingByDayAndProductType(DateUtils.getDayOfWeek(), productType);
        String am_begin = productTradeTimeSetting.getTransAmBegin();
        String am_end = productTradeTimeSetting.getTransAmEnd();
        String pm_begin = productTradeTimeSetting.getTransPmBegin();
        String pm_end = productTradeTimeSetting.getTransPmEnd();
        boolean am_flag = false;
        boolean pm_flag = false;
        try {
            am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
            pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        if (!am_flag && !pm_flag) {
            throw new LangException("hint_dealErrorOutOfTradingHours","交易失败，不在交易时段内");
        }
        //币种id
        Long currencyId = null;
        //统一交易币种开关
        Integer switchStatus56L = switchSetService.selectSwitchStatusById(56L);
        //如果统一交易币种开关开启
        if (switchStatus56L != null && switchStatus56L.equals(0)){
            try{
                currencyId = CacheUtil.getOtherValueByKey("default_trade_currency_id", Long.class);
            }catch (Exception e){

            }
        }
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取币种信息异常");
        }
        if (!platformCurrency.getStatus().equals(0)){
            throw new LangException(HintConstants.SYSTEM_BUSY,"此币种已禁用");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        //现货交易订单
        SpotTradeOrder spotTradeOrder = new SpotTradeOrder();
        //买入手续费率
        BigDecimal buyFeeRate = BigDecimal.ZERO;
        //美股
        if (productType.equals(1)){
            if (currencyId == null){
                currencyId = 1L;
            }
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null){
                return AjaxResult.error(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCode,false);
            //接口有获取到数据
            if (tickerInfoMap != null){
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                //如果接口调用到数据,则股票已开盘，取实时数据
                if (tickerInfo != null){
                    spotTradeOrder.setBuyOrderPrice(new BigDecimal(tickerInfo.getNowPrice()));
                }
            }
            //判断是否取到了行情
            if (spotTradeOrder.getBuyOrderPrice() == null){
                return AjaxResult.error("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
            }
            //买入手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("stock_spotTrade_buy_fee");
            if (buyFeeVo != null){
                buyFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(2)){
            //加密货币
            if (currencyId == null){
                currencyId = 2L;
            }
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product == null){
                return AjaxResult.error(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
            //接口有获取到数据
            if (tickerInfoMap != null){
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                //如果接口调用到数据,则股票已开盘，取实时数据
                if (tickerInfo != null){
                    spotTradeOrder.setBuyOrderPrice(new BigDecimal(tickerInfo.getNowPrice()));
                }
            }
            //判断是否取到了行情
            if (spotTradeOrder.getBuyOrderPrice() == null){
                return AjaxResult.error("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
            }
            //买入手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("cryptocurrency_spotTrade_buy_fee");
            if (buyFeeVo != null){
                buyFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(3)){
            //期货
            if (currencyId == null){
                currencyId = 7L;
            }
            FuturesProduct product = futuresProductService.selectFuturesProductByCode(productCode);
            if (product == null){
                return AjaxResult.error(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCode);
            //接口有获取到数据
            if (tickerInfoMap != null){
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                //如果接口调用到数据,则股票已开盘，取实时数据
                if (tickerInfo != null){
                    spotTradeOrder.setBuyOrderPrice(new BigDecimal(tickerInfo.getNowPrice()));
                }
            }
            //判断是否取到了行情
            if (spotTradeOrder.getBuyOrderPrice() == null){
                return AjaxResult.error("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
            }
            //买入手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("futures_spotTrade_buy_fee");
            if (buyFeeVo != null){
                buyFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(4)){
            //外汇
            if (currencyId == null){
                currencyId = 4L;
            }
            ForexProduct product = forexProductService.selectForexProductByCode(productCode);
            if (product == null){
                return AjaxResult.error(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCode);
            //接口有获取到数据
            if (tickerInfoMap != null){
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                //如果接口调用到数据,则股票已开盘，取实时数据
                if (tickerInfo != null){
                    spotTradeOrder.setBuyOrderPrice(new BigDecimal(tickerInfo.getNowPrice()));
                }
            }
            //判断是否取到了行情
            if (spotTradeOrder.getBuyOrderPrice() == null){
                return AjaxResult.error("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
            }
            //买入手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("forex_spotTrade_buy_fee");
            if (buyFeeVo != null){
                buyFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else {
            return AjaxResult.error(HintConstants.SYSTEM_BUSY,"产品类型错误");
        }

        //是否熔断
        String key = "stockFuseWarning"+spotTradeOrder.getProductCode();
        String key2 = "stockFuseFreeze"+spotTradeOrder.getProductCode();
        Object s = redisCache.getCacheObject(key);
        Object s2 = redisCache.getCacheObject(key2);
        if (s != null || s2 != null){
            return AjaxResult.error("hint_20","该产品触发熔断机制，短时间禁止交易");
        }

        //订单总金额
        BigDecimal orderTotalPrice = spotTradeOrder.getBuyOrderPrice().multiply(orderNum).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);

        //用户钱包余额
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //变更前的用户资金
        BigDecimal userAmountBefore = userAmount.getAmount();
        if (orderTotalPrice.compareTo(userAmountBefore) > 0){
            return AjaxResult.error("hint_17","此币种可用资金不足");
        }

        //变更后的用户资金
        BigDecimal userAmountAfter = userAmountBefore.subtract(orderTotalPrice);
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //更新打码量
        userInfo.setNeedOrderAmount(userInfo.getNeedOrderAmount().subtract(orderTotalPrice));
        int updateUser = userInfoMapper.updateUserInfo(userInfo);
        if (updateUser <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        spotTradeOrder.setProductType(productType);
        spotTradeOrder.setOrderCode(CodeUtils.generateOrderCode("S"));
        spotTradeOrder.setUserId(userId);
        spotTradeOrder.setProductCode(productCode);
        spotTradeOrder.setBuyOrderTime(new Date());
        spotTradeOrder.setOrderNum(orderNum);
        spotTradeOrder.setOrderTotalPrice(orderTotalPrice);
        BigDecimal buyFee = orderTotalPrice.multiply(buyFeeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        spotTradeOrder.setOrderFee(buyFee);
        spotTradeOrder.setCurrencyId(currencyId);
        //新增现货交易订单
        int insertSpotTradeOrder = spotTradeOrderMapper.insertSpotTradeOrder(spotTradeOrder);
        if (insertSpotTradeOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录现货交易订单信息
        HttpUtils.getRequestLogParams().put("spotTradeOrder", JSONObject.toJSONString(spotTradeOrder));

        //用户流水记录
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("现货交易下单");
        userBillDetail.setDeSummary("现货交易下单成功");
        userBillDetail.setOrderAmount(orderTotalPrice.negate());
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(spotTradeOrder.getId());
        userBillDetail.setOrderClass(23);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return AjaxResult.success().put("orderInfo",spotTradeOrder);
    }

    /**
     * 卖出现货交易订单
     * @param spotTradeOrderId 现货交易订单id
     * @param doType 平仓类型 0:强制平仓 1:用户平仓
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult sellSpotTradeOrder(Long spotTradeOrderId,Integer doType){
        //日志记录平仓类型
        HttpUtils.getRequestLogParams().put("doType",doType);
        //现货交易订单信息
        SpotTradeOrder spotTradeOrder = spotTradeOrderMapper.selectSpotTradeOrderById(spotTradeOrderId);
        if (spotTradeOrder == null){
            return AjaxResult.error(HintConstants.SYSTEM_BUSY,"获取订单信息异常");
        }
        if (!spotTradeOrder.getOrderStatus().equals(0)){
            return AjaxResult.error(HintConstants.SYSTEM_BUSY,"此订单已平仓");
        }
        ///币种id
        Long currencyId = spotTradeOrder.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取币种信息异常");
        }
        if (!platformCurrency.getStatus().equals(0)){
            throw new LangException(HintConstants.SYSTEM_BUSY,"此币种已禁用");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        //产品类型
        Integer productType = spotTradeOrder.getProductType();
        //用户id
        Long userId = spotTradeOrder.getUserId();
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("userId",userId);
        //如果不是强制平仓
        if (!doType.equals(0)){
            if (!UserApiKeyUtils.getUserId().equals(userId)){
                throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
            }
            //用户信息
            UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
            //验证用户交易状态
            if (!userInfo.getIsLock().equals(0)) {
                throw new LangException("hint_dealErrorAccountLocked","下单失败，用户已被锁定");
            }
            //是否在交易时间
            ProductTradeTimeSetting productTradeTimeSetting = productTradeTimeSettingService.selectProductTradeTimeSettingByDayAndProductType(DateUtils.getDayOfWeek(), productType);
            String am_begin = productTradeTimeSetting.getTransAmBegin();
            String am_end = productTradeTimeSetting.getTransAmEnd();
            String pm_begin = productTradeTimeSetting.getTransPmBegin();
            String pm_end = productTradeTimeSetting.getTransPmEnd();
            boolean am_flag = false;
            boolean pm_flag = false;
            try {
                am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
                pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            if (!am_flag && !pm_flag) {
                throw new LangException("hint_dealErrorOutOfTradingHours","交易失败，不在交易时段内");
            }
        }

        //是否熔断
        String key = "stockFuseWarning"+spotTradeOrder.getProductCode();
        String key2 = "stockFuseFreeze"+spotTradeOrder.getProductCode();
        Object s = redisCache.getCacheObject(key);
        Object s2 = redisCache.getCacheObject(key2);
        if (s != null || s2 != null){
            return AjaxResult.error("hint_20","该产品触发熔断机制，短时间禁止交易");
        }
        //产品代码
        String productCode = spotTradeOrder.getProductCode();
        //卖出费率
        BigDecimal sellFeeRate = BigDecimal.ZERO;
        //股票
        if (productType.equals(1)){
            //股票信息
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null){
                return AjaxResult.error(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCode,false);
            //接口有获取到数据
            if (tickerInfoMap != null){
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                //如果接口调用到数据,则股票已开盘，取实时数据
                if (tickerInfo != null){
                    spotTradeOrder.setSellOrderPrice(new BigDecimal(tickerInfo.getNowPrice()));
                }
            }
            //判断是否取到了行情
            if (spotTradeOrder.getSellOrderPrice() == null){
                return AjaxResult.error("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
            }
            //卖出手续费
            OrderFeeSetting sellFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("stock_spotTrade_sell_fee");
            if (sellFeeVo != null){
                sellFeeRate = sellFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(2)){
            //加密货币信息
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product == null){
                return AjaxResult.error(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
            //接口有获取到数据
            if (tickerInfoMap != null){
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                //如果接口调用到数据,则股票已开盘，取实时数据
                if (tickerInfo != null){
                    spotTradeOrder.setSellOrderPrice(new BigDecimal(tickerInfo.getNowPrice()));
                }
            }
            //判断是否取到了行情
            if (spotTradeOrder.getBuyOrderPrice() == null){
                return AjaxResult.error("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
            }
            //卖出手续费
            OrderFeeSetting sellFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("cryptocurrency_spotTrade_sell_fee");
            if (sellFeeVo != null){
                sellFeeRate = sellFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(3)){
            //期货信息
            FuturesProduct product = futuresProductService.selectFuturesProductByCode(productCode);
            if (product == null){
                return AjaxResult.error(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCode);
            //接口有获取到数据
            if (tickerInfoMap != null){
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                //如果接口调用到数据,则股票已开盘，取实时数据
                if (tickerInfo != null){
                    spotTradeOrder.setSellOrderPrice(new BigDecimal(tickerInfo.getNowPrice()));
                }
            }
            //判断是否取到了行情
            if (spotTradeOrder.getSellOrderPrice() == null){
                return AjaxResult.error("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
            }
            //卖出手续费率
            OrderFeeSetting sellFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("futures_spotTrade_sell_fee");
            if (sellFeeVo != null){
                sellFeeRate = sellFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(4)){
            //外汇信息
            ForexProduct product = forexProductService.selectForexProductByCode(productCode);
            if (product == null){
                return AjaxResult.error(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCode);
            //接口有获取到数据
            if (tickerInfoMap != null){
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                //如果接口调用到数据,则股票已开盘，取实时数据
                if (tickerInfo != null){
                    spotTradeOrder.setSellOrderPrice(new BigDecimal(tickerInfo.getNowPrice()));
                }
            }
            //判断是否取到了行情
            if (spotTradeOrder.getSellOrderPrice() == null){
                return AjaxResult.error("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
            }
            //卖出手续费
            OrderFeeSetting sellFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("forex_spotTrade_sell_fee");
            if (sellFeeVo != null){
                sellFeeRate = sellFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else {
            return AjaxResult.error(HintConstants.SYSTEM_BUSY,"产品信息异常");
        }
        //当前市值
        BigDecimal orderTotalPriceNow = spotTradeOrder.getSellOrderPrice().multiply(spotTradeOrder.getOrderNum()).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //盈亏
        BigDecimal profitAndLose = orderTotalPriceNow.subtract(spotTradeOrder.getOrderTotalPrice());
        //卖出手续费率
        BigDecimal sellFee = orderTotalPriceNow.multiply(sellFeeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        spotTradeOrder.setOrderFee(spotTradeOrder.getOrderFee().add(sellFee));
        spotTradeOrder.setSellOrderTime(new Date());
        spotTradeOrder.setProfitAndLose(profitAndLose);
        spotTradeOrder.setAllProfitAndLose(profitAndLose.subtract(spotTradeOrder.getOrderFee()));
        //变更状态为已卖出
        spotTradeOrder.setOrderStatus(1);
        //更新现货交易订单信息
        int updateSpotTradeOrder = spotTradeOrderMapper.updateSpotTradeOrder(spotTradeOrder);
        if (updateSpotTradeOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录现货交易订单信息
        HttpUtils.getRequestLogParams().put("spotTradeOrder", JSONObject.toJSONString(spotTradeOrder));
        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //变更前的用户资金
        BigDecimal userAmountBefore = userAmount.getAmount();
        //用户余额变更后
        BigDecimal userAmountAfter = userAmountBefore.add(spotTradeOrder.getAllProfitAndLose()).add(spotTradeOrder.getOrderTotalPrice());
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //用户流水记录(现货交易卖出明细)
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("现货交易卖出");
        userBillDetail.setDeSummary("现货交易卖出:" + spotTradeOrder.getProductCode() + "/" + spotTradeOrder.getProductType() + ",占用本金:" + spotTradeOrder.getOrderTotalPrice() + ",总手续费:" + spotTradeOrder.getOrderFee()
                + ",递延费:0,印花税:0,盈亏:" + spotTradeOrder.getProfitAndLose() + ",总盈亏:" + spotTradeOrder.getAllProfitAndLose() + ",交易订单:"+spotTradeOrder.getOrderCode());
        userBillDetail.setOrderAmount(spotTradeOrder.getAllProfitAndLose().add(spotTradeOrder.getOrderTotalPrice()));
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(spotTradeOrder.getId());
        userBillDetail.setOrderClass(24);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return AjaxResult.success().put("orderInfo",spotTradeOrder);
    }
}
