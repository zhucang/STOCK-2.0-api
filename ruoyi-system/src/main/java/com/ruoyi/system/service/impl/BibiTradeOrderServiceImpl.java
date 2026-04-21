package com.ruoyi.system.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.BibiTradeOrderMapper;
import com.ruoyi.system.mapper.ScheduledTaskExceptionLogMapper;
import com.ruoyi.system.mapper.UserApplyPurchaseOrderMapper;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.BuyAndSellUtils;
import com.ruoyi.system.utils.ProductQuoteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 币币交易订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
@Service
public class BibiTradeOrderServiceImpl implements IBibiTradeOrderService 
{
    @Resource
    private BibiTradeOrderMapper bibiTradeOrderMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IProductTradeTimeSettingService productTradeTimeSettingService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private IOrderFeeSettingService orderFeeSettingService;

    @Autowired
    private IStockProductService stockProductService;

    @Autowired
    private ICryptocurrencyProductService cryptocurrencyProductService;

    @Autowired
    private IFuturesProductService futuresProductService;

    @Autowired
    private IForexProductService forexProductService;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Autowired
    private IUserBibiAssetsService userBibiAssetsService;

    @Autowired
    private IBibiTradeOrderService bibiTradeOrderService;

    @Resource
    private ScheduledTaskExceptionLogMapper scheduledTaskExceptionLogMapper;

    @Resource
    private UserApplyPurchaseOrderMapper userApplyPurchaseOrderMapper;

    /**
     * 查询币币交易订单
     * 
     * @param id 币币交易订单主键
     * @return 币币交易订单
     */
    @Override
    public BibiTradeOrder selectBibiTradeOrderById(Long id)
    {
        return bibiTradeOrderMapper.selectBibiTradeOrderById(id);
    }

    /**
     * 查询币币交易订单列表
     * 
     * @param bibiTradeOrder 币币交易订单
     * @return 币币交易订单
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<BibiTradeOrder> selectBibiTradeOrderList(BibiTradeOrder bibiTradeOrder)
    {
        return bibiTradeOrderMapper.selectBibiTradeOrderList(bibiTradeOrder);
    }

    /**
     * 新增币币交易订单
     * 
     * @param bibiTradeOrder 币币交易订单
     * @return 结果
     */
    @Override
    public int insertBibiTradeOrder(BibiTradeOrder bibiTradeOrder)
    {
        bibiTradeOrder.setCreateTime(DateUtils.getNowDate());
        return bibiTradeOrderMapper.insertBibiTradeOrder(bibiTradeOrder);
    }

    /**
     * 修改币币交易订单
     * 
     * @param bibiTradeOrder 币币交易订单
     * @return 结果
     */
    @Override
    public int updateBibiTradeOrder(BibiTradeOrder bibiTradeOrder)
    {
        return bibiTradeOrderMapper.updateBibiTradeOrder(bibiTradeOrder);
    }

    /**
     * 批量删除币币交易订单
     * 
     * @param ids 需要删除的币币交易订单主键
     * @return 结果
     */
    @Override
    public int deleteBibiTradeOrderByIds(Long[] ids)
    {
        return bibiTradeOrderMapper.deleteBibiTradeOrderByIds(ids);
    }

    /**
     * 删除币币交易订单信息
     * 
     * @param id 币币交易订单主键
     * @return 结果
     */
    @Override
    public int deleteBibiTradeOrderById(Long id)
    {
        return bibiTradeOrderMapper.deleteBibiTradeOrderById(id);
    }


