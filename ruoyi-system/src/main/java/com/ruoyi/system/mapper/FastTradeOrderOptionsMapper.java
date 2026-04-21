package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.FastTradeOrderOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 极速交易下单选项Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public interface FastTradeOrderOptionsMapper 
{
    /**
     * 查询极速交易下单选项
     * 
     * @param id 极速交易下单选项主键
     * @return 极速交易下单选项
     */
    public FastTradeOrderOptions selectFastTradeOrderOptionsById(Long id);

    /**
     * 查询极速交易下单选项列表
     * 
     * @param fastTradeOrderOptions 极速交易下单选项
     * @return 极速交易下单选项集合
     */
    public List<FastTradeOrderOptions> selectFastTradeOrderOptionsList(FastTradeOrderOptions fastTradeOrderOptions);

    /**
     * 新增极速交易下单选项
     * 
     * @param fastTradeOrderOptions 极速交易下单选项
     * @return 结果
     */
    public int insertFastTradeOrderOptions(FastTradeOrderOptions fastTradeOrderOptions);

    /**
     * 批量新增极速交易下单选项
     *
     * @param fastTradeOrderOptionsList 极速交易下单选项列表
     * @return 结果
     */
    public int insertFastTradeOrderOptionsList(@Param("fastTradeOrderOptionsList") List<FastTradeOrderOptions> fastTradeOrderOptionsList);

    /**
     * 修改极速交易下单选项
     * 
     * @param fastTradeOrderOptions 极速交易下单选项
     * @return 结果
     */
    public int updateFastTradeOrderOptions(FastTradeOrderOptions fastTradeOrderOptions);

    /**
     * 批量
     *
     * @param ids ids
     * @param fastTradeOrderOptions 极速交易下单选项
     * @return 结果
     */
    int updateFastTradeOrderOptionsByIds(@Param("ids") List<Long> ids, @Param("fastTradeOrderOptions") FastTradeOrderOptions fastTradeOrderOptions);

    /**
     * 删除极速交易下单选项
     * 
     * @param id 极速交易下单选项主键
     * @return 结果
     */
    public int deleteFastTradeOrderOptionsById(Long id);

    /**
     * 批量删除极速交易下单选项
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFastTradeOrderOptionsByIds(Long[] ids);

    /**
     * 批量删除极速交易下单选项
     * @param productCodes 产品代码
     * @param productType 产品类型
     * @return
     */
    public int deleteFastTradeOrderOptionsByProductCodes(@Param("productCodes") List<String> productCodes,@Param("productType") Integer productType);

    /**
     * 清空选项
     * @param productType 产品代码
     * @return
     */
    int cleanOptions(Integer productType);
}
