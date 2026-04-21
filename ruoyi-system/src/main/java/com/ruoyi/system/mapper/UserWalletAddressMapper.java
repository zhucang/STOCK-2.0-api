package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserWalletAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户虚拟货币钱包地址Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-29
 */
public interface UserWalletAddressMapper 
{
    /**
     * 查询用户虚拟货币钱包地址
     * 
     * @param id 用户虚拟货币钱包地址主键
     * @return 用户虚拟货币钱包地址
     */
    public UserWalletAddress selectUserWalletAddressById(Long id);

    /**
     * 查询用户虚拟货币钱包地址列表
     * 
     * @param userWalletAddress 用户虚拟货币钱包地址
     * @return 用户虚拟货币钱包地址集合
     */
    public List<UserWalletAddress> selectUserWalletAddressList(UserWalletAddress userWalletAddress);

    /**
     * 新增用户虚拟货币钱包地址
     * 
     * @param userWalletAddress 用户虚拟货币钱包地址
     * @return 结果
     */
    public int insertUserWalletAddress(UserWalletAddress userWalletAddress);

    /**
     * 修改用户虚拟货币钱包地址
     * 
     * @param userWalletAddress 用户虚拟货币钱包地址
     * @return 结果
     */
    public int updateUserWalletAddress(UserWalletAddress userWalletAddress);

    /**
     * 删除用户虚拟货币钱包地址
     * 
     * @param id 用户虚拟货币钱包地址主键
     * @return 结果
     */
    public int deleteUserWalletAddressById(Long id);

    /**
     * 批量删除用户虚拟货币钱包地址
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserWalletAddressByIds(Long[] ids);

    /**
     * 获取用户绑定钱包个数
     * @param userId 用户id
     * @param currencyId 币种id
     * @param walletAddressType 钱包地址类型
     * @return
     */
    public int getUserBindWalletCount(@Param("userId") Long userId,
                                      @Param("currencyId") Long currencyId,
                                      @Param("walletAddressType") String walletAddressType);
}
