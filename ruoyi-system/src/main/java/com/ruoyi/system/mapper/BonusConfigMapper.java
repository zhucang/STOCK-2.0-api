package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.BonusConfig;

import java.util.List;

/**
 * 赠送彩金配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-29
 */
public interface BonusConfigMapper 
{
    /**
     * 查询赠送彩金配置
     * 
     * @param id 赠送彩金配置主键
     * @return 赠送彩金配置
     */
    public BonusConfig selectBonusConfigById(Long id);

    /**
     * 查询赠送彩金配置列表
     * 
     * @param bonusConfig 赠送彩金配置
     * @return 赠送彩金配置集合
     */
    public List<BonusConfig> selectBonusConfigList(BonusConfig bonusConfig);

    /**
     * 新增赠送彩金配置
     * 
     * @param bonusConfig 赠送彩金配置
     * @return 结果
     */
    public int insertBonusConfig(BonusConfig bonusConfig);

    /**
     * 修改赠送彩金配置
     * 
     * @param bonusConfig 赠送彩金配置
     * @return 结果
     */
    public int updateBonusConfig(BonusConfig bonusConfig);

    /**
     * 删除赠送彩金配置
     * 
     * @param id 赠送彩金配置主键
     * @return 结果
     */
    public int deleteBonusConfigById(Long id);

    /**
     * 批量删除赠送彩金配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBonusConfigByIds(Long[] ids);
}
