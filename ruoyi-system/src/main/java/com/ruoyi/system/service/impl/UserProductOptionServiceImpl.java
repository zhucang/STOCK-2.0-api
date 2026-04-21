package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.UserProductOption;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.system.mapper.UserProductOptionMapper;
import com.ruoyi.system.service.IUserProductOptionService;
import com.ruoyi.system.utils.ProductQuoteUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户产品自选关联信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
@Service
public class UserProductOptionServiceImpl implements IUserProductOptionService 
{
    @Resource
    private UserProductOptionMapper userProductOptionMapper;

    /**
     * 查询用户产品自选关联信息
     * 
     * @param id 用户产品自选关联信息主键
     * @return 用户产品自选关联信息
     */
    @Override
    public UserProductOption selectUserProductOptionById(Long id)
    {
        return userProductOptionMapper.selectUserProductOptionById(id);
    }

    /**
     * 查询用户产品自选关联信息列表
     * 
     * @param userProductOption 用户产品自选关联信息
     * @return 用户产品自选关联信息
     */
    @Override
    public List<UserProductOption> selectUserProductOptionList(UserProductOption userProductOption)
    {
        List<UserProductOption> userProductOptions = userProductOptionMapper.selectUserProductOptionList(userProductOption);
        //填充行情信息
        if (userProductOption.getProductType() == null){
            fillProductQuote(userProductOptions);
        }else {
            fillProductQuote(userProductOptions,userProductOption.getProductType());
        }
        return userProductOptions;
    }

