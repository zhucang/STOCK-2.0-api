package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.FastTradeOrderOptionsMapper;
import com.ruoyi.system.service.*;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 极速交易下单选项Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
@Service
public class FastTradeOrderOptionsServiceImpl implements IFastTradeOrderOptionsService 
{
    @Resource
    private FastTradeOrderOptionsMapper fastTradeOrderOptionsMapper;

    @Autowired
    private IStockProductService stockProductService;

    @Autowired
    private ICryptocurrencyProductService cryptocurrencyProductService;

    @Autowired
    private IFuturesProductService futuresProductService;

    @Autowired
    private IForexProductService forexProductService;

    /**
     * 查询极速交易下单选项
     * 
     * @param id 极速交易下单选项主键
     * @return 极速交易下单选项
     */
    @Override
    @Cacheable(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.ENTITY,key = "#id")
    public FastTradeOrderOptions selectFastTradeOrderOptionsById(Long id)
    {
        return fastTradeOrderOptionsMapper.selectFastTradeOrderOptionsById(id);
    }

    /**
     * 查询极速交易下单选项列表
     * 
     * @param fastTradeOrderOptions 极速交易下单选项
     * @return 极速交易下单选项
     */
    @Override
    @Cacheable(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.LIST,key = "#fastTradeOrderOptions.cacheableKey()")
    public List<FastTradeOrderOptions> selectFastTradeOrderOptionsList(FastTradeOrderOptions fastTradeOrderOptions)
    {
        return fastTradeOrderOptionsMapper.selectFastTradeOrderOptionsList(fastTradeOrderOptions);
    }

