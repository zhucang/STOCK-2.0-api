package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.UserProductPosition;

/**
 * 用户合约交易订单Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-25
 */
public interface UserProductPositionMapper 
{
    /**
     * 查询用户合约交易订单
     * 
     * @param id 用户合约交易订单主键
     * @return 用户合约交易订单
     */
    public UserProductPosition selectUserProductPositionById(Long id);

    /**
     * 查询用户合约交易订单列表
     * 
     * @param userProductPosition 用户合约交易订单
     * @return 用户合约交易订单集合
     */
    public List<UserProductPosition> selectUserProductPositionList(UserProductPosition userProductPosition);

    /**
     * 新增用户合约交易订单
     * 
     * @param userProductPosition 用户合约交易订单
     * @return 结果
     */
    public int insertUserProductPosition(UserProductPosition userProductPosition);

    /**
     * 修改用户合约交易订单
     * 
     * @param userProductPosition 用户合约交易订单
     * @return 结果
     */
    public int updateUserProductPosition(UserProductPosition userProductPosition);

    /**
     * 删除用户合约交易订单
     * 
     * @param id 用户合约交易订单主键
     * @return 结果
     */
    public int deleteUserProductPositionById(Long id);

    /**
     * 批量删除用户合约交易订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserProductPositionByIds(Long[] ids);
}
