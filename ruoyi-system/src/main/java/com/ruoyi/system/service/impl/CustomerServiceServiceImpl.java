package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.CustomerService;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.mapper.CustomerServiceMapper;
import com.ruoyi.system.service.ICustomerServiceService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 客服配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@Service
public class CustomerServiceServiceImpl implements ICustomerServiceService 
{
    @Resource
    private CustomerServiceMapper customerServiceMapper;

    /**
     * 查询客服配置
     * 
     * @param id 客服配置主键
     * @return 客服配置
     */
    @Override
//    @Cacheable(value = CacheableKey.CUSTOMER_SERVICE + CacheableKey.ENTITY,key = "#id")
    public CustomerService selectCustomerServiceById(Long id)
    {
        return customerServiceMapper.selectCustomerServiceById(id);
    }

    /**
     * 查询客服配置列表
     * 
     * @param customerService 客服配置
     * @return 客服配置
     */
    @Override
    @Cacheable(value = CacheableKey.CUSTOMER_SERVICE + CacheableKey.LIST,key = "#customerService.cacheableKey()")
    public List<CustomerService> selectCustomerServiceList(CustomerService customerService)
    {
        return customerServiceMapper.selectCustomerServiceList(customerService);
    }

    /**
     * 新增客服配置
     * 
     * @param customerService 客服配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.CUSTOMER_SERVICE + CacheableKey.LIST,allEntries = true)
    public int insertCustomerService(CustomerService customerService)
    {
        return customerServiceMapper.insertCustomerService(customerService);
    }

    /**
     * 修改客服配置
     * 
     * @param customerService 客服配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.CUSTOMER_SERVICE + CacheableKey.ENTITY,key = "#customerService.id"),
            @CacheEvict(value = CacheableKey.CUSTOMER_SERVICE + CacheableKey.LIST,allEntries = true)})
    public int updateCustomerService(CustomerService customerService)
    {
        return customerServiceMapper.updateCustomerService(customerService);
    }

    /**
     * 修改客服名称多语言配置
     * @param customerServiceId 客服配置id
     * @param customerServiceNameLang 客服名称语言包
     * @return
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.CUSTOMER_SERVICE + CacheableKey.ENTITY,key = "#customerServiceId"),
            @CacheEvict(value = CacheableKey.CUSTOMER_SERVICE + CacheableKey.LIST,allEntries = true)})
    public int updateCustomerServiceNameLang(Long customerServiceId, LangMgr customerServiceNameLang){
        CustomerService customerService = new CustomerService();
        customerService.setId(customerServiceId);
        customerService.setCustomerServiceNameLang(customerServiceNameLang);
        return customerServiceMapper.updateCustomerService(customerService);
    }

    /**
     * 批量删除客服配置
     * 
     * @param ids 需要删除的客服配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.CUSTOMER_SERVICE + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.CUSTOMER_SERVICE + CacheableKey.LIST,allEntries = true)})
    public int deleteCustomerServiceByIds(Long[] ids)
    {
        CustomerService search = new CustomerService();
        search.getParams().put("ids", Arrays.asList(ids));
        List<CustomerService> customerServices = customerServiceMapper.selectCustomerServiceList(search);
        //日志记录客服配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:customerServices", JSONObject.toJSONString(customerServices));
        return customerServiceMapper.deleteCustomerServiceByIds(ids);
    }

    /**
     * 删除客服配置信息
     * 
     * @param id 客服配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.CUSTOMER_SERVICE + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.CUSTOMER_SERVICE + CacheableKey.LIST,allEntries = true)})
    public int deleteCustomerServiceById(Long id)
    {
        return customerServiceMapper.deleteCustomerServiceById(id);
    }
}
