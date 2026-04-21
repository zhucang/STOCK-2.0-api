package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserWalletAddress;

import java.util.List;

/**
 * 用户虚拟货币钱包地址Service接口
 * 
 * @author ruoyi
 * @date 2023-10-29
 */
public interface IUserWalletAddressService 
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
     * 批量删除用户虚拟货币钱包地址
     * 
     * @param ids 需要删除的用户虚拟货币钱包地址主键集合
     * @return 结果
     */
    public int deleteUserWalletAddressByIds(Long[] ids);

    /**
     * 删除用户虚拟货币钱包地址信息
     * 
     * @param id 用户虚拟货币钱包地址主键
     * @return 结果
     */
    public int deleteUserWalletAddressById(Long id);

    /**
     * 用户添加虚拟货币钱包地址
     *
     * @param userWalletAddress 用户虚拟货币钱包地址
     * @return 结果
     */
    public int userAddWalletAddress(UserWalletAddress userWalletAddress);

    /**
     * 用户编辑虚拟货币钱包地址
     *
     * @param userWalletAddress 用户虚拟货币钱包地址
     * @return 结果
     */
    public int userModifyWalletAddress(UserWalletAddress userWalletAddress);
}
