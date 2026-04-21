package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.FinancialOrder;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 理财订单Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-26
 */
public interface FinancialOrderMapper 
{
    /**
     * 查询理财订单
     * 
     * @param id 理财订单主键
     * @return 理财订单
     */
    public FinancialOrder selectFinancialOrderById(Long id);

    /**
     * 查询理财订单列表
     * 
     * @param financialOrder 理财订单
     * @return 理财订单集合
     */
    public List<FinancialOrder> selectFinancialOrderList(FinancialOrder financialOrder);

    /**
     * 新增理财订单
     * 
     * @param financialOrder 理财订单
     * @return 结果
     */
    public int insertFinancialOrder(FinancialOrder financialOrder);

    /**
     * 修改理财订单
     * 
     * @param financialOrder 理财订单
     * @return 结果
     */
    public int updateFinancialOrder(FinancialOrder financialOrder);

    /**
     * 删除理财订单
     * 
     * @param id 理财订单主键
     * @return 结果
     */
    public int deleteFinancialOrderById(Long id);

    /**
     * 批量删除理财订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinancialOrderByIds(Long[] ids);

    /**
     * 总订单数
     * @param userId 用户id
     * @return
     */
    int selectAllOrderCountByUserId(Long userId);

    /**
     * 累计收益
     * @param userId 用户id
     * @return
     */
    BigDecimal selectAllPayInterestAmountByUserId(Long userId);

    /**
     * 正在托管资金
     * @param userId 用户id
     * @return
     */
    BigDecimal selectHostingAmountByUserId(Long userId);

    /**
     * 预计今日收益
     * @param userId 用户id
     * @return
     */
    BigDecimal selectEstimatedIncomeTodayByUserId(Long userId);

    /**
     * 获取未完成订单
     */
    List<FinancialOrder> selectNotDoneOrder();

    /**
     * 统计时间段内购买次数
     */
    public Integer getUserBuyCountByPeriodOfTime(@Param("userId") Long userId,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    /**
     * 理财订单待审核数量
     * @param baseEntity
     * @return
     */
    List<Long> getUserFinancialPendingReviewNum(BaseEntity baseEntity);
}
