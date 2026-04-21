package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.UserBibiAssets;
import com.ruoyi.system.mapper.UserBibiAssetsMapper;
import com.ruoyi.system.service.IUserBibiAssetsService;
import com.ruoyi.system.utils.ProductQuoteUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户币币资产Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
@Service
public class UserBibiAssetsServiceImpl implements IUserBibiAssetsService 
{
    @Resource
    private UserBibiAssetsMapper userBibiAssetsMapper;

    /**
     * 查询用户币币资产
     * 
     * @param id 用户币币资产主键
     * @return 用户币币资产
     */
    @Override
    public UserBibiAssets selectUserBibiAssetsById(Long id)
    {
        return userBibiAssetsMapper.selectUserBibiAssetsById(id);
    }

    /**
     * 查询用户币币资产
     * @param userId 用户id
     * @param productCode 产品代码
     * @param productType 产品类型
     * @return
     */
    @Override
    public UserBibiAssets getUserBibiAssets(Long userId,String productCode,Integer productType){
        UserBibiAssets search = new UserBibiAssets();
        search.setUserId(userId);
        search.setProductCode(productCode);
        search.setProductType(productType);
        List<UserBibiAssets> userBibiAssetsList = userBibiAssetsMapper.selectUserBibiAssetsList(search);
        if (userBibiAssetsList.size() == 1){
            return userBibiAssetsList.get(0);
        }else {
            UserBibiAssets userBibiAssets = new UserBibiAssets();
            userBibiAssets.setUserId(userId);
            userBibiAssets.setProductCode(productCode);
            userBibiAssets.setProductType(productType);
            userBibiAssets.setBibiAmount(BigDecimal.ZERO);
            userBibiAssets.setBibiFrozenAmount(BigDecimal.ZERO);
            userBibiAssets.setBuyAmountAll(BigDecimal.ZERO);
            userBibiAssets.setSellAmountAll(BigDecimal.ZERO);
            userBibiAssets.setBuyAndSellAmountDifference(BigDecimal.ZERO);
            userBibiAssets.setSqlVersion(1L);
            return userBibiAssets;
        }
    }

    /**
     * 查询用户币币资产列表
     * 
     * @param userBibiAssets 用户币币资产
     * @return 用户币币资产
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserBibiAssets> selectUserBibiAssetsList(UserBibiAssets userBibiAssets)
    {
        return userBibiAssetsMapper.selectUserBibiAssetsList(userBibiAssets);
    }

    /**
     * 填充其他信息
     * @param userBibiAssets 用户币币资产
     */
    public void fillOtherInfo(List<UserBibiAssets> userBibiAssets){
        fillConvertedToUSDTValue(userBibiAssets);
    }

    /**
     * 填充在持折合USDT价值
     */
    public void fillConvertedToUSDTValue(List<UserBibiAssets> userBibiAssets){
        if (userBibiAssets.size() == 0){
            return;
        }
        //行情map
        Map<String, TickerInfo> tickerInfoMap = new HashMap<>();
        //股票产品信息
        List<UserBibiAssets> stock = userBibiAssets.stream().filter(a -> a.getProductType().equals(1)).collect(Collectors.toList());
        if (stock.size() > 0){
            //codes
            String productCodes = stock.stream().map(UserBibiAssets::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getStockQuote(productCodes,false);
            tickerInfoMap.putAll(map);
        }
        //加密货币产品信息
        List<UserBibiAssets> cryptocurrency = userBibiAssets.stream().filter(a -> a.getProductType().equals(2)).collect(Collectors.toList());
        if (cryptocurrency.size() > 0){
            //codes
            String productCodes = cryptocurrency.stream().map(UserBibiAssets::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
            tickerInfoMap.putAll(map);
        }
        //期货产品信息
        List<UserBibiAssets> futures = userBibiAssets.stream().filter(a -> a.getProductType().equals(3)).collect(Collectors.toList());
        if (futures.size() > 0){
            //codes
            String productCodes = futures.stream().map(UserBibiAssets::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getFuturesQuote(productCodes);
            tickerInfoMap.putAll(map);
        }
        //外汇产品信息
        List<UserBibiAssets> forex = userBibiAssets.stream().filter(a -> a.getProductType().equals(4)).collect(Collectors.toList());
        if (forex.size() > 0){
            //codes
            String productCodes = forex.stream().map(UserBibiAssets::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getForexQuote(productCodes);
            tickerInfoMap.putAll(map);
        }
        for (int i = 0; i < userBibiAssets.size(); i++) {
            //资产信息
            UserBibiAssets vo = userBibiAssets.get(i);
            //产品代码
            String productCode = vo.getProductCode();
            //现价
            BigDecimal nowPrice = BigDecimal.ZERO;
            //行情信息
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null){
                nowPrice = new BigDecimal(tickerInfo.getNowPrice());
            }
            vo.getParams().put("convertUSDT",vo.getBibiAmount().multiply(nowPrice).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE));
        }
    }

    /**
     * 新增用户币币资产
     * 
     * @param userBibiAssets 用户币币资产
     * @return 结果
     */
    @Override
    public int insertUserBibiAssets(UserBibiAssets userBibiAssets)
    {
        userBibiAssets.setCreateTime(DateUtils.getNowDate());
        return userBibiAssetsMapper.insertUserBibiAssets(userBibiAssets);
    }

    /**
     * 修改用户币币资产
     * 
     * @param userBibiAssets 用户币币资产
     * @return 结果
     */
    @Override
    public int updateUserBibiAssets(UserBibiAssets userBibiAssets)
    {
        if (userBibiAssets.getId() != null){
            return userBibiAssetsMapper.updateUserBibiAssets(userBibiAssets);
        }else {
            userBibiAssets.setCreateTime(new Date());
            return userBibiAssetsMapper.insertUserBibiAssets(userBibiAssets);
        }
    }

    /**
     * 批量删除用户币币资产
     * 
     * @param ids 需要删除的用户币币资产主键
     * @return 结果
     */
    @Override
    public int deleteUserBibiAssetsByIds(Long[] ids)
    {
        return userBibiAssetsMapper.deleteUserBibiAssetsByIds(ids);
    }

    /**
     * 删除用户币币资产信息
     * 
     * @param id 用户币币资产主键
     * @return 结果
     */
    @Override
    public int deleteUserBibiAssetsById(Long id)
    {
        return userBibiAssetsMapper.deleteUserBibiAssetsById(id);
    }
}
