package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SelfSellProductDailyDataConfigMapper;
import com.ruoyi.system.mapper.SelfSellProductMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.PolygonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自营产品Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
@Service
public class SelfSellProductServiceImpl implements ISelfSellProductService {
    @Resource
    private SelfSellProductMapper selfSellProductMapper;

    @Autowired
    private IStockProductService stockProductService;

    @Resource
    private ICryptocurrencyProductService cryptocurrencyProductService;

    @Resource
    private SelfSellProductDailyDataConfigMapper selfSellProductDailyDataConfigMapper;

    @Autowired
    private ISelfSellProductRealTimeService selfSellProductRealTimeService;

    @Autowired
    private IStockEverydayRecordService stockEverydayRecordService;

    @Autowired
    private ICryptocurrencyEverydayRecordService cryptocurrencyEverydayRecordService;

    @Autowired
    private IFastTradeOrderOptionsService fastTradeOrderOptionsService;

    @Autowired
    private IHomeRecommendProductsService homeRecommendProductsService;

    /**
     * 查询自营产品
     *
     * @param id 自营产品主键
     * @return 自营产品
     */
    @Override
    public SelfSellProduct selectSelfSellProductById(Long id) {
        return selfSellProductMapper.selectSelfSellProductById(id);
    }

    /**
     * 查询自营产品列表
     *
     * @param selfSellProduct 自营产品
     * @return 自营产品
     */
    @Override
    public List<SelfSellProduct> selectSelfSellProductList(SelfSellProduct selfSellProduct) {
        return selfSellProductMapper.selectSelfSellProductList(selfSellProduct);
    }

