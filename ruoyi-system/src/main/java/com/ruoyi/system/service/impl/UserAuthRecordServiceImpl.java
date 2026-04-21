package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.UserAuthRecord;
import com.ruoyi.system.mapper.UserAuthRecordMapper;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserAuthRecordService;
import com.ruoyi.system.service.IUserInfoService;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.utils.telegram.TelegramUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户实名认证信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-05
 */
@Service
public class UserAuthRecordServiceImpl implements IUserAuthRecordService 
{
    @Resource
    private UserAuthRecordMapper userAuthRecordMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 查询用户实名认证信息
     * 
     * @param id 用户实名认证信息主键
     * @return 用户实名认证信息
     */
    @Override
    public UserAuthRecord selectUserAuthRecordById(Long id)
    {
        return userAuthRecordMapper.selectUserAuthRecordById(id);
    }

    /**
     * 获取最近一条用户实名认证记录
     * @param userId 用户id
     * @param authLevel 认证等级
     * @return
     */
    @Override
    public UserAuthRecord selectLastOne(Long userId, Integer authLevel){
        UserAuthRecord lastOne = userAuthRecordMapper.selectLastOne(userId, authLevel);
        if (lastOne == null){
            UserAuthRecord userAuthRecord = new UserAuthRecord();
            userAuthRecord.setAuthLevel(authLevel);
            userAuthRecord.setUserId(userId);
            //未实名认证
            userAuthRecord.setAuthStatus(-1);
            userAuthRecord.setAuthMethod(0);
            lastOne = userAuthRecord;
        }
        return lastOne;
    }

    /**
     * 查询用户实名认证信息列表
     * 
     * @param userAuthRecord 用户实名认证信息
     * @return 用户实名认证信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserAuthRecord> selectUserAuthRecordList(UserAuthRecord userAuthRecord)
    {
        return userAuthRecordMapper.selectUserAuthRecordList(userAuthRecord);
    }

    /**
     * 新增用户实名认证信息
     * 
     * @param userAuthRecord 用户实名认证信息
     * @return 结果
     */
    @Override
    public int insertUserAuthRecord(UserAuthRecord userAuthRecord)
    {
        userAuthRecord.setCreateTime(DateUtils.getNowDate());
        return userAuthRecordMapper.insertUserAuthRecord(userAuthRecord);
    }

    /**
     * 修改用户实名认证信息
     * 
     * @param userAuthRecord 用户实名认证信息
     * @return 结果
     */
    @Override
    public int updateUserAuthRecord(UserAuthRecord userAuthRecord)
    {
        return userAuthRecordMapper.updateUserAuthRecord(userAuthRecord);
    }

    /**
     * 批量删除用户实名认证信息
     * 
     * @param ids 需要删除的用户实名认证信息主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserAuthRecordByIds(Long[] ids)
    {
        //获取即将删除的所有实名认证信息
        UserAuthRecord userAuthRecord = new UserAuthRecord();
        userAuthRecord.getParams().put("ids", Arrays.asList(ids));
        List<UserAuthRecord> userAuthRecords = userAuthRecordMapper.selectUserAuthRecordList(userAuthRecord);
        //日志记录用户实名认证信息
        HttpUtils.getRequestLogParams().put("JSONArray:userAuthRecords", JSONObject.toJSONString(userAuthRecords));
        //批量删除实名认证信息
        int deleteUserAuthRecordByIds = userAuthRecordMapper.deleteUserAuthRecordByIds(ids);
        if (deleteUserAuthRecordByIds <= 0){
            throw new ServiceException("系统繁忙");
        }
        //初级认证的用户ids
        List<Long> userIdsByJuniorAuth = userAuthRecords.stream().filter(a -> a.getAuthLevel().equals(0)).map(UserAuthRecord::getUserId).distinct().collect(Collectors.toList());
        //成功条数
        int successCount = 0;
        //遍历
        for (int i = 0; i < userIdsByJuniorAuth.size(); i++) {
            //用户信息
            UserInfo userInfo = new UserInfo();
            //用户id
            Long userId = userIdsByJuniorAuth.get(i);
            userInfo.setId(userId);
            UserAuthRecord lastOne = userAuthRecordMapper.selectLastOne(userId, 0);
            if (lastOne != null){
                userInfo.setAuthStatusJunior(lastOne.getAuthStatus()+1);
            }else {
                userInfo.setAuthStatusJunior(0);
            }
            int updateUserInfo = userInfoMapper.updateUserInfo(userInfo);
            if (updateUserInfo <= 0){
                throw new ServiceException("系统繁忙");
            }
            successCount++;
        }
        //高级认证的用户ids
        List<Long> userIdsBySeniorAuth = userAuthRecords.stream().filter(a -> a.getAuthLevel().equals(1)).map(UserAuthRecord::getUserId).collect(Collectors.toList());
        for (int i = 0; i < userIdsBySeniorAuth.size(); i++) {
            //用户信息
            UserInfo userInfo = new UserInfo();
            //用户id
            Long userId = userIdsBySeniorAuth.get(i);
            userInfo.setId(userId);
            UserAuthRecord lastOne = userAuthRecordMapper.selectLastOne(userId, 1);
            if (lastOne != null){
                userInfo.setAuthStatusSenior(lastOne.getAuthStatus()+1);
            }else {
                userInfo.setAuthStatusSenior(0);
            }
            int updateUserInfo = userInfoMapper.updateUserInfo(userInfo);
            if (updateUserInfo <= 0){
                throw new ServiceException("系统繁忙");
            }
            successCount++;
        }
        if (successCount != ids.length || successCount != deleteUserAuthRecordByIds){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**`
     * 删除用户实名认证信息信息
     * 
     * @param id 用户实名认证信息主键
     * @return 结果
     */
    @Override
    public int deleteUserAuthRecordById(Long id)
    {
        return userAuthRecordMapper.deleteUserAuthRecordById(id);
    }


