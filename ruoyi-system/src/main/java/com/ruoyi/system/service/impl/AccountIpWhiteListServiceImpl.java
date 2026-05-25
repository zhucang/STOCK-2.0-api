package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.AccountIpWhiteList;
import com.ruoyi.system.mapper.AccountIpWhiteListMapper;
import com.ruoyi.system.service.IAccountIpWhiteListService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 账号ip白名单Service业务层处理
 *
 * @author ruoyi
 * @date 2026-05-25
 */
@Service
public class AccountIpWhiteListServiceImpl implements IAccountIpWhiteListService
{
    @Resource
    private AccountIpWhiteListMapper accountIpWhiteListMapper;

    /**
     * 查询账号ip白名单
     *
     * @param id 账号ip白名单主键
     * @return 账号ip白名单
     */
    @Override
    public AccountIpWhiteList selectAccountIpWhiteListById(Long id)
    {
        return accountIpWhiteListMapper.selectAccountIpWhiteListById(id);
    }

    /**
     * 查询账号ip白名单列表
     *
     * @param accountIpWhiteList 账号ip白名单
     * @return 账号ip白名单
     */
    @Override
    @Cacheable(value = CacheableKey.ACCOUNT_IP_WHITE_LIST + CacheableKey.LIST, key = "#accountIpWhiteList.cacheableKey()")
    public List<AccountIpWhiteList> selectAccountIpWhiteListList(AccountIpWhiteList accountIpWhiteList)
    {
        return accountIpWhiteListMapper.selectAccountIpWhiteListList(accountIpWhiteList);
    }

    /**
     * 校验账号ip是否在白名单
     *
     * @param accountType 账号类型
     * @param accountId 账号ID
     * @param ipAddress ip地址
     * @return 是否允许登录
     */
    @Override
    @Cacheable(value = CacheableKey.ACCOUNT_IP_WHITE_LIST + CacheableKey.ENTITY + "accountIp",
        key = "#accountType + ':' + #accountId + ':' + #ipAddress")
    public boolean isIpAllowed(Integer accountType, Long accountId, String ipAddress)
    {
        if ((accountType == null && accountId == null) || StringUtils.isEmpty(ipAddress))
        {
            return false;
        }
        return accountIpWhiteListMapper.selectMatchedAccountIpWhiteList(accountType, accountId, ipAddress) != null;
    }

    /**
     * 新增账号ip白名单
     *
     * @param accountIpWhiteList 账号ip白名单
     * @return 结果
     */
    @Override
    @Caching(evict = {
        @CacheEvict(value = CacheableKey.ACCOUNT_IP_WHITE_LIST + CacheableKey.LIST, allEntries = true),
        @CacheEvict(value = CacheableKey.ACCOUNT_IP_WHITE_LIST + CacheableKey.ENTITY + "accountIp", allEntries = true)})
    public int insertAccountIpWhiteList(AccountIpWhiteList accountIpWhiteList)
    {
        validate(accountIpWhiteList);
        AccountIpWhiteList existed = accountIpWhiteListMapper.selectAccountIpWhiteListByAccountAndIp(
            accountIpWhiteList.getAccountType(), accountIpWhiteList.getAccountId(), accountIpWhiteList.getIpAddress());
        if (existed != null)
        {
            throw new ServiceException("此账号ip已在白名单中");
        }
        accountIpWhiteList.setCreateTime(DateUtils.getNowDate());
        int count = accountIpWhiteListMapper.insertAccountIpWhiteList(accountIpWhiteList);
        if (count <= 0)
        {
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改账号ip白名单
     *
     * @param accountIpWhiteList 账号ip白名单
     * @return 结果
     */
    @Override
    @Caching(evict = {
        @CacheEvict(value = CacheableKey.ACCOUNT_IP_WHITE_LIST + CacheableKey.LIST, allEntries = true),
        @CacheEvict(value = CacheableKey.ACCOUNT_IP_WHITE_LIST + CacheableKey.ENTITY + "accountIp", allEntries = true)})
    public int updateAccountIpWhiteList(AccountIpWhiteList accountIpWhiteList)
    {
        validate(accountIpWhiteList);
        AccountIpWhiteList existed = accountIpWhiteListMapper.selectAccountIpWhiteListByAccountAndIp(
            accountIpWhiteList.getAccountType(), accountIpWhiteList.getAccountId(), accountIpWhiteList.getIpAddress());
        if (existed != null && !existed.getId().equals(accountIpWhiteList.getId()))
        {
            throw new ServiceException("此账号ip已在白名单中");
        }
        accountIpWhiteList.setUpdateTime(DateUtils.getNowDate());
        int count = accountIpWhiteListMapper.updateAccountIpWhiteList(accountIpWhiteList);
        if (count <= 0)
        {
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 批量删除账号ip白名单
     *
     * @param ids 需要删除的账号ip白名单主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
        @CacheEvict(value = CacheableKey.ACCOUNT_IP_WHITE_LIST + CacheableKey.LIST, allEntries = true),
        @CacheEvict(value = CacheableKey.ACCOUNT_IP_WHITE_LIST + CacheableKey.ENTITY + "accountIp", allEntries = true)})
    public int deleteAccountIpWhiteListByIds(Long[] ids)
    {
        return accountIpWhiteListMapper.deleteAccountIpWhiteListByIds(ids);
    }

    /**
     * 删除账号ip白名单信息
     *
     * @param id 账号ip白名单主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
        @CacheEvict(value = CacheableKey.ACCOUNT_IP_WHITE_LIST + CacheableKey.LIST, allEntries = true),
        @CacheEvict(value = CacheableKey.ACCOUNT_IP_WHITE_LIST + CacheableKey.ENTITY + "accountIp", allEntries = true)})
    public int deleteAccountIpWhiteListById(Long id)
    {
        return accountIpWhiteListMapper.deleteAccountIpWhiteListById(id);
    }

    private void validate(AccountIpWhiteList accountIpWhiteList)
    {
        if (accountIpWhiteList.getAccountType() == null && accountIpWhiteList.getAccountId() == null)
        {
            throw new ServiceException("账号类型和账号ID至少填写一个");
        }
        if (StringUtils.isEmpty(accountIpWhiteList.getIpAddress()))
        {
            throw new ServiceException("请输入ip");
        }
        accountIpWhiteList.setIpAddress(accountIpWhiteList.getIpAddress().trim());
    }
}
