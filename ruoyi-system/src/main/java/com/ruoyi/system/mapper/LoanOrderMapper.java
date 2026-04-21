package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.LoanOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 贷款订单Mapper接口
 * 
 * @author ruoyi
 * @date 2024-05-22
 */
public interface LoanOrderMapper 
{
    /**
     * 查询贷款订单
     * 
     * @param id 贷款订单主键
     * @return 贷款订单
     */
    public LoanOrder selectLoanOrderById(Long id);

    /**
     * 查询贷款订单列表
     * 
     * @param loanOrder 贷款订单
     * @return 贷款订单集合
     */
    public List<LoanOrder> selectLoanOrderList(LoanOrder loanOrder);

    /**
     * 获取统计数据
     * @param loanOrder
     * @return
     */
    public List<LoanOrder> getStatisticalData(LoanOrder loanOrder);

    /**
     * 新增贷款订单
     * 
     * @param loanOrder 贷款订单
     * @return 结果
     */
    public int insertLoanOrder(LoanOrder loanOrder);

    /**
     * 修改贷款订单
     * 
     * @param loanOrder 贷款订单
     * @return 结果
     */
    public int updateLoanOrder(LoanOrder loanOrder);

    /**
     * 删除贷款订单
     * 
     * @param id 贷款订单主键
     * @return 结果
     */
    public int deleteLoanOrderById(Long id);

    /**
     * 批量删除贷款订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLoanOrderByIds(Long[] ids);

    /**
     * 获取用户某贷款产品的已借贷次数
     */
    public int getUserAlreadyLoanCountByLoanProductId(@Param("loanProductId") Long loanProductId,
                                                      @Param("userId") Long userId);

    /**
     * 贷款订单待审核数量
     * @param baseEntity
     * @return
     */
    List<Long> getUserLoanPendingReviewNum(BaseEntity baseEntity);

    /**
     * 获取用户的各币种的贷款金额
     * @param userId 用户id
     * @return
     */
    public List<LoanOrder> selectUserLoanAmountAllCurrencyByUserId(Long userId);

}
