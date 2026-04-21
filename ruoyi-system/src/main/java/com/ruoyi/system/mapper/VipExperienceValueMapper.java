package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.VipExperienceValue;

import java.math.BigDecimal;
import java.util.List;

/**
 * VIP经验值Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-24
 */
public interface VipExperienceValueMapper 
{
    /**
     * 查询VIP经验值
     * 
     * @param id VIP经验值主键
     * @return VIP经验值
     */
    public VipExperienceValue selectVipExperienceValueById(Long id);

    /**
     * 查询VIP经验值列表
     * 
     * @param vipExperienceValue VIP经验值
     * @return VIP经验值集合
     */
    public List<VipExperienceValue> selectVipExperienceValueList(VipExperienceValue vipExperienceValue);

    /**
     * 新增VIP经验值
     * 
     * @param vipExperienceValue VIP经验值
     * @return 结果
     */
    public int insertVipExperienceValue(VipExperienceValue vipExperienceValue);

    /**
     * 修改VIP经验值
     * 
     * @param vipExperienceValue VIP经验值
     * @return 结果
     */
    public int updateVipExperienceValue(VipExperienceValue vipExperienceValue);

    /**
     * 删除VIP经验值
     * 
     * @param id VIP经验值主键
     * @return 结果
     */
    public int deleteVipExperienceValueById(Long id);

    /**
     * 批量删除VIP经验值
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVipExperienceValueByIds(Long[] ids);

    /**
     * 获取用户当前vip经验值
     */
    public BigDecimal getUserCurrentVipExperienceValue(Long userId);
}
