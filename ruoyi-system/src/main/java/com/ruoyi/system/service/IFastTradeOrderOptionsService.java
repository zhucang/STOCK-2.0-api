package com.ruoyi.system.service;

import com.ruoyi.system.domain.FastTradeOrderOptions;

import java.util.List;

/**
 * 极速交易下单选项Service接口
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public interface IFastTradeOrderOptionsService 
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
    public int insertFastTradeOrderOptions(FastTradeOrderOptions fastTradeOrderOptions) throws Exception;

    /**
     * 修改极速交易下单选项
     * 
     * @param fastTradeOrderOptions 极速交易下单选项
     * @return 结果
     */
    public int updateFastTradeOrderOptions(FastTradeOrderOptions fastTradeOrderOptions);

    /**
     * 批量修改极速交易下单选项
     *
     * @param fastTradeOrderOptions 极速交易下单选项
     * @return 结果
     */
    public int batchUpdateUpdateFastTradeOrderOptions(FastTradeOrderOptions fastTradeOrderOptions);

    /**
     * 批量删除极速交易下单选项
     * 
     * @param ids 需要删除的极速交易下单选项主键集合
     * @return 结果
     */
    public int deleteFastTradeOrderOptionsByIds(Long[] ids);

    /**
     * 批量删除极速交易下单选项
     * @param productCodes 产品代码
     * @param productType 产品类型
     * @return
     */
    public int deleteFastTradeOrderOptionsByProductCodes(List<String> productCodes,Integer productType);

    /**
     * 删除极速交易下单选项信息
     * 
     * @param id 极速交易下单选项主键
     * @return 结果
     */
    public int deleteFastTradeOrderOptionsById(Long id);

    /**
     * 清空选项
     * @param productType 产品类型
     * @return
     */
    public int cleanOptions(Integer productType);

    /**
     * 复制模板
     * @param productType 产品类型
     * @param productCodes 产品代码
     * @return
     */
    public int copyTemp(Integer productType,List<String> productCodes);
}
