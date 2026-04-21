package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserBank;

import java.util.List;

/**
 * 用户银行卡Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-09
 */
public interface UserBankMapper 
{
    /**
     * 查询用户银行卡
     * 
     * @param id 用户银行卡主键
     * @return 用户银行卡
     */
    public UserBank selectUserBankById(Long id);

    /**
     * 查询用户银行卡列表
     * 
     * @param userBank 用户银行卡
     * @return 用户银行卡集合
     */
    public List<UserBank> selectUserBankList(UserBank userBank);

    /**
     * 新增用户银行卡
     * 
     * @param userBank 用户银行卡
     * @return 结果
     */
    public int insertUserBank(UserBank userBank);

    /**
     * 修改用户银行卡
     * 
     * @param userBank 用户银行卡
     * @return 结果
     */
    public int updateUserBank(UserBank userBank);

    /**
     * 删除用户银行卡
     * 
     * @param id 用户银行卡主键
     * @return 结果
     */
    public int deleteUserBankById(Long id);

    /**
     * 批量删除用户银行卡
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserBankByIds(Long[] ids);

    /**
     * 获取用户银行卡绑定数量
     */
    public int getUserBankCardCountByUserId(Long userId);
}
