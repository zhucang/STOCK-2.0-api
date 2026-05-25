package com.ruoyi.system.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.ICopyTradeOrderService;
import com.ruoyi.system.service.ICopyTradeSyncTaskService;
import com.ruoyi.system.service.IPlatformCurrencyService;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserAmountService;
import com.ruoyi.system.service.IUserCryptocurrencyPositionService;
import com.ruoyi.system.utils.BuyAndSellUtils;
import com.ruoyi.system.utils.ForceSellUtils;
import com.ruoyi.system.utils.ProductQuoteUtils;
import com.ruoyi.system.utils.UserApiKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 用户加密货币持仓Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
@Service
public class UserCryptocurrencyPositionServiceImpl implements IUserCryptocurrencyPositionService 
{
    @Resource
    private UserCryptocurrencyPositionMapper userCryptocurrencyPositionMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private ProductTradeTimeSettingMapper productTradeTimeSettingMapper;

    @Resource
    private CryptocurrencyProductMapper cryptocurrencyProductMapper;

    @Resource
    private ProductSettingMapper productSettingMapper;

    @Autowired
    private IUserAmountService userAmountService;

    @Resource
    private OrderFeeSettingMapper orderFeeSettingMapper;

    @Resource
    private UserBillDetailMapper userBillDetailMapper;

    @Autowired
    private IUserCryptocurrencyPositionService userCryptocurrencyPositionService;

    @Resource
    private ScheduledTaskExceptionLogMapper scheduledTaskExceptionLogMapper;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Lazy
    @Autowired
    private ICopyTradeSyncTaskService copyTradeSyncTaskService;

    @Lazy
    @Resource
    private ICopyTradeOrderService copyTradeOrderService;

    /**
     * 查询用户加密货币持仓
     * 
     * @param id 用户加密货币持仓主键
     * @return 用户加密货币持仓
     */
    @Override
    public UserCryptocurrencyPosition selectUserCryptocurrencyPositionById(Long id)
    {
        return userCryptocurrencyPositionMapper.selectUserCryptocurrencyPositionById(id);
    }

    /**
     * 查询用户加密货币持仓列表
     * 
     * @param userCryptocurrencyPosition 用户加密货币持仓
     * @return 用户加密货币持仓
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserCryptocurrencyPosition> selectUserCryptocurrencyPositionList(UserCryptocurrencyPosition userCryptocurrencyPosition)
    {
        return userCryptocurrencyPositionMapper.selectUserCryptocurrencyPositionList(userCryptocurrencyPosition);
    }

    /**
     * 填充其他信息
     * @param positions 股票持仓列表
     */
    @Override
    public void fillOtherInfo(List<UserCryptocurrencyPosition> positions){
        fillProductQuote(positions);
    }

    /**
     * 填充行情信息
     * @param positions 产品订单列表
     */
    void fillProductQuote(List<UserCryptocurrencyPosition> positions){
        if (positions.size() == 0){
            return;
        }
        //productCodes
        String productCodes = positions.stream().filter(a->a.getOrderStatus().equals(0)).map(UserCryptocurrencyPosition::getProductCode).distinct().collect(Collectors.joining(","));
        //行情map
        Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
        for (int i = 0; i < positions.size(); i++) {
            //持仓信息
            UserCryptocurrencyPosition position = positions.get(i);
            //如果持仓中
            if (position.getOrderStatus().equals(0)){
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(position.getProductCode());
                if (tickerInfo != null){
                    //现价
                    BigDecimal nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                    position.setNowPrice(nowPrice);
                    //购买时候的市值
                    BigDecimal orderTotalPrice = position.getOrderTotalPrice();
                    //现在的市值
                    BigDecimal orderTotalPriceNow = nowPrice.multiply(position.getOrderNum()).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    //如果持仓中，计算浮动盈亏
                    if (position.getOrderDirection().equals(0)) {
                        position.setProfitAndLose(orderTotalPriceNow.subtract(orderTotalPrice));
                    } else {
                        position.setProfitAndLose(orderTotalPrice.subtract(orderTotalPriceNow));
                    }
                    //总盈亏 = 浮动盈亏 - 手续费
                    position.setAllProfitAndLose(position.getProfitAndLose().subtract(position.getOrderFee()));
                }
            }
        }
    }


