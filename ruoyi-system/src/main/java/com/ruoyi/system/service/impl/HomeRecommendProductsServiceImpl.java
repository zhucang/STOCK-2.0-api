package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.IHomeRecommendProductsService;
import com.ruoyi.system.utils.ProductQuoteUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 首页推荐产品Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
@Service
public class HomeRecommendProductsServiceImpl implements IHomeRecommendProductsService 
{
    @Resource
    private HomeRecommendProductsMapper homeRecommendProductsMapper;

    @Resource
    private StockProductMapper stockProductMapper;

    @Resource
    private CryptocurrencyProductMapper cryptocurrencyProductMapper;

    @Resource
    private FuturesProductMapper futuresProductMapper;

    @Resource
    private ForexProductMapper forexProductMapper;

    /**
     * 查询首页推荐产品
     * 
     * @param id 首页推荐产品主键
     * @return 首页推荐产品
     */
    @Override
    public HomeRecommendProducts selectHomeRecommendProductsById(Long id)
    {
        return homeRecommendProductsMapper.selectHomeRecommendProductsById(id);
    }

    /**
     * 查询首页推荐产品列表
     * 
     * @param homeRecommendProducts 首页推荐产品
     * @return 首页推荐产品
     */
    @Override
    public List<HomeRecommendProducts> selectHomeRecommendProductsList(HomeRecommendProducts homeRecommendProducts)
    {
        List<HomeRecommendProducts> homeRecommendProductsList = homeRecommendProductsMapper.selectHomeRecommendProductsList(homeRecommendProducts);
        //填充行情信息
        if (homeRecommendProducts.getParams().get("getQuote") != null){
            fillProductQuote(homeRecommendProductsList);
        }
        return homeRecommendProductsList;
    }

