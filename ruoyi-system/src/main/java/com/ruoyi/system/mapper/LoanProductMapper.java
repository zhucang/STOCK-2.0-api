package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.LoanProduct;

import java.util.List;

/**
 * 贷款产品配置Mapper接口
 * 
 * @author ruoyi
 * @date 2024-05-21
 */
public interface LoanProductMapper 
{
    /**
     * 查询贷款产品配置
     * 
     * @param id 贷款产品配置主键
     * @return 贷款产品配置
     */
    public LoanProduct selectLoanProductById(Long id);

    /**
     * 查询贷款产品配置列表
     * 
     * @param loanProduct 贷款产品配置
     * @return 贷款产品配置集合
     */
    public List<LoanProduct> selectLoanProductList(LoanProduct loanProduct);

    /**
     * 新增贷款产品配置
     * 
     * @param loanProduct 贷款产品配置
     * @return 结果
     */
    public int insertLoanProduct(LoanProduct loanProduct);

    /**
     * 修改贷款产品配置
     * 
     * @param loanProduct 贷款产品配置
     * @return 结果
     */
    public int updateLoanProduct(LoanProduct loanProduct);

    /**
     * 删除贷款产品配置
     * 
     * @param id 贷款产品配置主键
     * @return 结果
     */
    public int deleteLoanProductById(Long id);

    /**
     * 批量删除贷款产品配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLoanProductByIds(Long[] ids);
}
