package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.mapper.SysOperLogMapper;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 操作日志 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        List<SysOperLog> sysOperLogs = operLogMapper.selectOperLogList(operLog);
        fillUserInfo(sysOperLogs);
        return sysOperLogs;
    }

    void fillUserInfo(List<SysOperLog> sysOperLogs){
        List<Long> userIds = sysOperLogs.stream().map(SysOperLog::getRelateAppUserId).distinct().collect(Collectors.toList());
        if (userIds.size() <= 0){
            return;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.getParams().put("userIds",userIds);
        Map<String, UserInfo> map = userInfoMapper.selectUserInfoList(userInfo).stream().collect(Collectors.toMap(a -> String.valueOf(a.getId()), a -> a));
        for (int i = 0; i < sysOperLogs.size(); i++) {
            //日志信息
            SysOperLog sysOperLog = sysOperLogs.get(i);
            //涉及的用户id
            Long relateAppUserId = sysOperLog.getRelateAppUserId();
            if (relateAppUserId != null){
                //用户信息
                UserInfo userInfoVo = map.get(String.valueOf(relateAppUserId));
                if (userInfoVo != null){
                    sysOperLog.setRelateAppUserInfo(userInfoVo);
                }
            }
        }
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}
