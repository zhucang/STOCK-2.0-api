package com.ruoyi.system.service;

import com.ruoyi.system.domain.OtherValue;

import java.util.List;
import java.util.Map;

/**
 * 其他值Service接口
 * 
 * @author ruoyi
 * @date 2024-03-27
 */
public interface IOtherValueService 
{
    /**
     * 查询其他值
     * 
     * @param id 其他值主键
     * @return 其他值
     */
    public OtherValue selectOtherValueById(Long id);

    /**
     * 查询其他值列表
     * 
     * @param otherValue 其他值
     * @return 其他值集合
     */
    public List<OtherValue> selectOtherValueList(OtherValue otherValue);

    /**
     * 获取其他值map
     */
    public Map<String,String> getOtherValueMap(OtherValue otherValue);

    /**
     * 获取其他值
     * @param otherKey key
     * @return
     */
    public <T>T getOtherValueByKey(String otherKey,Class<T> c);

    /**
     * 新增其他值
     * 
     * @param otherValue 其他值
     * @return 结果
     */
    public int insertOtherValue(OtherValue otherValue);

    /**
     * 修改其他值
     * 
     * @param otherValue 其他值
     * @return 结果
     */
    public int updateOtherValue(OtherValue otherValue);

    /**
     * 批量删除其他值
     * 
     * @param ids 需要删除的其他值主键集合
     * @return 结果
     */
    public int deleteOtherValueByIds(Long[] ids);

    /**
     * 删除其他值信息
     * 
     * @param id 其他值主键
     * @return 结果
     */
    public int deleteOtherValueById(Long id);

    /**
     * 重载缓存
     */
    public void reloadOtherValueCache();
}
