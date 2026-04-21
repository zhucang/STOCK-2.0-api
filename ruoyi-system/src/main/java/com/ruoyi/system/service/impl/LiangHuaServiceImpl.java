package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.UserApiKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class LiangHuaServiceImpl implements ILiangHuaService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Resource
    private ProductTradeTimeSettingMapper productTradeTimeSettingMapper;

    @Autowired
    private IProductTradeTimeSettingService productTradeTimeSettingService;

    @Resource
    private CryptocurrencyProductMapper cryptocurrencyProductMapper;

    @Resource
    private ProductSettingMapper productSettingMapper;

    @Autowired
    private IUserAmountService userAmountService;

    @Resource
    private OrderFeeSettingMapper orderFeeSettingMapper;

    @Resource
    private UserCryptocurrencyPositionMapper userCryptocurrencyPositionMapper;

    @Resource
    private UserBillDetailMapper userBillDetailMapper;

    @Autowired
    private IStockProductService stockProductService;

    @Autowired
    private ICryptocurrencyProductService cryptocurrencyProductService;

    @Autowired
    private IFuturesProductService futuresProductService;

    @Autowired
    private IForexProductService forexProductService;

    @Autowired
    private IOrderFeeSettingService orderFeeSettingService;

    @Resource
    private SpotTradeOrderMapper spotTradeOrderMapper;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    /**
     * 加密货币合约机器人订单录入
     * @return
     */
    @Override
    public int robotUserCryptocurrencyPosition(UserCryptocurrencyPosition position){
        //用户id
        Long userId = UserApiKeyUtils.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("userId",userId);
        if (userInfo == null || !userInfo.getIsDel().equals(0)){
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
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
        //产品代码
        String productCode = position.getProductCode();
        //杠杆倍数
        Integer orderLever = position.getOrderLever();
        //交易方向
        Integer orderDirection = position.getOrderDirection();
        //止盈线
        BigDecimal stopProfitPrice = position.getStopProfitPrice();
        //止损线
        BigDecimal stopLossPrice = position.getStopLossPrice();
        //币种id
        Long currencyId = 2L;
        //统一交易币种开关
        Integer switchStatusById56 = switchSetService.selectSwitchStatusById(56L);
        //如果统一交易币种开关开启
        if (switchStatusById56.equals(0)){
            try{
                currencyId = CacheUtil.getOtherValueByKey("default_trade_currency_id", Long.class);
            }catch (Exception e){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取平台默认交易币种异常");
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
//        //产品交易时间
//        ProductTradeTimeSetting productTradeTimeSetting = productTradeTimeSettingMapper.selectProductTradeTimeSettingByDayAndProductType(DateUtils.getDayOfWeek(), 2);
//        String am_begin = productTradeTimeSetting.getTransAmBegin();
//        String am_end = productTradeTimeSetting.getTransAmEnd();
//        String pm_begin = productTradeTimeSetting.getTransPmBegin();
//        String pm_end = productTradeTimeSetting.getTransPmEnd();
//        boolean am_flag = false;
//        boolean pm_flag = false;
//        try {
//            am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
//            pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
//        }catch (Exception e){
//            throw new RuntimeException(e);
//        }
//        if (!am_flag && !pm_flag) {
//            throw new LangException("hint_dealErrorOutOfTradingHours","交易失败，不在交易时段内");
//        }

        //产品信息
        CryptocurrencyProduct product = cryptocurrencyProductMapper.selectCryptocurrencyProductByCode(productCode);
        if (product == null) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
        }
        if (!product.getIsLock().equals(0)) {
            throw new LangException("hint_8","该产品已锁定，不能进行交易");
        }

        //现价
        BigDecimal nowPrice = position.getBuyOrderPrice();

        //判断设置的止盈线与止损线是否正确
        if (stopProfitPrice != null){
            if (orderDirection.equals(0)){
                if (stopProfitPrice.compareTo(nowPrice) <= 0){
                    throw new LangException("hint_36","请正确设置止盈线");
                }
            }else {
                if (stopProfitPrice.compareTo(nowPrice) >= 0){
                    throw new LangException("hint_36","请正确设置止盈线");
                }
            }
        }
        if (stopLossPrice != null){
            if (orderDirection.equals(0)){
                if (stopLossPrice.compareTo(nowPrice) >= 0){
                    throw new LangException("hint_37","请正确设置止损线");
                }
            }else {
                if (stopLossPrice.compareTo(nowPrice) <= 0){
                    throw new LangException("hint_37","请正确设置止损线");
                }
            }
        }

        //产品交易设置
        ProductSetting productSetting = productSettingMapper.selectProductSettingByProductType(2);
        if (productSetting == null) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品交易设置异常");
        }
        //订单数量
        BigDecimal orderNum = position.getOrderNum();
        //保证金
        BigDecimal marginAmount = null;
        try {
            marginAmount = new BigDecimal(String.valueOf(position.getParams().get("marginAmount")));
        }catch (Exception e){

        }
        //按保证金成本下单
        if (marginAmount != null){
            //订单数量
            orderNum = marginAmount.multiply(new BigDecimal(orderLever)).divide(nowPrice, Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        }
        //允许最小下单量
        BigDecimal buyMinNum = productSetting.getBuyMinNum();
        if (orderNum.compareTo(buyMinNum) < 0) {
            ArrayList<Object> list = new ArrayList<>();
            list.add(buyMinNum);
            throw new LangException("hint_9",list,"下单失败，购买数量小于" + buyMinNum + "股");
        }
        //允许最大下单量
        BigDecimal buyMaxNum = productSetting.getBuyMaxNum();
        if (orderNum.compareTo(buyMaxNum) > 0) {
            ArrayList<Object> list = new ArrayList<>();
            list.add(buyMaxNum);
            throw new LangException("hint_10",list,"下单失败，购买数量大于" + buyMaxNum + "股");
        }

        //下单时市值
        BigDecimal orderTotalPrice;
        //按成本下单
        if (marginAmount != null){
            //下单时市值
            orderTotalPrice = marginAmount.multiply(new BigDecimal(orderLever));
        }else {
            //按数量下单
            orderTotalPrice = nowPrice.multiply(orderNum);
            //支付金额(保证金)
            marginAmount = orderTotalPrice.divide(new BigDecimal(orderLever),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        }
        //日志记录保证金
        HttpUtils.getRequestLogParams().put("marginAmount",marginAmount);
        //允许最小下单金额
        BigDecimal buyMinAmt = productSetting.getBuyMinAmt();
        if (marginAmount.compareTo(buyMinAmt) < 0) {
            ArrayList<Object> list = new ArrayList<>();
            list.add(buyMinAmt);
            throw new LangException("hint_11",list,"下单失败，购买金额小于" + buyMinAmt);
        }

        //钱包余额信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //余额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //允许使用资金的最大百分比
        BigDecimal buyMaxAmtPercent = productSetting.getBuyMaxAmtPercent();
        //最大可用金额
        BigDecimal maxBuyAmt = userAmountBefore.multiply(buyMaxAmtPercent);
        if (marginAmount.compareTo(maxBuyAmt) > 0) {
            ArrayList<Object> list = new ArrayList<>();
            //百分比
            BigDecimal percent = buyMaxAmtPercent.multiply(new BigDecimal(100));
            list.add(percent);
            throw new LangException("hint_12",list,"下单失败，不能超过可用资金的" + percent + "%");
        }

        //下单手续费
        //买入费率
        BigDecimal buyFeeRate = BigDecimal.ZERO;
        OrderFeeSetting buyFeeVo = orderFeeSettingMapper.selectOrderFeeSettingByKey("cryptocurrency_contract_buy_fee");
        if (buyFeeVo != null){
            buyFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
        }
        //手续费
        BigDecimal buyFee = orderTotalPrice.multiply(buyFeeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);

        //余额变更后
        BigDecimal userAmountAfter = userAmountBefore.subtract(marginAmount);

        //更新打码量
        userInfo.setNeedOrderAmount(userInfo.getNeedOrderAmount().subtract(marginAmount));
        int updateUser = userInfoMapper.updateUserInfo(userInfo);
        if (updateUser <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //订单信息
        position.setOrderCode(CodeUtils.generateOrderCode("CRYPTO"));
        position.setUserId(userId);
        position.setProductName(product.getProductName());
        position.setOrderTotalPrice(orderTotalPrice);
        position.setOrderFee(buyFee);
        position.setOrderYhsFee(BigDecimal.ZERO);
        position.setProfitAndLose(BigDecimal.ZERO);
        position.setAllProfitAndLose(BigDecimal.ZERO.subtract(buyFee));
        position.setIsLock(0);
        position.setLockMsg(null);
        position.setCurrencyId(currencyId);
        position.setStopProfitPrice(stopProfitPrice);
        position.setStopLossPrice(stopLossPrice);
        position.setOrderStatus(0);
        int insertUserStockPosition = userCryptocurrencyPositionMapper.insertUserCryptocurrencyPosition(position);
        if (insertUserStockPosition <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录持仓订单信息
        HttpUtils.getRequestLogParams().put("position", JSONObject.toJSONString(position));

        //账户明细
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("加密货币合约交易下单");
        userBillDetail.setDeSummary("加密货币合约交易下单成功");
        userBillDetail.setOrderAmount(marginAmount.negate());
        userBillDetail.setOrderTime(position.getBuyOrderTime());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(position.getId());
        userBillDetail.setOrderClass(6);
        userBillDetail.setCurrencyId(currencyId);
        int count = userBillDetailMapper.insertUserBillDetail(userBillDetail);
        if (count <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }


        //卖出时市值
        BigDecimal allSellAmt = position.getSellOrderPrice().multiply(orderNum);
        //浮动盈亏
        BigDecimal profitAndLose = orderDirection.equals(0) ? allSellAmt.subtract(orderTotalPrice) : orderTotalPrice.subtract(allSellAmt);
        //卖出手续费率
        BigDecimal sellFeeRate = BigDecimal.ZERO;
        OrderFeeSetting sellFeeVo = orderFeeSettingMapper.selectOrderFeeSettingByKey("cryptocurrency_contract_sell_fee");
        if (sellFeeVo != null){
            sellFeeRate = sellFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
        }
        //卖出手续费
        BigDecimal sellFee = marginAmount.multiply(sellFeeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //买入卖出手续费
        BigDecimal orderFee = buyFee.add(sellFee);
        //总盈亏
        BigDecimal allProfitAndLose = profitAndLose.subtract(orderFee);
        //余额变更前
        userAmountBefore = userAmountAfter;
        //修改用户可用余额=当前可用余额+总盈亏+保证金
        //余额变更后
        userAmountAfter = userAmountBefore.add(allProfitAndLose).add(marginAmount);

        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //账户明细
        UserBillDetail userBillDetail2 = new UserBillDetail();
        userBillDetail2.setUserId(userId);
        userBillDetail2.setDeType("加密货币合约交易平仓");
        userBillDetail2.setDeSummary("卖出加密货币:"+productCode+"/"+2+",占用本金:"+marginAmount+",总手续费:"+orderFee+",盈亏:"+profitAndLose+",总盈亏:"+allProfitAndLose+",交易订单:"+position.getOrderCode());
        userBillDetail2.setRemark(productCode+"/"+2+"/"+marginAmount+"/"+orderFee+"/"+profitAndLose+"/"+allProfitAndLose+"/"+position.getOrderCode());
        userBillDetail2.setOrderAmount(allProfitAndLose.add(marginAmount));
        userBillDetail2.setOrderTime(position.getSellOrderTime());
        userBillDetail2.setAmountBefore(userAmountBefore);
        userBillDetail2.setAmountAfter(userAmountAfter);
        userBillDetail2.setRelateOrderId(position.getId());
        userBillDetail2.setOrderClass(7);
        userBillDetail2.setCurrencyId(currencyId);
        int count2 = userBillDetailMapper.insertUserBillDetail(userBillDetail2);
        if (count2 <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //订单信息
        position.setOrderFee(orderFee);
        position.setProfitAndLose(profitAndLose);
        position.setAllProfitAndLose(allProfitAndLose);
        position.setOrderStatus(1);
        int updateUserCryptocurrencyPosition = userCryptocurrencyPositionMapper.updateUserCryptocurrencyPosition(position);
        if (updateUserCryptocurrencyPosition <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录持仓订单信息
        HttpUtils.getRequestLogParams().put("position", JSONObject.toJSONString(position));
        return 1;
    }

    /**
     * 现货交易机器人订单录入
     * @param spotTradeOrder
     * @return
     */
    @Override
    public int robotSpotTradeOrder(SpotTradeOrder spotTradeOrder){
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

//        //是否在交易时间
//        ProductTradeTimeSetting productTradeTimeSetting = productTradeTimeSettingService.selectProductTradeTimeSettingByDayAndProductType(DateUtils.getDayOfWeek(), productType);
//        String am_begin = productTradeTimeSetting.getTransAmBegin();
//        String am_end = productTradeTimeSetting.getTransAmEnd();
//        String pm_begin = productTradeTimeSetting.getTransPmBegin();
//        String pm_end = productTradeTimeSetting.getTransPmEnd();
//        boolean am_flag = false;
//        boolean pm_flag = false;
//        try {
//            am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
//            pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
//        }catch (Exception e){
//            throw new RuntimeException(e);
//        }
//        if (!am_flag && !pm_flag) {
//            throw new LangException("hint_dealErrorOutOfTradingHours","交易失败，不在交易时段内");
//        }
        //币种id
        Long currencyId = null;
        //统一交易币种开关
        Integer switchStatus56L = switchSetService.selectSwitchStatusById(56L);
        //如果统一交易币种开关开启
        if (switchStatus56L.equals(0)){
            try{
                currencyId = CacheUtil.getOtherValueByKey("default_trade_currency_id", Long.class);
            }catch (Exception e){
                throw new LangException(HintConstants.SYSTEM_BUSY, "获取平台默认交易币种异常");
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
        //产品类型
        Integer productType = spotTradeOrder.getProductType();
        //产品代码
        String productCode = spotTradeOrder.getProductCode();
        //买入手续费率
        BigDecimal buyFeeRate = BigDecimal.ZERO;
        //美股
        if (productType.equals(1)){
            if (currencyId == null){
                currencyId = 1L;
            }
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
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
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
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
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
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
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            //买入手续费率
            OrderFeeSetting buyFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("forex_spotTrade_buy_fee");
            if (buyFeeVo != null){
                buyFeeRate = buyFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY,"产品类型错误");
        }
        //订单数量
        BigDecimal orderNum = spotTradeOrder.getOrderNum();
        //订单总金额
        BigDecimal orderTotalPrice = spotTradeOrder.getBuyOrderPrice().multiply(orderNum).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);

        //用户钱包余额
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //变更前的用户资金
        BigDecimal userAmountBefore = userAmount.getAmount();
        if (orderTotalPrice.compareTo(userAmountBefore) > 0){
            throw new LangException("hint_17","此币种可用资金不足");
        }

        //变更后的用户资金
        BigDecimal userAmountAfter = userAmountBefore.subtract(orderTotalPrice);
        //更新打码量
        userInfo.setNeedOrderAmount(userInfo.getNeedOrderAmount().subtract(orderTotalPrice));
        int updateUser = userInfoMapper.updateUserInfo(userInfo);
        if (updateUser <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        spotTradeOrder.setOrderCode(CodeUtils.generateOrderCode("S"));
        spotTradeOrder.setUserId(userId);
        spotTradeOrder.setOrderTotalPrice(orderTotalPrice);
        BigDecimal buyFee = orderTotalPrice.multiply(buyFeeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        spotTradeOrder.setOrderFee(buyFee);
        spotTradeOrder.setCurrencyId(currencyId);
        //新增现货交易订单
        int insertSpotTradeOrder = spotTradeOrderMapper.insertSpotTradeOrder(spotTradeOrder);
        if (insertSpotTradeOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //用户流水记录
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("现货交易下单");
        userBillDetail.setDeSummary("现货交易下单成功");
        userBillDetail.setOrderAmount(orderTotalPrice.negate());
        userBillDetail.setOrderTime(spotTradeOrder.getBuyOrderTime());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(spotTradeOrder.getId());
        userBillDetail.setOrderClass(23);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //卖出费率
        BigDecimal sellFeeRate = BigDecimal.ZERO;
        //股票
        if (productType.equals(1)){
            //股票信息
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            //卖出手续费
            OrderFeeSetting sellFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("stock_spotTrade_sell_fee");
            if (sellFeeVo != null){
                sellFeeRate = sellFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(2)){
            //加密货币信息
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            //卖出手续费
            OrderFeeSetting sellFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("cryptocurrency_spotTrade_sell_fee");
            if (sellFeeVo != null){
                sellFeeRate = sellFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(3)){
            //期货信息
            FuturesProduct product = futuresProductService.selectFuturesProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            //卖出手续费率
            OrderFeeSetting sellFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("futures_spotTrade_sell_fee");
            if (sellFeeVo != null){
                sellFeeRate = sellFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else if (productType.equals(4)){
            //外汇信息
            ForexProduct product = forexProductService.selectForexProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            spotTradeOrder.setProductName(product.getProductName());
            //卖出手续费
            OrderFeeSetting sellFeeVo = orderFeeSettingService.selectOrderFeeSettingByKey("forex_spotTrade_sell_fee");
            if (sellFeeVo != null){
                sellFeeRate = sellFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
            }
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY,"产品信息异常");
        }
        //卖出时市值
        BigDecimal orderTotalPriceNow = spotTradeOrder.getSellOrderPrice().multiply(spotTradeOrder.getOrderNum()).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //盈亏
        BigDecimal profitAndLose = orderTotalPriceNow.subtract(spotTradeOrder.getOrderTotalPrice());
        //卖出手续费率
        BigDecimal sellFee = orderTotalPriceNow.multiply(sellFeeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        spotTradeOrder.setOrderFee(buyFee.add(sellFee));
        spotTradeOrder.setProfitAndLose(profitAndLose);
        spotTradeOrder.setAllProfitAndLose(profitAndLose.subtract(spotTradeOrder.getOrderFee()));
        //变更前的用户资金
        userAmountBefore = userAmountAfter;
        //用户余额变更后
        userAmountAfter = userAmountBefore.add(spotTradeOrder.getAllProfitAndLose()).add(spotTradeOrder.getOrderTotalPrice());
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //用户流水记录(现货交易卖出明细)
        UserBillDetail userBillDetail2 = new UserBillDetail();
        userBillDetail2.setUserId(userId);
        userBillDetail2.setDeType("现货交易卖出");
        userBillDetail2.setDeSummary("现货交易卖出:" + spotTradeOrder.getProductCode() + "/" + spotTradeOrder.getProductType() + ",占用本金:" + spotTradeOrder.getOrderTotalPrice() + ",总手续费:" + spotTradeOrder.getOrderFee()
                + ",递延费:0,印花税:0,盈亏:" + spotTradeOrder.getProfitAndLose() + ",总盈亏:" + spotTradeOrder.getAllProfitAndLose() + ",交易订单:"+spotTradeOrder.getOrderCode());
        userBillDetail2.setOrderAmount(spotTradeOrder.getAllProfitAndLose().add(spotTradeOrder.getOrderTotalPrice()));
        userBillDetail2.setOrderTime(spotTradeOrder.getSellOrderTime());
        userBillDetail2.setAmountBefore(userAmountBefore);
        userBillDetail2.setAmountAfter(userAmountAfter);
        userBillDetail2.setRelateOrderId(spotTradeOrder.getId());
        userBillDetail2.setOrderClass(24);
        userBillDetail2.setCurrencyId(currencyId);
        int insert2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
        if (insert2 <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //变更状态为已卖出
        spotTradeOrder.setOrderStatus(1);
        //更新现货交易订单信息
        int updateSpotTradeOrder = spotTradeOrderMapper.updateSpotTradeOrder(spotTradeOrder);
        if (updateSpotTradeOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录现货交易订单信息
        HttpUtils.getRequestLogParams().put("spotTradeOrder", JSONObject.toJSONString(spotTradeOrder));
        return 1;
    }
}
