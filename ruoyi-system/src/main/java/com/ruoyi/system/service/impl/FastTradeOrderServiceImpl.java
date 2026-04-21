package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.BuyAndSellUtils;
import com.ruoyi.system.utils.ProductQuoteUtils;
import com.ruoyi.system.utils.TimeControlUtil;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.utils.telegram.TelegramUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 极速交易订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
@Service
public class FastTradeOrderServiceImpl implements IFastTradeOrderService 
{
    @Resource
    private FastTradeOrderMapper fastTradeOrderMapper;

    @Autowired
    private RedisCache redisCache;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IUserVipLevelConfigService userVipLevelConfigService;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IFastTradeOrderOptionsService fastTradeOrderOptionsService;

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
    private IFastTradeOrderService fastTradeOrderService;

    @Resource
    private UserFastTradeControlMapper userFastTradeControlMapper;

    @Resource
    private FastOrderControlConfigMapper fastOrderControlConfigMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private IOrderFeeSettingService orderFeeSettingService;

    @Autowired
    private ISwitchSetService switchSetService;

    @Resource
    private UserTeamLevelLineMapper userTeamLevelLineMapper;

    @Resource
    private UserRebateRateMapper userRebateRateMapper;

    @Resource
    private UserCommissionRecordMapper userCommissionRecordMapper;

    private static final Logger log = LoggerFactory.getLogger(FastTradeOrderServiceImpl.class);

    /**
     * 查询极速交易订单
     * 
     * @param id 极速交易订单主键
     * @return 极速交易订单
     */
    @Override
    public FastTradeOrder selectFastTradeOrderById(Long id)
    {
        return fastTradeOrderMapper.selectFastTradeOrderById(id);
    }

    /**
     * 查询极速交易订单列表
     * 
     * @param fastTradeOrder 极速交易订单
     * @return 极速交易订单
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<FastTradeOrder> selectFastTradeOrderList(FastTradeOrder fastTradeOrder)
    {
        return fastTradeOrderMapper.selectFastTradeOrderList(fastTradeOrder);
    }

    /**
     * 填充其他信息
     * @param fastTradeOrders 极速交易订单列表
     */
    @Override
    public void fillOtherInfo(List<FastTradeOrder> fastTradeOrders){
        fillProductQuote(fastTradeOrders);
    }

