package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.NewProductApplyPurchaseMapper;
import com.ruoyi.system.mapper.UserApplyPurchaseOrderMapper;
import com.ruoyi.system.mapper.UserBillDetailMapper;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用户新股新币申购订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-30
 */
@Service
public class UserApplyPurchaseOrderServiceImpl implements IUserApplyPurchaseOrderService 
{
    @Resource
    private UserApplyPurchaseOrderMapper userApplyPurchaseOrderMapper;

    @Resource
    private NewProductApplyPurchaseMapper newProductApplyPurchaseMapper;

    @Resource
    private IUserAmountService userAmountService;

    @Resource
    private UserBillDetailMapper userBillDetailMapper;

    @Autowired
    private IUserBibiAssetsService userBibiAssetsService;

    @Autowired
    private IUserApplyPurchaseOrderService userApplyPurchaseOrderService;

    @Autowired
    private IStockProductService stockProductService;

    @Autowired
    private ICryptocurrencyProductService cryptocurrencyProductService;

    /**
     * 查询用户新股新币申购订单
     * 
     * @param id 用户新股新币申购订单主键
     * @return 用户新股新币申购订单
     */
    @Override
    public UserApplyPurchaseOrder selectUserApplyPurchaseOrderById(Long id)
    {
        return userApplyPurchaseOrderMapper.selectUserApplyPurchaseOrderById(id);
    }

    /**
     * 查询用户新股新币申购订单列表
     * 
     * @param userApplyPurchaseOrder 用户新股新币申购订单
     * @return 用户新股新币申购订单
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserApplyPurchaseOrder> selectUserApplyPurchaseOrderList(UserApplyPurchaseOrder userApplyPurchaseOrder)
    {
        return userApplyPurchaseOrderMapper.selectUserApplyPurchaseOrderList(userApplyPurchaseOrder);
    }

    /**
     * 新增用户新股新币申购订单
     * 
     * @param userApplyPurchaseOrder 用户新股新币申购订单
     * @return 结果
     */
    @Override
    public int insertUserApplyPurchaseOrder(UserApplyPurchaseOrder userApplyPurchaseOrder)
    {
        return userApplyPurchaseOrderMapper.insertUserApplyPurchaseOrder(userApplyPurchaseOrder);
    }

    /**
     * 修改用户新股新币申购订单
     * 
     * @param userApplyPurchaseOrder 用户新股新币申购订单
     * @return 结果
     */
    @Override
    public int updateUserApplyPurchaseOrder(UserApplyPurchaseOrder userApplyPurchaseOrder)
    {
        return userApplyPurchaseOrderMapper.updateUserApplyPurchaseOrder(userApplyPurchaseOrder);
    }

    /**
     * 批量删除用户新股新币申购订单
     * 
     * @param ids 需要删除的用户新股新币申购订单主键
     * @return 结果
     */
    @Override
    public int deleteUserApplyPurchaseOrderByIds(Long[] ids)
    {
        return userApplyPurchaseOrderMapper.deleteUserApplyPurchaseOrderByIds(ids);
    }

    /**
     * 删除用户新股新币申购订单信息
     * 
     * @param id 用户新股新币申购订单主键
     * @return 结果
     */
    @Override
    public int deleteUserApplyPurchaseOrderById(Long id)
    {
        return userApplyPurchaseOrderMapper.deleteUserApplyPurchaseOrderById(id);
    }