    /**
     * 新增自营产品
     *
     * @param selfSellProduct 自营产品
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSelfSellProduct(SelfSellProduct selfSellProduct) {
        //产品类型
        Integer productType = selfSellProduct.getProductType();
        //产品代码
        String productCode = selfSellProduct.getProductCode();
        //获取是否有对应的官币
        List<StockProduct> products = (List<StockProduct>) PolygonUtils.tickersMarket(productCode, productType, null).get("results");
        if (products.size() > 0) {
            throw new ServiceException("此产品是官币，无法添加为自营产品");
        }
        //自营产品信息
        SelfSellProduct selfSellProductVo = selfSellProductMapper.selectSelfSellProductByProductCodeAndProductType(productCode, productType);
        if (selfSellProductVo != null) {
            throw new ServiceException("已存在此产品代码的自营产品");
        }
        //关联产品id
        Long relateProductId = 0L;
        //是否直接上市
        Integer isDirectListing = selfSellProduct.getIsDirectListing();
        //股票
        if (productType.equals(1)) {
            //验证股票是否已经存在
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product != null) {
                throw new ServiceException("已存在此产品代码的已上市股票");
            }
            //如果直接上市
            if (isDirectListing.equals(0)){
                //新增股票产品信息
                StockProduct productNew = new StockProduct();
                productNew.setProductName(selfSellProduct.getProductName());
                productNew.setProductCode(selfSellProduct.getProductCode());
                productNew.setProductImg(selfSellProduct.getProductImg());
                productNew.setCreateTime(new Date());
                productNew.setIsSelfSell(1);
                productNew.setIsShow(selfSellProduct.getStatus());
                productNew.setIsLock(selfSellProduct.getStatus());
                productNew.setSort(selfSellProduct.getRelateProductSort());
                productNew.setProductNameLang(selfSellProduct.getProductNameLang());
                int insertStockProduct = stockProductService.insertStockProduct(productNew);
                if (insertStockProduct <= 0) {
                    throw new ServiceException("系统繁忙");
                }
                //生成极速交易下单配置
                List<String> productCodes = new ArrayList<>();
                productCodes.add(productCode);
                fastTradeOrderOptionsService.copyTemp(1,productCodes);
                relateProductId = productNew.getId();
//                //更新产品名称多语言
//                int updateProductNameLang = stockProductService.updateProductNameLang(relateProductId, selfSellProduct.getProductNameLang());
//                if (updateProductNameLang <= 0) {
//                    throw new ServiceException("系统繁忙");
//                }
                //如果添加首页推荐
                if (selfSellProduct.getHomeRecommendProductFlag() != null && selfSellProduct.getHomeRecommendProductFlag().equals(1)){
                    HomeRecommendProducts homeRecommendProducts = new HomeRecommendProducts();
                    homeRecommendProducts.setProductId(relateProductId);
                    homeRecommendProducts.setProductType(productType);
                    homeRecommendProducts.setIsVisible(selfSellProduct.getStatus());
                    homeRecommendProducts.setSort(selfSellProduct.getHomeRecommendProductSort());
                    int insertHomeRecommendProducts = homeRecommendProductsService.insertHomeRecommendProducts(homeRecommendProducts);
                    if (insertHomeRecommendProducts <= 0){
                        throw new ServiceException("系统繁忙");
                    }
                }
            }
        } else if (productType.equals(2)) {
            //加密货币
            if (!productCode.startsWith("X:")) {
                throw new ServiceException("数字货币代码需要以X:开头");
            }
            //验证加密货币是否存在
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product != null) {
                throw new ServiceException("已存在此产品代码的已上市加密货币");
            }
            //如果直接上市
            if (isDirectListing.equals(0)){
                //新增加密货币产品信息
                CryptocurrencyProduct productNew = new CryptocurrencyProduct();
                productNew.setProductName(selfSellProduct.getProductName());
                productNew.setProductCode(selfSellProduct.getProductCode());
                productNew.setProductImg(selfSellProduct.getProductImg());
                productNew.setCreateTime(new Date());
                productNew.setIsSelfSell(1);
                productNew.setIsShow(selfSellProduct.getStatus());
                productNew.setIsLock(selfSellProduct.getStatus());
                productNew.setSort(selfSellProduct.getRelateProductSort());
                productNew.setProductDesc(selfSellProduct.getProductDesc());
                int insertCryptocurrencyProduct = cryptocurrencyProductService.insertCryptocurrencyProduct(productNew);
                if (insertCryptocurrencyProduct <= 0) {
                    throw new ServiceException("系统繁忙");
                }
                //生成极速交易下单配置
                List<String> productCodes = new ArrayList<>();
                productCodes.add(productCode);
                fastTradeOrderOptionsService.copyTemp(2,productCodes);
                relateProductId = productNew.getId();
//                //更新产品名称多语言
//                int updateProductNameLang = cryptocurrencyProductService.updateProductNameLang(relateProductId, selfSellProduct.getProductNameLang());
//                if (updateProductNameLang <= 0) {
//                    throw new ServiceException("系统繁忙");
//                }
                //如果添加首页推荐
                if (selfSellProduct.getHomeRecommendProductFlag() != null && selfSellProduct.getHomeRecommendProductFlag().equals(1)){
                    HomeRecommendProducts homeRecommendProducts = new HomeRecommendProducts();
                    homeRecommendProducts.setProductId(relateProductId);
                    homeRecommendProducts.setProductType(productType);
                    homeRecommendProducts.setIsVisible(selfSellProduct.getStatus());
                    homeRecommendProducts.setSort(selfSellProduct.getHomeRecommendProductSort());
                    int insertHomeRecommendProducts = homeRecommendProductsService.insertHomeRecommendProducts(homeRecommendProducts);
                    if (insertHomeRecommendProducts <= 0){
                        throw new ServiceException("系统繁忙");
                    }
                }
            }
        } else {
            throw new ServiceException("产品类型错误");
        }
        selfSellProduct.setRelateProductId(relateProductId);
        int count = selfSellProductMapper.insertSelfSellProduct(selfSellProduct);
        if (count <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //如果直接上市
        if (isDirectListing.equals(0)){
            //新增自营产品每日行情数据配置
            SelfSellProductDailyDataConfig selfSellProductDailyDataConfig = new SelfSellProductDailyDataConfig();
            selfSellProductDailyDataConfig.setSelfSellProductId(selfSellProduct.getId());
            selfSellProductDailyDataConfig.setProductCode(selfSellProduct.getProductCode());
            selfSellProductDailyDataConfig.setIsDefault(0);
            selfSellProductDailyDataConfig.setFinallyChangeRate(BigDecimal.ZERO);
            selfSellProductDailyDataConfig.setFinallyPrice(selfSellProduct.getInitialPrice());
            selfSellProductDailyDataConfig.setIsTemp(1);
            selfSellProductDailyDataConfig.setProductType(productType);
            selfSellProductDailyDataConfig.setCreateTime(new Date());
            int insertSelfSellProductDailyDataConfig = selfSellProductDailyDataConfigMapper.insertSelfSellProductDailyDataConfig(selfSellProductDailyDataConfig);
            if (insertSelfSellProductDailyDataConfig <= 0) {
                throw new ServiceException("系统繁忙");
            }
            //生成行情
            selfSellProductRealTimeService.generateRealTimeData(productCode,productType,selfSellProduct.getInitialPrice(),selfSellProduct.getInitialPrice(),null);
        }
        return 1;
    }

    /**
     * 修改自营产品
     *
     * @param selfSellProduct 自营产品
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSelfSellProduct(SelfSellProduct selfSellProduct) {
        //旧产品信息
        SelfSellProduct selfSellProductVo = selfSellProductMapper.selectSelfSellProductById(selfSellProduct.getId());
        //产品类型
        Integer productType = selfSellProduct.getProductType();
        //产品代码
        String productCode = selfSellProduct.getProductCode();
        //是否直接上市
        Integer isDirectListing = selfSellProduct.getIsDirectListing();
        //校验产品代码与产品类型是否相符
        if (!productCode.equals(selfSellProductVo.getProductCode()) || !productType.equals(selfSellProductVo.getProductType()) || !isDirectListing.equals(selfSellProductVo.getIsDirectListing())) {
            throw new ServiceException("产品代码、产品类型、产品是否直接上市状态不允许修改");
        }
        //股票
        if (productType.equals(1)) {
            //产品信息
            StockProduct product = stockProductService.selectStockProductById(selfSellProductVo.getRelateProductId());
            if (product != null) {
                if (!productCode.equals(product.getProductCode())) {
                    throw new ServiceException("股票产品信息校验异常");
                }
                //更新关联产品的信息
                product.setIsShow(selfSellProduct.getStatus());
                product.setIsLock(selfSellProduct.getStatus());
                product.setSort(selfSellProduct.getRelateProductSort());
                product.setProductName(selfSellProduct.getProductName());
                product.setProductImg(selfSellProduct.getProductImg());
                int updateStockProduct = stockProductService.updateStockProduct(product);
                if (updateStockProduct <= 0) {
                    throw new ServiceException("系统繁忙");
                }
            }
        } else if (productType.equals(2)) {
            //加密货币
            //产品信息
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductById(selfSellProduct.getRelateProductId());
            if (product != null) {
                if (!productCode.equals(product.getProductCode())) {
                    throw new ServiceException("加密货币产品信息校验异常");
                }
                product.setIsShow(selfSellProduct.getStatus());
                product.setIsLock(selfSellProduct.getStatus());
                product.setSort(selfSellProduct.getRelateProductSort());
                product.setProductName(selfSellProduct.getProductName());
                product.setProductImg(selfSellProduct.getProductImg());
                product.setProductDesc(selfSellProduct.getProductDesc());
                int updateStockProduct = cryptocurrencyProductService.updateCryptocurrencyProduct(product);
                if (updateStockProduct <= 0) {
                    throw new ServiceException("系统繁忙");
                }
            }
        } else {
            throw new ServiceException("产品类型错误");
        }
        //如果产品已上市并且修改了首页推荐的排序
        if (selfSellProduct.getHomeRecommendProductSort() != null
                && selfSellProductVo.getInitialPrice().compareTo(BigDecimal.ZERO) > 0
                && !selfSellProduct.getHomeRecommendProductSort().equals(selfSellProductVo.getHomeRecommendProductSort())){
            HomeRecommendProducts homeRecommendProducts = new HomeRecommendProducts();
            homeRecommendProducts.setProductId(selfSellProductVo.getRelateProductId());
            homeRecommendProducts.setProductType(productType);
            homeRecommendProducts.setIsVisible(selfSellProduct.getStatus());
            homeRecommendProducts.setSort(selfSellProduct.getHomeRecommendProductSort());
            int updateHomeRecommendProducts = homeRecommendProductsService.updateHomeRecommendProductByProductId(homeRecommendProducts);
            if (updateHomeRecommendProducts <= 0){
                throw new ServiceException("系统繁忙");
            }
        }
        int count = selfSellProductMapper.updateSelfSellProduct(selfSellProduct);
        if (count <= 0) {
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改自营产品多语言配置
     * @param selfSellProductId 自营产品id
     * @param productNameLangLang 产品名称多语言
     * @return
     */
    @Override
    public int updateProductNameLangLang(Long selfSellProductId, LangMgr productNameLangLang){
        SelfSellProduct selfSellProduct = new SelfSellProduct();
        selfSellProduct.setId(selfSellProductId);
        selfSellProduct.setProductNameLang(productNameLangLang);
        //旧信息
        SelfSellProduct selfSellProductOld = selfSellProductMapper.selectSelfSellProductById(selfSellProductId);
        if (selfSellProductOld == null){
            throw new ServiceException("获取自营产品信息异常");
        }
        //如果产品已经上市
        if (selfSellProductOld.getInitialPrice().compareTo(BigDecimal.ZERO) > 0){
            //产品类型
            Integer productType = selfSellProductOld.getProductType();
            //关联产品id
            Long relateProductId = selfSellProductOld.getRelateProductId();
            if (productType.equals(1)){
                //更新产品名称多语言
                int updateProductNameLang = stockProductService.updateProductNameLang(relateProductId, productNameLangLang);
                if (updateProductNameLang <= 0) {
                    throw new ServiceException("系统繁忙");
                }
            }else {
                //更新产品名称多语言
                int updateProductNameLang = cryptocurrencyProductService.updateProductNameLang(relateProductId, productNameLangLang);
                if (updateProductNameLang <= 0) {
                    throw new ServiceException("系统繁忙");
                }
            }
        }
        return selfSellProductMapper.updateSelfSellProduct(selfSellProduct);
    }

