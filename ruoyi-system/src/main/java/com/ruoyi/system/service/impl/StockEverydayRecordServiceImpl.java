package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ScheduledTaskExceptionLog;
import com.ruoyi.system.domain.StockEverydayRecord;
import com.ruoyi.system.domain.StockProduct;
import com.ruoyi.system.mapper.ScheduledTaskExceptionLogMapper;
import com.ruoyi.system.mapper.SelfSellProductRealTimeMapper;
import com.ruoyi.system.mapper.StockEverydayRecordMapper;
import com.ruoyi.system.mapper.StockProductMapper;
import com.ruoyi.system.service.IStockEverydayRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 股票日涨幅记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
@Service
public class StockEverydayRecordServiceImpl implements IStockEverydayRecordService 
{
    @Resource
    private StockEverydayRecordMapper stockEverydayRecordMapper;

    @Resource
    private StockProductMapper stockProductMapper;

    @Resource
    private ScheduledTaskExceptionLogMapper scheduledTaskExceptionLogMapper;

    @Resource
    private SelfSellProductRealTimeMapper selfSellProductRealTimeMapper;

    /**
     * 查询股票日涨幅记录
     * 
     * @param id 股票日涨幅记录主键
     * @return 股票日涨幅记录
     */
    @Override
    public StockEverydayRecord selectStockEverydayRecordById(Long id)
    {
        return stockEverydayRecordMapper.selectStockEverydayRecordById(id);
    }

    /**
     * 查询股票日涨幅记录列表
     * 
     * @param stockEverydayRecord 股票日涨幅记录
     * @return 股票日涨幅记录
     */
    @Override
    public List<StockEverydayRecord> selectStockEverydayRecordList(StockEverydayRecord stockEverydayRecord)
    {
        return stockEverydayRecordMapper.selectStockEverydayRecordList(stockEverydayRecord);
    }

    /**
     * 新增股票日涨幅记录
     * 
     * @param stockEverydayRecord 股票日涨幅记录
     * @return 结果
     */
    @Override
    public int insertStockEverydayRecord(StockEverydayRecord stockEverydayRecord)
    {
        stockEverydayRecord.setCreateTime(DateUtils.getNowDate());
        return stockEverydayRecordMapper.insertStockEverydayRecord(stockEverydayRecord);
    }

    /**
     * 修改股票日涨幅记录
     * 
     * @param stockEverydayRecord 股票日涨幅记录
     * @return 结果
     */
    @Override
    public int updateStockEverydayRecord(StockEverydayRecord stockEverydayRecord)
    {
        return stockEverydayRecordMapper.updateStockEverydayRecord(stockEverydayRecord);
    }

    /**
     * 批量删除股票日涨幅记录
     * 
     * @param ids 需要删除的股票日涨幅记录主键
     * @return 结果
     */
    @Override
    public int deleteStockEverydayRecordByIds(Long[] ids)
    {
        return stockEverydayRecordMapper.deleteStockEverydayRecordByIds(ids);
    }

    /**
     * 删除股票日涨幅记录信息
     * 
     * @param id 股票日涨幅记录主键
     * @return 结果
     */
    @Override
    public int deleteStockEverydayRecordById(Long id)
    {
        return stockEverydayRecordMapper.deleteStockEverydayRecordById(id);
    }

    /**
     * 清空股票日涨幅记录信息
     *
     * @param productCodes 股票代码
     * @return 结果
     */
    @Override
    public int cleanStockEverydayRecord(List<String> productCodes){
        return stockEverydayRecordMapper.cleanStockEverydayRecord(productCodes);
    }

    /**
     * 每日收盘时保存每日数据
     */
    @Override
    public void saveStockEverydayRecordTask(){
        //所有产品
        StockProduct search = new StockProduct();
        search.setIsSelfSell(1);
        List<StockProduct> allProducts = stockProductMapper.selectStockProductList(search);
        if (allProducts.size() == 0){
            return;
        }
        //昨日时间
        Date yesterdayDateTime = DateUtils.getDateBeforeOrAfterDate(new Date(), Calendar.DAY_OF_YEAR,-1);
        //即将插入的行情每日记录
        List<StockEverydayRecord> everydayRecords = new ArrayList<>();
        //遍历
        for (int i = 0; i < allProducts.size(); i++) {
            //产品信息
            StockProduct product = allProducts.get(i);
            //产品代码
            String productCode = product.getProductCode();
            try{
                //行情详情map
                Map<String, Map> map = selfSellProductRealTimeMapper.selectRealTimeTradeDetail(productCode, 1, yesterdayDateTime, 1);
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
                    StockEverydayRecord everydayRecord = new StockEverydayRecord();
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
                scheduledTaskExceptionLog.setJobName("股票每日收盘时保存每日数据");
                scheduledTaskExceptionLog.setExceptionInfo("记录每日数据异常");
                scheduledTaskExceptionLog.setCreateTime(new Date());
                scheduledTaskExceptionLog.setExceptionInfoDetail("记录每日数据异常");
                scheduledTaskExceptionLog.setRelateInfo("productCode:"+productCode);
                scheduledTaskExceptionLog.setType(10);
                scheduledTaskExceptionLogMapper.insertScheduledTaskExceptionLog(scheduledTaskExceptionLog);
            }
        }
        if (everydayRecords.size() > 0){
            stockEverydayRecordMapper.insertStockEverydayRecords(everydayRecords);
        }
    }
}