    /**
     * 用户实名认证审核
     * @param userAuthRecord 用户实名认证信息信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int userAuthReview(UserAuthRecord userAuthRecord){
        //用户实名认证记录信息
        UserAuthRecord userAuthRecordVo = userAuthRecordMapper.selectUserAuthRecordById(userAuthRecord.getId());
        if (userAuthRecordVo == null){
            throw new ServiceException("获取用户实名认证信息异常");
        }
        if (!userAuthRecordVo.getAuthStatus().equals(0)){
            throw new ServiceException("此申请已审核过，请勿重复操作");
        }
        if (!userAuthRecordVo.getUserId().equals(userAuthRecord.getUserId())){
            throw new ServiceException("核对用户信息错误");
        }
        //用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userAuthRecord.getUserId());
        //通过
        if (userAuthRecord.getAuthStatus().equals(1)){
            //如果是初级认证
            if (userAuthRecord.getAuthLevel().equals(0)){
                userInfo.setAuthStatusJunior(2);
            }else {
                //如果是高级认证
                userInfo.setAuthStatusSenior(2);
            }
        }else if (userAuthRecord.getAuthStatus().equals(2)){
            //驳回
            //如果是初级认证
            if (userAuthRecord.getAuthLevel().equals(0)){
                userInfo.setAuthStatusJunior(3);
            }else {
                //如果是高级认证
                userInfo.setAuthStatusSenior(3);
            }
        }else if (userAuthRecord.getAuthStatus().equals(0)){
            //驳回
            //如果是初级认证
            if (userAuthRecord.getAuthLevel().equals(0)){
                userInfo.setAuthStatusJunior(1);
            }else {
                //如果是高级认证
                userInfo.setAuthStatusSenior(1);
            }
        }else {
            throw new ServiceException("实名状态错误");
        }
        //更新用户信息
        userInfo.setRealName(userAuthRecord.getRealName());
        int updateUserInfo = userInfoMapper.updateUserInfo(userInfo);
        if (updateUserInfo <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //更新用户实名认证记录
        userAuthRecord.setApproveTime(new Date());
        userAuthRecord.setApproveName(SecurityUtils.getUsername());
        int updateUserAuthRecord = userAuthRecordMapper.updateUserAuthRecord(userAuthRecord);
        if (updateUserAuthRecord <= 0) {
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 用户实名认证申请
     * @param userAuthRecord 用户实名认证信息信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int userAuthApply(UserAuthRecord userAuthRecord){
        //用户信息
        UserInfo userInfo = userInfoService.selectUserInfoById(userAuthRecord.getUserId());
        //实名认证审核中不可修改实名信息开关
        Integer selectSwitchStatusById59 = switchSetService.selectSwitchStatusById(59L);
        //实名认证通过后不可修改实名信息开关
        Integer selectSwitchStatusById19 = switchSetService.selectSwitchStatusById(19L);
        //如果是初级认证
        if (userAuthRecord.getAuthLevel().equals(0)){
            if (selectSwitchStatusById59.equals(0) && userInfo.getAuthStatusJunior().equals(1)){
                throw new LangException(HintConstants.SYSTEM_BUSY,"实名认证审核中不可修改实名信息");
            }
            if (selectSwitchStatusById19.equals(0) && userInfo.getAuthStatusJunior().equals(2)){
                throw new LangException(HintConstants.SYSTEM_BUSY,"实名认证通过后不可修改实名信息");
            }
        }else {
            //如果是高级认证
            //是否开启初级实名认证
            Integer selectSwitchStatusById68 = switchSetService.selectSwitchStatusById(68L);
            //如果初级认证开启
            if (selectSwitchStatusById68.equals(0)){
                //还未通过初级认证
                if (!userInfo.getAuthStatusJunior().equals(2)){
                    throw new LangException(HintConstants.SYSTEM_BUSY,"请先进行初级认证");
                }
                //是否要求高级实名认证名字与初级实名认证名字一致
                Integer selectSwitchStatusById108 = switchSetService.selectSwitchStatusById(108L);
                if (selectSwitchStatusById108.equals(0)){
                    //验证填写的真实姓名是否与初级认证所填写的真实姓名相同
                    if (StringUtils.isNotEmpty(userInfo.getRealName())){
                        if (!userInfo.getRealName().equals(userAuthRecord.getRealName())){
                            throw new LangException("hint_71","填写的真实姓名与初级实名认证真实姓名不符");
                        }
                    }
                }
            }
            if (selectSwitchStatusById59.equals(0) && userInfo.getAuthStatusSenior().equals(1)){
                throw new LangException(HintConstants.SYSTEM_BUSY,"实名认证审核中不可修改实名信息");
            }
            if (selectSwitchStatusById19.equals(0) && userInfo.getAuthStatusSenior().equals(2)){
                throw new LangException(HintConstants.SYSTEM_BUSY,"实名认证通过后不可修改实名信息");
            }
        }
        //如果是身份证认证
        if (userAuthRecord.getAuthMethod().equals(0) && StringUtils.isNotEmpty(userAuthRecord.getIdNumber())){
            //身份证位数限制开关
            Integer idCardNumberDigitLimit = switchSetService.selectSwitchStatusById(17L);
            if (idCardNumberDigitLimit.equals(0)){
                //身份证位数
                Integer digit = CacheUtils.getOtherValueByKey("idCardNumber.digit",Integer.class);
                if (digit != null && userAuthRecord.getIdNumber().length() != digit){
                    throw new LangException("hint_correctIDNumberError","请输入正确的身份证号码");
                }
            }
        }
        //账号类型： 0：真实用户 1：游客
        Integer accountType = userInfo.getAccountType();
        //实名认证自动通过审核开关
        Integer userAuthAutoApproveSwitch = 1;
        //如果是用户
        if (accountType.equals(0)){
            //用户实名认证自动通过审核开关
            userAuthAutoApproveSwitch = switchSetService.selectSwitchStatusById(57L);
        }else if (accountType.equals(1)){
            //如果是游客
            //游客实名认证自动通过审核开关
            userAuthAutoApproveSwitch = switchSetService.selectSwitchStatusById(65L);
        }else {
            throw new LangException(HintConstants.SYSTEM_ERR,"用户账号类型异常");
        }
        //自动审核开关开启
        if (userAuthAutoApproveSwitch.equals(0)){
            //初级认证
            if (userAuthRecord.getAuthLevel().equals(0)){
                userInfo.setAuthStatusJunior(2);
            }else {
                //高级认证
                userInfo.setAuthStatusSenior(2);
            }
            userAuthRecord.setAuthStatus(1);
            userAuthRecord.setApproveTime(new Date());
            userAuthRecord.setApproveName("自动审核");
        }else {
            //初级认证
            if (userAuthRecord.getAuthLevel().equals(0)){
                userInfo.setAuthStatusJunior(1);
            }else {
                //高级认证
                userInfo.setAuthStatusSenior(1);
            }
        }
       //更新用户信息
        userInfo.setRealName(userAuthRecord.getRealName());
        int updateUserInfo = userInfoMapper.updateUserInfo(userInfo);
        if (updateUserInfo <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //新增用户实名认证审核记录
        userAuthRecord.setCreateTime(new Date());
        int insertUserAuthRecord = userAuthRecordMapper.insertUserAuthRecord(userAuthRecord);
        if (insertUserAuthRecord <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //如果不是真实用户
        if (!userInfo.getAccountType().equals(0)) {
            //如果是模拟账号，不推消息
            if (userInfo.getAccountType().equals(2)) {
                return 1;
            }
            //telegram通知是否只推送真实用户通知
            Integer switchStatusById123 = switchSetService.selectSwitchStatusById(123L);
            if (switchStatusById123.equals(0)){
                return 1;
            }
        }

        //telegram通知（实名认证）
        Integer switchStatusById133 = switchSetService.selectSwitchStatusById(133L);
        if (switchStatusById133.equals(0)) {
            //telegram消息
            String telegramMsg = "\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89实名认证\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\n" +
                    "⏰时间：" + userAuthRecord.getCreateTime() + "\n" +
                    "ID: " + userInfo.getUserNo() + "\n" +
                    "用户账号: " + userInfo.getUserAccount() + "\n" +
                    "用户昵称: " + userInfo.getNickName() + "\n" +
                    "邀请码: " + userInfo.getInviteCode() + "\n" +
                    "所属代理: " + userInfo.getAgentId() + "/" + userInfo.getAgentName() + "\n" +
                    "代理昵称: " + userInfo.getAgentNickName() + "\n" +
                    "认证等级: " + (userAuthRecord.getAuthLevel().equals(0)?"初级":"高级") + "\n" +
                    "已提交实名认证,请及时处理！";
            TelegramUtils.sendAsyncMessage(telegramMsg, "default", "default");
        }
        return 1;
    }
}
