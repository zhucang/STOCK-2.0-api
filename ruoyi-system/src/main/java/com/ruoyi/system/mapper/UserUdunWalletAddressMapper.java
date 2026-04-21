package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.UserUdunWalletAddress;

/**
 * 优盾加密货币钱包信息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-05
 */
public interface UserUdunWalletAddressMapper 
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
     * 删除优盾加密货币钱包信息
     * 
     * @param id 优盾加密货币钱包信息主键
     * @return 结果
     */
    public int deleteUserUdunWalletAddressById(Long id);

    /**
     * 批量删除优盾加密货币钱包信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserUdunWalletAddressByIds(Long[] ids);
}
