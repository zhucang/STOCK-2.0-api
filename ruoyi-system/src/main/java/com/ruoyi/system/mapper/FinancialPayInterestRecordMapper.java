package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.FinancialPayInterestRecord;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 理财产品派息记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-10
 */
public interface FinancialPayInterestRecordMapper 
{
    /**
     * 查询理财产品派息记录
     * 
     * @param id 理财产品派息记录主键
     * @return 理财产品派息记录
     */
    public FinancialPayInterestRecord selectFinancialPayInterestRecordById(Long id);

    /**
     * 查询理财产品派息记录列表
     * 
     * @param financialPayInterestRecord 理财产品派息记录
     * @return 理财产品派息记录集合
     */
    public List<FinancialPayInterestRecord> selectFinancialPayInterestRecordList(FinancialPayInterestRecord financialPayInterestRecord);

    /**
     * 新增理财产品派息记录
     * 
     * @param financialPayInterestRecord 理财产品派息记录
     * @return 结果
     */
    public int insertFinancialPayInterestRecord(FinancialPayInterestRecord financialPayInterestRecord);

    /**
     * 修改理财产品派息记录
     * 
     * @param financialPayInterestRecord 理财产品派息记录
     * @return 结果
     */
    public int updateFinancialPayInterestRecord(FinancialPayInterestRecord financialPayInterestRecord);

    /**
     * 删除理财产品派息记录
     * 
     * @param id 理财产品派息记录主键
     * @return 结果
     */
    public int deleteFinancialPayInterestRecordById(Long id);

    /**
     * 批量删除理财产品派息记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinancialPayInterestRecordByIds(Long[] ids);

    /**
     * 获取理财订单派息金额
     */
    @MapKey("financialOrderId")
    public Map<Long, Map<Long, BigDecimal>> getTotalPayAmountByOrderId(@Param("list") List<Long> financialOrderIds);
}
