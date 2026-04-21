package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserProductOption;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户产品自选关联信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public interface UserProductOptionMapper 
{
    /**
     * 查询用户产品自选关联信息
     * 
     * @param id 用户产品自选关联信息主键
     * @return 用户产品自选关联信息
     */
    public UserProductOption selectUserProductOptionById(Long id);

    /**
     * 查询用户产品自选关联信息列表
     * 
     * @param userProductOption 用户产品自选关联信息
     * @return 用户产品自选关联信息集合
     */
    public List<UserProductOption> selectUserProductOptionList(UserProductOption userProductOption);

    /**
     * 新增用户产品自选关联信息
     * 
     * @param userProductOption 用户产品自选关联信息
     * @return 结果
     */
    public int insertUserProductOption(UserProductOption userProductOption);

    /**
     * 修改用户产品自选关联信息
     * 
     * @param userProductOption 用户产品自选关联信息
     * @return 结果
     */
    public int updateUserProductOption(UserProductOption userProductOption);

    /**
     * 删除用户产品自选关联信息
     * 
     * @param id 用户产品自选关联信息主键
     * @return 结果
     */
    public int deleteUserProductOptionById(Long id);

    /**
     * 批量删除用户产品自选关联信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserProductOptionByIds(Long[] ids);

    /**
     * 获取用户自选信息
     * @param userId 用户id
     * @param productCode 产品代码
     * @param productType 产品类型（1：美股 2：加密货币 3：期货 4：外汇）
     * @return
     */
    public UserProductOption getUserOptionInfo(@Param("userId")Long userId,
                                                @Param("productCode")String productCode,
                                                @Param("productType")Integer productType);

    /**
     * 获取productCodes数组中用户已添加自选的内容
     * @param userId 用户id
     * @param productCodes 产品代码
     * @param productType 产品类型（1：美股 2：加密货币 3：期货 4：外汇）
     * @return
     */
    public List<String> getUserOptionInProducts(@Param("userId")Long userId,
                                                @Param("productCodes")List<String> productCodes,
                                                @Param("productType")Integer productType);
}