    /**
     * 填充行情信息
     * @param fastTradeOrders 极速交易订单列表
     */
    void fillProductQuote(List<FastTradeOrder> fastTradeOrders){
        if (fastTradeOrders.size() == 0){
            return;
        }
        //需要获取行情的订单map
        Map<Integer, List<FastTradeOrder>> map = fastTradeOrders.stream().filter(a->a.getOrderStatus().equals(0)).collect(Collectors.groupingBy(a -> a.getProductType()));
        //行情map
        Map<String, TickerInfo> tickerInfoMap = new HashMap<>();
        //股票订单列表
        List<FastTradeOrder> stockFastTradeOrders = map.get(1);
        //如果有股票订单列表
        if (stockFastTradeOrders != null && stockFastTradeOrders.size() > 0){
            //productCodes
            String productCodes = stockFastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            Map<String, TickerInfo> stockQuote = ProductQuoteUtils.getStockQuote(productCodes, false);
            tickerInfoMap.putAll(stockQuote);
        }
        //加密货币订单列表
        List<FastTradeOrder> cryptocurrencyFastTradeOrders = map.get(2);
        //如果有加密货币订单列表
        if (cryptocurrencyFastTradeOrders != null && cryptocurrencyFastTradeOrders.size() > 0){
            //productCodes
            String productCodes = cryptocurrencyFastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            Map<String, TickerInfo> stockQuote = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes, false);
            tickerInfoMap.putAll(stockQuote);
        }
        //期货订单列表
        List<FastTradeOrder> futuresFastTradeOrders = map.get(3);
        //如果有期货订单列表
        if (futuresFastTradeOrders != null && futuresFastTradeOrders.size() > 0){
            //productCodes
            String productCodes = futuresFastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            Map<String, TickerInfo> stockQuote = ProductQuoteUtils.getFuturesQuote(productCodes);
            tickerInfoMap.putAll(stockQuote);
        }
        //外汇订单列表
        List<FastTradeOrder> forexFastTradeOrders = map.get(4);
        //如果有外汇订单列表
        if (forexFastTradeOrders != null && forexFastTradeOrders.size() > 0){
            //productCodes
            String productCodes = forexFastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            Map<String, TickerInfo> stockQuote = ProductQuoteUtils.getForexQuote(productCodes);
            tickerInfoMap.putAll(stockQuote);
        }
        //遍历塞入行情信息
        for (int i = 0; i < fastTradeOrders.size(); i++) {
            //订单信息
            FastTradeOrder fastTradeOrder = fastTradeOrders.get(i);
            //如果订单持仓中
            if (fastTradeOrder.getOrderStatus().equals(0)){
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(fastTradeOrder.getProductCode());
                if (tickerInfo != null){
                    fastTradeOrder.setNowPrice(new BigDecimal(tickerInfo.getNowPrice()));
                }
            }
        }
    }

    /**
     * 新增极速交易订单
     * 
     * @param fastTradeOrder 极速交易订单
     * @return 结果
     */
    @Override
    public int insertFastTradeOrder(FastTradeOrder fastTradeOrder)
    {
        return fastTradeOrderMapper.insertFastTradeOrder(fastTradeOrder);
    }

    /**
     * 修改极速交易订单
     * 
     * @param fastTradeOrder 极速交易订单
     * @return 结果
     */
    @Override
    public int updateFastTradeOrder(FastTradeOrder fastTradeOrder)
    {
        return fastTradeOrderMapper.updateFastTradeOrder(fastTradeOrder);
    }

    /**
     * 批量删除极速交易订单
     * 
     * @param ids 需要删除的极速交易订单主键
     * @return 结果
     */
    @Override
    public int deleteFastTradeOrderByIds(Long[] ids)
    {
        return fastTradeOrderMapper.deleteFastTradeOrderByIds(ids);
    }

    /**
     * 删除极速交易订单信息
     * 
     * @param id 极速交易订单主键
     * @return 结果
     */
    @Override
    public int deleteFastTradeOrderById(Long id)
    {
        return fastTradeOrderMapper.deleteFastTradeOrderById(id);
    }

    /**
     * 极速交易订单单控
     * @param fastTradeOrderId 极速交易订单id
     * @param orderControlFlag 订单单控状态：0：未控  1：赢  2：输 3：平
     * @return
     */
    @Override
    public int fastOrderControl(Long fastTradeOrderId, Integer orderControlFlag){
        //订单信息
        FastTradeOrder fastTradeOrderVo = fastTradeOrderMapper.selectFastTradeOrderById(fastTradeOrderId);
        if (fastTradeOrderVo == null){
            throw new ServiceException("获取订单信息异常");
        }
        //判断订单状态
        if (!fastTradeOrderVo.getOrderStatus().equals(0)){
            throw new ServiceException("只能控制未结算订单");
        }
        //订单key
        String orderIdKey = "fastOrderControl/"+fastTradeOrderVo.getId();
        //判断订单数据是否已经生成过
        if (redisCache.getCacheObject(orderIdKey) != null){
            throw new ServiceException("订单结算最后10秒不允许操作");
        }
        fastTradeOrderVo.setOrderControlFlag(orderControlFlag);
        int count = fastTradeOrderMapper.updateFastTradeOrder(fastTradeOrderVo);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        //日志记录订单信息
        HttpUtils.getRequestLogParams().put("fastTradeOrderVo",JSONObject.toJSONString(fastTradeOrderVo));
        return 1;
    }

    /**
     * 极速交易订单单控
     * @param fastTradeOrderIds 极速交易订单ids
     * @param orderControlFlag 订单单控状态：0：未控  1：赢  2：输 3：平
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchFastOrderControl(List<Long> fastTradeOrderIds, Integer orderControlFlag){
        //订单信息
        FastTradeOrder search = new FastTradeOrder();
        search.getParams().put("ids",fastTradeOrderIds);
        List<FastTradeOrder> fastTradeOrders = fastTradeOrderMapper.selectFastTradeOrderList(search);
        if (fastTradeOrders.size() == 0){
            throw new ServiceException("获取订单信息异常");
        }
        //订单信息map
        Map<Long, FastTradeOrder> map = fastTradeOrders.stream().collect(Collectors.toMap(a -> a.getId(), a -> a));
        for (int i = 0; i < fastTradeOrderIds.size(); i++) {
            //订单ID
            Long id = Long.valueOf(String.valueOf(fastTradeOrderIds.get(i)));
            //订单信息
            FastTradeOrder fastTradeOrder = map.get(id);
            if (fastTradeOrder == null){
                if (fastTradeOrder == null){
                    throw new ServiceException("获取订单信息异常");
                }
            }
            //判断订单状态
            if (!fastTradeOrder.getOrderStatus().equals(0)){
                throw new ServiceException("只能控制未结算订单");
            }
            //订单key
            String orderIdKey = "fastOrderControl/"+fastTradeOrder.getId();
            //判断订单数据是否已经生成过
            if (redisCache.getCacheObject(orderIdKey) != null){
                throw new ServiceException("订单结算最后10秒不允许操作");
            }
            fastTradeOrder.setOrderControlFlag(orderControlFlag);
            int count = fastTradeOrderMapper.updateFastTradeOrder(fastTradeOrder);
            if (count <= 0){
                throw new ServiceException("更新订单信息异常");
            }

        }
        //日志记录订单信息
        HttpUtils.getRequestLogParams().put("JSONArray:fastTradeOrders", JSONObject.toJSONString(fastTradeOrders));
        return 1;
    }

    /**
     * 用户极速交易下单
     * @param fastTradeOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FastTradeOrder addFastTradeOrder(FastTradeOrder fastTradeOrder){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //验证用户交易状态
        if (!userInfo.getIsLock().equals(0)) {
            throw new LangException("hint_dealErrorAccountLocked","下单失败，用户已被锁定");
        }
        //交易币种
        Long currencyId = fastTradeOrder.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取币种信息异常");
        }
        //币种名称
        String currencyName = platformCurrency.getCurrencyName();
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",currencyName);
        //极速交易下单选项id
        Long fastTradeOrderOptionsId = fastTradeOrder.getFastTradeOrderOptionsId();
        //下单选项信息
        FastTradeOrderOptions fastTradeOrderOptions = fastTradeOrderOptionsService.selectFastTradeOrderOptionsById(fastTradeOrderOptionsId);
        //产品代码
        String productCode = fastTradeOrderOptions.getProductCode();
        //产品类型
        Integer productType = fastTradeOrderOptions.getProductType();
        //验证是否在交易时间
        //产品交易时间信息获取
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
            //用户vip等级
            Integer vipLevel = userInfo.getVipLevel();
            //极速交易vip等级要求
            Integer fastTradeVipLevelLimit = CacheUtils.getOtherValueByKey("fast_trade_vip_level_limit",Integer.class);
            if (fastTradeVipLevelLimit != null && vipLevel < fastTradeVipLevelLimit){
                List<Object> param = new ArrayList<>();
                param.add(fastTradeVipLevelLimit);
                throw new LangException("hint_51",param,"参与极速交易玩法vip等级需要达到"+fastTradeVipLevelLimit);
            }
            //该vip下单数量限制
            UserVipLevelConfig userVipLevelConfig = userVipLevelConfigService.selectUserVipLevelConfigByVipLevel(vipLevel);
            if (userVipLevelConfig != null && userVipLevelConfig.equals(0)){
                Integer day = userVipLevelConfig.getDay();
                Integer orderNum = userVipLevelConfig.getOrderNum();
                if (day > 0 && orderNum > 0){
                    //获取某时间至当前时间的订单数量
                    Integer orderNumForPeriod = fastTradeOrderMapper.getOrderNumForPeriod(userId, DateUtils.getDateBeforeOrAfterDate(new Date(), Calendar.DAY_OF_YEAR, -day));
                    if (orderNumForPeriod >= orderNum){
                        List<Object> param = new ArrayList<>();
                        param.add(vipLevel);
                        param.add(day);
                        param.add(orderNum);
                        throw new LangException("hint_50",param,"vip等级"+vipLevel+"的用户"+day+"天只能下"+orderNum+"单");
                    }
                }
            }
            //vip等级显示
            String vipLevelLimit = fastTradeOrderOptions.getVipLevelLimit();
            if (StringUtils.isNotEmpty(vipLevelLimit) && !Arrays.asList(vipLevelLimit.split(",")).contains(vipLevel.toString())){
                List<Object> list = new ArrayList<>();
                list.add(vipLevelLimit);
                throw new LangException("hint_63",list,"此交易选项只有vip等级为"+vipLevelLimit+"的用户可以下单");
            }
        }

        // 有未结算极速交易订单时禁止下单
        Integer switchStatusById135 = switchSetService.selectSwitchStatusById(135L);
        if (switchStatusById135.equals(0)) {
            //
            FastTradeOrder fastTradeOrderSearch = new FastTradeOrder();
            fastTradeOrderSearch.setUserId(userId);
            fastTradeOrderSearch.setOrderStatus(0);
            if (fastTradeOrderMapper.selectFastTradeOrderList(fastTradeOrderSearch).size() > 0) {
                throw new LangException("hint_96", "存在未结算的极速交易订单");
            }
        }

        //下单金额
        BigDecimal orderPrice = fastTradeOrder.getOrderPrice();
        //交易方向
        Integer orderDirection = fastTradeOrder.getOrderDirection();
        //判断下单金额是否超出允许范围
        BigDecimal minBuyAmount = fastTradeOrderOptions.getMinBuyAmount();
        if (orderPrice.compareTo(minBuyAmount) < 0){
            List<Object> list = new ArrayList<>();
            list.add(minBuyAmount);
            throw new LangException("hint_65",list,"此选项下单金额最小为"+minBuyAmount);
        }
        BigDecimal maxBuyAmount = fastTradeOrderOptions.getMaxBuyAmount();
        if (maxBuyAmount != null && orderPrice.compareTo(maxBuyAmount) > 0){
            List<Object> list = new ArrayList<>();
            list.add(maxBuyAmount);
            throw new LangException("hint_66",list,"此选项下单金额最大为"+maxBuyAmount);
        }
        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //余额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //判断用户余额是否达到下单此选项的要求
        if (userAmountBefore.compareTo(fastTradeOrderOptions.getMinUserAmount()) == -1){
            List<Object> list = new ArrayList<>();
            list.add(fastTradeOrderOptions.getMinUserAmount());
            throw new LangException("hint_16",list,"下单此选项需要余额不小于"+fastTradeOrderOptions.getMinUserAmount());
        }
        //输钱方式：0:全部扣除 1：按收益率扣除
        Integer loseMoneyMethod = fastTradeOrderOptions.getLoseMoneyMethod();
        //前端传入的收益率
        BigDecimal winProfitRatioRandom = fastTradeOrder.getWinProfitRatio();
        //前端传入的输扣除率
        BigDecimal loseProfitRatioRandom = fastTradeOrder.getLoseProfitRatio();
        //买多
        if (orderDirection.equals(0)){
            //针对用户设置的极速赢收益率
            BigDecimal fastTradeWinProfitRatio = userInfo.getFastTradeWinProfitRatio();
            if (fastTradeWinProfitRatio.compareTo(BigDecimal.ZERO) > 0){
                winProfitRatioRandom = fastTradeWinProfitRatio;
            }else {
                //涨赢收益率
                BigDecimal upWinProfitRatio = fastTradeOrderOptions.getUpWinProfitRatio();
                //固定收益率
                if (fastTradeOrderOptions.getProfitRatioMethod().equals(0)){
                    winProfitRatioRandom = upWinProfitRatio;
                }else {
                    //涨赢波动率
                    BigDecimal upFluctuationRatio = fastTradeOrderOptions.getUpFluctuationRatio();
//                    //判断profitRatio是否在波动范围内
//                    if (winProfitRatioRandom.compareTo(upWinProfitRatio.add(upFluctuationRatio)) > 0 || winProfitRatioRandom.compareTo(upWinProfitRatio.subtract(upFluctuationRatio)) < 0){
//                        throw new LangException(HintConstants.SYSTEM_BUSY,"收益率异常");
//                    }
                    BigDecimal min = upWinProfitRatio.subtract(upFluctuationRatio);
                    BigDecimal max = upWinProfitRatio.add(upFluctuationRatio);
                    if (min.compareTo(max) == 0){
                        winProfitRatioRandom = min;
                    }else {
                        winProfitRatioRandom = RandomUtil.randomBigDecimal(min,max);
                    }
                }
            }
            //针对用户设置的极速输扣除率
            BigDecimal fastTradeLoseProfitRatio = userInfo.getFastTradeLoseProfitRatio();
            if (fastTradeLoseProfitRatio.compareTo(BigDecimal.ZERO) > 0){
                loseProfitRatioRandom = fastTradeLoseProfitRatio;
                loseMoneyMethod = 1;
            }else {
                //涨输扣除率
                BigDecimal upLoseProfitRatio = fastTradeOrderOptions.getUpLoseProfitRatio();
                //固定收益率
                if (fastTradeOrderOptions.getProfitRatioMethod().equals(0)){
                    loseProfitRatioRandom = upLoseProfitRatio;
                }else {
                    //涨输波动率
                    BigDecimal upFluctuationRatio = fastTradeOrderOptions.getUpFluctuationRatio();
//                    //判断profitRatio是否在波动范围内
//                    if (loseProfitRatioRandom.compareTo(upLoseProfitRatio.add(upFluctuationRatio)) > 0 || loseProfitRatioRandom.compareTo(upLoseProfitRatio.subtract(upFluctuationRatio)) < 0){
//                        throw new LangException(HintConstants.SYSTEM_BUSY,"收益率异常");
//                    }
                    BigDecimal min = upLoseProfitRatio.subtract(upFluctuationRatio);
                    BigDecimal max = upLoseProfitRatio.add(upFluctuationRatio);
                    if (min.compareTo(max) == 0){
                        loseProfitRatioRandom = min;
                    }else {
                        loseProfitRatioRandom = RandomUtil.randomBigDecimal(min,max);
                    }
                }
            }
        }else {
            //买空
            //针对用户设置的极速赢收益率
            BigDecimal fastTradeWinProfitRatio = userInfo.getFastTradeWinProfitRatio();
            if (fastTradeWinProfitRatio.compareTo(BigDecimal.ZERO) > 0){
                winProfitRatioRandom = fastTradeWinProfitRatio;
            }else {
                //跌赢收益率
                BigDecimal downWinProfitRatio = fastTradeOrderOptions.getDownWinProfitRatio();
                //固定收益率
                if (fastTradeOrderOptions.getProfitRatioMethod().equals(0)){
                    winProfitRatioRandom = downWinProfitRatio;
                }else {
                    //跌赢波动率
                    BigDecimal downFluctuationRatio = fastTradeOrderOptions.getDownFluctuationRatio();
//                    //判断profitRatio是否在波动范围内
//                    if (winProfitRatioRandom.compareTo(downWinProfitRatio.add(downFluctuationRatio)) > 0 || winProfitRatioRandom.compareTo(downWinProfitRatio.subtract(downFluctuationRatio)) < 0){
//                        throw new LangException(HintConstants.SYSTEM_BUSY,"收益率异常");
//                    }
                    BigDecimal min = downWinProfitRatio.subtract(downFluctuationRatio);
                    BigDecimal max = downWinProfitRatio.add(downFluctuationRatio);
                    if (min.compareTo(max) == 0){
                        winProfitRatioRandom = min;
                    }else {
                        winProfitRatioRandom = RandomUtil.randomBigDecimal(min,max);
                    }
                }
            }
            //针对用户设置的极速输扣除率
            BigDecimal fastTradeLoseProfitRatio = userInfo.getFastTradeLoseProfitRatio();
            if (fastTradeLoseProfitRatio.compareTo(BigDecimal.ZERO) > 0){
                loseProfitRatioRandom = fastTradeLoseProfitRatio;
                loseMoneyMethod = 1;
            }else {
                //跌输扣除率
                BigDecimal downLoseProfitRatio = fastTradeOrderOptions.getDownLoseProfitRatio();
                //固定收益率
                if (fastTradeOrderOptions.getProfitRatioMethod().equals(0)){
                    loseProfitRatioRandom = downLoseProfitRatio;
                }else {
                    //跌输波动率
                    BigDecimal downFluctuationRatio = fastTradeOrderOptions.getDownFluctuationRatio();
//                    //判断profitRatio是否在波动范围内
//                    if (loseProfitRatioRandom.compareTo(downLoseProfitRatio.add(downFluctuationRatio)) > 0 || loseProfitRatioRandom.compareTo(downLoseProfitRatio.subtract(downFluctuationRatio)) < 0){
//                        throw new LangException(HintConstants.SYSTEM_BUSY,"收益率异常");
//                    }
                    BigDecimal min = downLoseProfitRatio.subtract(downFluctuationRatio);
                    BigDecimal max = downLoseProfitRatio.add(downFluctuationRatio);
                    if (min.compareTo(max) == 0){
                        loseProfitRatioRandom = min;
                    }else {
                        loseProfitRatioRandom = RandomUtil.randomBigDecimal(min,max);
                    }
                }
            }
        }

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
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCode,false);
            //行情数据
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                buyPrice = new BigDecimal(tickerInfo.getNowPrice());
            }
            //极速交易下单手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("stock_fast_trade_buy_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
        }else if (productType.equals(2)){
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
            //行情数据
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                buyPrice = new BigDecimal(tickerInfo.getNowPrice());
            }
            //极速交易下单手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("cryptocurrency_fast_trade_buy_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
            fastTradeOrder.setProductDesc(product.getProductDesc());
        }else if (productType.equals(3)){
            FuturesProduct product = futuresProductService.selectFuturesProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCode);
            //行情数据
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                buyPrice = new BigDecimal(tickerInfo.getNowPrice());
            }
            //极速交易下单手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("futures_fast_trade_buy_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
        }else if (productType.equals(4)){
            ForexProduct product = forexProductService.selectForexProductByCode(productCode);
            if (product == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
            }
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCode);
            //行情数据
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                buyPrice = new BigDecimal(tickerInfo.getNowPrice());
            }
            //极速交易下单手续费
            OrderFeeSetting stockFastTradeBuyFee = orderFeeSettingService.selectOrderFeeSettingByKey("forex_fast_trade_buy_fee");
            if (stockFastTradeBuyFee != null){
                feeRate = stockFastTradeBuyFee.getFeeRate();
            }
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY,"产品类型错误");
        }
        //判断是否取到了行情
        if (buyPrice == null){
            throw new LangException("hint_QuoteZeroTryAgain","获取该产品行情信息异常，请刷新后重新尝试");
        }

        //日志记录手续费率
        HttpUtils.getRequestLogParams().put("feeRate",feeRate+"%");
        //手续费
        BigDecimal handingFee = orderPrice.multiply(feeRate).divide(new BigDecimal(100), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        if (orderPrice.add(handingFee).compareTo(userAmountBefore) > 0){
            throw new LangException("hint_4","此币种可用资金不足");
        }

        //下单时间
        Date orderTime = new Date();
        //订单玩法时长
        Integer durationValue = fastTradeOrderOptions.getDurationValue();
        //玩法时间（毫秒）
        long ms = 1000*durationValue.longValue();
        //时长标签（0:秒 1：分钟 2：小时 3:天）
        Integer durationLabel = fastTradeOrderOptions.getDurationLabel();
        if (durationLabel.equals(1)){
            ms = ms*60;
        }else if (durationLabel.equals(2)){
            ms = ms*60*60;
        }else if (durationLabel.equals(3)){
            ms = ms*60*60*24;
        }
        //结算时间
        Date deliverTime = new Date(orderTime.getTime() + ms);

        fastTradeOrder.setProductType(productType);
        fastTradeOrder.setOrderCode(CodeUtils.generateOrderCode("F"));
        fastTradeOrder.setUserId(userId);
        fastTradeOrder.setProductCode(productCode);
        fastTradeOrder.setProductName(null);
        fastTradeOrder.setHandingFee(handingFee);
        fastTradeOrder.setOrderTime(orderTime);
        fastTradeOrder.setDurationValue(durationValue);
        fastTradeOrder.setDurationLabel(durationLabel);
        fastTradeOrder.setWinProfitRatio(winProfitRatioRandom);
        fastTradeOrder.setLoseProfitRatio(loseProfitRatioRandom);
        fastTradeOrder.setBuyPrice(buyPrice);
        fastTradeOrder.setSellPrice(null);
        fastTradeOrder.setOrderStatus(0);
        fastTradeOrder.setDeliverTime(deliverTime);
        fastTradeOrder.setOrderProfit(orderPrice.negate());
        fastTradeOrder.setLoseMoneyMethod(loseMoneyMethod);
        fastTradeOrder.setOrderControlFlag(0);
        int insertFastTradeOrder = fastTradeOrderMapper.insertFastTradeOrder(fastTradeOrder);
        if (insertFastTradeOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录极速交易订单信息
        HttpUtils.getRequestLogParams().put("fastTradeOrder", JSONObject.toJSONString(fastTradeOrder));

        String  deType = "";
        String  deSummary = "";
        Integer  orderClass = null;
        String  handingFeeDeType = "";
        String  handingFeeDeSummary = "";
        Integer  handingFeeOrderClass = null;
        //交易方向
        String orderDirectionStr;
        if (orderDirection.equals(0)){
            orderDirectionStr = "买多";
        }else if (orderDirection.equals(1)){
            orderDirectionStr = "买空";
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY,"交易方向异常");
        }
        //时长标签
        String durationLabelStr;
        if (durationLabel.equals(0)){
            durationLabelStr = "秒";
        }else if (durationLabel.equals(1)){
            durationLabelStr = "分钟";
        }else if (durationLabel.equals(2)){
            durationLabelStr = "小时";
        }else if (durationLabel.equals(3)){
            durationLabelStr = "天";
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY,"时长标签异常");
        }
        //交易信息(交易产品+订单时长+订单金额+订单币种)
        String orderInfo = productCode + orderDirectionStr + "[" + durationValue + durationLabelStr + "][" + orderPrice + "]" + currencyName;
        //股票
        if (productType.equals(1)){
            deType = "股票产品极速交易下单";
            deSummary = "股票产品" + orderInfo;
            orderClass = 28;
            handingFeeDeType = "股票产品极速交易下单手续费扣除";
            handingFeeDeSummary = "股票产品极速交易下单手续费扣除";
            handingFeeOrderClass = 57;
        }else if (productType.equals(2)){
            //加密货币
            deType = "加密货币产品极速交易下单";
            deSummary = "加密货币产品" + orderInfo;
            orderClass = 29;
            handingFeeDeType = "加密货币产品极速交易下单手续费扣除";
            handingFeeDeSummary = "加密货币产品极速交易下单手续费扣除";
            handingFeeOrderClass = 58;
        }else if (productType.equals(3)){
            //期货
            deType = "期货产品极速交易下单";
            deSummary = "期货产品" + orderInfo;
            orderClass = 30;
            handingFeeDeType = "期货产品极速交易下单手续费扣除";
            handingFeeDeSummary = "期货产品极速交易下单手续费扣除";
            handingFeeOrderClass = 59;
        }else if (productType.equals(4)){
            //外汇
            deType = "外汇产品极速交易下单";
            deSummary = "外汇产品" + orderInfo;
            orderClass = 48;
            handingFeeDeType = "外汇产品极速交易下单手续费扣除";
            handingFeeDeSummary = "外汇产品极速交易下单手续费扣除";
            handingFeeOrderClass = 60;
        }

        //变更后的加密货币资金 = 变更前-下单金额-手续费
        BigDecimal userAmountAfter = userAmountBefore.subtract(orderPrice);

        //用户流水记录
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType(deType);
        userBillDetail.setDeSummary(deSummary);
        userBillDetail.setOrderAmount(orderPrice.negate());
        userBillDetail.setOrderTime(orderTime);
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(fastTradeOrder.getId());
        userBillDetail.setOrderClass(orderClass);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //如果有手续费2
        if (handingFee.compareTo(BigDecimal.ZERO) > 0){
            //用户流水记录
            UserBillDetail userBillDetail2 = new UserBillDetail();
            userBillDetail2.setUserId(userId);
            userBillDetail2.setDeType(handingFeeDeType);
            userBillDetail2.setDeSummary(handingFeeDeSummary);
            userBillDetail2.setOrderAmount(handingFee.negate());
            userBillDetail2.setOrderTime(orderTime);
            userAmountBefore=userAmountAfter;
            userAmountAfter = userAmountAfter.subtract(handingFee);
            userBillDetail2.setAmountBefore(userAmountBefore);
            userBillDetail2.setAmountAfter(userAmountAfter);
            userBillDetail2.setRelateOrderId(fastTradeOrder.getId());
            userBillDetail2.setOrderClass(handingFeeOrderClass);
            userBillDetail2.setCurrencyId(currencyId);
            int insert2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
            if (insert2 <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
        }

        //变更金额
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //更新打码量
        userInfo.setNeedOrderAmount(userInfo.getNeedOrderAmount().subtract(orderPrice));
        int updateUser = userInfoMapper.updateUserInfo(userInfo);
        if (updateUser <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        // 返佣
        rebate(fastTradeOrder);

        //如果不是真实用户
        if (!userInfo.getAccountType().equals(0)) {
            //如果是模拟账号，不推消息
            if (userInfo.getAccountType().equals(2)) {
                return fastTradeOrder;
            }
            //telegram通知是否只推送真实用户通知
            Integer switchStatusById123 = switchSetService.selectSwitchStatusById(123L);
            if (switchStatusById123.equals(0)){
                return fastTradeOrder;
            }
        }

        //telegram通知（合约下注）
        Integer switchStatusById130 = switchSetService.selectSwitchStatusById(130L);
        if (switchStatusById130.equals(0)) {
            //telegram消息
            String telegramMsg = "\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89合约下注\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\n" +
                    "⏰时间：" + fastTradeOrder.getOrderTime() + "\n" +
                    "ID: " + userInfo.getUserNo() + "\n" +
                    "用户账号: " + userInfo.getUserAccount() + "\n" +
                    "用户昵称: " + userInfo.getNickName() + "\n" +
                    "用户备注: " + userInfo.getRemark() + "\n" +
                    "邀请码: " + userInfo.getInviteCode() + "\n" +
                    "所属代理: " + userInfo.getAgentId() + "/" + userInfo.getAgentName() + "\n" +
                    "代理昵称: " + userInfo.getAgentNickName() + "\n" +
                    "下注金额: " + fastTradeOrder.getOrderPrice().stripTrailingZeros().toPlainString() + platformCurrency.getCurrencyName() + "\n" +
                    "订单号: " + fastTradeOrder.getOrderCode() + "\n" +
                    "余额变更后: " + userAmountAfter.stripTrailingZeros().toPlainString() + platformCurrency.getCurrencyName() + "\n" +
                    "正在进行极速交易,请及时处理！";
            TelegramUtils.sendAsyncMessage(telegramMsg, "default", "default");
        }
        return fastTradeOrder;
    }

    /**
     * 极速交易下单返佣
     * @param  fastTradeOrder 极速交易订单
     */
    void rebate(FastTradeOrder fastTradeOrder){
        // 共有多少个上级需要返利
        Integer rebateNum = CacheUtils.getOtherValueByKey("team_userTeamLevel",Integer.class);
        if (rebateNum == null || rebateNum <= 0){
            return;
        }
        // 需要返佣的上级用户List
        List<UserTeamLevelLine> supTeamLine = userTeamLevelLineMapper.getSupTeamLine(fastTradeOrder.getUserId(), rebateNum, 0);
        // 获取返佣比率
        UserRebateRate userRebateRate = new UserRebateRate();
        userRebateRate.setRebateType(1);
        List<UserRebateRate> userRebateRates = userRebateRateMapper.selectUserRebateRateList(userRebateRate);
        Map<Integer, BigDecimal> RebateRateMap = userRebateRates.stream().collect(Collectors.toMap(UserRebateRate::getRebateLevel, a -> a.getRebateRate()));

        // 订单ID
        Long userRechargeOrderId = fastTradeOrder.getId();
        // 币种
        Long currencyId = fastTradeOrder.getCurrencyId();
        // 下单金额
        BigDecimal orderPrice = fastTradeOrder.getOrderPrice();
        //
        for (int i = 0; i < supTeamLine.size(); i++) {
            // 团队关系信息
            UserTeamLevelLine userTeamLevelLine = supTeamLine.get(i);
            // 上级用户id
            Long supUserId = userTeamLevelLine.getSupUserId();
            // 上级用户信息
            UserInfo supUser = userInfoMapper.selectUserInfoById(supUserId);
            if (supUser == null){
                continue;
            }
            // 等级关系
            Integer teamLevel = userTeamLevelLine.getTeamLevel();
            // 返佣比例
            BigDecimal rebateRate = RebateRateMap.get(teamLevel);
            // 如果返佣比例是空或者比例不大于0，则跳过
            if (rebateRate == null || rebateRate.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }
            // 返利额度
            BigDecimal rebateAmount = orderPrice.multiply(rebateRate).multiply(new BigDecimal("0.01")).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //如果返利金额不大于0，跳过
            if (rebateAmount.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }
            // 获取上级钱包信息
            UserAmount userAmount = userAmountService.getUserAmount(supUserId, currencyId);
            // 余额变更前
            BigDecimal userAmountBefore = userAmount.getAmount();
            // 余额变更后
            BigDecimal userAmountAfter = userAmountBefore.add(rebateAmount);
            // 更新返利后的总额
            userAmount.setAmount(userAmountAfter);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new RuntimeException("系统繁忙");
            }

            // 极速交易下单返佣收入明细
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(supUserId);
            userBillDetail.setDeType("下级极速交易下单返佣");
            userBillDetail.setDeSummary("下级极速交易下单返佣");
            userBillDetail.setOrderAmount(rebateAmount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(userRechargeOrderId);
            userBillDetail.setOrderClass(83);
            userBillDetail.setCurrencyId(userAmount.getCurrencyId());
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail <= 0) {
                throw new RuntimeException("系统繁忙");
            }

            // 返佣记录
            UserCommissionRecord userCommissionRecord = new UserCommissionRecord();
            userCommissionRecord.setSuperId(supUserId);
            userCommissionRecord.setLowerId(fastTradeOrder.getUserId());
            userCommissionRecord.setCommissionLevel(teamLevel);
            userCommissionRecord.setCommissionAmount(rebateAmount);
            userCommissionRecord.setCommissionProfit(rebateRate);
            userCommissionRecord.setOrderCodeSource(fastTradeOrder.getOrderCode());
            userCommissionRecord.setOrderCodeCommission(String.valueOf(userBillDetail.getId()));
            userCommissionRecord.setCreateTime(new Date());
            userCommissionRecord.setCurrencyId(currencyId);
            userCommissionRecord.setCommissionType(2);
            int insertUserCommissionRecord = userCommissionRecordMapper.insertUserCommissionRecord(userCommissionRecord);
            if (insertUserCommissionRecord <= 0){
                throw new RuntimeException();
            }
        }
    }

    /**
     * 股票极速交易订单结算任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stockFastTradeOrderSettleTask(){
        //获取即将结算的订单
        List<FastTradeOrder> fastTradeOrders = fastTradeOrderMapper.getSettlementComingSoonOrder(1,new Date(System.currentTimeMillis() + 1500));
        if (fastTradeOrders.size() == 0){
            return;
        }
        //productCodes
        String productCodes = fastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
        //行情map
        Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCodes,false);
        ExecutorService executorService = Executors.newFixedThreadPool(fastTradeOrders.size());
        try {
            for (int i = 0; i < fastTradeOrders.size(); i++) {
                FastTradeOrder fastTradeOrderVo = fastTradeOrders.get(i);
                executorService.execute(()->{
                    try{
                        fastTradeOrderService.fastTradeOrderSettle(fastTradeOrderVo,tickerInfoMap);
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
     * 加密货币极速交易订单结算任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cryptocurrencyFastTradeOrderSettleTask(){
        //获取即将结算的订单
        List<FastTradeOrder> fastTradeOrders = fastTradeOrderMapper.getSettlementComingSoonOrder(2,new Date(System.currentTimeMillis() + 1500));
//        List<FastTradeOrder> fastTradeOrders = fastTradeOrderMapper.getSettlementComingSoonOrder(2,new Date(System.currentTimeMillis() - 5000));
        if (fastTradeOrders.size() == 0){
            return;
        }
        //productCodes
        String productCodes = fastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
        //行情map
        Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
        ExecutorService executorService = Executors.newFixedThreadPool(fastTradeOrders.size());
        try {
            for (int i = 0; i < fastTradeOrders.size(); i++) {
                FastTradeOrder fastTradeOrderVo = fastTradeOrders.get(i);
                executorService.execute(()->{
                    try{
                        fastTradeOrderService.fastTradeOrderSettle(fastTradeOrderVo,tickerInfoMap);
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
     * 期货极速交易订单结算任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void futuresFastTradeOrderSettleTask(){
        //获取即将结算的订单
        List<FastTradeOrder> fastTradeOrders = fastTradeOrderMapper.getSettlementComingSoonOrder(3,new Date(System.currentTimeMillis() + 1500));
        if (fastTradeOrders.size() == 0){
            return;
        }
        //productCodes
        String productCodes = fastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
        //行情map
        Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCodes);
        ExecutorService executorService = Executors.newFixedThreadPool(fastTradeOrders.size());
        try {
            for (int i = 0; i < fastTradeOrders.size(); i++) {
                FastTradeOrder fastTradeOrderVo = fastTradeOrders.get(i);
                executorService.execute(()->{
                    try{
                        fastTradeOrderService.fastTradeOrderSettle(fastTradeOrderVo,tickerInfoMap);
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
     * 外汇极速交易订单结算任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forexFastTradeOrderSettleTask(){
        //获取即将结算的订单
        List<FastTradeOrder> fastTradeOrders = fastTradeOrderMapper.getSettlementComingSoonOrder(4,new Date(System.currentTimeMillis() + 1500));
        if (fastTradeOrders.size() == 0){
            return;
        }
        //productCodes
        String productCodes = fastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
        //行情map
        Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCodes);
        ExecutorService executorService = Executors.newFixedThreadPool(fastTradeOrders.size());
        try {
            for (int i = 0; i < fastTradeOrders.size(); i++) {
                FastTradeOrder fastTradeOrderVo = fastTradeOrders.get(i);
                executorService.execute(()->{
                    try{
                        fastTradeOrderService.fastTradeOrderSettle(fastTradeOrderVo,tickerInfoMap);
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
     * 极速交易订单结算方法
     * @param fastTradeOrder 订单信息
     * @param tickerInfoMap 行情map
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fastTradeOrderSettle(FastTradeOrder fastTradeOrder,Map<String, TickerInfo> tickerInfoMap) {
        //验证订单是否已结算
        if (!fastTradeOrder.getOrderStatus().equals(0)){
            return;
        }
        //产品代码
        String productCode = fastTradeOrder.getProductCode();
        //产品类型
        Integer productType = fastTradeOrder.getProductType();
        //产品类型名称
        String productTypeName = "";
        if (productType.equals(1)){
            productTypeName = "股票";
        }else if (productType.equals(2)){
            productTypeName = "加密货币";
        }else if (productType.equals(3)){
            productTypeName = "期货";
        }else if (productType.equals(4)){
            productTypeName = "外汇";
        }else {
            throw new RuntimeException("产品类型错误");
        }
        //产品实时行情信息
        TickerInfo tickerInfo = tickerInfoMap.get(productCode);
        if (tickerInfo == null){
            throw new RuntimeException(productTypeName + "极速交易结算出现异常，获取" + productCode + "实时价格异常");
        }
        //用户id
        Long userId = fastTradeOrder.getUserId();
        //现价
        BigDecimal nowPrice = new BigDecimal(tickerInfo.getNowPrice());
        //获取控制价格缓存
        String key = "fastOrderControl/" + userId + "/" + productCode + "/" + productType;
        //控制价格
        Map<String, String> map = redisCache.getCacheObject(key);
        if (map != null){
            String controlPrice = map.get(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, fastTradeOrder.getDeliverTime()));
            if (StringUtils.isNotEmpty(controlPrice)){
                try{
                    nowPrice = new BigDecimal(controlPrice);
                }catch (Exception e){

                }
            }
        }
        //购入时价格
        BigDecimal buyPrice = fastTradeOrder.getBuyPrice();
        //差价=现价-购入时价格
        BigDecimal subtract = nowPrice.subtract(buyPrice);
        //结算时的涨跌方向
        //默认平
        Integer direction = 2;
        //跌
        if (subtract.compareTo(BigDecimal.ZERO) < 0){
            direction = 1;
        }else if (subtract.compareTo(BigDecimal.ZERO) > 0){
            //涨
            direction = 0;
        }
        //购买方向 0:涨 1：跌 2：平
        Integer orderDirection = fastTradeOrder.getOrderDirection();
        //下单金额
        BigDecimal orderPrice = fastTradeOrder.getOrderPrice();
        //收益率
        BigDecimal profitRatio = fastTradeOrder.getWinProfitRatio();
        //币种id
        Long currencyId = fastTradeOrder.getCurrencyId();
        //如果平了
        if (direction.equals(2)){
            //极速交易价格一样时（0：用户输 1：退回 2：用户赢）
            Integer fastTradeResultDraw = CacheUtils.getOtherValueByKey("fast_trade_result_draw", Integer.class);
            if (fastTradeResultDraw == null){
                fastTradeResultDraw = 1;
            }
            if (fastTradeResultDraw.equals(0)){
                //亏损
                //扣除率
                profitRatio = fastTradeOrder.getLoseProfitRatio();
                //输钱方式：0:全部扣除 1：按收益率扣除
                Integer loseMoneyMethod = fastTradeOrder.getLoseMoneyMethod();
                if (loseMoneyMethod.equals(0)){
                    //输了亏损本金
                    fastTradeOrder.setOrderProfit(orderPrice.negate());
                }else {
                    //按收益率亏损
                    //亏损金额
                    BigDecimal loseAmount = orderPrice.multiply(profitRatio).divide(new BigDecimal(100), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    //返还金额
                    BigDecimal returnAmount = orderPrice.subtract(loseAmount);
                    fastTradeOrder.setOrderProfit(loseAmount.negate());
                    //用户钱包信息
                    UserAmount userAmount = userAmountService.getUserAmount(userId,currencyId);
                    //变更前资金
                    BigDecimal userAmountBefore = userAmount.getAmount();
                    //变更后资金 = 变更前资金 + 返还金额
                    BigDecimal userAmountAfter = userAmountBefore.add(returnAmount);
                    userAmount.setAmount(userAmountAfter);
                    int updateUserAmount = userAmountService.updateUserAmount(userAmount);
                    if (updateUserAmount <= 0){
                        throw new RuntimeException(productTypeName + "极速交易结算出现异常，更新用户资金出错");
                    }

                    //用户流水记录(极速交易结算返还部分本金)
                    UserBillDetail userBillDetail = new UserBillDetail();
                    userBillDetail.setUserId(userId);
                    userBillDetail.setDeType(productTypeName + "极速交易返还部分本金");
                    userBillDetail.setDeSummary(productTypeName + "极速交易结算成功，结果为输，返还部分本金");
                    userBillDetail.setOrderAmount(returnAmount);
                    userBillDetail.setOrderTime(new Date());
                    userBillDetail.setAmountBefore(userAmountBefore);
                    userBillDetail.setAmountAfter(userAmountAfter);
                    userBillDetail.setRelateOrderId(fastTradeOrder.getId());
                    userBillDetail.setOrderClass(38);
                    userBillDetail.setCurrencyId(currencyId);
                    int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
                    if (insert <= 0) {
                        throw new RuntimeException(productTypeName + "极速交易结算出现异常，新增极速交易结算返还部分本金明细异常");
                    }
                }
            }else if (fastTradeResultDraw.equals(1)){
                //结果为平,返还本金
                //盈亏是0
                fastTradeOrder.setOrderProfit(BigDecimal.ZERO);
                //钱包信息
                UserAmount userAmount = userAmountService.getUserAmount(userId,currencyId);
                //变更前资金
                BigDecimal userAmountBefore = userAmount.getAmount();
                //变更后资金 = 变更前资金 + 本金返回
                BigDecimal userAmountAfter = userAmountBefore.add(orderPrice);
                userAmount.setAmount(userAmountAfter);
                int updateUserAmount = userAmountService.updateUserAmount(userAmount);
                if (updateUserAmount <= 0){
                    throw new RuntimeException(productTypeName + "极速交易结算出现异常，更新用户资金出错");
                }
                Integer orderClass = null;
                if (productType.equals(1)){
                    orderClass = 49;
                }else if (productType.equals(2)){
                    orderClass = 50;
                }else if (productType.equals(3)){
                    orderClass = 51;
                }else if (productType.equals(4)){
                    orderClass = 52;
                }else {
                    throw new RuntimeException("产品类型错误");
                }
                //用户流水记录(极速交易结算返还本金)
                UserBillDetail userBillDetail = new UserBillDetail();
                userBillDetail.setUserId(userId);
                userBillDetail.setDeType(productTypeName + "极速交易结算返还本金");
                userBillDetail.setDeSummary(productTypeName + "极速交易结算成功，结果为平，返还本金");
                userBillDetail.setOrderAmount(orderPrice);
                userBillDetail.setOrderTime(new Date());
                userBillDetail.setAmountBefore(userAmountBefore);
                userBillDetail.setAmountAfter(userAmountAfter);
                userBillDetail.setRelateOrderId(fastTradeOrder.getId());
                userBillDetail.setOrderClass(orderClass);
                userBillDetail.setCurrencyId(currencyId);
                int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
                if (insert <= 0) {
                    throw new RuntimeException(productTypeName + "极速交易结算出现异常，新增极速交易结算返还本金明细异常");
                }
            }else if (fastTradeResultDraw.equals(2)){
                //盈利
                //收益率
                profitRatio = fastTradeOrder.getWinProfitRatio();
                //盈利金额
                BigDecimal orderProfit = orderPrice.multiply(profitRatio).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                fastTradeOrder.setOrderProfit(orderProfit);
                //用户钱包信息
                UserAmount userAmount = userAmountService.getUserAmount(userId,currencyId);
                //变更前资金
                BigDecimal userAmountBefore = userAmount.getAmount();
                //变更后资金 = 变更前资金 + 盈利 + 本金返回
                BigDecimal userAmountAfter = userAmountBefore.add(orderProfit).add(orderPrice);
                userAmount.setAmount(userAmountAfter);
                int updateUserAmount = userAmountService.updateUserAmount(userAmount);
                if (updateUserAmount <= 0){
                    throw new RuntimeException(productTypeName + "极速交易结算出现异常，更新用户资金出错");
                }

                Integer orderClass1 = null;
                Integer orderClass2 = null;
                if (productType.equals(1)){
                    orderClass1 = 31;
                    orderClass2 = 34;
                }else if (productType.equals(2)){
                    orderClass1 = 32;
                    orderClass2 = 35;
                }else if (productType.equals(3)){
                    orderClass1 = 33;
                    orderClass2 = 36;
                }else if (productType.equals(4)){
                    orderClass1 = 45;
                    orderClass2 = 46;
                }else {
                    throw new RuntimeException("产品类型错误");
                }

                //用户流水记录(极速交易结算返还本金)
                UserBillDetail userBillDetail = new UserBillDetail();
                userBillDetail.setUserId(userId);
                userBillDetail.setDeType(productTypeName + "极速交易结算返还本金");
                userBillDetail.setDeSummary(productTypeName + "极速交易结算成功，结果为赢，返还本金");
                userBillDetail.setOrderAmount(orderPrice);
                userBillDetail.setOrderTime(new Date());
                userBillDetail.setAmountBefore(userAmountBefore);
                userBillDetail.setAmountAfter(userAmountAfter.subtract(orderProfit));
                userBillDetail.setRelateOrderId(fastTradeOrder.getId());
                userBillDetail.setOrderClass(orderClass1);
                userBillDetail.setCurrencyId(currencyId);
                int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
                if (insert <= 0) {
                    throw new RuntimeException(productTypeName + "极速交易结算出现异常，新增极速交易结算返还本金明细异常");
                }

                //用户流水记录(极速交易结算收益收入)
                UserBillDetail userBillDetail2 = new UserBillDetail();
                userBillDetail2.setUserId(userId);
                userBillDetail2.setDeType(productTypeName + "极速交易结算收益收入");
                userBillDetail2.setDeSummary(productTypeName + "极速交易结算成功，结果为赢，获得收益");
                userBillDetail2.setOrderAmount(orderProfit);
                userBillDetail2.setOrderTime(new Date());
                userBillDetail2.setAmountBefore(userAmountAfter.subtract(orderProfit));
                userBillDetail2.setAmountAfter(userAmountAfter);
                userBillDetail2.setRelateOrderId(fastTradeOrder.getId());
                userBillDetail2.setOrderClass(orderClass2);
                userBillDetail2.setCurrencyId(currencyId);
                int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
                if (insertUserBillDetail2 <= 0) {
                    throw new RuntimeException(productTypeName + "极速交易结算出现异常，新增极速交易结算收益收入异常");
                }
            }else {
                throw new RuntimeException(productTypeName + "极速交易结算出现异常，极速交易价格一样时的处理方式获取异常");
            }
        }else {
            if (direction.equals(orderDirection)){
                //盈利
                //收益率
                profitRatio = fastTradeOrder.getWinProfitRatio();
                //盈利金额
                BigDecimal orderProfit = orderPrice.multiply(profitRatio).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                fastTradeOrder.setOrderProfit(orderProfit);
                //用户钱包信息
                UserAmount userAmount = userAmountService.getUserAmount(userId,currencyId);
                //变更前资金
                BigDecimal userAmountBefore = userAmount.getAmount();
                //变更后资金 = 变更前资金 + 盈利 + 本金返回
                BigDecimal userAmountAfter = userAmountBefore.add(orderProfit).add(orderPrice);
                userAmount.setAmount(userAmountAfter);
                int updateUserAmount = userAmountService.updateUserAmount(userAmount);
                if (updateUserAmount <= 0){
                    throw new RuntimeException(productTypeName + "极速交易结算出现异常，更新用户资金出错");
                }
                Integer orderClass1 = null;
                Integer orderClass2 = null;
                if (productType.equals(1)){
                    orderClass1 = 31;
                    orderClass2 = 34;
                }else if (productType.equals(2)){
                    orderClass1 = 32;
                    orderClass2 = 35;
                }else if (productType.equals(3)){
                    orderClass1 = 33;
                    orderClass2 = 36;
                }else if (productType.equals(4)){
                    orderClass1 = 45;
                    orderClass2 = 46;
                }else {
                    throw new RuntimeException("产品类型错误");
                }
                //用户流水记录(极速交易结算返还本金)
                UserBillDetail userBillDetail = new UserBillDetail();
                userBillDetail.setUserId(userId);
                userBillDetail.setDeType(productTypeName + "极速交易结算返还本金");
                userBillDetail.setDeSummary(productTypeName + "极速交易结算成功，结果为赢，返还本金");
                userBillDetail.setOrderAmount(orderPrice);
                userBillDetail.setOrderTime(new Date());
                userBillDetail.setAmountBefore(userAmountBefore);
                userBillDetail.setAmountAfter(userAmountAfter.subtract(orderProfit));
                userBillDetail.setRelateOrderId(fastTradeOrder.getId());
                userBillDetail.setOrderClass(orderClass1);
                userBillDetail.setCurrencyId(currencyId);
                int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
                if (insert <= 0) {
                    throw new RuntimeException(productTypeName + "极速交易结算出现异常，新增极速交易结算返还本金明细异常");
                }

                //用户流水记录(极速交易结算收益收入)
                UserBillDetail userBillDetail2 = new UserBillDetail();
                userBillDetail2.setUserId(userId);
                userBillDetail2.setDeType(productTypeName + "极速交易结算收益收入");
                userBillDetail2.setDeSummary(productTypeName + "极速交易结算成功，结果为赢，获得收益");
                userBillDetail2.setOrderAmount(orderProfit);
                userBillDetail2.setOrderTime(new Date());
                userBillDetail2.setAmountBefore(userAmountAfter.subtract(orderProfit));
                userBillDetail2.setAmountAfter(userAmountAfter);
                userBillDetail2.setRelateOrderId(fastTradeOrder.getId());
                userBillDetail2.setOrderClass(orderClass2);
                userBillDetail2.setCurrencyId(currencyId);
                int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
                if (insertUserBillDetail2 <= 0) {
                    throw new RuntimeException(productTypeName + "极速交易结算出现异常，新增极速交易结算收益收入异常");
                }
            } else {
                //亏损
                //扣除率
                profitRatio = fastTradeOrder.getLoseProfitRatio();
                //输钱方式：0:全部扣除 1：按收益率扣除
                Integer loseMoneyMethod = fastTradeOrder.getLoseMoneyMethod();
                if (loseMoneyMethod.equals(0)){
                    //输了亏损本金
                    fastTradeOrder.setOrderProfit(orderPrice.negate());
                }else {
                    //按收益率亏损
                    //亏损金额
                    BigDecimal loseAmount = orderPrice.multiply(profitRatio).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    //返还金额
                    BigDecimal returnAmount = orderPrice.subtract(loseAmount);
                    fastTradeOrder.setOrderProfit(loseAmount.negate());
                    //用户钱包信息
                    UserAmount userAmount = userAmountService.getUserAmount(userId,currencyId);
                    //变更前资金
                    BigDecimal userAmountBefore = userAmount.getAmount();
                    //变更后资金 = 变更前资金 + 返还金额
                    BigDecimal userAmountAfter = userAmountBefore.add(returnAmount);
                    userAmount.setAmount(userAmountAfter);
                    int updateUserAmount = userAmountService.updateUserAmount(userAmount);
                    if (updateUserAmount <= 0){
                        throw new RuntimeException(productTypeName + "极速交易结算出现异常，更新用户资金出错");
                    }

                    //用户流水记录(极速交易结算返还部分本金)
                    UserBillDetail userBillDetail = new UserBillDetail();
                    userBillDetail.setUserId(userId);
                    userBillDetail.setDeType(productTypeName + "极速交易返还部分本金");
                    userBillDetail.setDeSummary(productTypeName + "极速交易结算成功，结果为输，返还部分本金");
                    userBillDetail.setOrderAmount(returnAmount);
                    userBillDetail.setOrderTime(new Date());
                    userBillDetail.setAmountBefore(userAmountBefore);
                    userBillDetail.setAmountAfter(userAmountAfter);
                    userBillDetail.setRelateOrderId(fastTradeOrder.getId());
                    userBillDetail.setOrderClass(38);
                    userBillDetail.setCurrencyId(currencyId);
                    int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
                    if (insert <= 0) {
                        throw new RuntimeException(productTypeName + "极速交易结算出现异常，新增极速交易结算返还部分本金明细异常");
                    }
                }
            }
        }
        //卖出时价格
        fastTradeOrder.setSellPrice(nowPrice);
        //变更状态为已结算
        fastTradeOrder.setOrderStatus(1);
        int updateFastTradeOrder = fastTradeOrderMapper.updateFastTradeOrder(fastTradeOrder);
        if (updateFastTradeOrder <= 0){
            throw new RuntimeException(productTypeName + "极速交易结算出现异常，更新极速交易订单信息出错");
        }
    }

    /**
     * 股票极速交易控制定时器
     */
    @Override
    public void stockFastTradeOrderControlTask() {
        //产品类型
        Integer productType = 1;
        //获取即将结算的订单
        List<FastTradeOrder> fastTradeOrders = fastTradeOrderMapper.getSettlementComingSoonOrder(productType,new Date(System.currentTimeMillis() + 10*1000));
        if (fastTradeOrders.size() == 0){
            return;
        }
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(fastTradeOrders.size());
        try {
            //productCodes
            String productCodes = fastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getStockQuote(productCodes,false);
            //随机差大小金额
            BigDecimal randomDiffAmount;
            //随机差大小开关
            Integer switchValue = CacheUtils.getOtherValueByKey("switch.randomDifferenceSizeAmount",Integer.class);
            //随机差大小开关开启
            if (switchValue != null && switchValue.equals(0)){
                //随机差大小金额
                randomDiffAmount = CacheUtils.getOtherValueByKey("amount.randomDifferenceSizeAmount",BigDecimal.class);
            } else {
                randomDiffAmount = null;
            }
            //极速交易用户控制信息
            //涉及用户ids
            List<Long> userIds = fastTradeOrders.stream().map(FastTradeOrder::getUserId).distinct().collect(Collectors.toList());
            //获取涉及用户的极速交易用户控制配置
            UserFastTradeControl userFastTradeControl = new UserFastTradeControl();
            userFastTradeControl.getParams().put("userIds",userIds);
            Map<Long, UserFastTradeControl> userFastTradeControls = userFastTradeControlMapper.selectUserFastTradeControlList(userFastTradeControl).stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
            //极速交易群控配置
            FastOrderControlConfig fastOrderControlConfig = new FastOrderControlConfig();
            fastOrderControlConfig.setProductType(productType);
            fastOrderControlConfig.getParams().put("productCodes",Arrays.asList(productCodes.split(",")));
            //获取涉及产品的极速交易产品群控控制配置
            List<FastOrderControlConfig> fastOrderControlConfigs = fastOrderControlConfigMapper.selectFastOrderControlConfigList(fastOrderControlConfig);
            for (int i = 0; i < fastTradeOrders.size(); i++) {
                FastTradeOrder fastTradeOrderVo = fastTradeOrders.get(i);
                executorService.execute(()->{
                    try{
                        //极速交易用户控制配置
                        UserFastTradeControl userFastTradeControlVo = userFastTradeControls.get(fastTradeOrderVo.getUserId());
                        //极速交易产品群控控制配置
                        List<FastOrderControlConfig> fastOrderControlConfigVos = fastOrderControlConfigs.stream().filter(a -> a.getProductCode().equals(fastTradeOrderVo.getProductCode())).collect(Collectors.toList());
                        this.doStockFastTradeOrderControlTask(fastTradeOrderVo,tickerInfoMap,userFastTradeControlVo,fastOrderControlConfigVos,randomDiffAmount);
                    }catch (Exception e){
                        log.error("极速交易订单："+fastTradeOrderVo.getOrderCode()+"控制异常，时间"+DateUtils.getTime()+"信息"+e.getMessage());
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
     * 加密货币极速交易控制定时器
     */
    @Override
    public void cryptocurrencyFastTradeOrderControlTask() {
        //产品类型
        Integer productType = 2;
        //获取即将结算的订单
        List<FastTradeOrder> fastTradeOrders = fastTradeOrderMapper.getSettlementComingSoonOrder(productType,new Date(System.currentTimeMillis() + 10*1000));
        if (fastTradeOrders.size() == 0){
            return;
        }
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(fastTradeOrders.size());
        try {
            //productCodes
            String productCodes = fastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
            //随机差大小金额
            BigDecimal randomDiffAmount;
            //随机差大小开关
            Integer switchValue = CacheUtils.getOtherValueByKey("switch.randomDifferenceSizeAmount",Integer.class);
            //随机差大小开关开启
            if (switchValue != null && switchValue.equals(0)){
                //随机差大小金额
                randomDiffAmount = CacheUtils.getOtherValueByKey("amount.randomDifferenceSizeAmount",BigDecimal.class);
            } else {
                randomDiffAmount = null;
            }
            //极速交易用户控制信息
            //涉及用户ids
            List<Long> userIds = fastTradeOrders.stream().map(FastTradeOrder::getUserId).distinct().collect(Collectors.toList());
            //获取涉及用户的极速交易用户控制配置
            UserFastTradeControl userFastTradeControl = new UserFastTradeControl();
            userFastTradeControl.getParams().put("userIds",userIds);
            Map<Long, UserFastTradeControl> userFastTradeControls = userFastTradeControlMapper.selectUserFastTradeControlList(userFastTradeControl).stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
            //极速交易群控配置
            FastOrderControlConfig fastOrderControlConfig = new FastOrderControlConfig();
            fastOrderControlConfig.setProductType(productType);
            fastOrderControlConfig.getParams().put("productCodes",Arrays.asList(productCodes.split(",")));
            //获取涉及产品的极速交易产品群控控制配置
            List<FastOrderControlConfig> fastOrderControlConfigs = fastOrderControlConfigMapper.selectFastOrderControlConfigList(fastOrderControlConfig);
            for (int i = 0; i < fastTradeOrders.size(); i++) {
                FastTradeOrder fastTradeOrderVo = fastTradeOrders.get(i);
                executorService.execute(()->{
                    try{
                        //极速交易用户控制配置
                        UserFastTradeControl userFastTradeControlVo = userFastTradeControls.get(fastTradeOrderVo.getUserId());
                        //极速交易产品群控控制配置
                        List<FastOrderControlConfig> fastOrderControlConfigVos = fastOrderControlConfigs.stream().filter(a -> a.getProductCode().equals(fastTradeOrderVo.getProductCode())).collect(Collectors.toList());
                        this.doStockFastTradeOrderControlTask(fastTradeOrderVo,tickerInfoMap,userFastTradeControlVo,fastOrderControlConfigVos,randomDiffAmount);
                    }catch (Exception e){
                        log.error("极速交易订单："+fastTradeOrderVo.getOrderCode()+"控制异常，时间"+DateUtils.getTime()+"信息："+e.getMessage());
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
     * 期货极速交易控制定时器
     */
    @Override
    public void futuresFastTradeOrderControlTask() {
        //产品类型
        Integer productType = 3;
        //获取即将结算的订单
        List<FastTradeOrder> fastTradeOrders = fastTradeOrderMapper.getSettlementComingSoonOrder(productType,new Date(System.currentTimeMillis() + 10*1000));
        if (fastTradeOrders.size() == 0){
            return;
        }
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(fastTradeOrders.size());
        try {
            //productCodes
            String productCodes = fastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCodes);
            //随机差大小金额
            BigDecimal randomDiffAmount;
            //随机差大小开关
            Integer switchValue = CacheUtils.getOtherValueByKey("switch.randomDifferenceSizeAmount",Integer.class);
            //随机差大小开关开启
            if (switchValue != null && switchValue.equals(0)){
                //随机差大小金额
                randomDiffAmount = CacheUtils.getOtherValueByKey("amount.randomDifferenceSizeAmount",BigDecimal.class);
            } else {
                randomDiffAmount = null;
            }
            //极速交易用户控制信息
            //涉及用户ids
            List<Long> userIds = fastTradeOrders.stream().map(FastTradeOrder::getUserId).distinct().collect(Collectors.toList());
            //获取涉及用户的极速交易用户控制配置
            UserFastTradeControl userFastTradeControl = new UserFastTradeControl();
            userFastTradeControl.getParams().put("userIds",userIds);
            Map<Long, UserFastTradeControl> userFastTradeControls = userFastTradeControlMapper.selectUserFastTradeControlList(userFastTradeControl).stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
            //极速交易群控配置
            FastOrderControlConfig fastOrderControlConfig = new FastOrderControlConfig();
            fastOrderControlConfig.setProductType(productType);
            fastOrderControlConfig.getParams().put("productCodes",Arrays.asList(productCodes.split(",")));
            //获取涉及产品的极速交易产品群控控制配置
            List<FastOrderControlConfig> fastOrderControlConfigs = fastOrderControlConfigMapper.selectFastOrderControlConfigList(fastOrderControlConfig);
            for (int i = 0; i < fastTradeOrders.size(); i++) {
                FastTradeOrder fastTradeOrderVo = fastTradeOrders.get(i);
                executorService.execute(()->{
                    try{
                        //极速交易用户控制配置
                        UserFastTradeControl userFastTradeControlVo = userFastTradeControls.get(fastTradeOrderVo.getUserId());
                        //极速交易产品群控控制配置
                        List<FastOrderControlConfig> fastOrderControlConfigVos = fastOrderControlConfigs.stream().filter(a -> a.getProductCode().equals(fastTradeOrderVo.getProductCode())).collect(Collectors.toList());
                        this.doStockFastTradeOrderControlTask(fastTradeOrderVo,tickerInfoMap,userFastTradeControlVo,fastOrderControlConfigVos,randomDiffAmount);
                    }catch (Exception e){
                        log.error("极速交易订单："+fastTradeOrderVo.getOrderCode()+"控制异常，时间"+DateUtils.getTime()+"信息"+e.getMessage());
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
     * 外汇极速交易控制定时器
     */
    @Override
    public void forexFastTradeOrderControlTask() {
        //产品类型
        Integer productType = 4;
        //获取即将结算的订单
        List<FastTradeOrder> fastTradeOrders = fastTradeOrderMapper.getSettlementComingSoonOrder(productType,new Date(System.currentTimeMillis() + 10*1000));
        if (fastTradeOrders.size() == 0){
            return;
        }
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(fastTradeOrders.size());
        try {
            //productCodes
            String productCodes = fastTradeOrders.stream().map(FastTradeOrder::getProductCode).distinct().collect(Collectors.joining(","));
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCodes);
            //随机差大小金额
            BigDecimal randomDiffAmount;
            //随机差大小开关
            Integer switchValue = CacheUtils.getOtherValueByKey("switch.randomDifferenceSizeAmount",Integer.class);
            //随机差大小开关开启
            if (switchValue != null && switchValue.equals(0)){
                //随机差大小金额
                randomDiffAmount = CacheUtils.getOtherValueByKey("amount.randomDifferenceSizeAmount",BigDecimal.class);
            } else {
                randomDiffAmount = null;
            }
            //极速交易用户控制信息
            //涉及用户ids
            List<Long> userIds = fastTradeOrders.stream().map(FastTradeOrder::getUserId).distinct().collect(Collectors.toList());
            //获取涉及用户的极速交易用户控制配置
            UserFastTradeControl userFastTradeControl = new UserFastTradeControl();
            userFastTradeControl.getParams().put("userIds",userIds);
            Map<Long, UserFastTradeControl> userFastTradeControls = userFastTradeControlMapper.selectUserFastTradeControlList(userFastTradeControl).stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
            //极速交易群控配置
            FastOrderControlConfig fastOrderControlConfig = new FastOrderControlConfig();
            fastOrderControlConfig.setProductType(productType);
            fastOrderControlConfig.getParams().put("productCodes",Arrays.asList(productCodes.split(",")));
            //获取涉及产品的极速交易产品群控控制配置
            List<FastOrderControlConfig> fastOrderControlConfigs = fastOrderControlConfigMapper.selectFastOrderControlConfigList(fastOrderControlConfig);
            for (int i = 0; i < fastTradeOrders.size(); i++) {
                FastTradeOrder fastTradeOrderVo = fastTradeOrders.get(i);
                executorService.execute(()->{
                    try{
                        //极速交易用户控制配置
                        UserFastTradeControl userFastTradeControlVo = userFastTradeControls.get(fastTradeOrderVo.getUserId());
                        //极速交易产品群控控制配置
                        List<FastOrderControlConfig> fastOrderControlConfigVos = fastOrderControlConfigs.stream().filter(a -> a.getProductCode().equals(fastTradeOrderVo.getProductCode())).collect(Collectors.toList());
                        this.doStockFastTradeOrderControlTask(fastTradeOrderVo,tickerInfoMap,userFastTradeControlVo,fastOrderControlConfigVos,randomDiffAmount);
                    }catch (Exception e){
                        log.error("极速交易订单："+fastTradeOrderVo.getOrderCode()+"控制异常，时间"+DateUtils.getTime()+"信息"+e.getMessage());
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

    void doStockFastTradeOrderControlTask(FastTradeOrder fastTradeOrder,Map<String, TickerInfo> tickerInfoMap,UserFastTradeControl userFastTradeControl,List<FastOrderControlConfig> fastOrderControlConfigs,BigDecimal randomDiffAmount){
        //产品类型
        Integer productType = fastTradeOrder.getProductType();
        //订单key
        String orderIdKey = "fastOrderControl/"+fastTradeOrder.getId();
        //如果该订单控制数据已经生成过，则跳过
        if (redisCache.getCacheObject(orderIdKey) != null){
            return;
        }
        //产品代码
        String productCode = fastTradeOrder.getProductCode();
        //现价
        BigDecimal nowPrice = BigDecimal.ZERO;
        //行情信息
        TickerInfo tickerInfo = tickerInfoMap.get(productCode);
        if (tickerInfo != null){
            nowPrice = new BigDecimal(tickerInfo.getNowPrice());
        }
        if (nowPrice.compareTo(BigDecimal.ZERO) == 0){
            throw new RuntimeException("极速交易控制定时任务获取"+productCode+"/"+productType+"行情信息异常，异常原因：未获取到当前行情价格");
        }
        //用户id
        Long userId = fastTradeOrder.getUserId();
        //币种id
        Long currencyId = fastTradeOrder.getCurrencyId();
        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId,currencyId);
        //控制数据(key是时间，value是价格)
        Map<String,String> orderControlPriceMap = TimeControlUtil.generalOrderControlPriceList(fastTradeOrder, nowPrice,userFastTradeControl,fastOrderControlConfigs,randomDiffAmount,userAmount);
        //key
        String key = "fastOrderControl/" + fastTradeOrder.getUserId() + "/" + productCode + "/" + productType;
        //缓存中的控制数据
        Map<String,String> map = redisCache.getCacheObject(key);
        if (map == null){
            redisCache.setCacheObject(key,orderControlPriceMap,30, TimeUnit.SECONDS);
        }else {
            //如果数据不冲突，则缓加入缓存
            for (String mapKey : orderControlPriceMap.keySet()) {
                if (!map.containsKey(mapKey)){
                    map.put(mapKey,orderControlPriceMap.get(mapKey));
                }
            }
            redisCache.setCacheObject(key,map,30,TimeUnit.SECONDS);
        }
        //存储订单key，用来识别该订单是否已经生成过控制数据
        redisCache.setCacheObject(orderIdKey, orderIdKey,30, TimeUnit.SECONDS);
    }
}
