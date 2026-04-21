package com.ruoyi.system.service;

import com.ruoyi.system.domain.HelpCenter;

import java.util.List;

/**
 * 帮助中心Service接口
 * 
 * @author ruoyi
 * @date 2023-11-22
 */
public interface IHelpCenterService 
{
    /**
     * 查询帮助中心
     * 
     * @param id 帮助中心主键
     * @return 帮助中心
     */
    public HelpCenter selectHelpCenterById(Long id);

    /**
     * 查询帮助中心列表
     * 
     * @param helpCenter 帮助中心
     * @return 帮助中心集合
     */
    public List<HelpCenter> selectHelpCenterList(HelpCenter helpCenter);

    /**
     * 新增帮助中心
     * 
     * @param helpCenter 帮助中心
     * @return 结果
     */
    public int insertHelpCenter(HelpCenter helpCenter);

    /**
     * 修改帮助中心
     * 
     * @param helpCenter 帮助中心
     * @return 结果
     */
    public int updateHelpCenter(HelpCenter helpCenter);

    /**
     * 批量删除帮助中心
     * 
     * @param ids 需要删除的帮助中心主键集合
     * @return 结果
     */
    public int deleteHelpCenterByIds(Long[] ids);

    /**
     * 删除帮助中心信息
     * 
     * @param id 帮助中心主键
     * @return 结果
     */
    public int deleteHelpCenterById(Long id);
}
