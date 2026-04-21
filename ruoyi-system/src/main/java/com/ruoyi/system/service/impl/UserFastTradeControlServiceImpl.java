package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.UserFastTradeControl;
import com.ruoyi.system.mapper.UserFastTradeControlMapper;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.service.IUserFastTradeControlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 极速交易用户单控参数Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-20
 */
@Service
public class UserFastTradeControlServiceImpl implements IUserFastTradeControlService 
{
    @Resource
    private UserFastTradeControlMapper userFastTradeControlMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 查询极速交易用户单控参数
     * 
     * @param id 极速交易用户单控参数主键
     * @return 极速交易用户单控参数
     */
    @Override
    public UserFastTradeControl selectUserFastTradeControlById(Long id)
    {
        return userFastTradeControlMapper.selectUserFastTradeControlById(id);
    }

    /**
     * 查询极速交易用户单控参数列表
     * 
     * @param userFastTradeControl 极速交易用户单控参数
     * @return 极速交易用户单控参数
     */
    @Override
    public List<UserFastTradeControl> selectUserFastTradeControlList(UserFastTradeControl userFastTradeControl)
    {
        return userFastTradeControlMapper.selectUserFastTradeControlList(userFastTradeControl);
    }

    /**
     * 新增极速交易用户单控参数
     * 
     * @param userFastTradeControl 极速交易用户单控参数
     * @return 结果
     */
    @Override
    public int insertUserFastTradeControl(UserFastTradeControl userFastTradeControl)
    {
        return userFastTradeControlMapper.insertUserFastTradeControl(userFastTradeControl);
    }

    /**
     * 修改极速交易用户单控参数
     * 
     * @param userFastTradeControl 极速交易用户单控参数
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserFastTradeControl(UserFastTradeControl userFastTradeControl)
    {
        //控制的用户类型
        Object accountType = userFastTradeControl.getParams().get("accountType");
        if (accountType == null){
            //默认控制当前账号
            accountType = 1;
        }

        //需要操作的用户IDS
        List<Long> userIdsArr = new ArrayList<>();
        //单用户操作
        if (userFastTradeControl.getUserId() != null){
            userIdsArr.add(userFastTradeControl.getUserId());
        }else {
            //批量操作
            //用户IDS
            Object userIds = userFastTradeControl.getParams().get("userIds");
            if (userIds != null){
                userIdsArr = (List<Long>) userIds;
            }
            //如果未选择用户，则全部更新
            if (userIdsArr.size() == 0){
                throw new ServiceException("请选择需要操作的用户");
            }
        }

        //如果是控制对应的关联账号
        if (accountType.toString().equals("2")){
            UserInfo userInfo = new UserInfo();
            userInfo.getParams().put("userIds", userIdsArr);
            List<UserInfo> userInfos = userInfoMapper.selectUserInfoList(userInfo);
            //只允许通过此来控制模拟账号
            List<Long> relateUserIds = userInfos.stream().filter(a -> a.getAccountType().equals(0) || a.getAccountType().equals(1)).map(a -> a.getRelateUserId()).filter(a -> a != null).collect(Collectors.toList());
            if (relateUserIds.size() == 0){
                throw new ServiceException("暂未获取到相应的模拟账户");
            }
            userIdsArr = relateUserIds;
        }

        //极速交易用户单控参数
        UserFastTradeControl search = new UserFastTradeControl();
        search.getParams().put("userIds", userIdsArr);
        List<UserFastTradeControl> userFastTradeControls = userFastTradeControlMapper.selectUserFastTradeControlList(search);
        //极速交易用户单控参数map
        Map<Long, UserFastTradeControl> map = userFastTradeControls.stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
        //需要更新的对象
        UserFastTradeControl vo = new UserFastTradeControl();
        //遍历
        for (int i = 0; i < userIdsArr.size(); i++) {
            //用户ID
            Long userId = Long.valueOf(String.valueOf(userIdsArr.get(i)));
            //用户原本的控制参数
            UserFastTradeControl userFastTradeControlVo = map.get(userId);
            //
            BeanUtils.copyProperties(userFastTradeControl,vo);
            vo.setUserId(userId);
            //如果还没有单控数据
            if (userFastTradeControlVo == null){
                int insertUserFastTradeControl = userFastTradeControlMapper.insertUserFastTradeControl(vo);
                if (insertUserFastTradeControl <= 0){
                    throw new ServiceException("系统繁忙");
                }
            }else {
                vo.setId(userFastTradeControlVo.getId());
                int updateUserFastTradeControl = userFastTradeControlMapper.updateUserFastTradeControl(vo);
                if (updateUserFastTradeControl <= 0){
                    throw new ServiceException("系统繁忙");
                }
            }
        }
        return 1;
    }

    /**
     * 批量删除极速交易用户单控参数
     * 
     * @param ids 需要删除的极速交易用户单控参数主键
     * @return 结果
     */
    @Override
    public int deleteUserFastTradeControlByIds(Long[] ids)
    {
        return userFastTradeControlMapper.deleteUserFastTradeControlByIds(ids);
    }

    /**
     * 删除极速交易用户单控参数信息
     * 
     * @param id 极速交易用户单控参数主键
     * @return 结果
     */
    @Override
    public int deleteUserFastTradeControlById(Long id)
    {
        return userFastTradeControlMapper.deleteUserFastTradeControlById(id);
    }
}