    /**
     * 新增用户加密货币持仓
     * 
     * @param userCryptocurrencyPosition 用户加密货币持仓
     * @return 结果
     */
    @Override
    public int insertUserCryptocurrencyPosition(UserCryptocurrencyPosition userCryptocurrencyPosition)
    {
        return userCryptocurrencyPositionMapper.insertUserCryptocurrencyPosition(userCryptocurrencyPosition);
    }

    /**
     * 修改用户加密货币持仓
     * 
     * @param userCryptocurrencyPosition 用户加密货币持仓
     * @return 结果
     */
    @Override
    public int updateUserCryptocurrencyPosition(UserCryptocurrencyPosition userCryptocurrencyPosition)
    {
        return userCryptocurrencyPositionMapper.updateUserCryptocurrencyPosition(userCryptocurrencyPosition);
    }

    /**
     * 批量删除用户加密货币持仓
     * 
     * @param ids 需要删除的用户加密货币持仓主键
     * @return 结果
     */
    @Override
    public int deleteUserCryptocurrencyPositionByIds(Long[] ids)
    {
        return userCryptocurrencyPositionMapper.deleteUserCryptocurrencyPositionByIds(ids);
    }

    /**
     * 删除用户加密货币持仓信息
     * 
     * @param id 用户加密货币持仓主键
     * @return 结果
     */
    @Override
    public int deleteUserCryptocurrencyPositionById(Long id)
    {
        return userCryptocurrencyPositionMapper.deleteUserCryptocurrencyPositionById(id);
    }

    /**
     * 获取保证金金额
     * @param userId 用户id
     * @param positions 用户持仓
     * @return
     */
    @Override
    public BigDecimal getAllMarginAmountAmountByUserId(Long userId,List<UserCryptocurrencyPosition> positions){
        if (positions == null){
            if (userId == null){
                return BigDecimal.ZERO;
            }
            //获取持仓中的订单
            UserCryptocurrencyPosition position = new UserCryptocurrencyPosition();
            position.setUserId(userId);
            position.setOrderStatus(0);
            positions = userCryptocurrencyPositionMapper.selectUserCryptocurrencyPositionList(position);
        }
        BigDecimal allMarginAmount = positions.stream().map(a -> a.getOrderTotalPrice().divide(new BigDecimal(a.getOrderLever()),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE)).reduce(BigDecimal.ZERO, BigDecimal::add);
        return allMarginAmount;
    }

