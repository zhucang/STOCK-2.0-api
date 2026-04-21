package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.IpBlackList;
import com.ruoyi.system.mapper.IpBlackListMapper;
import com.ruoyi.system.service.IIpBlackListService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * ip黑名单Service业务层处理
 *
 * @author ruoyi
 * @date 2023-11-03
 */
@Service
public class IpBlackListServiceImpl implements IIpBlackListService
{
    @Resource
    private IpBlackListMapper ipBlackListMapper;

    /**
     * 查询ip黑名单
     *
     * @param id ip黑名单主键
     * @return ip黑名单
     */
    @Override
//    @Cacheable(value = CacheableKey.IP_BLACK_LIST + CacheableKey.ENTITY,key = "#id")
    public IpBlackList selectIpBlackListById(Long id)
    {
        return ipBlackListMapper.selectIpBlackListById(id);
    }

    /**
     * 查询ip黑名单
     *
     * @param ip ip
     * @return ip黑名单
     */
    @Override
    @Cacheable(value = CacheableKey.IP_BLACK_LIST + CacheableKey.ENTITY + "ip",key = "#ip")
    public IpBlackList selectIpBlackListByIp(String ip) {
        return ipBlackListMapper.selectIpBlackListByIp(ip);
    }

    /**
     * 查询ip黑名单列表
     *
     * @param ipBlackList ip黑名单
     * @return ip黑名单
     */
    @Cacheable(value = CacheableKey.IP_BLACK_LIST + CacheableKey.LIST,key = "#ipBlackList.cacheableKey()")
    public List<IpBlackList> selectIpBlackListList(IpBlackList ipBlackList)
    {
        return ipBlackListMapper.selectIpBlackListList(ipBlackList);
    }

    /**
     * 新增ip黑名单
     *
     * @param ipBlackList ip黑名单
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.IP_BLACK_LIST + CacheableKey.LIST,allEntries = true)
    public int insertIpBlackList(IpBlackList ipBlackList)
    {
        IpBlackList ipBlackListVo = selectIpBlackListByIp(ipBlackList.getIpAddress());
        if (ipBlackListVo != null){
            throw new ServiceException("此ip已在黑名单中");
        }
        ipBlackList.setCreateTime(new Date());
        int count = ipBlackListMapper.insertIpBlackList(ipBlackList);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改ip黑名单
     *
     * @param ipBlackList ip黑名单
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.IP_BLACK_LIST + CacheableKey.ENTITY,key = "#ipBlackList.id"),
            @CacheEvict(value = CacheableKey.IP_BLACK_LIST + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.IP_BLACK_LIST + CacheableKey.ENTITY + "ip",allEntries = true)})
    public int updateIpBlackList(IpBlackList ipBlackList)
    {
        IpBlackList ipBlackListVo = ipBlackListMapper.selectIpBlackListByIp(ipBlackList.getIpAddress());
        if (ipBlackListVo != null && ipBlackList.getId().equals(ipBlackListVo.getId())){
            throw new ServiceException("此ip已在黑名单中");
        }
        int count = ipBlackListMapper.updateIpBlackList(ipBlackList);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 批量删除ip黑名单
     *
     * @param ids 需要删除的ip黑名单主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.IP_BLACK_LIST + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.IP_BLACK_LIST + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.IP_BLACK_LIST + CacheableKey.ENTITY + "ip",allEntries = true)})
    public int deleteIpBlackListByIds(Long[] ids)
    {
        return ipBlackListMapper.deleteIpBlackListByIds(ids);
    }

    /**
     * 删除ip黑名单信息
     *
     * @param id ip黑名单主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.IP_BLACK_LIST + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.IP_BLACK_LIST + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.IP_BLACK_LIST + CacheableKey.ENTITY + "ip",allEntries = true)})
    public int deleteIpBlackListById(Long id)
    {
        return ipBlackListMapper.deleteIpBlackListById(id);
    }
}
