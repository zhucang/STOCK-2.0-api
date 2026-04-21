package com.ruoyi.system.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 新股新币申购初始配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-30
 */
@Service
public class NewProductApplyPurchaseServiceImpl implements INewProductApplyPurchaseService 
{
    @Resource
    private NewProductApplyPurchaseMapper newProductApplyPurchaseMapper;

    @Resource
    private ScheduledTaskExceptionLogMapper scheduledTaskExceptionLogMapper;

    @Autowired
    private INewProductApplyPurchaseService newProductApplyPurchaseService;

    @Resource
    private SelfSellProductMapper selfSellProductMapper;

    @Autowired
    private IStockProductService stockProductService;

    @Autowired
    private ICryptocurrencyProductService cryptocurrencyProductService;

    @Resource
    private UserApplyPurchaseOrderMapper userApplyPurchaseOrderMapper;

    @Resource
    private SpotTradeOrderMapper spotTradeOrderMapper;

    @Resource
    private SelfSellProductDailyDataConfigMapper selfSellProductDailyDataConfigMapper;

    @Autowired
    private IUserApplyPurchaseOrderService userApplyPurchaseOrderService;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IFastTradeOrderOptionsService fastTradeOrderOptionsService;

    @Autowired
    private ISelfSellProductRealTimeService selfSellProductRealTimeService;

    @Resource
    private BibiTradeOrderMapper bibiTradeOrderMapper;

    @Autowired
    private IUserBibiAssetsService userBibiAssetsService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IHomeRecommendProductsService homeRecommendProductsService;

    /**
     * 查询新股新币申购初始配置
     * 
     * @param id 新股新币申购初始配置主键
     * @return 新股新币申购初始配置
     */
    @Override
    public NewProductApplyPurchase selectNewProductApplyPurchaseById(Long id)
    {
        return newProductApplyPurchaseMapper.selectNewProductApplyPurchaseById(id);
    }

    /**
     * 查询新股新币申购初始配置列表
     * 
     * @param newProductApplyPurchase 新股新币申购初始配置
     * @return 新股新币申购初始配置
     */
    @Override
    public List<NewProductApplyPurchase> selectNewProductApplyPurchaseList(NewProductApplyPurchase newProductApplyPurchase)
    {
        return newProductApplyPurchaseMapper.selectNewProductApplyPurchaseList(newProductApplyPurchase);
    }