    /**
     * 批量删除自营产品
     *
     * @param ids 需要删除的自营产品主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSelfSellProductByIds(Long[] ids)
    {
        //删除自营产品信息的同时，在上市产品信息那边也删除
        SelfSellProduct selfSellProduct = new SelfSellProduct();
        List<Long> idList = Arrays.asList(ids);
        selfSellProduct.getParams().put("selfSellProductIds", idList);
        //即将删除的自营产品信息列表
        List<SelfSellProduct> selfSellProducts = selfSellProductMapper.selectSelfSellProductList(selfSellProduct);
        //日志记录自营产品信息
        HttpUtils.getRequestLogParams().put("JSONArray:selfSellProducts", JSONObject.toJSONString(selfSellProducts));
        Map<Long, SelfSellProduct> map = selfSellProducts.stream().collect(Collectors.toMap(SelfSellProduct::getId, a -> a));
        //即将删除的股票产品信息ids
        List<Long> stockProductIds = new ArrayList<>();
        //即将删除的加密货币产品信息ids
        List<Long> cryptocurrencyProductIds = new ArrayList<>();
        //即将删除的股票产品代码
        List<String> stockProductCodes= new ArrayList<>();
        //即将删除的加密货币产品代码
        List<String> cryptocurrencyProductCodes = new ArrayList<>();
        //遍历
        for (int i = 0; i < ids.length; i++) {
            //自营产品id
            Long selfSellProductsId = ids[i];
            //自营产品信息
            SelfSellProduct selfSellProductVo = map.get(selfSellProductsId);
            //产品类型
            Integer productType = selfSellProductVo.getProductType();
            //产品代码
            String productCode = selfSellProductVo.getProductCode();
            //股票
            if (productType.equals(1)){
                StockProduct product = stockProductService.selectStockProductById(selfSellProductVo.getRelateProductId());
                if (product != null){
                    if (!productCode.equals(product.getProductCode())){
                        throw new ServiceException("股票产品信息校验异常");
                    }
                    stockProductIds.add(product.getId());
                    stockProductCodes.add(product.getProductCode());
                }
            }else if (productType.equals(2)){
                //加密货币
                CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductById(selfSellProductVo.getRelateProductId());
                if (product != null){
                    if (!productCode.equals(product.getProductCode())){
                        throw new ServiceException("加密货币产品信息校验异常");
                    }
                    cryptocurrencyProductIds.add(product.getId());
                    cryptocurrencyProductCodes.add(product.getProductCode());
                }
            }else {
                throw new ServiceException("产品类型错误");
            }
        }
        //删除股票产品信息
        if (stockProductIds.size() > 0){
            int deleteStockProductByIds = stockProductService.deleteStockProductByIds(stockProductIds.toArray(new Long[stockProductIds.size()]));
            if (deleteStockProductByIds <= 0){
                throw new ServiceException("系统繁忙");
            }
        }
        //删除加密货币产品信息
        if (cryptocurrencyProductIds.size() > 0){
            int deleteCryptocurrencyProductByIds = cryptocurrencyProductService.deleteCryptocurrencyProductByIds(cryptocurrencyProductIds.toArray(new Long[stockProductIds.size()]));
            if (deleteCryptocurrencyProductByIds <= 0){
                throw new ServiceException("系统繁忙");
            }
        }
        //删除自营货币产品信息
        int deleteSelfSellProductByIds = selfSellProductMapper.deleteSelfSellProductByIds(ids);
        if (deleteSelfSellProductByIds <= 0){
            throw new ServiceException("系统繁忙");
        }
        //删除自营产品每日行情信息配置
        selfSellProductDailyDataConfigMapper.deleteSelfSellProductDailyDataConfigBySelfSellProductIds(idList);
        //删除行情模板
        selfSellProductRealTimeService.cleanProductRealTimeData(null,null,selfSellProducts.stream().map(SelfSellProduct::getProductCode).collect(Collectors.toList()));
        //删除每日行情记录
        //删除股票每日行情记录
        if (stockProductCodes.size() > 0){
            stockEverydayRecordService.cleanStockEverydayRecord(stockProductCodes);
        }
        //删除加密货币每日行情记录
        if (cryptocurrencyProductCodes.size() > 0){
            cryptocurrencyEverydayRecordService.cleanCryptocurrencyEverydayRecord(cryptocurrencyProductCodes);
        }
        return 1;
    }

    /**
     * 删除自营产品信息
     * 
     * @param id 自营产品主键
     * @return 结果
     */
    @Override
    public int deleteSelfSellProductById(Long id)
    {
        return selfSellProductMapper.deleteSelfSellProductById(id);
    }
}
