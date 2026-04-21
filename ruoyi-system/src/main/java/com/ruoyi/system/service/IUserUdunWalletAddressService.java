package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserUdunWalletAddress;

import java.util.List;

/**
 * 优盾加密货币钱包信息Service接口
 * 
 * @author ruoyi
 * @date 2024-09-05
 */
public interface IUserUdunWalletAddressService 
{
    /**
     * 查询优盾加密货币钱包信息
     * 
     * @param id 优盾加密货币钱包信息主键
     * @return 优盾加密货币钱包信息
     */
    public UserUdunWalletAddress selectUserUdunWalletAddressById(Long id);

    /**
     * 查询优盾加密货币钱包信息列表
     * 
     * @param userUdunWalletAddress 优盾加密货币钱包信息
     * @return 优盾加密货币钱包信息集合
     */
    public List<UserUdunWalletAddress> selectUserUdunWalletAddressList(UserUdunWalletAddress userUdunWalletAddress);

    /**
     * 新增优盾加密货币钱包信息
     *
     * @param userUdunWalletAddress 优盾加密货币钱包信息
     * @return 结果
     */
    public int insertUserUdunWalletAddress(UserUdunWalletAddress userUdunWalletAddress);

    /**
     * 修改优盾加密货币钱包信息
     * 
     * @param userUdunWalletAddress 优盾加密货币钱包信息
     * @return 结果
     */
    public int updateUserUdunWalletAddress(UserUdunWalletAddress userUdunWalletAddress);

    /**
     * 批量删除优盾加密货币钱包信息
     * 
     * @param ids 需要删除的优盾加密货币钱包信息主键集合
     * @return 结果
     */
    public int deleteUserUdunWalletAddressByIds(Long[] ids);

    /**
     * 删除优盾加密货币钱包信息信息
     * 
     * @param id 优盾加密货币钱包信息主键
     * @return 结果
     */
    public int deleteUserUdunWalletAddressById(Long id);



    //---------------

    /**
     * 用户添加优盾钱包
     *
     * @param userUdunWalletAddress 优盾加密货币钱包信息
     * @return 结果
     */
    public int addUserUdunWalletAddress(UserUdunWalletAddress userUdunWalletAddress);
}