    /**
     * 新增新股新币申购初始配置
     * 
     * @param newProductApplyPurchase 新股新币申购初始配置
     * @return 结果
     */
    @Override
    public int insertNewProductApplyPurchase(NewProductApplyPurchase newProductApplyPurchase)
    {
        //新股新币产品id
        Long selfSellProductId = newProductApplyPurchase.getSelfSellProductId();
        //新股新币产品信息
        SelfSellProduct selfSellProduct = selfSellProductMapper.selectSelfSellProductById(selfSellProductId);
        if (selfSellProduct == null || !selfSellProduct.getIsDirectListing().equals(1)){
            throw new ServiceException("获取产品信息异常");
        }
        NewProductApplyPurchase search = new NewProductApplyPurchase();
        search.setSelfSellProductId(selfSellProductId);
        List<NewProductApplyPurchase> newProductApplyPurchases = newProductApplyPurchaseMapper.selectNewProductApplyPurchaseList(search);
        if (newProductApplyPurchases.size() > 0){
            throw new ServiceException("此产品已经配置过申购初始配置");
        }
        //产品类型
        Integer productType = selfSellProduct.getProductType();
        //产品代码
        String productCode = selfSellProduct.getProductCode();
        //股票
        if (productType.equals(1)) {
            //产品信息
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product != null) {
                throw new ServiceException("已存在此产品代码的已上市股票");
            }
        } else if (productType.equals(2)) {
            //加密货币
            //产品信息
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product != null) {
                throw new ServiceException("已存在此产品代码的已上市加密货币");
            }
        } else {
            throw new ServiceException("产品类型错误");
        }
        int count = newProductApplyPurchaseMapper.insertNewProductApplyPurchase(newProductApplyPurchase);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改新股新币申购初始配置
     * 
     * @param newProductApplyPurchase 新股新币申购初始配置
     * @return 结果
     */
    @Override
    public int updateNewProductApplyPurchase(NewProductApplyPurchase newProductApplyPurchase)
    {
        //上市状态
        Integer listingStatus = newProductApplyPurchase.getListingStatus();
        //如果已经上市
        if (listingStatus.equals(2)){
            //不允许修改信息
            throw new ServiceException("该产品已上市");
        }
        //旧信息
        NewProductApplyPurchase vo = newProductApplyPurchaseMapper.selectNewProductApplyPurchaseById(newProductApplyPurchase.getId());
        if (vo == null){
            throw new ServiceException("获取配置信息异常");
        }
        //产品类型
        Integer productType = newProductApplyPurchase.getProductType();
        //产品代码
        String productCode = newProductApplyPurchase.getProductCode();
        //校验产品代码与产品类型是否相符
        if (!productCode.equals(vo.getProductCode()) || !productType.equals(vo.getProductType())
                || !newProductApplyPurchase.getApplyPurchaseStartDate().equals(vo.getApplyPurchaseStartDate())
                || !newProductApplyPurchase.getApplyPurchaseEndDate().equals(vo.getApplyPurchaseEndDate())
                || !newProductApplyPurchase.getListingStartDate().equals(vo.getListingStartDate())
                || !newProductApplyPurchase.getListingEndDate().equals(vo.getListingEndDate())) {
            throw new ServiceException("产品代码、产品类型、申购时间、上市时间不允许修改");
        }
        //新股新币产品id
        Long selfSellProductId = newProductApplyPurchase.getSelfSellProductId();
        //新股新币产品信息
        SelfSellProduct selfSellProduct = selfSellProductMapper.selectSelfSellProductById(selfSellProductId);
        if (selfSellProduct == null || !selfSellProduct.getIsDirectListing().equals(1)){
            throw new ServiceException("获取产品信息异常");
        }
        NewProductApplyPurchase search = new NewProductApplyPurchase();
        search.setSelfSellProductId(selfSellProductId);
        List<NewProductApplyPurchase> newProductApplyPurchases = newProductApplyPurchaseMapper.selectNewProductApplyPurchaseList(search);
        if (newProductApplyPurchases.size() > 0){
            if (!newProductApplyPurchases.get(0).getId().equals(newProductApplyPurchase.getId())){
                throw new ServiceException("此产品已经配置过申购初始配置");
            }
        }
        int count = newProductApplyPurchaseMapper.updateNewProductApplyPurchase(newProductApplyPurchase);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 批量删除新股新币申购初始配置
     * 
     * @param ids 需要删除的新股新币申购初始配置主键
     * @return 结果
     */
    @Override
    public int deleteNewProductApplyPurchaseByIds(Long[] ids)
    {
        return newProductApplyPurchaseMapper.deleteNewProductApplyPurchaseByIds(ids);
    }

    /**
     * 删除新股新币申购初始配置信息
     * 
     * @param id 新股新币申购初始配置主键
     * @return 结果
     */
    @Override
    public int deleteNewProductApplyPurchaseById(Long id)
    {
        return newProductApplyPurchaseMapper.deleteNewProductApplyPurchaseById(id);
    }


    /**
     * 新股新币上市定时任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void newProductListingTask() {
        //获取上市时间到了的新股新币
        List<NewProductApplyPurchase> newProductApplyPurchases = newProductApplyPurchaseMapper.selectListingNewProduct(new Date());
        if (newProductApplyPurchases.size() == 0){
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try {
            //新股新币上市
            for (int i = 0; i < newProductApplyPurchases.size(); i++) {
                NewProductApplyPurchase newProductApplyPurchase = newProductApplyPurchases.get(i);
                executorService.execute(()->{
                    try {
                        newProductApplyPurchaseService.doNewProductListingTask(newProductApplyPurchase);
                    } catch (Exception e) {
                        //记录异常日志
                        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
                        scheduledTaskExceptionLog.setJobName("新股新币上市定时器");
                        scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
                        scheduledTaskExceptionLog.setCreateTime(new Date());
                        scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
                        scheduledTaskExceptionLog.setRelateInfo("id:"+newProductApplyPurchase.getId());
                        scheduledTaskExceptionLog.setType(5);
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
     * 新股新币上市定时任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doNewProductListingTask(NewProductApplyPurchase newProductApplyPurchase){
        //如果状态不是申购中
        if (!newProductApplyPurchase.getListingStatus().equals(1)){
            return;
        }
        //新股新币产品id
        Long selfSellProductId = newProductApplyPurchase.getSelfSellProductId();
        //新股新币产品信息
        SelfSellProduct selfSellProduct = selfSellProductMapper.selectSelfSellProductById(selfSellProductId);
        if (selfSellProduct == null){
            throw new ServiceException("获取产品信息异常");
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
        //产品类型
        Integer productType = newProductApplyPurchase.getProductType();
        //产品代码
        String productCode = newProductApplyPurchase.getProductCode();
        //如果是股票
        if (productType.equals(1)){
            //产品信息
            StockProduct search = stockProductService.selectStockProductByCode(productCode);
            if (search != null) {
                throw new ServiceException("已存在此产品代码的已上市股票");
            }
            //插入新股票
            StockProduct product = new StockProduct();
            product.setProductName(selfSellProduct.getProductName());
            product.setProductCode(productCode);
            product.setProductImg(selfSellProduct.getProductImg());
            product.setSort(selfSellProduct.getRelateProductSort());
            product.setCreateTime(new Date());
            product.setIsSelfSell(1);
            int insertStockProduct = stockProductService.insertStockProduct(product);
            if (insertStockProduct <= 0){
                throw new ServiceException("新增股票异常");
            }
            selfSellProduct.setRelateProductId(product.getId());
            //生成极速交易下单配置
            List<String> productCodes = new ArrayList<>();
            productCodes.add(productCode);
            fastTradeOrderOptionsService.copyTemp(1,productCodes);
            if (currencyId == null){
                currencyId = 1L;
            }
        }else if (productType.equals(2)){
            //插入新加密货币
            //产品信息
            CryptocurrencyProduct search = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (search != null) {
                throw new ServiceException("已存在此产品代码的已上市加密货币");
            }
            CryptocurrencyProduct product = new CryptocurrencyProduct();
            product.setProductName(selfSellProduct.getProductName());
            product.setProductCode(productCode);
            product.setProductImg(selfSellProduct.getProductImg());
            product.setSort(selfSellProduct.getRelateProductSort());
            product.setProductDesc(selfSellProduct.getProductDesc());
            product.setCreateTime(new Date());
            product.setIsSelfSell(1);
            int insertCryptocurrencyProduct = cryptocurrencyProductService.insertCryptocurrencyProduct(product);
            if (insertCryptocurrencyProduct <= 0){
                throw new ServiceException("新增加密货币异常");
            }
            selfSellProduct.setRelateProductId(product.getId());
            //生成极速交易下单配置
            List<String> productCodes = new ArrayList<>();
            productCodes.add(productCode);
            fastTradeOrderOptionsService.copyTemp(2,productCodes);
            if (currencyId == null){
                currencyId = 2L;
            }
        }else {
            throw new ServiceException("产品类型异常");
        }
        //如果添加首页推荐
        if (selfSellProduct.getHomeRecommendProductFlag().equals(1)){
            HomeRecommendProducts homeRecommendProducts = new HomeRecommendProducts();
            homeRecommendProducts.setProductId(selfSellProduct.getRelateProductId());
            homeRecommendProducts.setProductType(productType);
            homeRecommendProducts.setIsVisible(selfSellProduct.getStatus());
            homeRecommendProducts.setSort(selfSellProduct.getHomeRecommendProductSort());
            int insertHomeRecommendProducts = homeRecommendProductsService.insertHomeRecommendProducts(homeRecommendProducts);
            if (insertHomeRecommendProducts <= 0){
                throw new ServiceException("系统繁忙");
            }
        }
        //关联产品id
        Long relateProductId = selfSellProduct.getRelateProductId();
        if (productType.equals(1)){
            //更新产品名称多语言
            int updateProductNameLang = stockProductService.updateProductNameLang(relateProductId, selfSellProduct.getProductNameLang());
            if (updateProductNameLang <= 0) {
                throw new ServiceException("系统繁忙");
            }
        }else {
            //更新产品名称多语言
            int updateProductNameLang = cryptocurrencyProductService.updateProductNameLang(relateProductId, selfSellProduct.getProductNameLang());
            if (updateProductNameLang <= 0) {
                throw new ServiceException("系统繁忙");
            }
        }
        //变更状态为已上市
        newProductApplyPurchase.setListingStatus(2);
        //变更剩余数量为0
        newProductApplyPurchase.setRemainingQuantity(0);
        int updateNewProductApplyPurchase = newProductApplyPurchaseMapper.updateNewProductApplyPurchase(newProductApplyPurchase);
        if (updateNewProductApplyPurchase <= 0){
            throw new ServiceException("更新用户新股新币申购初始配置异常");
        }

        //上市价格
        BigDecimal listingPrice = newProductApplyPurchase.getListingPrice();
        //该产品的用户申购数据
        UserApplyPurchaseOrder userApplyPurchaseOrder = new UserApplyPurchaseOrder();
        userApplyPurchaseOrder.setProductCode(newProductApplyPurchase.getProductCode());
        List<UserApplyPurchaseOrder> userApplyPurchaseOrders = userApplyPurchaseOrderMapper.selectUserApplyPurchaseOrderList(userApplyPurchaseOrder);
        //即将插入的币币交易订单
        List<BibiTradeOrder> bibiTradeOrders = new ArrayList<>();
        for (int j = 0; j < userApplyPurchaseOrders.size(); j++) {
            //用户申购信息
            userApplyPurchaseOrder = userApplyPurchaseOrders.get(j);
            //用户id
            Long userId = userApplyPurchaseOrder.getUserId();
            //用户中签率
            BigDecimal winningRate = userApplyPurchaseOrder.getWinningRate();
            //如果未中签
            if (winningRate == null){
                //设置中签率为0
                AjaxResult ajaxResult = userApplyPurchaseOrderService.setWinningRate(null, BigDecimal.ZERO, userApplyPurchaseOrder);
                if (!ajaxResult.isSuccess()){
                    throw new ServiceException(String.valueOf(ajaxResult.get("msg")));
                }
                continue;
            }

            //币币订单
            BibiTradeOrder bibiTradeOrder = new BibiTradeOrder();
            bibiTradeOrder.setOrderCode(CodeUtils.generateOrderCode("BIBI"));
            bibiTradeOrder.setUserId(userId);
            bibiTradeOrder.setProductType(productType);
            bibiTradeOrder.setProductCode(productCode);
            bibiTradeOrder.setOrderAmount(userApplyPurchaseOrder.getWinningAmount());
            bibiTradeOrder.setTurnoverAmount(userApplyPurchaseOrder.getWinningAmount());
            bibiTradeOrder.setHandingFee(BigDecimal.ZERO);
            bibiTradeOrder.setOrderVolume(userApplyPurchaseOrder.getWinningQuantity());
            bibiTradeOrder.setProductPrice(listingPrice);
            bibiTradeOrder.setOrderType(0);
            bibiTradeOrder.setOrderMethod(1);
            bibiTradeOrder.setOrderStatus(1);
            bibiTradeOrder.setCreateTime(new Date());
            bibiTradeOrder.setRemark(null);
            bibiTradeOrder.setCurrencyId(3L);
            bibiTradeOrders.add(bibiTradeOrder);
        }

        //更新自营产品初始价格信息
        selfSellProduct.setInitialPrice(listingPrice);
        int updateSelfSellProduct = selfSellProductMapper.updateSelfSellProduct(selfSellProduct);
        if (updateSelfSellProduct <= 0){
            throw new ServiceException("更新新股新币产品信息初始价格异常");
        }

        if (bibiTradeOrders.size() > 0){
            //批量插入币币交易订单
            int insertBibiTradeOrders = bibiTradeOrderMapper.insertBibiTradeOrders(bibiTradeOrders);
            if (insertBibiTradeOrders != bibiTradeOrders.size()){
                throw new ServiceException("插入币币交易订单异常");
            }
        }

        //分组
        Map<String, List<BibiTradeOrder>> group = bibiTradeOrders.stream().collect(Collectors.groupingBy(a ->a.getProductType() + "," + a.getProductCode() + "," + a.getUserId()));
        for (Map.Entry<String, List<BibiTradeOrder>> entry : group.entrySet()) {
            //key
            String key = entry.getKey();
            //数量
            BigDecimal amount = entry.getValue().stream().map(a -> a.getOrderVolume()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //成交额
            BigDecimal orderAmount = entry.getValue().stream().map(a -> a.getOrderAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //更新币币资产
            UserBibiAssets userBibiAssets = new UserBibiAssets();
            userBibiAssets.setUserId(Long.valueOf(key.split(",")[2]));
            userBibiAssets.setProductCode(key.split(",")[1]);
            userBibiAssets.setProductType(Integer.valueOf(key.split(",")[0]));
            userBibiAssets.setBibiAmount(BigDecimal.ZERO);
            userBibiAssets.setBibiFrozenAmount(amount);
            userBibiAssets.setBuyAmountAll(orderAmount);
            userBibiAssets.setSellAmountAll(BigDecimal.ZERO);
            userBibiAssets.setBuyAndSellAmountDifference(orderAmount);
            userBibiAssets.setCreateTime(new Date());
            int insertUserBibiAssets = userBibiAssetsService.insertUserBibiAssets(userBibiAssets);
            if (insertUserBibiAssets <= 0){
                throw new ServiceException("更新用户币币资产异常");
            }
        }

        //插入新股新币上市后每天的行情数据配置
        SelfSellProductDailyDataConfig selfSellProductDailyDataConfig = new SelfSellProductDailyDataConfig();
        selfSellProductDailyDataConfig.setSelfSellProductId(selfSellProductId);
        selfSellProductDailyDataConfig.setProductCode(productCode);
        selfSellProductDailyDataConfig.setIsDefault(0);
        selfSellProductDailyDataConfig.setFinallyChangeRate(newProductApplyPurchase.getListedDayIncreaseRate());
        selfSellProductDailyDataConfig.setFinallyPrice(BigDecimal.ZERO);
        selfSellProductDailyDataConfig.setIsTemp(1);
        selfSellProductDailyDataConfig.setProductType(productType);
        selfSellProductDailyDataConfig.setCreateTime(new Date());
        int insertSelfSellProductDailyDataConfig = selfSellProductDailyDataConfigMapper.insertSelfSellProductDailyDataConfig(selfSellProductDailyDataConfig);
        if (insertSelfSellProductDailyDataConfig <= 0) {
            throw new ServiceException("插入新股新币上市后每天的行情数据配置异常");
        }
        //上市当天最终价格
        BigDecimal targetPrice = listingPrice.add(listingPrice.multiply(newProductApplyPurchase.getListingDayIncreaseRate()).divide(new BigDecimal(100), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE));
        //缓存今日上市的产品
        String cacheKey = "listingProduct::"+productType+"::"+productCode+ DateUtils.getDate();
        redisCache.setCacheObject(cacheKey,targetPrice,24, TimeUnit.HOURS);
        //生成行情
        selfSellProductRealTimeService.generateRealTimeData(productCode,productType,listingPrice,targetPrice,null);
    }


    /**
     * 新股新币开始申购定时任务
     */
    @Override
    public void newProductStartApplyPurchaseTask() {
        try {
            newProductApplyPurchaseMapper.startApplyPurchaseNewProduct(new Date());
        } catch (Exception e) {
            //记录异常日志
            ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
            scheduledTaskExceptionLog.setJobName("新股新币开始申购定时器");
            scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
            scheduledTaskExceptionLog.setCreateTime(new Date());
            scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
            scheduledTaskExceptionLog.setRelateInfo("申购定时任务异常");
            scheduledTaskExceptionLog.setType(6);
            scheduledTaskExceptionLogMapper.insertScheduledTaskExceptionLog(scheduledTaskExceptionLog);
            throw new RuntimeException(e);
        }
    }
}
