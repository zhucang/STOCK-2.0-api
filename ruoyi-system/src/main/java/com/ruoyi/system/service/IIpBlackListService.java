package com.ruoyi.system.service;

import com.ruoyi.system.domain.IpBlackList;

import java.util.List;

/**
 * ip黑名单Service接口
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
public interface IIpBlackListService 
{
    /**
     * 查询ip黑名单
     * 
     * @param id ip黑名单主键
     * @return ip黑名单
     */
    public IpBlackList selectIpBlackListById(Long id);

    /**
     * 查询ip黑名单
     *
     * @param ip ip
     * @return ip黑名单
     */
    public IpBlackList selectIpBlackListByIp(String ip);

    /**
     * 查询ip黑名单列表
     * 
     * @param ipBlackList ip黑名单
     * @return ip黑名单集合
     */
    public List<IpBlackList> selectIpBlackListList(IpBlackList ipBlackList);

    /**
     * 新增ip黑名单
     * 
     * @param ipBlackList ip黑名单
     * @return 结果
     */
    public int insertIpBlackList(IpBlackList ipBlackList);

    /**
     * 修改ip黑名单
     *
     * @param ipBlackList ip黑名单
     * @return 结果
     */
    public int updateIpBlackList(IpBlackList ipBlackList);

    /**
     * 批量删除ip黑名单
     * 
     * @param ids 需要删除的ip黑名单主键集合
     * @return 结果
     */
    public int deleteIpBlackListByIds(Long[] ids);

    /**
     * 删除ip黑名单信息
     * 
     * @param id ip黑名单主键
     * @return 结果
     */
    public int deleteIpBlackListById(Long id);
}