    /**
     * 人工正常平仓
     * @param bibiTradeOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int manualSell(BibiTradeOrder bibiTradeOrder){
        //用户id
        Long userId = bibiTradeOrder.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //验证用户交易状态
        if (!userInfo.getIsLock().equals(0)) {
            throw new ServiceException("用户已被锁定");
        }
        //产品类型
        Integer productType = bibiTradeOrder.getProductType();
        //产品代码
        String productCode = bibiTradeOrder.getProductCode();
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
            throw new ServiceException("交易失败，不在交易时段内");
        }
        //币币资产
        UserBibiAssets userBibiAssets = userBibiAssetsService.getUserBibiAssets(userId, productCode, productType);
        //成交量
        BigDecimal orderVolume = bibiTradeOrder.getOrderVolume();
        if (userBibiAssets.getBibiAmount().compareTo(orderVolume) < 0){
            throw new ServiceException("可用币币资产不足");
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
        //卖出手续费率
        BigDecimal sellFeeRate = BigDecimal.ZERO;
        //现价
        BigDecimal nowPrice = null;
        //美股
        if (productType.equals(1)){
            if (currencyId == null){
                currencyId = 1L;
            }
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCode,false);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //卖出手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("stock_bibi_trade_sell_fee");
            if (buyFeeVo != null){
                sellFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(2)){
            //加密货币
            if (currencyId == null){
                currencyId = 2L;
            }
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //卖出手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("cryptocurrency_bibi_trade_sell_fee");
            if (buyFeeVo != null){
                sellFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(3)){
            //期货
            if (currencyId == null){
                currencyId = 7L;
            }
            FuturesProduct product = futuresProductService.selectFuturesProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCode);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //卖出手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("futures_bibi_trade_sell_fee");
            if (buyFeeVo != null){
                sellFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(4)){
            //外汇
            if (currencyId == null){
                currencyId = 4L;
            }
            ForexProduct product = forexProductService.selectForexProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCode);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //卖出手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("forex_bibi_trade_sell_fee");
            if (buyFeeVo != null){
                sellFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else {
            throw new ServiceException("产品类型错误");
        }
        //判断是否取到了行情
        if (nowPrice == null){
            throw new ServiceException("获取该产品行情信息异常，请刷新后重新尝试");
        }
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        if (!platformCurrency.getStatus().equals(0)){
            throw new ServiceException("此币种已禁用");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        //行情价格的1%
        BigDecimal diff = nowPrice.multiply(new BigDecimal(0.01)).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //卖出价格
        BigDecimal productPrice = bibiTradeOrder.getProductPrice();
        //如果卖出价格不在合理范围内，不允许卖出
        if (productPrice.compareTo(nowPrice.subtract(diff)) < 0 || productPrice.compareTo(nowPrice.add(diff)) > 0){
            throw new ServiceException("设置的卖出价格与当前行情相差较大，请重新设置卖出价格");
        }
        nowPrice = productPrice;
        //orderAmount
        BigDecimal orderAmount = orderVolume.multiply(nowPrice).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        if (orderAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("当前成交量价值成交额为0");
        }
        //手续费
        BigDecimal handingFee = orderAmount.multiply(sellFeeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //成交额
        BigDecimal turnoverAmount = orderAmount.subtract(handingFee);
        bibiTradeOrder.setOrderCode(CodeUtils.generateOrderCode("BIBITRADE"));
        bibiTradeOrder.setOrderAmount(orderAmount);
        bibiTradeOrder.setTurnoverAmount(turnoverAmount);
        bibiTradeOrder.setHandingFee(handingFee);
        bibiTradeOrder.setProductPrice(nowPrice);
        bibiTradeOrder.setOrderType(1);
        //默认市价委托
        bibiTradeOrder.setOrderMethod(1);
        //产品价格取行情价
        bibiTradeOrder.setProductPrice(nowPrice);
        //直接成交
        bibiTradeOrder.setOrderStatus(1);
        //加钱
        //用户钱包余额
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //变更前的用户资金
        BigDecimal userAmountBefore = userAmount.getAmount();
        //变更后的用户资金
        BigDecimal userAmountAfter = userAmountBefore.add(orderAmount);
        userAmount.setAmount(userAmountAfter.subtract(handingFee));
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new ServiceException("系统繁忙");
        }
        //插入币币交易卖出流水明细
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("币币交易卖出");
        userBillDetail.setDeSummary("币币交易卖出成功");
        userBillDetail.setOrderAmount(orderAmount);
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(bibiTradeOrder.getId());
        userBillDetail.setOrderClass(68);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //插入币币交易卖出手续费流水明细
        UserBillDetail userBillDetail2 = new UserBillDetail();
        userBillDetail2.setUserId(userId);
        userBillDetail2.setDeType("币币交易卖出手续费扣除");
        userBillDetail2.setDeSummary("币币交易卖出手续费扣除");
        userBillDetail2.setOrderAmount(handingFee.negate());
        userBillDetail2.setOrderTime(new Date());
        userBillDetail2.setAmountBefore(userAmountAfter);
        userBillDetail2.setAmountAfter(userAmountAfter.subtract(handingFee));
        userBillDetail2.setRelateOrderId(bibiTradeOrder.getId());
        userBillDetail2.setOrderClass(70);
        userBillDetail2.setCurrencyId(currencyId);
        int insert2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
        if (insert2 <= 0) {
            throw new ServiceException("系统繁忙");
        }
        bibiTradeOrder.setCreateTime(new Date());
        bibiTradeOrder.setRemark(null);
        bibiTradeOrder.setCurrencyId(currencyId);
        int insertBibiTradeOrder = bibiTradeOrderMapper.insertBibiTradeOrder(bibiTradeOrder);
        if (insertBibiTradeOrder <= 0){
            throw new ServiceException("系统繁忙");
        }
        //更新币币资产
        userBibiAssets.setBibiAmount(userBibiAssets.getBibiAmount().subtract(orderVolume));
        userBibiAssets.setSellAmountAll(userBibiAssets.getSellAmountAll().add(orderAmount));
        userBibiAssets.setBuyAndSellAmountDifference(userBibiAssets.getBuyAmountAll().subtract(userBibiAssets.getSellAmountAll()));
        int updateUserBibiAssets = userBibiAssetsService.updateUserBibiAssets(userBibiAssets);
        if (updateUserBibiAssets <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }





    /**
     * 用户币币交易买入
     * @param bibiTradeOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int buy(BibiTradeOrder bibiTradeOrder){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //验证用户交易状态
        if (!userInfo.getIsLock().equals(0)) {
            throw new LangException("hint_dealErrorAccountLocked","下单失败，用户已被锁定");
        }
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

        //产品类型
        Integer productType = bibiTradeOrder.getProductType();
        //产品代码
        String productCode = bibiTradeOrder.getProductCode();
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
        //买入金额
        BigDecimal orderAmount = bibiTradeOrder.getOrderAmount();
        //扣钱
        //用户钱包余额
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //变更前的用户资金
        BigDecimal userAmountBefore = userAmount.getAmount();
        if (orderAmount.compareTo(userAmountBefore) > 0){
            throw new LangException("hint_17","此币种可用资金不足");
        }
        //变更后的用户资金
        BigDecimal userAmountAfter = userAmountBefore.subtract(orderAmount);
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //买入手续费率
        BigDecimal buyFeeRate = BigDecimal.ZERO;
        //现价
        BigDecimal nowPrice = null;
        //委托方式
        Integer orderMethod = bibiTradeOrder.getOrderMethod();
        //如果是限价委托
        if (orderMethod.equals(0)){
            nowPrice = bibiTradeOrder.getProductPrice();
        }
        //美股
        if (productType.equals(1)){
            if (currencyId == null){
                currencyId = 1L;
            }
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCode,false);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //买入手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("stock_bibi_trade_buy_fee");
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
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //买入手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("cryptocurrency_bibi_trade_buy_fee");
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
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCode);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //买入手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("futures_bibi_trade_buy_fee");
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
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCode);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //买入手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("forex_bibi_trade_buy_fee");
            if (buyFeeVo != null){
                buyFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY,"产品类型错误");
        }
        //判断是否取到了行情
        if (nowPrice == null){
            throw new LangException("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
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
        //手续费
        BigDecimal handingFee = orderAmount.multiply(buyFeeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //成交额
        BigDecimal turnoverAmount = orderAmount.subtract(handingFee);
        //成交量
        BigDecimal orderVolume = turnoverAmount.divide(nowPrice,Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        if (orderVolume.compareTo(BigDecimal.ZERO) <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"当前金额可购买成交量为0");
        }
        bibiTradeOrder.setOrderCode(CodeUtils.generateOrderCode("BIBITRADE"));
        bibiTradeOrder.setTurnoverAmount(turnoverAmount);
        bibiTradeOrder.setOrderVolume(orderVolume);
        bibiTradeOrder.setHandingFee(handingFee);
        bibiTradeOrder.setOrderType(0);
        //如果是限价委托
        if (orderMethod.equals(0)){
            //委托中
            bibiTradeOrder.setOrderStatus(0);
        }else if (orderMethod.equals(1)){
            //产品价格取行情价
            bibiTradeOrder.setProductPrice(nowPrice);
            //直接成交
            bibiTradeOrder.setOrderStatus(1);
            //更新币币资产
            UserBibiAssets userBibiAssets = userBibiAssetsService.getUserBibiAssets(userId, productCode, productType);
            userBibiAssets.setBibiAmount(userBibiAssets.getBibiAmount().add(orderVolume));
            userBibiAssets.setBuyAmountAll(userBibiAssets.getBuyAmountAll().add(turnoverAmount));
            userBibiAssets.setBuyAndSellAmountDifference(userBibiAssets.getBuyAmountAll().subtract(userBibiAssets.getSellAmountAll()));
            int updateUserBibiAssets = userBibiAssetsService.updateUserBibiAssets(userBibiAssets);
            if (updateUserBibiAssets <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY, "订单方式错误");
        }
        bibiTradeOrder.setCreateTime(new Date());
        bibiTradeOrder.setRemark(null);
        bibiTradeOrder.setCurrencyId(currencyId);
        int insertBibiTradeOrder = bibiTradeOrderMapper.insertBibiTradeOrder(bibiTradeOrder);
        if (insertBibiTradeOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //插入币币交易买入流水明细
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("币币交易买入");
        userBillDetail.setDeSummary("币币交易买入成功");
        userBillDetail.setOrderAmount(turnoverAmount.negate());
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountBefore.subtract(turnoverAmount));
        userBillDetail.setRelateOrderId(bibiTradeOrder.getId());
        userBillDetail.setOrderClass(67);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //插入币币交易买入手续费流水明细
        UserBillDetail userBillDetail2 = new UserBillDetail();
        userBillDetail2.setUserId(userId);
        userBillDetail2.setDeType("币币交易买入手续费扣除");
        userBillDetail2.setDeSummary("币币交易买入手续费扣除");
        userBillDetail2.setOrderAmount(handingFee.negate());
        userBillDetail2.setOrderTime(new Date());
        userBillDetail2.setAmountBefore(userAmountAfter.add(handingFee));
        userBillDetail2.setAmountAfter(userAmountAfter);
        userBillDetail2.setRelateOrderId(bibiTradeOrder.getId());
        userBillDetail2.setOrderClass(69);
        userBillDetail2.setCurrencyId(currencyId);
        int insert2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
        if (insert2 <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }

    /**
     * 用户币币交易卖出
     * @param bibiTradeOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sell(BibiTradeOrder bibiTradeOrder){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //验证用户交易状态
        if (!userInfo.getIsLock().equals(0)) {
            throw new LangException("hint_dealErrorAccountLocked","下单失败，用户已被锁定");
        }
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
        //产品类型
        Integer productType = bibiTradeOrder.getProductType();
        //产品代码
        String productCode = bibiTradeOrder.getProductCode();
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
        //币币资产
        UserBibiAssets userBibiAssets = userBibiAssetsService.getUserBibiAssets(userId, productCode, productType);
        //成交量
        BigDecimal orderVolume = bibiTradeOrder.getOrderVolume();
        if (userBibiAssets.getBibiAmount().compareTo(orderVolume) < 0){
            throw new LangException("hint_83","可用币币资产不足");
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
        //卖出手续费率
        BigDecimal sellFeeRate = BigDecimal.ZERO;
        //现价
        BigDecimal nowPrice = null;
        //委托方式
        Integer orderMethod = bibiTradeOrder.getOrderMethod();
        //如果是限价委托
        if (orderMethod.equals(0)){
            nowPrice = bibiTradeOrder.getProductPrice();
        }
        //美股
        if (productType.equals(1)){
            if (currencyId == null){
                currencyId = 1L;
            }
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCode,false);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //卖出手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("stock_bibi_trade_sell_fee");
            if (buyFeeVo != null){
                sellFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(2)){
            //加密货币
            if (currencyId == null){
                currencyId = 2L;
            }
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //卖出手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("cryptocurrency_bibi_trade_sell_fee");
            if (buyFeeVo != null){
                sellFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(3)){
            //期货
            if (currencyId == null){
                currencyId = 7L;
            }
            FuturesProduct product = futuresProductService.selectFuturesProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCode);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //卖出手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("futures_bibi_trade_sell_fee");
            if (buyFeeVo != null){
                sellFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(4)){
            //外汇
            if (currencyId == null){
                currencyId = 4L;
            }
            ForexProduct product = forexProductService.selectForexProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (nowPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCode);
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //卖出手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("forex_bibi_trade_sell_fee");
            if (buyFeeVo != null){
                sellFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY,"产品类型错误");
        }
        //判断是否取到了行情
        if (nowPrice == null){
            throw new LangException("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
        }
        //orderAmount
        BigDecimal orderAmount = orderVolume.multiply(nowPrice).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        if (orderAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"当前成交量价值成交额为0");
        }
        //手续费
        BigDecimal handingFee = orderAmount.multiply(sellFeeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //成交额
        BigDecimal turnoverAmount = orderAmount.subtract(handingFee);
        bibiTradeOrder.setOrderCode(CodeUtils.generateOrderCode("BIBITRADE"));
        bibiTradeOrder.setOrderAmount(orderAmount);
        bibiTradeOrder.setTurnoverAmount(turnoverAmount);
        bibiTradeOrder.setHandingFee(handingFee);
        bibiTradeOrder.setProductPrice(nowPrice);
        bibiTradeOrder.setOrderType(1);
        //如果是限价委托
        if (orderMethod.equals(0)){
            //委托中
            bibiTradeOrder.setOrderStatus(0);
        }else if (orderMethod.equals(1)){
            //产品价格取行情价
            bibiTradeOrder.setProductPrice(nowPrice);
            //直接成交
            bibiTradeOrder.setOrderStatus(1);
            //加钱
            //用户钱包余额
            UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
            //变更前的用户资金
            BigDecimal userAmountBefore = userAmount.getAmount();
            //变更后的用户资金
            BigDecimal userAmountAfter = userAmountBefore.add(orderAmount);
            userAmount.setAmount(userAmountAfter.subtract(handingFee));
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
            //插入币币交易卖出流水明细
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("币币交易卖出");
            userBillDetail.setDeSummary("币币交易卖出成功");
            userBillDetail.setOrderAmount(orderAmount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(bibiTradeOrder.getId());
            userBillDetail.setOrderClass(68);
            userBillDetail.setCurrencyId(currencyId);
            int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insert <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
            //插入币币交易卖出手续费流水明细
            UserBillDetail userBillDetail2 = new UserBillDetail();
            userBillDetail2.setUserId(userId);
            userBillDetail2.setDeType("币币交易卖出手续费扣除");
            userBillDetail2.setDeSummary("币币交易卖出手续费扣除");
            userBillDetail2.setOrderAmount(handingFee.negate());
            userBillDetail2.setOrderTime(new Date());
            userBillDetail2.setAmountBefore(userAmountAfter);
            userBillDetail2.setAmountAfter(userAmountAfter.subtract(handingFee));
            userBillDetail2.setRelateOrderId(bibiTradeOrder.getId());
            userBillDetail2.setOrderClass(70);
            userBillDetail2.setCurrencyId(currencyId);
            int insert2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
            if (insert2 <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY, "订单方式错误");
        }
        bibiTradeOrder.setCreateTime(new Date());
        bibiTradeOrder.setRemark(null);
        bibiTradeOrder.setCurrencyId(currencyId);
        int insertBibiTradeOrder = bibiTradeOrderMapper.insertBibiTradeOrder(bibiTradeOrder);
        if (insertBibiTradeOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //更新币币资产
        userBibiAssets.setBibiAmount(userBibiAssets.getBibiAmount().subtract(orderVolume));
        userBibiAssets.setSellAmountAll(userBibiAssets.getSellAmountAll().add(orderAmount));
        userBibiAssets.setBuyAndSellAmountDifference(userBibiAssets.getBuyAmountAll().subtract(userBibiAssets.getSellAmountAll()));
        int updateUserBibiAssets = userBibiAssetsService.updateUserBibiAssets(userBibiAssets);
        if (updateUserBibiAssets <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }

    /**
     * 撤销委托
     * @param bibiTradeOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancel(Long bibiTradeOrderId){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //币币交易订单
        BibiTradeOrder bibiTradeOrder = bibiTradeOrderMapper.selectBibiTradeOrderById(bibiTradeOrderId);
        if (bibiTradeOrder == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取币币交易订单信息异常");
        }
        if (!bibiTradeOrder.getOrderStatus().equals(0)){
            throw new LangException("hint_82","订单状态非委托中，无法撤销");
        }
        //订单类型
        Integer orderType = bibiTradeOrder.getOrderType();
        //币种id
        Long currencyId = bibiTradeOrder.getCurrencyId();
        //撤销买入订单
        if (orderType.equals(0)){
            //退钱
            //订单金额
            BigDecimal orderAmount = bibiTradeOrder.getOrderAmount();
            //用户钱包余额
            UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
            //变更前的用户资金
            BigDecimal userAmountBefore = userAmount.getAmount();
            //变更后的用户资金
            BigDecimal userAmountAfter = userAmountBefore.add(orderAmount);
            userAmount.setAmount(userAmountAfter);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
            //插入币币交易撤销买入订单流水明细
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("币币交易买入订单撤销返还");
            userBillDetail.setDeSummary("币币交易买入订单撤销返还成功");
            userBillDetail.setOrderAmount(orderAmount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(bibiTradeOrder.getId());
            userBillDetail.setOrderClass(71);
            userBillDetail.setCurrencyId(currencyId);
            int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insert <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
        }else if (orderType.equals(1)){
            //撤销卖出订单
            //返还订单数量到币币资产
            //更新币币资产
            UserBibiAssets userBibiAssets = userBibiAssetsService.getUserBibiAssets(userId, bibiTradeOrder.getProductCode(), bibiTradeOrder.getProductType());
            userBibiAssets.setBibiAmount(userBibiAssets.getBibiAmount().add(bibiTradeOrder.getOrderVolume()));
            userBibiAssets.setSellAmountAll(userBibiAssets.getSellAmountAll().subtract(bibiTradeOrder.getTurnoverAmount()));
            userBibiAssets.setBuyAndSellAmountDifference(userBibiAssets.getBuyAmountAll().subtract(userBibiAssets.getSellAmountAll()));
            int updateUserBibiAssets = userBibiAssetsService.updateUserBibiAssets(userBibiAssets);
            if (updateUserBibiAssets <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY,"订单类型错误");
        }
        //设置状态为撤销
        bibiTradeOrder.setOrderStatus(2);
        //更新币币交易订单信息
        int updateBibiTradeOrder = bibiTradeOrderMapper.updateBibiTradeOrder(bibiTradeOrder);
        if (updateBibiTradeOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }

    /**
     * 委托订单自动通过定时任务
     */
    @Override
    public void bibiOrderAutoDealTask(){
        //获取委托中的订单
        BibiTradeOrder search = new BibiTradeOrder();
        search.setOrderStatus(0);
        List<BibiTradeOrder> bibiTradeOrders = bibiTradeOrderMapper.selectBibiTradeOrderList(search);
        if (bibiTradeOrders.size() == 0){
            return;
        }
        //行情map
        Map<String, TickerInfo> tickerInfoMap = new HashMap<>();
        //股票产品信息
        List<BibiTradeOrder> stock = bibiTradeOrders.stream().filter(a -> a.getProductType().equals(1)).collect(Collectors.toList());
        if (stock.size() > 0){
            //codes
            String productCodes = stock.stream().map(BibiTradeOrder::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getStockQuote(productCodes,false);
            tickerInfoMap.putAll(map);
        }
        //加密货币产品信息
        List<BibiTradeOrder> cryptocurrency = bibiTradeOrders.stream().filter(a -> a.getProductType().equals(2)).collect(Collectors.toList());
        if (cryptocurrency.size() > 0){
            //codes
            String productCodes = cryptocurrency.stream().map(BibiTradeOrder::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
            tickerInfoMap.putAll(map);
        }
        //期货产品信息
        List<BibiTradeOrder> futures = bibiTradeOrders.stream().filter(a -> a.getProductType().equals(3)).collect(Collectors.toList());
        if (futures.size() > 0){
            //codes
            String productCodes = futures.stream().map(BibiTradeOrder::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getFuturesQuote(productCodes);
            tickerInfoMap.putAll(map);
        }
        //外汇产品信息
        List<BibiTradeOrder> forex = bibiTradeOrders.stream().filter(a -> a.getProductType().equals(4)).collect(Collectors.toList());
        if (forex.size() > 0){
            //codes
            String productCodes = forex.stream().map(BibiTradeOrder::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getForexQuote(productCodes);
            tickerInfoMap.putAll(map);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            //自动通过委托订单
            for (int i = 0; i < bibiTradeOrders.size(); i++) {
                BibiTradeOrder bibiTradeOrder = bibiTradeOrders.get(i);
                executorService.execute(()->{
                    try {
                        bibiTradeOrderService.doBibiOrderAutoDealTask(bibiTradeOrder,tickerInfoMap);
                    } catch (Exception e) {
                        //记录异常日志
                        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
                        scheduledTaskExceptionLog.setJobName("币币交易委托订单自动通过定时任务");
                        scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
                        scheduledTaskExceptionLog.setCreateTime(new Date());
                        scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
                        scheduledTaskExceptionLog.setRelateInfo("id:"+bibiTradeOrder.getId());
                        scheduledTaskExceptionLog.setType(18);
                        scheduledTaskExceptionLogMapper.insertScheduledTaskExceptionLog(scheduledTaskExceptionLog);
                        throw new RuntimeException(e);
                    }
                });
            }
        }catch (Exception e){
            throw new RuntimeException("创建定时任务异常");
        }finally {
            executorService.shutdown();
        }
    }

    /**
     * 委托订单自动通过定时任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doBibiOrderAutoDealTask(BibiTradeOrder bibiTradeOrder,Map<String,TickerInfo> tickerInfoMap){
        if (!bibiTradeOrder.getOrderStatus().equals(0)){
            return;
        }
        //产品类型
        Integer productType = bibiTradeOrder.getProductType();
        //产品代码
        String productCode = bibiTradeOrder.getProductCode();
        //订单类型 0：买入 1：卖出
        Integer orderType = bibiTradeOrder.getOrderType();
        //行情信息
        TickerInfo tickerInfo = tickerInfoMap.get(productCode);
        if (tickerInfo != null){
            //产品价格
            BigDecimal productPrice = bibiTradeOrder.getProductPrice();
            //现价
            BigDecimal nowPrice =  new BigDecimal(tickerInfo.getNowPrice());
            //如果是买入
            if (orderType.equals(0)){
                //如果限制的价格低于市场价格，买不到
                if (productPrice.compareTo(nowPrice) < 0){
                    return;
                }
            }else {
                //如果是卖出
                //如果限制的价格高于市场价，则无人购买
                if (productPrice.compareTo(nowPrice) > 0){
                    return;
                }
            }
        }else {
            return;
        }
        //用户id
        Long userId = bibiTradeOrder.getUserId();
        //币种id
        Long currencyId = bibiTradeOrder.getCurrencyId();
        //如果是买入
        if (orderType.equals(0)){
            //订单状态变为成交
            bibiTradeOrder.setOrderStatus(1);
            //更新币币资产
            UserBibiAssets userBibiAssets = userBibiAssetsService.getUserBibiAssets(userId, productCode, productType);
            userBibiAssets.setBibiAmount(userBibiAssets.getBibiAmount().add(bibiTradeOrder.getOrderVolume()));
            userBibiAssets.setBuyAmountAll(userBibiAssets.getBuyAmountAll().add(bibiTradeOrder.getTurnoverAmount()));
            userBibiAssets.setBuyAndSellAmountDifference(userBibiAssets.getBuyAmountAll().subtract(userBibiAssets.getSellAmountAll()));
            int updateUserBibiAssets = userBibiAssetsService.updateUserBibiAssets(userBibiAssets);
            if (updateUserBibiAssets <= 0){
                throw new ServiceException("系统繁忙");
            }
        }else {
            //如果是卖出
            //订单状态变为成交
            bibiTradeOrder.setOrderStatus(1);
            //加钱
            //用户钱包余额
            UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
            //变更前的用户资金
            BigDecimal userAmountBefore = userAmount.getAmount();
            //订单金额
            BigDecimal orderAmount = bibiTradeOrder.getOrderAmount();
            //手续费
            BigDecimal handingFee = bibiTradeOrder.getHandingFee();
            //变更后的用户资金
            BigDecimal userAmountAfter = userAmountBefore.add(orderAmount);
            userAmount.setAmount(userAmountAfter.subtract(handingFee));
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
            //插入币币交易卖出流水明细
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("币币交易卖出");
            userBillDetail.setDeSummary("币币交易卖出成功");
            userBillDetail.setOrderAmount(orderAmount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(bibiTradeOrder.getId());
            userBillDetail.setOrderClass(68);
            userBillDetail.setCurrencyId(currencyId);
            int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insert <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
            //插入币币交易卖出手续费流水明细
            UserBillDetail userBillDetail2 = new UserBillDetail();
            userBillDetail2.setUserId(userId);
            userBillDetail2.setDeType("币币交易卖出手续费扣除");
            userBillDetail2.setDeSummary("币币交易卖出手续费扣除");
            userBillDetail2.setOrderAmount(handingFee.negate());
            userBillDetail2.setOrderTime(new Date());
            userBillDetail2.setAmountBefore(userAmountAfter);
            userBillDetail2.setAmountAfter(userAmountAfter.subtract(handingFee));
            userBillDetail2.setRelateOrderId(bibiTradeOrder.getId());
            userBillDetail2.setOrderClass(70);
            userBillDetail2.setCurrencyId(currencyId);
            int insert2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
            if (insert2 <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
        }
        //更新币币交易订单信息
        int updateBibiTradeOrder = bibiTradeOrderMapper.updateBibiTradeOrder(bibiTradeOrder);
        if (updateBibiTradeOrder <= 0){
            throw new ServiceException("系统繁忙");
        }
    }
}
