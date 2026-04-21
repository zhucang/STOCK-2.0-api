package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserBank;

import java.util.List;

/**
 * 用户银行卡Service接口
 * 
 * @author ruoyi
 * @date 2023-11-09
 */
public interface IUserBankService 
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
     * 批量删除用户银行卡
     * 
     * @param ids 需要删除的用户银行卡主键集合
     * @return 结果
     */
    public int deleteUserBankByIds(Long[] ids);

    /**
     * 删除用户银行卡信息
     * 
     * @param id 用户银行卡主键
     * @return 结果
     */
    public int deleteUserBankById(Long id);

    /**
     * 用户添加银行卡
     * @param userBank
     * @return
     */
    public int addUserBankCard(UserBank userBank);


    /**
     * 用户修改银行卡
     *
     * @param userBank 用户银行卡
     * @return 结果
     */
    public int updateUserBankCard(UserBank userBank);
}
