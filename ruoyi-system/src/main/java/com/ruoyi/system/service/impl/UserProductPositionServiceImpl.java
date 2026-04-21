package com.ruoyi.system.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.ScheduledTaskExceptionLogMapper;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.mapper.UserProductPositionMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.BuyAndSellUtils;
import com.ruoyi.system.utils.ProductQuoteUtils;
import com.ruoyi.system.utils.UserApiKeyUtils;
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
 * 用户合约交易订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-25
 */
@Service
public class UserProductPositionServiceImpl implements IUserProductPositionService 
{
    @Resource
    private UserProductPositionMapper userProductPositionMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

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
    private IOrderFeeSettingService orderFeeSettingService;

    @Autowired
    private IProductSettingService productSettingService;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Autowired
    private IUserProductPositionService userProductPositionService;

    @Resource
    private ScheduledTaskExceptionLogMapper scheduledTaskExceptionLogMapper;

    /**
     * 查询用户合约交易订单
     * 
     * @param id 用户合约交易订单主键
     * @return 用户合约交易订单
     */
    @Override
    public UserProductPosition selectUserProductPositionById(Long id)
    {
        return userProductPositionMapper.selectUserProductPositionById(id);
    }

    /**
     * 查询用户合约交易订单列表
     * 
     * @param userProductPosition 用户合约交易订单
     * @return 用户合约交易订单
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserProductPosition> selectUserProductPositionList(UserProductPosition userProductPosition)
    {
        return userProductPositionMapper.selectUserProductPositionList(userProductPosition);
    }

    /**
     * 填充其他信息
     * @param positions 用户持仓列表
     */
    @Override
    public void fillOtherInfo(List<UserProductPosition> positions){
        fillProductQuote(positions);
    }

