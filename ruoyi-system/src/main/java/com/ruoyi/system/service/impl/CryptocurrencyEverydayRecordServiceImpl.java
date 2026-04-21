package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.CryptocurrencyEverydayRecord;
import com.ruoyi.system.domain.CryptocurrencyProduct;
import com.ruoyi.system.domain.ScheduledTaskExceptionLog;
import com.ruoyi.system.mapper.CryptocurrencyEverydayRecordMapper;
import com.ruoyi.system.mapper.CryptocurrencyProductMapper;
import com.ruoyi.system.mapper.ScheduledTaskExceptionLogMapper;
import com.ruoyi.system.mapper.SelfSellProductRealTimeMapper;
import com.ruoyi.system.service.ICryptocurrencyEverydayRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 加密货币日涨幅记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
@Service
public class CryptocurrencyEverydayRecordServiceImpl implements ICryptocurrencyEverydayRecordService 
{
    @Resource
    private CryptocurrencyEverydayRecordMapper cryptocurrencyEverydayRecordMapper;

    @Resource
    private CryptocurrencyProductMapper cryptocurrencyProductMapper;

    @Resource
    private ScheduledTaskExceptionLogMapper scheduledTaskExceptionLogMapper;

    @Resource
    private SelfSellProductRealTimeMapper selfSellProductRealTimeMapper;

    /**
     * 查询加密货币日涨幅记录
     * 
     * @param id 加密货币日涨幅记录主键
     * @return 加密货币日涨幅记录
     */
    @Override
    public CryptocurrencyEverydayRecord selectCryptocurrencyEverydayRecordById(Long id)
    {
        return cryptocurrencyEverydayRecordMapper.selectCryptocurrencyEverydayRecordById(id);
    }

    /**
     * 查询加密货币日涨幅记录列表
     * 
     * @param cryptocurrencyEverydayRecord 加密货币日涨幅记录
     * @return 加密货币日涨幅记录
     */
    @Override
    public List<CryptocurrencyEverydayRecord> selectCryptocurrencyEverydayRecordList(CryptocurrencyEverydayRecord cryptocurrencyEverydayRecord)
    {
        return cryptocurrencyEverydayRecordMapper.selectCryptocurrencyEverydayRecordList(cryptocurrencyEverydayRecord);
    }

    /**
     * 新增加密货币日涨幅记录
     * 
     * @param cryptocurrencyEverydayRecord 加密货币日涨幅记录
     * @return 结果
     */
    @Override
    public int insertCryptocurrencyEverydayRecord(CryptocurrencyEverydayRecord cryptocurrencyEverydayRecord)
    {
        cryptocurrencyEverydayRecord.setCreateTime(DateUtils.getNowDate());
        return cryptocurrencyEverydayRecordMapper.insertCryptocurrencyEverydayRecord(cryptocurrencyEverydayRecord);
    }

    /**
     * 修改加密货币日涨幅记录
     * 
     * @param cryptocurrencyEverydayRecord 加密货币日涨幅记录
     * @return 结果
     */
    @Override
    public int updateCryptocurrencyEverydayRecord(CryptocurrencyEverydayRecord cryptocurrencyEverydayRecord)
    {
        return cryptocurrencyEverydayRecordMapper.updateCryptocurrencyEverydayRecord(cryptocurrencyEverydayRecord);
    }

    /**
     * 批量删除加密货币日涨幅记录
     * 
     * @param ids 需要删除的加密货币日涨幅记录主键
     * @return 结果
     */
    @Override
    public int deleteCryptocurrencyEverydayRecordByIds(Long[] ids)
    {
        return cryptocurrencyEverydayRecordMapper.deleteCryptocurrencyEverydayRecordByIds(ids);
    }

    /**
     * 删除加密货币日涨幅记录信息
     * 
     * @param id 加密货币日涨幅记录主键
     * @return 结果
     */
    @Override
    public int deleteCryptocurrencyEverydayRecordById(Long id)
    {
        return cryptocurrencyEverydayRecordMapper.deleteCryptocurrencyEverydayRecordById(id);
    }