    /**
     * 获取总盈亏
     * @param userId 用户id
     * @param positions 用户持仓
     * @param tickerInfoMap 行情map
     * @return
     */
    public BigDecimal getAllProfitAndLoseByUserId(Long userId, List<UserCryptocurrencyPosition> positions, Map<String, TickerInfo> tickerInfoMap){
        if (positions == null){
            if (userId == null){
                return BigDecimal.ZERO;
            }
            //获取持仓中的订单
            UserCryptocurrencyPosition position = new UserCryptocurrencyPosition();
            position.setUserId(userId);
            position.setOrderStatus(0);
            positions = userCryptocurrencyPositionMapper.selectUserCryptocurrencyPositionList(position);
            if (positions.size() == 0){
                return BigDecimal.ZERO;
            }
        }
        //总盈亏
        BigDecimal allProfitAndLose = BigDecimal.ZERO;
        for (int i = 0; i < positions.size(); i++) {
            //持仓信息
            UserCryptocurrencyPosition position = positions.get(i);
            //产品代码
            String productCode = position.getProductCode();
            //行情信息
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                //现价
                BigDecimal nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                if (nowPrice.compareTo(BigDecimal.ZERO) == 0){
                    return BigDecimal.ZERO;
                }
                //购买时市值
                BigDecimal totalPriceBefore = position.getOrderTotalPrice();
                //当前市值
                BigDecimal totalPriceNow = nowPrice.multiply(position.getOrderNum()).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                //浮动盈亏
                BigDecimal profitAndLose = totalPriceNow.subtract(totalPriceBefore);
                //总盈亏总合
                allProfitAndLose = allProfitAndLose.add(profitAndLose).subtract(position.getOrderFee()).subtract(position.getOrderYhsFee());
            }
        }
        return allProfitAndLose;
    }

    /**
     * 用户持仓锁仓、解仓操作
     * @param positionId 持仓id
     * @param lockStatus 锁定状态 0：解锁 1：锁定
     * @param lockMsg
     * @return
     */
    @Override
    public AjaxResult lockUserPosition(Long positionId, Integer lockStatus, String lockMsg){
        //持仓信息
        UserCryptocurrencyPosition position = userCryptocurrencyPositionMapper.selectUserCryptocurrencyPositionById(positionId);
        if (position == null) {
            return AjaxResult.error("获取持仓信息异常");
        }
        if (position.getOrderStatus().equals(1)) {
            return AjaxResult.error("平仓单不能锁仓");
        }
//        if (lockStatus.equals(1) && StringUtils.isEmpty(lockMsg)) {
//            return AjaxResult.error("锁仓提示信息必填");
//        }

        UserCryptocurrencyPosition positionVo = new UserCryptocurrencyPosition();
        positionVo.setId(positionId);
        positionVo.setIsLock(lockStatus);
        positionVo.setLockMsg(lockMsg);
        positionVo.setSqlVersion(position.getSqlVersion());
        int count = userCryptocurrencyPositionMapper.updateUserCryptocurrencyPosition(positionVo);
        if (count <= 0) {
            return AjaxResult.error("系统繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 强制平仓操作
     * @param positionId 持仓id
     * @param sellMode 平仓模式 0：平仓价格平仓 1：盈亏比例平仓 2：盈亏金额平仓
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult forceSell(Long positionId,Integer sellMode,BigDecimal target){
        //持仓信息
        UserCryptocurrencyPosition position = userCryptocurrencyPositionMapper.selectUserCryptocurrencyPositionById(positionId);
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
            //购买数量
            BigDecimal orderNum = position.getOrderNum();
            //购买时总市值
            BigDecimal orderTotalPrice = position.getOrderTotalPrice();
            //浮动盈亏
            BigDecimal profitAndLose = orderTotalPrice.multiply(target).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //目前市值
            BigDecimal allSellAmt = BigDecimal.ZERO;
            if (position.getOrderDirection().equals(0)) {
                allSellAmt = orderTotalPrice.add(profitAndLose);
            } else {
                allSellAmt = orderTotalPrice.subtract(profitAndLose);
            }
            //现价
            nowPrice = allSellAmt.divide(orderNum,Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        }else if (sellMode.equals(2)){
            //购买数量
            BigDecimal orderNum = position.getOrderNum();
            //购买时总市值
            BigDecimal orderTotalPrice = position.getOrderTotalPrice();
            //浮动盈亏
            BigDecimal profitAndLose = target;
            //目前市值
            BigDecimal allSellAmt = BigDecimal.ZERO;
            if (position.getOrderDirection().equals(0)) {
                allSellAmt = orderTotalPrice.add(profitAndLose);
            } else {
                allSellAmt = orderTotalPrice.subtract(profitAndLose);
            }
            //现价
            nowPrice = allSellAmt.divide(orderNum,Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        }else {
            throw new ServiceException("平仓模式错误");
        }
        return sell(positionId,0,nowPrice);
    }

    /**
     * 用户加密货币合约交易下单
     * @param position
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int buy(UserCryptocurrencyPosition position){
        // 用户端普通开仓仍然走原有交易校验和扣款流程。
        Long userId = UserApiKeyUtils.getUserId();
        createPosition(position, userId);
        try {
            // 如果当前用户同时是交易员，则在主单创建成功后触发跟单同步。
            copyTradeSyncTaskService.enqueueLeaderOpenSyncTasks(CopyTradePositionSnapshot.PRODUCT_TYPE_CRYPTOCURRENCY, position.getId());
        } catch (Exception e) {
            // 跟单同步失败不应该影响交易员本人开仓，因此这里只记录异常。
            recordCopyTradeSyncError("跟单开仓触发失败", position.getId(), e);
        }
        return 1;
    }

    /**
     * 为跟单用户开出一笔和交易员主单方向一致的仓位。
     *
     * @param followerUserId 跟单用户ID
     * @param leaderPosition 交易员主仓位
     * @param relation 跟单关系(跟单人员)配置
     * @return 跟单仓位
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCryptocurrencyPosition openCopyTradePosition(Long followerUserId, CopyTradePositionSnapshot leaderPosition, CopyTradeRelation relation) {
        // 先换算出交易员主单对应的保证金，作为按比例模式的计算基准。
        BigDecimal leaderMarginAmount = leaderPosition.getOrderTotalPrice()
                .divide(new BigDecimal(leaderPosition.getOrderLever()), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        BigDecimal marginAmount;
        if (relation.getFollowMode().equals(0)) {
            // 固定金额模式直接使用配置金额。
            marginAmount = relation.getFollowAmount();
        } else {
            // 比例模式下，按主单保证金 * 比例 得到跟单保证金。
            BigDecimal followRatio = relation.getFollowRatio() == null ? BigDecimal.ONE : relation.getFollowRatio();
            marginAmount = leaderMarginAmount.multiply(followRatio).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        }
        // 跟单金额无效时直接拒绝创建子单。
        if (marginAmount == null || marginAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("跟单金额必须大于0");
        }
        // 跟单单只继承交易相关参数，不允许跟单人篡改产品、方向、杠杆等核心信息。
        UserCryptocurrencyPosition position = new UserCryptocurrencyPosition();
        position.setProductCode(leaderPosition.getProductCode());
        position.setOrderDirection(leaderPosition.getOrderDirection());
        position.setOrderLever(leaderPosition.getOrderLever());
        position.setStopProfitPrice(leaderPosition.getStopProfitPrice());
        position.setStopLossPrice(leaderPosition.getStopLossPrice());
        // 复用原开仓逻辑时，通过 params 传入保证金金额。
        position.getParams().put("marginAmount", marginAmount);
        position.getParams().put("copyTradeBuyPrice", leaderPosition.getBuyOrderPrice());
        createPosition(position, followerUserId);
        return position;
    }

    /**
     * 创建一笔真实的加密货币合约持仓。
     * 该方法既服务普通用户下单，也服务跟单用户下单。
     *
     * @param position 下单参数
     * @param userId 下单用户ID
     */
    private void createPosition(UserCryptocurrencyPosition position, Long userId) {
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
        //产品交易时间
        ProductTradeTimeSetting productTradeTimeSetting = productTradeTimeSettingMapper.selectProductTradeTimeSettingByDayAndProductType(DateUtils.getDayOfWeek(), 2);
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

        //产品信息
        CryptocurrencyProduct product = cryptocurrencyProductMapper.selectCryptocurrencyProductByCode(productCode);
        if (product == null) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
        }
        if (!product.getIsLock().equals(0)) {
            throw new LangException("hint_8","该产品已锁定，不能进行交易");
        }

        //现价
        BigDecimal nowPrice = BigDecimal.ZERO;
        Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
        TickerInfo tickerInfo = tickerInfoMap.get(productCode);
        if (tickerInfo != null){
            nowPrice = new BigDecimal(tickerInfo.getNowPrice());
        }
        try {
            Object copyTradeBuyPrice = position.getParams().get("copyTradeBuyPrice");
            if (copyTradeBuyPrice != null) {
                BigDecimal leaderBuyPrice = new BigDecimal(String.valueOf(copyTradeBuyPrice));
                if (leaderBuyPrice.compareTo(BigDecimal.ZERO) > 0) {
                    nowPrice = leaderBuyPrice;
                }
            }
        } catch (Exception e) {

        }
        if (nowPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new LangException("hint_QuoteZeroTryAgain","报价0，请稍后再试");
        }

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
        BigDecimal orderFee = orderTotalPrice.multiply(buyFeeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);



        //实时时间
        Date nowDateTime = new Date();

        position.setOrderCode(CodeUtils.generateOrderCode("CRYPTO"));
        position.setUserId(userId);
        position.setProductCode(productCode);
        position.setProductName(product.getProductName());
        position.setBuyOrderTime(nowDateTime);
        position.setBuyOrderPrice(nowPrice);
        position.setSellOrderTime(null);
        position.setSellOrderPrice(null);
        position.setOrderDirection(orderDirection);
        position.setOrderNum(orderNum);
        position.setOrderLever(orderLever);
        position.setOrderTotalPrice(orderTotalPrice);
        position.setOrderFee(orderFee);
        position.setOrderYhsFee(BigDecimal.ZERO);
        position.setProfitAndLose(BigDecimal.ZERO);
        position.setAllProfitAndLose(orderFee.negate());
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

        //余额变更后
        BigDecimal userAmountAfter = userAmountBefore.subtract(marginAmount);
        //更新余额
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //更新打码量
        userInfo.setNeedOrderAmount(userInfo.getNeedOrderAmount().subtract(marginAmount));
        int updateUser = userInfoMapper.updateUserInfo(userInfo);
        if (updateUser <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //账户明细
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("加密货币合约交易下单");
        userBillDetail.setDeSummary("加密货币合约交易下单成功");
        userBillDetail.setOrderAmount(marginAmount.negate());
        userBillDetail.setOrderTime(nowDateTime);
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(position.getId());
        userBillDetail.setOrderClass(6);
        userBillDetail.setCurrencyId(currencyId);
        int count = userBillDetailMapper.insertUserBillDetail(userBillDetail);
        if (count <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
    }

    /**
     * 用户加密货币合约交易卖出
     *
     * @param positionId 持仓id
     * @param doType 平仓类型 0:强制平仓 1:用户平仓
     * @param nowPrice 现价
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult sell(Long positionId, Integer doType, BigDecimal nowPrice){
        //日志记录平仓类型
        HttpUtils.getRequestLogParams().put("doType",doType);
        //持仓信息
        UserCryptocurrencyPosition position = userCryptocurrencyPositionMapper.selectUserCryptocurrencyPositionById(positionId);
        if (position == null) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取订单信息异常");
        }
        //验证订单状态
        if (!position.getOrderStatus().equals(0)) {
            return AjaxResult.error("hint_FailedToClosePosition4","平仓失败，此订单已平仓").put("taskFlag",0);
        }

        //用户id
        Long userId = position.getUserId();
        //如果不是强制平仓
        if (!doType.equals(0)){
            if (!UserApiKeyUtils.getUserId().equals(userId)){
                throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
            }
            //验证订单交易状态
            if (!position.getIsLock().equals(0)){
                return AjaxResult.error(position.getLockMsg());
            }
            //产品交易时间
            ProductTradeTimeSetting productTradeTimeSetting = productTradeTimeSettingMapper.selectProductTradeTimeSettingByDayAndProductType(DateUtils.getDayOfWeek(), 2);
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

        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null){
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("userId",userId);
        //验证用户交易状态
        if (!userInfo.getIsLock().equals(0)) {
            return AjaxResult.error("hint_FailedToClosePosition3","平仓失败，用户已被锁定");
        }

        //产品交易设置
        ProductSetting productSetting = productSettingMapper.selectProductSettingByProductType(2);
        if (productSetting == null) {
            return AjaxResult.error(HintConstants.SYSTEM_BUSY,"获取产品交易设置异常");
        }
        //至少持仓时间（分钟）
        Integer cantSellTimes = productSetting.getCantSellTimes();
        if (!BuyAndSellUtils.isCanSell(position.getBuyOrderTime(),cantSellTimes)) {
            List<Object> list = new ArrayList<>();
            list.add(cantSellTimes);
            return AjaxResult.error("hint_19",list,cantSellTimes + "分钟内不能平仓");
        }

        //产品代码
        String productCode = position.getProductCode();
        //现价
        if (nowPrice == null){
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,false);
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                nowPrice = new BigDecimal(tickerInfo.getNowPrice());
            }
        }
        if (nowPrice.compareTo(BigDecimal.ZERO) == 0) {
            return AjaxResult.error("hint_QuoteZeroTryAgain","报价0，请稍后再试");
        }

        //币种id
        Long currencyId = position.getCurrencyId();
        //购买数量
        BigDecimal orderNum = position.getOrderNum();
        //购买时总市值
        BigDecimal orderTotalPrice = position.getOrderTotalPrice();
        //目前市值
        BigDecimal allSellAmt = nowPrice.multiply(orderNum);
        //浮动盈亏
        BigDecimal profitAndLose = BigDecimal.ZERO;
        if (position.getOrderDirection().equals(0)) {
            profitAndLose = allSellAmt.subtract(orderTotalPrice);
        } else {
            profitAndLose = orderTotalPrice.subtract(allSellAmt);
        }
        //买入手续费
        BigDecimal buyFee = position.getOrderFee();
        //卖出手续费率
        BigDecimal sellFeeRate = BigDecimal.ZERO;
        OrderFeeSetting sellFeeVo = orderFeeSettingMapper.selectOrderFeeSettingByKey("cryptocurrency_contract_sell_fee");
        if (sellFeeVo != null){
            sellFeeRate = sellFeeVo.getFeeRate().multiply(new BigDecimal(0.01));
        }
        //支付金额(保证金)
        BigDecimal marginAmount = orderTotalPrice.divide(new BigDecimal(position.getOrderLever()),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //卖出手续费
        BigDecimal sellFee = marginAmount.multiply(sellFeeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //买入卖出手续费
        BigDecimal orderFee = buyFee.add(sellFee);
        position.setOrderFee(orderFee);
        position.setSellOrderPrice(nowPrice);
        position.setOrderStatus(1);
        //实时时间
        Date nowDateTime = new Date();
        position.setSellOrderTime(nowDateTime);
        position.setProfitAndLose(profitAndLose);
        //总盈亏
        BigDecimal allProfitAndLose = profitAndLose.subtract(orderFee);
        position.setAllProfitAndLose(allProfitAndLose);
        //更新持仓订单信息
        int updateUserStockPosition = userCryptocurrencyPositionMapper.updateUserCryptocurrencyPosition(position);
        if (updateUserStockPosition <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录持仓订单信息
        HttpUtils.getRequestLogParams().put("position", JSONObject.toJSONString(position));

        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId,currencyId);
        //余额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();

        //修改用户可用余额=当前可用余额+总盈亏+保证金
        //余额变更后
        BigDecimal userAmountAfter = userAmountBefore.add(allProfitAndLose).add(marginAmount);
        //如果余额成了负数
        if (userAmountAfter.compareTo(BigDecimal.ZERO) < 0){
            userAmountAfter = BigDecimal.ZERO;
        }
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //账户明细
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("加密货币合约交易平仓");
        userBillDetail.setDeSummary("卖出加密货币:"+productCode+"/"+2+",占用本金:"+marginAmount+",总手续费:"+orderFee+",盈亏:"+profitAndLose+",总盈亏:"+allProfitAndLose+",交易订单:"+position.getOrderCode());
        userBillDetail.setRemark(productCode+"/"+2+"/"+marginAmount+"/"+orderFee+"/"+profitAndLose+"/"+allProfitAndLose+"/"+position.getOrderCode());
        if (userAmountAfter.compareTo(BigDecimal.ZERO) == 0){
            userBillDetail.setOrderAmount(userAmountBefore.negate());
        }else {
            userBillDetail.setOrderAmount(allProfitAndLose.add(marginAmount));
        }
        userBillDetail.setOrderTime(nowDateTime);
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(position.getId());
        userBillDetail.setOrderClass(7);
        userBillDetail.setCurrencyId(currencyId);
        int count = userBillDetailMapper.insertUserBillDetail(userBillDetail);
        if (count <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        copyTradeOrderService.closeFollowerOrderByFollowerPosition(
                CopyTradePositionSnapshot.PRODUCT_TYPE_CRYPTOCURRENCY,
                position.getId(),
                doType.equals(1) ? 1 : 2);
        try {
            // 主单平仓成功后，再尝试同步所有关联的跟单单平仓。
            copyTradeSyncTaskService.enqueueLeaderCloseSyncTasks(CopyTradePositionSnapshot.PRODUCT_TYPE_CRYPTOCURRENCY, position.getId());
        } catch (Exception e) {
            // 同步失败不回滚主单平仓，只记录异常等待后续排查。
            recordCopyTradeSyncError("跟单平仓触发失败", position.getId(), e);
        }
        return AjaxResult.success().put("orderInfo",position);
    }

    /**
     * 记录跟单同步异常。
     *
     * @param jobName 任务名称
     * @param positionId 主单持仓ID
     * @param e 异常对象
     */
    private void recordCopyTradeSyncError(String jobName, Long positionId, Exception e) {
        // 将同步异常落库，方便后台统一排查。
        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
        scheduledTaskExceptionLog.setJobName(jobName);
        scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
        scheduledTaskExceptionLog.setCreateTime(new Date());
        scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
        scheduledTaskExceptionLog.setRelateInfo("positionId:" + positionId);
        scheduledTaskExceptionLogMapper.insertScheduledTaskExceptionLog(scheduledTaskExceptionLog);
    }

    /**
     * 加密货币爆仓定时任务
     */
    @Override
    public void cryptocurrencyPositionForceSellTask(){
        //获取持仓中的订单
        UserCryptocurrencyPosition search = new UserCryptocurrencyPosition();
        search.setOrderStatus(0);
        List<UserCryptocurrencyPosition> positions = userCryptocurrencyPositionMapper.selectUserCryptocurrencyPositionList(search);
        if (positions.size() == 0){
            return;
        }
        //持仓中的用户和各自的持仓信息
        Map<Long, List<UserCryptocurrencyPosition>> positionMap = positions.stream().collect(Collectors.groupingBy(UserCryptocurrencyPosition::getUserId));
        //productCodes
        String productCodes = positions.stream().map(UserCryptocurrencyPosition::getProductCode).collect(Collectors.joining(","));
        //行情map
        Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
        //平台设置
        ProductSetting productSetting = productSettingMapper.selectProductSettingByProductType(2);
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(positionMap.size());
        try {
            //币种id
            Long currencyId = 2L;
            //统一交易币种开关
            Integer switchStatus56L = switchSetService.selectSwitchStatusById(56L);
            //如果统一交易币种开关开启
            if (switchStatus56L != null && switchStatus56L.equals(0)){
                try{
                    currencyId = CacheUtil.getOtherValueByKey("default_trade_currency_id", Long.class);
                }catch (Exception e){

                }
            }
            for (Map.Entry<Long, List<UserCryptocurrencyPosition>> entry : positionMap.entrySet()) {
                //用户id
                Long userId = entry.getKey();
                //持仓信息
                List<UserCryptocurrencyPosition> userPositions = entry.getValue();
                //币种id
                Long finalCurrencyId = currencyId;
                //如果持仓订单中的订单使用币种与当前统计币种有不同的，则跳过
                if (userPositions.stream().filter(a->!a.getCurrencyId().equals(finalCurrencyId)).count() > 0){
                    continue;
                }
                executorService.execute(()->{
                    try {
                        userCryptocurrencyPositionService.doCryptocurrencyPositionForceSellTask(userId,userPositions,tickerInfoMap,productSetting);
                    } catch (Exception e) {
                        //记录异常日志
                        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
                        scheduledTaskExceptionLog.setJobName("加密货币强制平仓定时任务");
                        scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
                        scheduledTaskExceptionLog.setCreateTime(new Date());
                        scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
                        scheduledTaskExceptionLog.setRelateInfo("ids:"+userPositions.stream().map(UserCryptocurrencyPosition::getId).collect(Collectors.toList()));
                        scheduledTaskExceptionLog.setType(14);
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
     * 加密货币爆仓定时任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doCryptocurrencyPositionForceSellTask(Long userId,List<UserCryptocurrencyPosition> userPositions,Map<String, TickerInfo> tickerInfoMap,ProductSetting productSetting){
        //币种id
        Long currencyId = userPositions.get(0).getCurrencyId();
        //持仓总保证金
        BigDecimal allMarginAmount = this.getAllMarginAmountAmountByUserId(userId, userPositions);
        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //用户余额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //爆仓线
        BigDecimal userForceSellAmount = ForceSellUtils.getUserForceAmount(userAmountBefore,allMarginAmount,productSetting);
        if(userForceSellAmount.compareTo(BigDecimal.ZERO) < 0){
            return ;
        }
        //持仓总盈亏
        BigDecimal allProfitAndLose = this.getAllProfitAndLoseByUserId(userId,userPositions,tickerInfoMap);
        //如果总盈亏达到爆仓线
        if (userForceSellAmount.negate().compareTo(allProfitAndLose) >= 0){
            for (int i = 0; i < userPositions.size(); i++) {
                //持仓信息
                UserCryptocurrencyPosition position = userPositions.get(i);
                //产品代码
                String productCode = position.getProductCode();
                //现价
                BigDecimal nowPrice = BigDecimal.ZERO;
                //行情信息
                TickerInfo tickerInfo = tickerInfoMap.get(position.getProductCode());
                if (tickerInfo != null){
                    //现价
                    nowPrice = new BigDecimal(tickerInfo.getNowPrice());
                }
                if (nowPrice.compareTo(BigDecimal.ZERO) == 0){
                    throw new RuntimeException(productCode+"报价0");
                }
                AjaxResult result = userCryptocurrencyPositionService.sell(userPositions.get(i).getId(), 0, nowPrice);
                if (!result.isSuccess()){
                    throw new RuntimeException(String.valueOf(result.get("msg")));
                }
            }
        }
    }

    /**
     * 加密货币止盈止损定时任务
     */
    @Override
    public void cryptocurrencyStopProfitAndLossTask(){
        //获取持仓中的订单
        UserCryptocurrencyPosition search = new UserCryptocurrencyPosition();
        search.setOrderStatus(0);
        List<UserCryptocurrencyPosition> positions = userCryptocurrencyPositionMapper.selectUserCryptocurrencyPositionList(search);
        if (positions.size() == 0){
            return ;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            //productCodes
            String productCodes = positions.stream().map(UserCryptocurrencyPosition::getProductCode).distinct().collect(Collectors.joining(","));
            //行情map
            Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
            for (int i = 0; i < positions.size(); i++) {
                //订单信息
                UserCryptocurrencyPosition position = positions.get(i);
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
                            AjaxResult ajaxResult = userCryptocurrencyPositionService.sell(position.getId(), 0, nowPrice);
                            //如果没结算成功
                            if (!ajaxResult.isSuccess() && ajaxResult.get("taskFlag") != null) {
                                throw new RuntimeException(String.valueOf(ajaxResult.get("msg")));
                            }
                        }
                    } catch (Exception e) {
                        //记录异常日志
                        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
                        scheduledTaskExceptionLog.setJobName("加密货币止盈止损触发定时任务");
                        scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
                        scheduledTaskExceptionLog.setCreateTime(new Date());
                        scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
                        scheduledTaskExceptionLog.setRelateInfo("id:" + position.getId());
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