    /**
     * 填充行情信息
     * @param positions 产品订单列表
     */
    void fillProductQuote(List<UserProductPosition> positions){
        if (positions.size() == 0){
            return;
        }
        //行情map
        Map<String, TickerInfo> tickerInfoMap = new HashMap<>();
        //股票产品代码
        String codes = positions.stream().filter(a -> a.getProductType().equals(1)).map(a->a.getProductCode()).collect(Collectors.joining(","));
        if (StringUtils.isNotEmpty(codes)){
            Map<String, TickerInfo> map = ProductQuoteUtils.getStockQuote(codes,false);
            tickerInfoMap.putAll(map);
        }
        //加密货币产品代码
        codes = positions.stream().filter(a -> a.getProductType().equals(2)).map(a->a.getProductCode()).collect(Collectors.joining(","));
        if (StringUtils.isNotEmpty(codes)){
            Map<String, TickerInfo> map = ProductQuoteUtils.getCryptoCurrencyQuote(codes,false);
            tickerInfoMap.putAll(map);
        }
        //期货产品代码
        codes = positions.stream().filter(a -> a.getProductType().equals(3)).map(a->a.getProductCode()).collect(Collectors.joining(","));
        if (StringUtils.isNotEmpty(codes)){
            Map<String, TickerInfo> map = ProductQuoteUtils.getFuturesQuote(codes);
            tickerInfoMap.putAll(map);
        }
        //外汇产品代码
        codes = positions.stream().filter(a -> a.getProductType().equals(4)).map(a->a.getProductCode()).collect(Collectors.joining(","));
        if (StringUtils.isNotEmpty(codes)){
            Map<String, TickerInfo> map = ProductQuoteUtils.getForexQuote(codes);
            tickerInfoMap.putAll(map);
        }
        //遍历
        for (int i = 0; i < positions.size(); i++) {
            //持仓信息
            UserProductPosition position = positions.get(i);
            //如果持仓中
            if (position.getOrderStatus().equals(0)){
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(position.getProductCode());
                if (tickerInfo != null){
                    //现价
                    BigDecimal nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                    position.setNowPrice(nowPrice);
                    //收益系数
                    BigDecimal positionIncomeCoefficient = (BigDecimal) position.getParams().get("positionIncomeCoefficient");
                    //购买时候行情
                    BigDecimal buyOrderPrice = position.getBuyOrderPrice();
                    //订单金额
                    BigDecimal orderTotalPrice = position.getOrderTotalPrice();
                    //如果持仓中，计算浮动盈亏
                    if (position.getOrderDirection().equals(0)) {
                        position.setProfitAndLose(nowPrice.subtract(buyOrderPrice).multiply(orderTotalPrice).multiply(positionIncomeCoefficient).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE));
                    } else {
                        position.setProfitAndLose(buyOrderPrice.subtract(nowPrice).multiply(orderTotalPrice).multiply(positionIncomeCoefficient).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE));
                    }
                    //总盈亏 = 浮动盈亏 - 手续费
                    position.setAllProfitAndLose(position.getProfitAndLose().subtract(position.getOrderFee()));
                }
            }
        }
    }

    /**
     * 新增用户合约交易订单
     * 
     * @param userProductPosition 用户合约交易订单
     * @return 结果
     */
    @Override
    public int insertUserProductPosition(UserProductPosition userProductPosition)
    {
        return userProductPositionMapper.insertUserProductPosition(userProductPosition);
    }

    /**
     * 修改用户合约交易订单
     * 
     * @param userProductPosition 用户合约交易订单
     * @return 结果
     */
    @Override
    public int updateUserProductPosition(UserProductPosition userProductPosition)
    {
        return userProductPositionMapper.updateUserProductPosition(userProductPosition);
    }

    /**
     * 批量删除用户合约交易订单
     * 
     * @param ids 需要删除的用户合约交易订单主键
     * @return 结果
     */
    @Override
    public int deleteUserProductPositionByIds(Long[] ids)
    {
        return userProductPositionMapper.deleteUserProductPositionByIds(ids);
    }

    /**
     * 用户持仓锁仓、解仓操作
     * @param positionId 持仓id
     * @param lockStatus 锁定状态 0：解锁 1：锁定
     * @param lockMsg
     * @return
     */
    @Override
    public int lockUserPosition(Long positionId, Integer lockStatus, String lockMsg){
        //持仓信息
        UserProductPosition position = userProductPositionMapper.selectUserProductPositionById(positionId);
        if (position == null) {
            throw new ServiceException("获取持仓信息异常");
        }
        if (position.getOrderStatus().equals(1)) {
            throw new ServiceException("平仓单不能锁仓");
        }
//        if (lockStatus.equals(1) && StringUtils.isEmpty(lockMsg)) {
//            return AjaxResult.error("锁仓提示信息必填");
//        }

        UserProductPosition positionVo = new UserProductPosition();
        positionVo.setId(positionId);
        positionVo.setIsLock(lockStatus);
        positionVo.setLockMsg(lockMsg);
        positionVo.setSqlVersion(position.getSqlVersion());
        int count = userProductPositionMapper.updateUserProductPosition(positionVo);
        if (count <= 0) {
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 强制平仓操作
     * @param positionId 持仓id
     * @param sellMode 平仓模式 0：平仓价格平仓 1：盈亏比例平仓 2：盈亏金额平仓
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int forceSell(Long positionId,Integer sellMode,BigDecimal target){
        //持仓信息
        UserProductPosition position = userProductPositionMapper.selectUserProductPositionById(positionId);
        if (position == null) {
            throw new ServiceException("获取订单信息异常");
        }
        //现价
        BigDecimal nowPrice = null;
        if (sellMode.equals(0)){
            if (target.compareTo(BigDecimal.ZERO) <= 0){
                throw new ServiceException("价格不允许小于0");
            }
            nowPrice = target;
        }else if (sellMode.equals(1)){
            if (target.compareTo(new BigDecimal(-100)) < 0 || target.compareTo(new BigDecimal(100)) > 0){
                throw new ServiceException("请输入-100~100之间的有效比例");
            }
            target = target.divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //订单总金额
            BigDecimal orderTotalPrice = position.getOrderTotalPrice();
            //浮动盈亏
            BigDecimal profitAndLose = orderTotalPrice.multiply(target).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //购买时产品行情
            BigDecimal buyOrderPrice = position.getBuyOrderPrice();
            //合约收益系数
            BigDecimal positionIncomeCoefficient = (BigDecimal) position.getParams().get("positionIncomeCoefficient");
            //如果持仓中，计算浮动盈亏
            if (position.getOrderDirection().equals(0)) {
                nowPrice = profitAndLose.multiply(new BigDecimal("100")).divide(orderTotalPrice.multiply(positionIncomeCoefficient), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE).add(buyOrderPrice);
            } else {
                nowPrice = buyOrderPrice.subtract(profitAndLose.multiply(new BigDecimal("100")).divide(orderTotalPrice.multiply(positionIncomeCoefficient), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE));
            }
        }else if (sellMode.equals(2)){
            //订单总金额
            BigDecimal orderTotalPrice = position.getOrderTotalPrice();
            //购买时产品行情
            BigDecimal buyOrderPrice = position.getBuyOrderPrice();
            //合约收益系数
            BigDecimal positionIncomeCoefficient = (BigDecimal) position.getParams().get("positionIncomeCoefficient");
            //浮动盈亏
            BigDecimal profitAndLose = target;
            //如果持仓中，计算浮动盈亏
            if (position.getOrderDirection().equals(0)) {
                nowPrice = profitAndLose.multiply(new BigDecimal("100")).divide(orderTotalPrice.multiply(positionIncomeCoefficient), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE).add(buyOrderPrice);
            } else {
                nowPrice = buyOrderPrice.subtract(profitAndLose.multiply(new BigDecimal("100")).divide(orderTotalPrice.multiply(positionIncomeCoefficient), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE));
            }
        }else {
            throw new ServiceException("平仓模式错误");
        }
        return sell(positionId, 0, nowPrice);
    }

    /**
     * 删除用户合约交易订单信息
     * 
     * @param id 用户合约交易订单主键
     * @return 结果
     */
    @Override
    public int deleteUserProductPositionById(Long id)
    {
        return userProductPositionMapper.deleteUserProductPositionById(id);
    }

    /**
     * 用户合约交易下单
     * @param position
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int buy(UserProductPosition position){
        //用户id
        Long userId = UserApiKeyUtils.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null || !userInfo.getIsDel().equals(0)){
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
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

        //产品代码
        String productCode = position.getProductCode();
        //交易方向
        Integer orderDirection = position.getOrderDirection();
        //止盈线
        BigDecimal stopProfitPrice = position.getStopProfitPrice();
        //止损线
        BigDecimal stopLossPrice = position.getStopLossPrice();
        //币种id
        Long currencyId = 3L;
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
        //产品类型
        Integer productType = position.getProductType();
        //产品交易时间
        ProductTradeTimeSetting productTradeTimeSetting = productTradeTimeSettingService.selectProductTradeTimeSettingByDayAndProductType(DateUtils.getDayOfWeek(), productType);
        String am_begin = productTradeTimeSetting.getTransAmBegin();
        String am_end = productTradeTimeSetting.getTransAmEnd();
        String pm_begin = productTradeTimeSetting.getTransPmBegin();
        String pm_end = productTradeTimeSetting.getTransPmEnd();
        boolean am_flag;
        boolean pm_flag;
        try {
            am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
            pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        if (!am_flag && !pm_flag) {
            throw new LangException("hint_dealErrorOutOfTradingHours","交易失败，不在交易时段内");
        }

        //产品类型名称
        String productTypeName;
        //流水类型
        Integer orderClass;
        //手续费率
        BigDecimal feeRate = BigDecimal.ZERO;
        //购买价格
        BigDecimal buyPrice = null;
        //股票
        if (productType.equals(1)){
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (!product.getIsLock().equals(0)) {
                throw new LangException("hint_8","该产品已锁定，不能进行交易");
            }
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCode,false);
            //行情数据
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                buyPrice = new BigDecimal(tickerInfo.getNowPrice());
            }
            //合约交易下单手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("stock_contract_buy_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
            productTypeName = "股票";
            orderClass = 4;
        }else if (productType.equals(2)){
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (!product.getIsLock().equals(0)) {
                throw new LangException("hint_8","该产品已锁定，不能进行交易");
            }
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
            //行情数据
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                buyPrice = new BigDecimal(tickerInfo.getNowPrice());
            }
            //合约交易下单手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("cryptocurrency_contract_buy_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
            productTypeName = "加密货币";
            orderClass = 6;
        }else if (productType.equals(3)){
            FuturesProduct product = futuresProductService.selectFuturesProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (!product.getIsLock().equals(0)) {
                throw new LangException("hint_8","该产品已锁定，不能进行交易");
            }
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCode);
            //行情数据
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                buyPrice = new BigDecimal(tickerInfo.getNowPrice());
            }
            //合约交易下单手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("futures_contract_buy_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
            productTypeName = "期货";
            orderClass = 25;
        }else if (productType.equals(4)){
            ForexProduct product = forexProductService.selectForexProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (!product.getIsLock().equals(0)) {
                throw new LangException("hint_8","该产品已锁定，不能进行交易");
            }
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCode);
            //行情数据
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                buyPrice = new BigDecimal(tickerInfo.getNowPrice());
            }
            //合约交易下单手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("forex_contract_buy_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
            productTypeName = "外汇";
            orderClass = 43;
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY,"产品类型错误");
        }
        //判断是否取到了行情
        if (buyPrice == null){
            throw new LangException("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
        }

        //判断设置的止盈线与止损线是否正确
        if (stopProfitPrice != null){
            if (orderDirection.equals(0)){
                if (stopProfitPrice.compareTo(buyPrice) <= 0){
                    throw new LangException("hint_36","请正确设置止盈线");
                }
            }else {
                if (stopProfitPrice.compareTo(buyPrice) >= 0){
                    throw new LangException("hint_36","请正确设置止盈线");
                }
            }
        }
        if (stopLossPrice != null){
            if (orderDirection.equals(0)){
                if (stopLossPrice.compareTo(buyPrice) >= 0){
                    throw new LangException("hint_37","请正确设置止损线");
                }
            }else {
                if (stopLossPrice.compareTo(buyPrice) <= 0){
                    throw new LangException("hint_37","请正确设置止损线");
                }
            }
        }

        //产品交易设置
        ProductSetting productSetting = productSettingService.selectProductSettingByProductType(productType);
        if (productSetting == null) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品交易设置异常");
        }
        //下单金额
        BigDecimal orderTotalPrice = position.getOrderTotalPrice();
        //允许最小下单金额
        BigDecimal buyMinAmt = productSetting.getBuyMinAmt();
        if (orderTotalPrice.compareTo(buyMinAmt) < 0) {
            ArrayList<Object> list = new ArrayList<>();
            list.add(buyMinAmt);
            throw new LangException("hint_11",list,"下单失败，购买金额小于" + buyMinAmt);
        }
        //钱包余额信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //余额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //如果余额不足
        if (orderTotalPrice.compareTo(userAmountBefore) > 0) {
            throw new LangException("hint_4","此币种可用资金不足");
        }
        //手续费
        BigDecimal orderFee = orderTotalPrice.multiply(feeRate).divide(new BigDecimal(100), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //实时时间
        Date nowDateTime = new Date();
        position.setOrderCode(CodeUtils.generateOrderCode("POSITION"));
        position.setUserId(userId);
        position.setBuyOrderTime(nowDateTime);
        position.setBuyOrderPrice(buyPrice);
        position.setSellOrderTime(null);
        position.setSellOrderPrice(null);
        position.setOrderDirection(orderDirection);
        position.setOrderFee(orderFee);
        position.setProfitAndLose(BigDecimal.ZERO);
        position.setAllProfitAndLose(orderFee.negate());
        position.setIsLock(0);
        position.setLockMsg(null);
        position.setCurrencyId(currencyId);
        position.setStopProfitPrice(stopProfitPrice);
        position.setStopLossPrice(stopLossPrice);
        position.setOrderStatus(0);
        int insertUserProductPosition = userProductPositionMapper.insertUserProductPosition(position);
        if (insertUserProductPosition <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"插入合约持仓异常");
        }

        //余额变更后
        BigDecimal userAmountAfter = userAmountBefore.subtract(orderTotalPrice);
        //更新余额
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"更新用户余额异常");
        }

        //更新打码量
        userInfo.setNeedOrderAmount(userInfo.getNeedOrderAmount().subtract(orderTotalPrice));
        int updateUser = userInfoMapper.updateUserInfo(userInfo);
        if (updateUser <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"更新用户信息异常");
        }

        //账户明细
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType(productTypeName + "合约交易下单");
        userBillDetail.setDeSummary(productTypeName + "合约交易下单成功");
        userBillDetail.setOrderAmount(orderTotalPrice.negate());
        userBillDetail.setOrderTime(nowDateTime);
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(position.getId());
        userBillDetail.setOrderClass(orderClass);
        userBillDetail.setCurrencyId(currencyId);
        int count = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (count <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }

    /**
     * 用户合约交易卖出
     * @param positionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sell(Long positionId, Integer doType, BigDecimal nowPrice){
        //持仓信息
        UserProductPosition position = userProductPositionMapper.selectUserProductPositionById(positionId);
        if (position == null) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取订单信息异常");
        }
        //验证订单状态
        if (!position.getOrderStatus().equals(0)) {
            throw new LangException("hint_FailedToClosePosition4","平仓失败，此订单已平仓");
        }
        //产品类型
        Integer productType = position.getProductType();
        //用户id
        Long userId = position.getUserId();
        //如果不是强制平仓
        if (!doType.equals(0)){
            if (!UserApiKeyUtils.getUserId().equals(userId)){
                throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
            }
            //验证订单交易状态
            if (!position.getIsLock().equals(0)){
                String lockMsg = position.getLockMsg();
                if (lockMsg == null){
                    lockMsg = "locked";
                }
                throw new LangException(lockMsg);
            }
            //产品交易时间
            ProductTradeTimeSetting productTradeTimeSetting = productTradeTimeSettingService.selectProductTradeTimeSettingByDayAndProductType(DateUtils.getDayOfWeek(), productType);
            String am_begin = productTradeTimeSetting.getTransAmBegin();
            String am_end = productTradeTimeSetting.getTransAmEnd();
            String pm_begin = productTradeTimeSetting.getTransPmBegin();
            String pm_end = productTradeTimeSetting.getTransPmEnd();
            boolean am_flag;
            boolean pm_flag;
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
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null){
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("userId",userId);
        //验证用户交易状态
        if (!userInfo.getIsLock().equals(0)) {
            throw new LangException("hint_FailedToClosePosition3","平仓失败，用户已被锁定");
        }
        //产品交易设置
        ProductSetting productSetting = productSettingService.selectProductSettingByProductType(productType);
        if (productSetting == null) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品交易设置异常");
        }
        //至少持仓时间（分钟）
        Integer cantSellTimes = productSetting.getCantSellTimes();
        if (!BuyAndSellUtils.isCanSell(position.getBuyOrderTime(),cantSellTimes)) {
            List<Object> list = new ArrayList<>();
            list.add(cantSellTimes);
            throw new LangException("hint_19",list,cantSellTimes + "分钟内不能平仓");
        }

        //产品代码
        String productCode = position.getProductCode();
        //当前行情
        BigDecimal sellPrice = nowPrice;
        //产品类型名称
        String productTypeName;
        //流水类型
        Integer orderClass;
        //手续费率
        BigDecimal feeRate = BigDecimal.ZERO;
        //股票
        if (productType.equals(1)){
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (!product.getIsLock().equals(0)) {
                throw new LangException("hint_8","该产品已锁定，不能进行交易");
            }
            if (sellPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCode,false);
                //行情数据
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    sellPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //合约交易卖出手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("stock_contract_sell_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
            productTypeName = "股票";
            orderClass = 5;
        }else if (productType.equals(2)){
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (!product.getIsLock().equals(0)) {
                throw new LangException("hint_8","该产品已锁定，不能进行交易");
            }
            if (sellPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
                //行情数据
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    sellPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //合约交易卖出手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("cryptocurrency_contract_sell_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
            productTypeName = "加密货币";
            orderClass = 7;
        }else if (productType.equals(3)){
            FuturesProduct product = futuresProductService.selectFuturesProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (!product.getIsLock().equals(0)) {
                throw new LangException("hint_8","该产品已锁定，不能进行交易");
            }
            if (sellPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCode);
                //行情数据
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    sellPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //合约交易卖出手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("futures_contract_sell_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
            productTypeName = "期货";
            orderClass = 26;
        }else if (productType.equals(4)){
            ForexProduct product = forexProductService.selectForexProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            if (!product.getIsLock().equals(0)) {
                throw new LangException("hint_8","该产品已锁定，不能进行交易");
            }
            if (sellPrice == null){
                //行情map
                Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCode);
                //行情数据
                TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                if (tickerInfo != null){
                    sellPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
            }
            //合约交易卖出手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("forex_contract_sell_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
            productTypeName = "外汇";
            orderClass = 44;
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY,"产品类型错误");
        }
        //判断是否取到了行情
        if (sellPrice == null){
            throw new LangException("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
        }

        //币种id
        Long currencyId = position.getCurrencyId();
        //收益系数
        BigDecimal positionIncomeCoefficient = (BigDecimal) position.getParams().get("positionIncomeCoefficient");
        //购买时候行情
        BigDecimal buyOrderPrice = position.getBuyOrderPrice();
        //订单金额
        BigDecimal orderTotalPrice = position.getOrderTotalPrice();
        //浮动盈亏
        BigDecimal profitAndLose;
        //如果持仓中，计算浮动盈亏
        if (position.getOrderDirection().equals(0)) {
            profitAndLose = sellPrice.subtract(buyOrderPrice).multiply(orderTotalPrice).multiply(positionIncomeCoefficient).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        } else {
            profitAndLose = buyOrderPrice.subtract(sellPrice).multiply(orderTotalPrice).multiply(positionIncomeCoefficient).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        }
        //买入手续费
        BigDecimal buyFee = position.getOrderFee();
        //卖出手续费
        BigDecimal sellFee = orderTotalPrice.multiply(feeRate).divide(new BigDecimal(100), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //买入卖出手续费
        BigDecimal orderFee = buyFee.add(sellFee);
        position.setOrderFee(orderFee);
        position.setSellOrderPrice(sellPrice);
        position.setOrderStatus(1);
        //实时时间
        Date nowDateTime = new Date();
        position.setSellOrderTime(nowDateTime);
        position.setProfitAndLose(profitAndLose);
        //总盈亏
        BigDecimal allProfitAndLose = profitAndLose.subtract(orderFee);
        position.setAllProfitAndLose(allProfitAndLose);
        //更新持仓订单信息
        int updateUserProductPosition = userProductPositionMapper.updateUserProductPosition(position);
        if (updateUserProductPosition <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"更新订单信息异常");
        }
        //日志记录持仓订单信息
        HttpUtils.getRequestLogParams().put("position", JSONObject.toJSONString(position));

        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId,currencyId);
        //余额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();

        //修改用户可用余额=当前可用余额+总盈亏+本金
        //余额变更后
        BigDecimal userAmountAfter = userAmountBefore.add(allProfitAndLose).add(orderTotalPrice);
        //如果余额成了负数
        if (userAmountAfter.compareTo(BigDecimal.ZERO) < 0){
            userAmountAfter = BigDecimal.ZERO;
        }
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"更新用户余额异常");
        }

        //账户明细
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType(productTypeName + "合约交易平仓");
        userBillDetail.setDeSummary("卖出"+productTypeName+":"+productCode+"/"+productType+",占用本金:"+orderTotalPrice+",总手续费:"+orderFee+",盈亏:"+profitAndLose+",总盈亏:"+allProfitAndLose+",交易订单:"+position.getOrderCode());
        userBillDetail.setRemark(productCode+"/"+productType+"/"+orderTotalPrice+"/"+orderFee+"/"+profitAndLose+"/"+allProfitAndLose+"/"+position.getOrderCode());
        if (userAmountAfter.compareTo(BigDecimal.ZERO) == 0){
            userBillDetail.setOrderAmount(userAmountBefore.negate());
        }else {
            userBillDetail.setOrderAmount(allProfitAndLose.add(orderTotalPrice));
        }
        userBillDetail.setOrderTime(nowDateTime);
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(position.getId());
        userBillDetail.setOrderClass(orderClass);
        userBillDetail.setCurrencyId(currencyId);
        int count = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (count <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }

    /**
     * 止盈止损定时任务
     */
    @Override
    public void stopProfitAndLossTask(){
        //获取持仓中的订单
        UserProductPosition search = new UserProductPosition();
        search.setOrderStatus(0);
        List<UserProductPosition> positions = userProductPositionMapper.selectUserProductPositionList(search);
        if (positions.size() == 0){
            return ;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try {
            //行情map
            Map<String, TickerInfo> tickerInfoMap = new HashMap<>();
            //股票产品代码
            String codes = positions.stream().filter(a -> a.getProductType().equals(1)).map(a->a.getProductCode()).collect(Collectors.joining(","));
            if (StringUtils.isNotEmpty(codes)){
                Map<String, TickerInfo> map = ProductQuoteUtils.getStockQuote(codes,false);
                tickerInfoMap.putAll(map);
            }
            //加密货币产品代码
            codes = positions.stream().filter(a -> a.getProductType().equals(2)).map(a->a.getProductCode()).collect(Collectors.joining(","));
            if (StringUtils.isNotEmpty(codes)){
                Map<String, TickerInfo> map = ProductQuoteUtils.getCryptoCurrencyQuote(codes,false);
                tickerInfoMap.putAll(map);
            }
            //期货产品代码
            codes = positions.stream().filter(a -> a.getProductType().equals(3)).map(a->a.getProductCode()).collect(Collectors.joining(","));
            if (StringUtils.isNotEmpty(codes)){
                Map<String, TickerInfo> map = ProductQuoteUtils.getFuturesQuote(codes);
                tickerInfoMap.putAll(map);
            }
            //外汇产品代码
            codes = positions.stream().filter(a -> a.getProductType().equals(4)).map(a->a.getProductCode()).collect(Collectors.joining(","));
            if (StringUtils.isNotEmpty(codes)){
                Map<String, TickerInfo> map = ProductQuoteUtils.getForexQuote(codes);
                tickerInfoMap.putAll(map);
            }
            for (int i = 0; i < positions.size(); i++) {
                //订单信息
                UserProductPosition position = positions.get(i);
                executorService.execute(() -> {
                    try {
                        //产品代码
                        String productCode = position.getProductCode();
                        //现价
                        BigDecimal nowPrice = BigDecimal.ZERO;
                        //行情信息
                        TickerInfo tickerInfo = tickerInfoMap.get(productCode);
                        if (tickerInfo != null) {
                            nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                        }
                        if (nowPrice.compareTo(BigDecimal.ZERO) == 0) {
                            return;
                        }
                        //止盈线
                        BigDecimal stopProfitPrice = position.getStopProfitPrice();
                        //止损线
                        BigDecimal stopLossPrice = position.getStopLossPrice();
                        //是否卖出
                        boolean isSell = false;
                        //买涨跌的方向
                        Integer orderDirection = position.getOrderDirection();
                        //买涨
                        if (orderDirection.equals(0)) {
                            if (stopProfitPrice != null && nowPrice.compareTo(stopProfitPrice) >= 0) {
                                isSell = true;
                            }
                            if (stopLossPrice != null && nowPrice.compareTo(stopLossPrice) <= 0) {
                                isSell = true;
                            }
                        } else {
                            //买跌
                            if (stopLossPrice != null && nowPrice.compareTo(stopLossPrice) >= 0) {
                                isSell = true;
                            }
                            if (stopProfitPrice != null && nowPrice.compareTo(stopProfitPrice) <= 0) {
                                isSell = true;
                            }
                        }
                        //如果触碰止盈止损线
                        if (isSell == true) {
                            position.setSellOrderPrice(nowPrice);
                            position.setSellOrderTime(new Date());
                            int count = userProductPositionService.sell(position.getId(), 0, null);
                            //如果没结算成功
                            if (count == 0) {
                                throw new RuntimeException();
                            }
                        }
                    } catch (Exception e) {
                        //记录异常日志
                        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
                        scheduledTaskExceptionLog.setJobName("止盈止损触发定时任务");
                        scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
                        scheduledTaskExceptionLog.setCreateTime(new Date());
                        scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
                        scheduledTaskExceptionLog.setRelateInfo("id:" + position.getId() + "/" + position.getSellOrderPrice() + "/" + position.getSellOrderTime());
                        scheduledTaskExceptionLog.setType(3);
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
}