    /**
     * 用户申购
     * @param userApplyPurchaseOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUserApplyPurchaseOrder(UserApplyPurchaseOrder userApplyPurchaseOrder) {
        //用户id
        Long userId = SecurityUtils.getUserId();
        //新股新币申购配置id
        Long newProductApplyPurchaseId = userApplyPurchaseOrder.getNewProductApplyPurchaseId();
        //申购数量
        Integer applyPurchaseQuantity = userApplyPurchaseOrder.getApplyPurchaseQuantity();
        //申购产品信息
        NewProductApplyPurchase newProductApplyPurchase = newProductApplyPurchaseMapper.selectNewProductApplyPurchaseById(newProductApplyPurchaseId);
        if (newProductApplyPurchase == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取产品信息异常");
        }
        //检验是否申购中
        if (!newProductApplyPurchase.getListingStatus().equals(1)){
            throw new LangException("hint_13","非可申购状态");
        }
        //币种id
        Long currencyId = null;
        try{
            currencyId = CacheUtil.getOtherValueByKey("default_trade_currency_id", Long.class);
        }catch (Exception e){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取默认交易币种异常");
        }
        //申购开始时间
        Date applyPurchaseStartDate = newProductApplyPurchase.getApplyPurchaseStartDate();
        //申购结束时间
        Date applyPurchaseEndDate = newProductApplyPurchase.getApplyPurchaseEndDate();
        //实时时间
        Date nowDateTime = new Date();
        //检验是否在申购时间内
        if (nowDateTime.after(DateUtils.getEndOfDay(applyPurchaseEndDate)) || nowDateTime.before(DateUtils.getStartOfDay(applyPurchaseStartDate))){
            throw new LangException("hint_13","不在申购时间内，购买失败");
        }
        //上市单价
        BigDecimal listingPrice = newProductApplyPurchase.getListingPrice();
        //剩余数量
        Integer remainingQuantity = newProductApplyPurchase.getRemainingQuantity();
        if (remainingQuantity != null){
            if (remainingQuantity < applyPurchaseQuantity){
                throw new LangException("hint_14","剩余数量不足，购买失败");
            }
            //变更新股剩余数量
            newProductApplyPurchase.setRemainingQuantity(remainingQuantity - applyPurchaseQuantity);
        }
        //购买数量
        BigDecimal applyPurchaseQuantityBigDecimal = new BigDecimal(applyPurchaseQuantity);
        //申购金额
        BigDecimal applyPurchaseAmount = listingPrice.multiply(applyPurchaseQuantityBigDecimal);
        //单笔最低购买数量
        BigDecimal applyPurchaseMin = newProductApplyPurchase.getApplyPurchaseMin();
        //单笔最高购买数量
        BigDecimal applyPurchaseMax = newProductApplyPurchase.getApplyPurchaseMax();
        if (applyPurchaseQuantityBigDecimal.compareTo(applyPurchaseMin) == -1 || applyPurchaseMax.compareTo(applyPurchaseQuantityBigDecimal) == -1){
            List<Object> list = new ArrayList<>();
            list.add(applyPurchaseMin);
            list.add(applyPurchaseMax);
            throw new LangException("hint_90",list,"单笔申购数量为" + applyPurchaseMin + "~" + applyPurchaseMax);
        }
        //用户购买门槛
        BigDecimal userAmountLimit = newProductApplyPurchase.getUserAmountLimit();
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //用户变更前金额
        BigDecimal userAmountBefore = userAmount.getAmount();
        if (userAmountBefore.compareTo(userAmountLimit) == -1){
            List<Object> list = new ArrayList<>();
            list.add(userAmountLimit);
            throw new LangException("hint_16",list,"钱包余额需要达到"+userAmountLimit+"才可购买");
        }
        if (userAmountBefore.compareTo(applyPurchaseAmount) == -1){
            throw new LangException("hint_17","钱包余额不足");
        }
        //用户变更前后
        BigDecimal userAmountAfter = userAmountBefore.subtract(applyPurchaseAmount);
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"更新用户余额异常");
        }

        userApplyPurchaseOrder.setOrderCode(CodeUtils.generateOrderCode("APPLYPURCHASE"));
        userApplyPurchaseOrder.setUserId(userId);
        userApplyPurchaseOrder.setApplyPurchaseTime(nowDateTime);
        userApplyPurchaseOrder.setApplyPurchaseAmount(applyPurchaseAmount);
        userApplyPurchaseOrder.setApplyPurchaseQuantity(applyPurchaseQuantity);
        userApplyPurchaseOrder.setApplyPurchaseSingleAmount(listingPrice);
        userApplyPurchaseOrder.setListingStartDate(newProductApplyPurchase.getListingStartDate());
        userApplyPurchaseOrder.setWinningRate(null);
        userApplyPurchaseOrder.setWinningAmount(BigDecimal.ZERO);
        userApplyPurchaseOrder.setWinningQuantity(BigDecimal.ZERO);
        userApplyPurchaseOrder.setSellPrice(null);
        userApplyPurchaseOrder.setSellTime(null);
        userApplyPurchaseOrder.setProfitAndLose(BigDecimal.ZERO);
        userApplyPurchaseOrder.setNewProductApplyPurchaseId(newProductApplyPurchaseId);
        userApplyPurchaseOrder.setStatus(0);
        userApplyPurchaseOrder.setProductType(newProductApplyPurchase.getProductType());
        userApplyPurchaseOrder.setLockFlag(0);
        userApplyPurchaseOrder.setUnLockTime(DateUtils.getDateBeforeOrAfterDate(newProductApplyPurchase.getListingStartDate(), Calendar.DAY_OF_YEAR,newProductApplyPurchase.getLockupPeriod()));
        //插入订单
        int insertUserApplyPurchaseOrder = userApplyPurchaseOrderMapper.insertUserApplyPurchaseOrder(userApplyPurchaseOrder);
        if (insertUserApplyPurchaseOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"插入申购订单异常");
        }

        int updateNewStockApplyPurchase = newProductApplyPurchaseMapper.updateNewProductApplyPurchase(newProductApplyPurchase);
        if (updateNewStockApplyPurchase <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"更新申购配置异常");
        }

        //账户明细
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("新股申购下单");
        userBillDetail.setDeSummary("新股申购下单成功");
        userBillDetail.setOrderAmount(applyPurchaseAmount.negate());
        userBillDetail.setOrderTime(nowDateTime);
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(userApplyPurchaseOrder.getId());
        userBillDetail.setOrderClass(21);
        userBillDetail.setCurrencyId(currencyId);
        int count = userBillDetailMapper.insertUserBillDetail(userBillDetail);
        if (count <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"插入流水明细异常");
        }
        return 1;
    }

    /**
     * 设置中签率
     * @param id 用户申购订单id
     * @param winningRate 中签率
     * @param userApplyPurchaseOrder 用户申购订单
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult setWinningRate(Long id, BigDecimal winningRate,UserApplyPurchaseOrder userApplyPurchaseOrder) {
        //用户申购订单信息
        if (userApplyPurchaseOrder == null){
            userApplyPurchaseOrder = userApplyPurchaseOrderMapper.selectUserApplyPurchaseOrderById(id);
            if (userApplyPurchaseOrder == null){
                throw new ServiceException("获取申购订单信息异常");
            }
        }
        if (userApplyPurchaseOrder.getWinningRate() != null){
            throw new ServiceException("此订单已配置过中签率");
        }
        //用户id
        Long userId = userApplyPurchaseOrder.getUserId();
        //申购金额
        BigDecimal applyPurchaseAmount = userApplyPurchaseOrder.getApplyPurchaseAmount();
        //中签金额
        BigDecimal winningAmount = applyPurchaseAmount.multiply(winningRate);
        //中签数量
        BigDecimal winningQuantity = new BigDecimal(userApplyPurchaseOrder.getApplyPurchaseQuantity()).multiply(winningRate);
        //退回金额
        BigDecimal backAmount = applyPurchaseAmount.subtract(winningAmount);
        //如果有退回金额
        if (backAmount.compareTo(BigDecimal.ZERO) > 0){
            //用户钱包信息
            UserAmount userAmount = userAmountService.getUserAmount(userId, 3L);
            //余额变更前
            BigDecimal userAmountBefore = userAmount.getAmount();
            //余额变更后
            BigDecimal userAmountAfter = userAmountBefore.add(backAmount);
            userAmount.setAmount(userAmountAfter);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new RuntimeException("更新用户余额异常");
            }

            //用户流水记录(用户新股新币申购未中签退回明细)
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("新股申购退回");
            userBillDetail.setDeSummary("新股申购退回成功");
            userBillDetail.setOrderAmount(backAmount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(userApplyPurchaseOrder.getId());
            userBillDetail.setOrderClass(22);
            userBillDetail.setCurrencyId(userAmount.getCurrencyId());
            int insert = userBillDetailMapper.insertUserBillDetail(userBillDetail);
            if (insert <= 0) {
                throw new LangException("新股申购退回】新增新股申购退回明细异常");
            }
        }

        //更新用户申购数据的中签信息
        UserApplyPurchaseOrder userApplyPurchaseOrderVo = new UserApplyPurchaseOrder();
        userApplyPurchaseOrderVo.setId(userApplyPurchaseOrder.getId());
        userApplyPurchaseOrderVo.setWinningRate(winningRate);
        userApplyPurchaseOrderVo.setWinningAmount(winningAmount);
        userApplyPurchaseOrderVo.setWinningQuantity(winningQuantity);
        int updateUserApplyPurchaseOrder = userApplyPurchaseOrderMapper.updateUserApplyPurchaseOrder(userApplyPurchaseOrderVo);
        if (updateUserApplyPurchaseOrder <= 0){
            throw new RuntimeException("更新用户申购数据信息异常");
        }
        return AjaxResult.success();
    }

    /**
     * 解锁用户申购订单锁仓
     * @param id 用户申购订单id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int unLockOrder(Long id,UserApplyPurchaseOrder userApplyPurchaseOrder){
        if (userApplyPurchaseOrder == null){
            userApplyPurchaseOrder = userApplyPurchaseOrderMapper.selectUserApplyPurchaseOrderById(id);
            if (userApplyPurchaseOrder == null){
                throw new ServiceException("获取申购订单信息异常");
            }
        }
        //产品类型
        Integer productType = userApplyPurchaseOrder.getProductType();
        //产品代码
        String productCode = userApplyPurchaseOrder.getProductCode();
        //股票
        if (productType.equals(1)) {
            //验证股票是否已经存在
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null) {
                throw new ServiceException("获取产品信息异常，请确认该产品已上市");
            }
        } else if (productType.equals(2)) {
            //验证加密货币是否存在
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product == null) {
                throw new ServiceException("获取产品信息异常，请确认该产品已上市");
            }
        } else {
            throw new ServiceException("产品类型错误");
        }

        if (userApplyPurchaseOrder.getLockFlag().equals(1)){
            throw new ServiceException("该订单已解锁过");
        }
        userApplyPurchaseOrder.setLockFlag(1);
        //更新订单锁定状态
        int updateUserApplyPurchaseOrder = userApplyPurchaseOrderMapper.updateUserApplyPurchaseOrder(userApplyPurchaseOrder);
        if (updateUserApplyPurchaseOrder <= 0){
            throw new ServiceException("更新订单信息异常");
        }
        //用户id
        Long userId = userApplyPurchaseOrder.getUserId();
        //用户币币资产
        UserBibiAssets userBibiAssets = userBibiAssetsService.getUserBibiAssets(userId, productCode, productType);
        //用户币币持有数量
        BigDecimal bibiAmount = userBibiAssets.getBibiAmount();
        //用户币币冻结数量
        BigDecimal bibiFrozenAmount = userBibiAssets.getBibiFrozenAmount();
        //中签数量
        BigDecimal winningQuantity = userApplyPurchaseOrder.getWinningQuantity();
        userBibiAssets.setBibiAmount(bibiAmount.add(winningQuantity));
        userBibiAssets.setBibiFrozenAmount(bibiFrozenAmount.subtract(winningQuantity));
        int updateUserBibiAssets = userBibiAssetsService.updateUserBibiAssets(userBibiAssets);
        if (updateUserBibiAssets <= 0){
            throw new ServiceException("更新币币资产信息异常");
        }
        return 1;
    }

    /**
     * 用户申购订单自动解锁定时任务
     */
    @Override
    public void userApplyPurchaseOrderAutoUnLockTask(){
        //获取锁仓期限到达的申购订单
        UserApplyPurchaseOrder search = new UserApplyPurchaseOrder();
        search.setLockFlag(0);
        search.setUnLockTime(new Date());
        List<UserApplyPurchaseOrder> userApplyPurchaseOrders = userApplyPurchaseOrderMapper.selectUserApplyPurchaseOrderList(search);
        if (userApplyPurchaseOrders.size() <= 0){
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            for (int i = 0; i < userApplyPurchaseOrders.size(); i++) {
                UserApplyPurchaseOrder userApplyPurchaseOrder = userApplyPurchaseOrders.get(i);
                executorService.execute(()->{
                    try{
                        userApplyPurchaseOrderService.unLockOrder(null,userApplyPurchaseOrder);
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
}