    /**
     * 清空加密货币日涨幅记录信息
     *
     * @param productCodes 产品代码
     * @return 结果
     */
    @Override
    public int cleanCryptocurrencyEverydayRecord(List<String> productCodes){
        return cryptocurrencyEverydayRecordMapper.cleanCryptocurrencyEverydayRecord(productCodes);
    }

    /**
     * 每日收盘时保存每日数据
     */
    @Override
    public void saveCryptocurrencyEverydayRecordTask(){
        //自营加密货币产品
        CryptocurrencyProduct search = new CryptocurrencyProduct();
        search.setIsSelfSell(1);
        List<CryptocurrencyProduct> products = cryptocurrencyProductMapper.selectCryptocurrencyProductList(search);
        if (products.size() == 0){
            return;
        }
        //昨日时间
        Date yesterdayDateTime = DateUtils.getDateBeforeOrAfterDate(new Date(), Calendar.DAY_OF_YEAR,-1);
        //即将插入的行情每日记录
        List<CryptocurrencyEverydayRecord> everydayRecords = new ArrayList<>();
        //遍历
        for (int i = 0; i < products.size(); i++) {
            //产品信息
            CryptocurrencyProduct product = products.get(i);
            //产品代码
            String productCode = product.getProductCode();
            try{
                //行情详情map
                Map<String, Map> map = selfSellProductRealTimeMapper.selectRealTimeTradeDetail(productCode, 2, yesterdayDateTime, 1);
                //行情详情
                Map<String,BigDecimal> tradeDetail = map.get(productCode);
                if (tradeDetail != null){
                    //开盘价格
                    BigDecimal openPrice = tradeDetail.get("openPrice");
                    //收盘价格
                    BigDecimal closePrice = tradeDetail.get("closePrice");
                    //最高价格
                    BigDecimal highPrice = tradeDetail.get("highPrice");
                    //最低价格
                    BigDecimal lowPrice = tradeDetail.get("lowPrice");
                    //交易量
                    BigDecimal volumes = tradeDetail.get("volumes");
                    //交易金额
                    BigDecimal amount = tradeDetail.get("amount").setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    //昨日收盘
                    BigDecimal closePricePrevDay = tradeDetail.get("closePricePrevDay");
                    //如果没有昨日收盘，则使用今日开盘价
                    if (closePricePrevDay == null){
                        closePricePrevDay = openPrice;
                    }
                    //当前价格
                    BigDecimal nowPrice = closePrice;
                    //涨跌幅
                    BigDecimal changeRate = nowPrice.subtract(closePricePrevDay).divide(closePricePrevDay,Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE).multiply(new BigDecimal(100)).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    //行情每日记录
                    CryptocurrencyEverydayRecord everydayRecord = new CryptocurrencyEverydayRecord();
                    everydayRecord.setProductCode(productCode);
                    everydayRecord.setNowPrice(nowPrice);
                    everydayRecord.setChangeRate(changeRate);
                    everydayRecord.setOpenPrice(openPrice);
                    everydayRecord.setClosePrice(closePrice);
                    everydayRecord.setBusinessAmount(amount);
                    everydayRecord.setBusinessVolume(volumes);
                    everydayRecord.setMaxPrice(highPrice);
                    everydayRecord.setMinPrice(lowPrice);
                    everydayRecord.setCreateTime(yesterdayDateTime);
                    //添加数据
                    everydayRecords.add(everydayRecord);
                }else {
                    throw new RuntimeException("获取行情信息异常");
                }
            }catch (Exception e){
                //记录异常日志
                ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
                scheduledTaskExceptionLog.setJobName("加密货币每日收盘时保存每日数据");
                scheduledTaskExceptionLog.setExceptionInfo("记录每日数据异常");
                scheduledTaskExceptionLog.setCreateTime(new Date());
                scheduledTaskExceptionLog.setExceptionInfoDetail("记录每日数据异常");
                scheduledTaskExceptionLog.setRelateInfo("productCode:"+productCode);
                scheduledTaskExceptionLog.setType(10);
                scheduledTaskExceptionLogMapper.insertScheduledTaskExceptionLog(scheduledTaskExceptionLog);
            }
        }
        if (everydayRecords.size() > 0){
            cryptocurrencyEverydayRecordMapper.insertCryptocurrencyEverydayRecords(everydayRecords);
        }
    }
}
