package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.VipExperienceValue;
import com.ruoyi.system.mapper.VipExperienceValueMapper;
import com.ruoyi.system.service.IVipExperienceValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * VIP经验值Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-24
 */
@Service
public class VipExperienceValueServiceImpl implements IVipExperienceValueService 
{
    @Resource
    private VipExperienceValueMapper vipExperienceValueMapper;

    /**
     * 查询VIP经验值
     * 
     * @param id VIP经验值主键
     * @return VIP经验值
     */
    @Override
    public VipExperienceValue selectVipExperienceValueById(Long id)
    {
        return vipExperienceValueMapper.selectVipExperienceValueById(id);
    }

    /**
     * 查询VIP经验值列表
     * 
     * @param vipExperienceValue VIP经验值
     * @return VIP经验值
     */
    @Override
    public List<VipExperienceValue> selectVipExperienceValueList(VipExperienceValue vipExperienceValue)
    {
        return vipExperienceValueMapper.selectVipExperienceValueList(vipExperienceValue);
    }

    /**
     * 新增VIP经验值
     * 
     * @param vipExperienceValue VIP经验值
     * @return 结果
     */
    @Override
    public int insertVipExperienceValue(VipExperienceValue vipExperienceValue)
    {
        vipExperienceValue.setCreateTime(DateUtils.getNowDate());
        return vipExperienceValueMapper.insertVipExperienceValue(vipExperienceValue);
    }

    /**
     * 修改VIP经验值
     * 
     * @param vipExperienceValue VIP经验值
     * @return 结果
     */
    @Override
    public int updateVipExperienceValue(VipExperienceValue vipExperienceValue)
    {
        return vipExperienceValueMapper.updateVipExperienceValue(vipExperienceValue);
    }

    /**
     * 批量删除VIP经验值
     * 
     * @param ids 需要删除的VIP经验值主键
     * @return 结果
     */
    @Override
    public int deleteVipExperienceValueByIds(Long[] ids)
    {
        return vipExperienceValueMapper.deleteVipExperienceValueByIds(ids);
    }

    /**
     * 删除VIP经验值信息
     * 
     * @param id VIP经验值主键
     * @return 结果
     */
    @Override
    public int deleteVipExperienceValueById(Long id)
    {
        return vipExperienceValueMapper.deleteVipExperienceValueById(id);
    }

    /**
     * 获取用户当前vip经验值
     */
    @Override
    public BigDecimal getUserCurrentVipExperienceValue(Long userId){
        return vipExperienceValueMapper.getUserCurrentVipExperienceValue(userId);
    }
}