    /**
     * 新增极速交易下单选项
     * 
     * @param fastTradeOrderOptions 极速交易下单选项
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.LIST,allEntries = true)
    public int insertFastTradeOrderOptions(FastTradeOrderOptions fastTradeOrderOptions) throws Exception {
        //产品代码
        String productCode = fastTradeOrderOptions.getProductCode();
        //产品类型
        Integer productType = fastTradeOrderOptions.getProductType();
        //如果产品代码是空，则新增所有
        if (StringUtils.isEmpty(productCode)){
            if (productType == null){
                for (int i = 1; i <= 4; i++) {
                    fastTradeOrderOptions.setProductType(i);
                    insertFastTradeOrderOptionsAll(fastTradeOrderOptions);
                }
                return 1;
            }else {
                return insertFastTradeOrderOptionsAll(fastTradeOrderOptions);
            }
        }else {
            if (productType == null){
                throw new ServiceException("请选择产品类型");
            }
        }
        //如果是股票
        if (productType.equals(1)){
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常，请刷新后重新尝试");
            }
            fastTradeOrderOptions.setProductName(product.getProductName());
        }else  if (productType.equals(2)){
            //如果是加密货币
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常，请刷新后重新尝试");
            }
            fastTradeOrderOptions.setProductName(product.getProductName());
        }else if (productType.equals(3)){
            //如果是期货
            FuturesProduct product = futuresProductService.selectFuturesProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常，请刷新后重新尝试");
            }
            fastTradeOrderOptions.setProductName(product.getProductName());
        }else if (productType.equals(4)){
            //如果是外汇
            ForexProduct product = forexProductService.selectForexProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常，请刷新后重新尝试");
            }
            fastTradeOrderOptions.setProductName(product.getProductName());
        }else {
            throw new ServiceException("产品类型错误");
        }
        fastTradeOrderOptions.setCreateTime(new Date());
        fastTradeOrderOptions.setCreateBy(SecurityUtils.getUsername());
        int count = fastTradeOrderOptionsMapper.insertFastTradeOrderOptions(fastTradeOrderOptions);
        if (count <= 0){
            throw new ServiceException("产品类型错误");
        }
        return 1;
    }

    /**
     * 新增极速交易下单选项
     *
     * @param fastTradeOrderOptions 极速交易下单选项
     * @return 结果
     */
    public int insertFastTradeOrderOptionsAll(FastTradeOrderOptions fastTradeOrderOptions) throws Exception{
        fastTradeOrderOptions.setProductCode("所有产品");
        //批量插入空值初始化
        if (fastTradeOrderOptions.getStatus() == null){
            fastTradeOrderOptions.setStatus(0);
        }
        if (fastTradeOrderOptions.getUpFluctuationRatio() == null){
            fastTradeOrderOptions.setUpFluctuationRatio(BigDecimal.ZERO);
        }
        if (fastTradeOrderOptions.getDownFluctuationRatio() == null){
            fastTradeOrderOptions.setDownFluctuationRatio(BigDecimal.ZERO);
        }
        if (fastTradeOrderOptions.getMinUserAmount() == null){
            fastTradeOrderOptions.setMinUserAmount(BigDecimal.ZERO);
        }
        if (fastTradeOrderOptions.getProfitRatioMethod() == null){
            fastTradeOrderOptions.setProfitRatioMethod(0);
        }
        if (fastTradeOrderOptions.getLoseMoneyMethod() == null){
            fastTradeOrderOptions.setLoseMoneyMethod(0);
        }
        //操作人
        String operationUserName = SecurityUtils.getUsername();
        //产品类型
        Integer productType = fastTradeOrderOptions.getProductType();
        //当前时间
        Date nowDate = new Date();
        List<FastTradeOrderOptions> list = new ArrayList<>();
        //如果是股票
        if (productType.equals(1)){
            List<StockProduct> products = stockProductService.selectStockProductList(new StockProduct());
            for (int i = 0; i < products.size(); i++) {
                FastTradeOrderOptions fastTradeOrderOptionsVo = (FastTradeOrderOptions) BeanUtils.cloneBean(fastTradeOrderOptions);
                fastTradeOrderOptionsVo.setProductName(products.get(i).getProductName());
                fastTradeOrderOptionsVo.setProductCode(products.get(i).getProductCode());
                fastTradeOrderOptionsVo.setCreateTime(nowDate);
                fastTradeOrderOptionsVo.setCreateBy(operationUserName);
                list.add(fastTradeOrderOptionsVo);
            }
        }else  if (productType.equals(2)){
            //如果是加密货币
            List<CryptocurrencyProduct> products = cryptocurrencyProductService.selectCryptocurrencyProductList(new CryptocurrencyProduct());
            for (int i = 0; i < products.size(); i++) {
                FastTradeOrderOptions fastTradeOrderOptionsVo = (FastTradeOrderOptions) BeanUtils.cloneBean(fastTradeOrderOptions);
                fastTradeOrderOptionsVo.setProductName(products.get(i).getProductName());
                fastTradeOrderOptionsVo.setProductCode(products.get(i).getProductCode());
                fastTradeOrderOptionsVo.setCreateTime(nowDate);
                fastTradeOrderOptionsVo.setCreateBy(operationUserName);
                list.add(fastTradeOrderOptionsVo);
            }
        }else if (productType.equals(3)){
            //如果是期货
            List<FuturesProduct> products = futuresProductService.selectFuturesProductList(new FuturesProduct());
            for (int i = 0; i < products.size(); i++) {
                FastTradeOrderOptions fastTradeOrderOptionsVo = (FastTradeOrderOptions) BeanUtils.cloneBean(fastTradeOrderOptions);
                fastTradeOrderOptionsVo.setProductName(products.get(i).getProductName());
                fastTradeOrderOptionsVo.setProductCode(products.get(i).getProductCode());
                fastTradeOrderOptionsVo.setCreateTime(nowDate);
                fastTradeOrderOptionsVo.setCreateBy(operationUserName);
                list.add(fastTradeOrderOptionsVo);
            }
        }else if (productType.equals(4)){
            //如果是外汇
            List<ForexProduct> products = forexProductService.selectForexProductList(new ForexProduct());
            for (int i = 0; i < products.size(); i++) {
                FastTradeOrderOptions fastTradeOrderOptionsVo = (FastTradeOrderOptions) BeanUtils.cloneBean(fastTradeOrderOptions);
                fastTradeOrderOptionsVo.setProductName(products.get(i).getProductName());
                fastTradeOrderOptionsVo.setProductCode(products.get(i).getProductCode());
                fastTradeOrderOptionsVo.setCreateTime(nowDate);
                fastTradeOrderOptionsVo.setCreateBy(operationUserName);
                list.add(fastTradeOrderOptionsVo);
            }
        }else {
            throw new ServiceException("产品类型错误");
        }
        int count = fastTradeOrderOptionsMapper.insertFastTradeOrderOptionsList(list);
        if (count != list.size()){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改极速交易下单选项
     * 
     * @param fastTradeOrderOptions 极速交易下单选项
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.ENTITY,key = "#fastTradeOrderOptions.id"),
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.LIST,allEntries = true)})
    public int updateFastTradeOrderOptions(FastTradeOrderOptions fastTradeOrderOptions)
    {
        //产品代码
        String productCode = fastTradeOrderOptions.getProductCode();
        //产品类型
        Integer productType = fastTradeOrderOptions.getProductType();
        //如果是股票
        if (productType.equals(1)){
            StockProduct product = stockProductService.selectStockProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常，请刷新后重新尝试");
            }
            fastTradeOrderOptions.setProductName(product.getProductName());
        }else  if (productType.equals(2)){
            //如果是加密货币
            CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常，请刷新后重新尝试");
            }
            fastTradeOrderOptions.setProductName(product.getProductName());
        }else if (productType.equals(3)){
            //如果是期货
            FuturesProduct product = futuresProductService.selectFuturesProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常，请刷新后重新尝试");
            }
            fastTradeOrderOptions.setProductName(product.getProductName());
        }else if (productType.equals(4)){
            //如果是外汇
            ForexProduct product = forexProductService.selectForexProductByCode(productCode);
            if (product == null){
                throw new ServiceException("获取产品信息异常，请刷新后重新尝试");
            }
            fastTradeOrderOptions.setProductName(product.getProductName());
        }else {
            throw new ServiceException("产品类型错误");
        }
        fastTradeOrderOptions.setUpdateTime(new Date());
        fastTradeOrderOptions.setUpdateBy(SecurityUtils.getUsername());
        int count = fastTradeOrderOptionsMapper.updateFastTradeOrderOptions(fastTradeOrderOptions);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 批量修改极速交易下单选项
     *
     * @param fastTradeOrderOptions 极速交易下单选项
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.LIST,allEntries = true)})
    public int batchUpdateUpdateFastTradeOrderOptions(FastTradeOrderOptions fastTradeOrderOptions)
    {
        fastTradeOrderOptions.setUpdateTime(new Date());
        int count = fastTradeOrderOptionsMapper.updateFastTradeOrderOptionsByIds(fastTradeOrderOptions.getIds(),fastTradeOrderOptions);
        if (count != fastTradeOrderOptions.getIds().size()){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 批量删除极速交易下单选项
     * 
     * @param ids 需要删除的极速交易下单选项主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.LIST,allEntries = true)})
    public int deleteFastTradeOrderOptionsByIds(Long[] ids)
    {
        FastTradeOrderOptions search = new FastTradeOrderOptions();
        search.getParams().put("ids", Arrays.asList(ids));
        List<FastTradeOrderOptions> fastTradeOrderOptions = fastTradeOrderOptionsMapper.selectFastTradeOrderOptionsList(search);
        //日志记录极速交易下单选项信息
        HttpUtils.getRequestLogParams().put("JSONArray:fastTradeOrderOptions", JSONObject.toJSONString(fastTradeOrderOptions));
        return fastTradeOrderOptionsMapper.deleteFastTradeOrderOptionsByIds(ids);
    }

    /**
     * 批量删除极速交易下单选项
     * @param productCodes 产品代码
     * @param productType 产品类型
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.LIST,allEntries = true)})
    public int deleteFastTradeOrderOptionsByProductCodes(List<String> productCodes,Integer productType)
    {
        return fastTradeOrderOptionsMapper.deleteFastTradeOrderOptionsByProductCodes(productCodes,productType);
    }

    /**
     * 删除极速交易下单选项信息
     * 
     * @param id 极速交易下单选项主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.LIST,allEntries = true)})
    public int deleteFastTradeOrderOptionsById(Long id)
    {
        return fastTradeOrderOptionsMapper.deleteFastTradeOrderOptionsById(id);
    }

    /**
     * 清空选项
     * @param productType 产品代码
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.LIST,allEntries = true)})
    public int cleanOptions(Integer productType){
        fastTradeOrderOptionsMapper.cleanOptions(productType);
        return 1;
    }

    /**
     * 复制模板
     * @param productType 产品类型
     * @param productCodes 产品代码
     * @return
     */
    @Override
    @CacheEvict(value = CacheableKey.FAST_TRADE_ORDER_OPTIONS + CacheableKey.LIST,allEntries = true)
    public int copyTemp(Integer productType,List<String> productCodes){
        //实时时间
        Date nowDateTime = new Date();
        //操作人
        String username = "";
        try{
            username = SecurityUtils.getUsername();
        }catch (Exception e){

        }
        //获取BTC模板
        FastTradeOrderOptions search = new FastTradeOrderOptions();
        search.setProductType(2);
        //模板代码
        search.setProductCode("X:BTCUSD");
        List<FastTradeOrderOptions> fastTradeOrderOptions = fastTradeOrderOptionsMapper.selectFastTradeOrderOptionsList(search);
        //即将新增的列表
        List<FastTradeOrderOptions> addList = new ArrayList<FastTradeOrderOptions>();
        //遍历新增模板
        for (int i = 0; i < productCodes.size(); i++) {
            String productCode = productCodes.get(i);
            for (int j = 0; j < fastTradeOrderOptions.size(); j++) {
                FastTradeOrderOptions vo = fastTradeOrderOptions.get(j);
                vo.setId(null);
                vo.setProductType(productType);
                vo.setProductCode(productCode);
                vo.setProductName(productCode);
                vo.setCreateBy(username);
                vo.setCreateTime(nowDateTime);
                try {
                    FastTradeOrderOptions item = (FastTradeOrderOptions)BeanUtils.cloneBean(vo);
                    addList.add(item);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        int insertFastTradeOrderOptionsList = fastTradeOrderOptionsMapper.insertFastTradeOrderOptionsList(addList);
        if (insertFastTradeOrderOptionsList != addList.size()){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }
}