    /**
     * 填充行情信息
     * @param homeRecommendProductsList 首页推荐产品信息列表
     */
    void fillProductQuote(List<HomeRecommendProducts> homeRecommendProductsList){
        if (homeRecommendProductsList.size() == 0){
            return;
        }
        //行情map
        Map<String, TickerInfo> tickerInfoMap = new HashMap<>();
        //股票产品信息
        List<HomeRecommendProducts> stock = homeRecommendProductsList.stream().filter(a -> a.getProductType().equals(1)).collect(Collectors.toList());
        if (stock.size() > 0){
            //codes
            String productCodes = stock.stream().map(HomeRecommendProducts::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getStockQuote(productCodes,false);
            tickerInfoMap.putAll(map);
        }
        //加密货币产品信息
        List<HomeRecommendProducts> cryptocurrency = homeRecommendProductsList.stream().filter(a -> a.getProductType().equals(2)).collect(Collectors.toList());
        if (cryptocurrency.size() > 0){
            //codes
            String productCodes = cryptocurrency.stream().map(HomeRecommendProducts::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
            tickerInfoMap.putAll(map);
        }
        //期货产品信息
        List<HomeRecommendProducts> futures = homeRecommendProductsList.stream().filter(a -> a.getProductType().equals(3)).collect(Collectors.toList());
        if (futures.size() > 0){
            //codes
            String productCodes = futures.stream().map(HomeRecommendProducts::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getForexQuote(productCodes);
            tickerInfoMap.putAll(map);
        }
        //外汇产品信息
        List<HomeRecommendProducts> forex = homeRecommendProductsList.stream().filter(a -> a.getProductType().equals(4)).collect(Collectors.toList());
        if (forex.size() > 0){
            //codes
            String productCodes = forex.stream().map(HomeRecommendProducts::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getForexQuote(productCodes);
            tickerInfoMap.putAll(map);
        }

        for (int i = 0; i < homeRecommendProductsList.size(); i++) {
            //行情信息
            TickerInfo tickerInfo = tickerInfoMap.get(homeRecommendProductsList.get(i).getProductCode());
            if (tickerInfo != null) {
                homeRecommendProductsList.get(i).setTickerInfo(tickerInfo);
            }
        }
    }

    /**
     * 新增首页推荐产品
     * 
     * @param homeRecommendProducts 首页推荐产品
     * @return 结果
     */
    @Override
    public int insertHomeRecommendProducts(HomeRecommendProducts homeRecommendProducts)
    {
        //产品类型
        Integer productType = homeRecommendProducts.getProductType();
        //产品id
        Long productId = homeRecommendProducts.getProductId();
        //验证此产品是否已经添加入首页推荐
        HomeRecommendProducts search = new HomeRecommendProducts();
        search.setProductId(productId);
        search.setProductType(productType);
        List<HomeRecommendProducts> homeRecommendProductsList = homeRecommendProductsMapper.selectHomeRecommendProductsList(search);
        if (homeRecommendProductsList.size() > 0){
            throw new ServiceException("此产品已添加入首页推荐");
        }
        //产品代码
        String productCode = "";
        //股票
        if (productType.equals(1)){
            //产品信息
            StockProduct product = stockProductMapper.selectStockProductById(productId);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
            productCode = product.getProductCode();
        }else if (productType.equals(2)){
            //产品信息
            CryptocurrencyProduct product = cryptocurrencyProductMapper.selectCryptocurrencyProductById(productId);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
            productCode = product.getProductCode();
        }else if (productType.equals(3)){
            //产品信息
            FuturesProduct product = futuresProductMapper.selectFuturesProductById(productId);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
            productCode = product.getProductCode();
        }else if (productType.equals(4)){
            //产品信息
            ForexProduct product = forexProductMapper.selectForexProductById(productId);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
            productCode = product.getProductCode();
        }else {
            throw new ServiceException("产品类型错误");
        }
        HttpUtils.getRequestLogParams().put("productCode",productCode);
        return homeRecommendProductsMapper.insertHomeRecommendProducts(homeRecommendProducts);
    }

    /**
     * 修改首页推荐产品
     * 
     * @param homeRecommendProducts 首页推荐产品
     * @return 结果
     */
    @Override
    public int updateHomeRecommendProducts(HomeRecommendProducts homeRecommendProducts)
    {
        //产品类型
        Integer productType = homeRecommendProducts.getProductType();
        //产品id
        Long productId = homeRecommendProducts.getProductId();
        //验证此产品是否已经添加入首页推荐
        HomeRecommendProducts search = new HomeRecommendProducts();
        search.setProductId(productId);
        search.setProductType(productType);
        List<HomeRecommendProducts> homeRecommendProductsList = homeRecommendProductsMapper.selectHomeRecommendProductsList(search);
        if (homeRecommendProductsList.size() > 0){
            if (!homeRecommendProductsList.get(0).getId().equals(homeRecommendProducts.getId())){
                throw new ServiceException("此产品已添加入首页推荐");
            }
        }
        //股票
        if (productType.equals(1)){
            //产品信息
            StockProduct product = stockProductMapper.selectStockProductById(productId);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
        }else if (productType.equals(2)){
            //产品信息
            CryptocurrencyProduct product = cryptocurrencyProductMapper.selectCryptocurrencyProductById(productId);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
        }else if (productType.equals(3)){
            //产品信息
            FuturesProduct product = futuresProductMapper.selectFuturesProductById(productId);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
        }else if (productType.equals(4)){
            //产品信息
            ForexProduct product = forexProductMapper.selectForexProductById(productId);
            if (product == null){
                throw new ServiceException("获取产品信息异常");
            }
        }else {
            throw new ServiceException("产品类型错误");
        }
        return homeRecommendProductsMapper.updateHomeRecommendProducts(homeRecommendProducts);
    }

    /**
     * 修改首页推荐产品
     * @param homeRecommendProducts
     * @return
     */
    @Override
    public int updateHomeRecommendProductByProductId(HomeRecommendProducts homeRecommendProducts){
        return homeRecommendProductsMapper.updateHomeRecommendProductByProductId(homeRecommendProducts);
    }

    /**
     * 批量删除首页推荐产品
     * 
     * @param ids 需要删除的首页推荐产品主键
     * @return 结果
     */
    @Override
    public int deleteHomeRecommendProductsByIds(Long[] ids)
    {
        HomeRecommendProducts search = new HomeRecommendProducts();
        search.getParams().put("ids", Arrays.asList(ids));
        List<HomeRecommendProducts> homeRecommendProducts = homeRecommendProductsMapper.selectHomeRecommendProductsList(search);
        //日志记录帮助中心信息
        HttpUtils.getRequestLogParams().put("JSONArray:homeRecommendProducts", JSONObject.toJSONString(homeRecommendProducts));
        return homeRecommendProductsMapper.deleteHomeRecommendProductsByIds(ids);
    }

    /**
     * 删除首页推荐产品信息
     * 
     * @param id 首页推荐产品主键
     * @return 结果
     */
    @Override
    public int deleteHomeRecommendProductsById(Long id)
    {
        return homeRecommendProductsMapper.deleteHomeRecommendProductsById(id);
    }
}
