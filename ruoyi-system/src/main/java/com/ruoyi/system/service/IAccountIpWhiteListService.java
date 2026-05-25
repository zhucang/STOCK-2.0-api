package com.ruoyi.system.service;

import com.ruoyi.system.domain.AccountIpWhiteList;

import java.util.List;

/**
 * 账号ip白名单Service接口
 *
 * @author ruoyi
 * @date 2026-05-25
 */
public interface IAccountIpWhiteListService
{
    /**
     * 查询账号ip白名单
     *
     * @param id 账号ip白名单主键
     * @return 账号ip白名单
     */
    public AccountIpWhiteList selectAccountIpWhiteListById(Long id);

    /**
     * 查询账号ip白名单列表
     *
     * @param accountIpWhiteList 账号ip白名单
     * @return 账号ip白名单集合
     */
    public List<AccountIpWhiteList> selectAccountIpWhiteListList(AccountIpWhiteList accountIpWhiteList);

    /**
     * 校验账号ip是否在白名单
     *
     * @param accountType 账号类型
     * @param accountId 账号ID
     * @param ipAddress ip地址
     * @return 是否允许登录
     */
    public boolean isIpAllowed(Integer accountType, Long accountId, String ipAddress);

    /**
     * 新增账号ip白名单
     *
     * @param accountIpWhiteList 账号ip白名单
     * @return 结果
     */
    public int insertAccountIpWhiteList(AccountIpWhiteList accountIpWhiteList);

    /**
     * 修改账号ip白名单
     *
     * @param accountIpWhiteList 账号ip白名单
     * @return 结果
     */
    public int updateAccountIpWhiteList(AccountIpWhiteList accountIpWhiteList);

    /**
     * 批量删除账号ip白名单
     *
     * @param ids 需要删除的账号ip白名单主键集合
     * @return 结果
     */
    public int deleteAccountIpWhiteListByIds(Long[] ids);

    /**
     * 删除账号ip白名单信息
     *
     * @param id 账号ip白名单主键
     * @return 结果
     */
    public int deleteAccountIpWhiteListById(Long id);
}