    /**
     * 填充行情信息
     * @param userProductOptions 自选列表
     * @param productType 产品类型（1：美股 2：加密货币 3：期货 4：外汇）
     */
    void fillProductQuote(List<UserProductOption> userProductOptions,Integer productType){
        if (userProductOptions.size() == 0){
            return;
        }
        //codes
        String productCodes = userProductOptions.stream().map(UserProductOption::getProductCode).collect(Collectors.joining(","));
        //行情map
        Map<String, TickerInfo> tickerInfoMap = null;
        if (productType.equals(1)){
            tickerInfoMap = ProductQuoteUtils.getStockQuote(productCodes,false);
        }else if(productType.equals(2)){
            tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
        }else if (productType.equals(3)){
            tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCodes);
        }else if (productType.equals(4)){
            tickerInfoMap = ProductQuoteUtils.getForexQuote(productCodes);
        }else {
            return;
        }
        for (int i = 0; i < userProductOptions.size(); i++) {
            //持仓信息
            UserProductOption userProductOption = userProductOptions.get(i);
            //行情信息
            TickerInfo tickerInfo = tickerInfoMap.get(userProductOption.getProductCode());
            if (tickerInfo != null){
                userProductOption.setTickerInfo(tickerInfo);
            }
        }
    }

    /**
     * 填充行情信息
     * @param userProductOptions 自选列表
     */
    void fillProductQuote(List<UserProductOption> userProductOptions){
        if (userProductOptions.size() == 0){
            return;
        }
        //行情map
        Map<String, TickerInfo> tickerInfoMap = new HashMap<>();
        //股票产品信息
        List<UserProductOption> stock = userProductOptions.stream().filter(a -> a.getProductType().equals(1)).collect(Collectors.toList());
        if (stock.size() > 0){
            //codes
            String productCodes = stock.stream().map(UserProductOption::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getStockQuote(productCodes,false);
            tickerInfoMap.putAll(map);
        }
        //加密货币产品信息
        List<UserProductOption> cryptocurrency = userProductOptions.stream().filter(a -> a.getProductType().equals(2)).collect(Collectors.toList());
        if (cryptocurrency.size() > 0){
            //codes
            String productCodes = cryptocurrency.stream().map(UserProductOption::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getCryptoCurrencyQuote(productCodes,false);
            tickerInfoMap.putAll(map);
        }
        //期货产品信息
        List<UserProductOption> futures = userProductOptions.stream().filter(a -> a.getProductType().equals(3)).collect(Collectors.toList());
        if (futures.size() > 0){
            //codes
            String productCodes = futures.stream().map(UserProductOption::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getFuturesQuote(productCodes);
            tickerInfoMap.putAll(map);
        }
        //外汇产品信息
        List<UserProductOption> forex = userProductOptions.stream().filter(a -> a.getProductType().equals(4)).collect(Collectors.toList());
        if (forex.size() > 0){
            //codes
            String productCodes = forex.stream().map(UserProductOption::getProductCode).collect(Collectors.joining(","));
            Map<String, TickerInfo> map = ProductQuoteUtils.getForexQuote(productCodes);
            tickerInfoMap.putAll(map);
        }

        for (int i = 0; i < userProductOptions.size(); i++) {
            //行情信息
            TickerInfo tickerInfo = tickerInfoMap.get(userProductOptions.get(i).getProductCode());
            if (tickerInfo != null) {
                userProductOptions.get(i).setTickerInfo(tickerInfo);
            }
        }
    }

    /**
     * 新增用户产品自选关联信息
     * 
     * @param userProductOption 用户产品自选关联信息
     * @return 结果
     */
    @Override
    public int insertUserProductOption(UserProductOption userProductOption)
    {
        userProductOption.setCreateTime(DateUtils.getNowDate());
        UserProductOption search = new UserProductOption();
        search.setProductId(userProductOption.getProductId());
        search.setProductType(userProductOption.getProductType());
        search.setUserId(userProductOption.getUserId());
        if (userProductOptionMapper.selectUserProductOptionList(search).size() > 0){
            throw new LangException("hint_62","此产品已添加自选");
        }
        return userProductOptionMapper.insertUserProductOption(userProductOption);
    }

    /**
     * 修改用户产品自选关联信息
     * 
     * @param userProductOption 用户产品自选关联信息
     * @return 结果
     */
    @Override
    public int updateUserProductOption(UserProductOption userProductOption)
    {
        return userProductOptionMapper.updateUserProductOption(userProductOption);
    }

    /**
     * 批量删除用户产品自选关联信息
     * 
     * @param ids 需要删除的用户产品自选关联信息主键
     * @return 结果
     */
    @Override
    public int deleteUserProductOptionByIds(Long[] ids)
    {
        return userProductOptionMapper.deleteUserProductOptionByIds(ids);
    }

    /**
     * 删除用户产品自选关联信息信息
     * 
     * @param id 用户产品自选关联信息主键
     * @return 结果
     */
    @Override
    public int deleteUserProductOptionById(Long id)
    {
        return userProductOptionMapper.deleteUserProductOptionById(id);
    }

    /**
     * 删除自选产品
     * @param productCode 产品代码
     * @param productType 产品类型（1：美股 2：加密货币 3：期货 4：外汇）
     * @return
     */
    @Override
    public AjaxResult delOption(String productCode, Integer productType){
        //用户id
        Long userId = SecurityUtils.getUserId();
        UserProductOption userOptionInfo = userProductOptionMapper.getUserOptionInfo(userId, productCode, productType);
        if (userOptionInfo == null){
            return AjaxResult.error("hint_delErrorOptionNotExist","删除失败, 自选股不存在");
        }
        int count = userProductOptionMapper.deleteUserProductOptionById(userOptionInfo.getId());
        if (count <= 0){
            return AjaxResult.error(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 获取productCodes数组中用户已添加自选的内容
     * @param userId 用户id
     * @param productCodes 产品代码
     * @param productType 产品类型（1：美股 2：加密货币 3：期货 4：外汇）
     * @return
     */
    @Override
    public List<String> getUserOptionInProducts(Long userId,List<String> productCodes,Integer productType){
        return userProductOptionMapper.getUserOptionInProducts(userId,productCodes,productType);
    }

}
