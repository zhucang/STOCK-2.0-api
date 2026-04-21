package com.ruoyi.system.service;

import com.ruoyi.system.domain.CustomerService;
import com.ruoyi.system.domain.LangMgr;

import java.util.List;

/**
 * 客服配置Service接口
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public interface ICustomerServiceService 
{
    /**
     * 查询客服配置
     * 
     * @param id 客服配置主键
     * @return 客服配置
     */
    public CustomerService selectCustomerServiceById(Long id);

    /**
     * 查询客服配置列表
     * 
     * @param customerService 客服配置
     * @return 客服配置集合
     */
    public List<CustomerService> selectCustomerServiceList(CustomerService customerService);

    /**
     * 新增客服配置
     * 
     * @param customerService 客服配置
     * @return 结果
     */
    public int insertCustomerService(CustomerService customerService);

    /**
     * 修改客服配置
     * 
     * @param customerService 客服配置
     * @return 结果
     */
    public int updateCustomerService(CustomerService customerService);

    /**
     * 修改客服名称多语言配置
     * @param customerServiceId 客服配置id
     * @param customerServiceNameLang 客服名称语言包
     * @return
     */

    public int updateCustomerServiceNameLang(Long customerServiceId, LangMgr customerServiceNameLang);

    /**
     * 批量删除客服配置
     * 
     * @param ids 需要删除的客服配置主键集合
     * @return 结果
     */
    public int deleteCustomerServiceByIds(Long[] ids);

    /**
     * 删除客服配置信息
     * 
     * @param id 客服配置主键
     * @return 结果
     */
    public int deleteCustomerServiceById(Long id);
}
