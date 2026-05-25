package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.AccountIpWhiteList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账号ip白名单Mapper接口
 *
 * @author ruoyi
 * @date 2026-05-25
 */
public interface AccountIpWhiteListMapper
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
     * 按账号和ip查询白名单
     *
     * @param accountType 账号类型
     * @param accountId 账号ID
     * @param ipAddress ip地址
     * @return 账号ip白名单
     */
    public AccountIpWhiteList selectAccountIpWhiteListByAccountAndIp(@Param("accountType") Integer accountType,
                                                                     @Param("accountId") Long accountId,
                                                                     @Param("ipAddress") String ipAddress);

    /**
     * 查询命中的账号ip白名单
     *
     * @param accountType 账号类型
     * @param accountId 账号ID
     * @param ipAddress ip地址
     * @return 账号ip白名单
     */
    public AccountIpWhiteList selectMatchedAccountIpWhiteList(@Param("accountType") Integer accountType,
                                                              @Param("accountId") Long accountId,
                                                              @Param("ipAddress") String ipAddress);

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
     * 删除账号ip白名单
     *
     * @param id 账号ip白名单主键
     * @return 结果
     */
    public int deleteAccountIpWhiteListById(Long id);

    /**
     * 批量删除账号ip白名单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAccountIpWhiteListByIds(Long[] ids);
}
