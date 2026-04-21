package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.logDict.UserInfoLogDict;
import com.ruoyi.common.service.TokenService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户Controller
 * 
 * @author ruoyi
 * @date 2023-10-28
 *  日志待优化
 */
@RestController
@RequestMapping("/api/userInfo")
public class ApiUserInfoController extends BaseController
{
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 用户登陆
     */
    @PostMapping(value = "login")
    @RepeatSubmit
    @Log(title = "用户登陆",relateAppUserId = "id",businessType = BusinessType.OTHER,dict = UserInfoLogDict.class,
            saveParamNames = {"userAccount","userPwd"})
    public AjaxResult login(String userAccount,String userPwd) {
        if (StringUtils.isEmpty(userAccount) || StringUtils.isEmpty(userPwd)) {
            return AjaxResult.error(HintConstants.PARAM_NULL,"账号和密码不能为空");
        }
        UserInfo userInfo = new UserInfo();
        //返回结果
        AjaxResult ajaxResult = userInfoService.userLogin(userAccount,userPwd);
        if (ajaxResult.isSuccess()){
            userInfo = (UserInfo) ajaxResult.get("userInfo");
            //日志记录用户id
            HttpUtils.getRequestLogParams().put("id",userInfo.getId());
            //日志记录用户账号
            HttpUtils.getRequestLogParams().put("userAccount",userInfo.getUserAccount());
        }else {
            userInfo.setUserAccount(userAccount);
        }
        //记录登陆日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(userInfo.getId(),userInfo.getUserAccount(),Constants.APP_LOGIN, ajaxResult.isSuccess() ? Constants.LOGIN_SUCCESS:Constants.LOGIN_FAIL, String.valueOf(ajaxResult.get(AjaxResult.MSG_TAG))));
        return ajaxResult;
    }

    /**
     * 退出登录
     */
    @PostMapping(value = "loginOut")
    @RepeatSubmit
    public AjaxResult loginOut() {
        //token标识
        String tokenFlag = "app";
        //此用户当前登录的token
        String tokenCurrent = redisCache.getCacheMapValue(tokenFlag, String.valueOf(SecurityUtils.getUserId()));
        //删除登录信息
        tokenService.delLoginUser(tokenCurrent);
        return AjaxResult.success();
    }

    /**
     * 用户注册
     */
    @PostMapping(value = "reg")
    @RepeatSubmit
    @Log(title = "用户注册",relateAppUserId = "id", businessType = BusinessType.OTHER,dict = UserInfoLogDict.class,
            saveParamNames = {"userAccount","userPwd","注册使用的邀请码","regType"})
    public AjaxResult reg(String userAccount,String userPwd,String inviteCode,String yzmCode,Integer regType,String uuid,String imgValidateCode) {
        //日志记录用户注册使用的邀请码
        HttpUtils.getRequestLogParams().put("注册使用的邀请码",inviteCode);
        if (StringUtils.isEmpty(userAccount)){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请输入用户账号");
        }else {
            //去除头尾空格
            userAccount = userAccount.trim();
        }
        if (StringUtils.isNotEmpty(inviteCode)) {
            //去除头尾空格
            inviteCode = inviteCode.trim();
        }
        if (StringUtils.isNotEmpty(userPwd) && userPwd.length() < 6){
            List<Object> params = new ArrayList<>();
            params.add(6);
            throw new LangException("hint_88",params,"请输入至少" + 6 + "位数密码");
        }
        if (regType == null || (!regType.equals(0) && !regType.equals(1))){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择注册类型");
        }
        return userInfoService.reg(userAccount,userPwd,inviteCode,yzmCode,regType,uuid,imgValidateCode);
    }

    /**
     * 获取国际区号列表
     */
    @GetMapping(value = "getPhoneAreaCodeList")
    public TableDataInfo getPhoneAreaCodeList(){
        return getDataTable(userInfoService.getPhoneAreaCodeList());
    }

    /**
     * 发送手机验证码
     */
    @RepeatSubmit
    @PostMapping(value = "sendPhoneVerificationCode")
    @Log(title = "发送手机验证码", businessType = BusinessType.OTHER)
    public AjaxResult sendPhoneVerificationCode(String phone) {
        if (StringUtils.isEmpty(phone)){
            throw new LangException(HintConstants.PARAM_NULL,"请输入手机号");
        }else {
            //去除头尾空格
            phone = phone.trim();
        }
        //验证手机号格式
        String[] split = phone.split("-");
        if (split.length != 2 || !StringUtils.isNumeric(split[0]) || !StringUtils.isNumeric(split[1])){
            throw new LangException("hint_41","请输入正确的手机号码");
        }
        return toAjax(userInfoService.sendPhoneVerificationCode(phone));
    }

    /**
     * 发送邮箱验证码
     */
    @RepeatSubmit
    @PostMapping(value = "sendEmailVerificationCode")
    @Log(title = "发送邮箱验证码", businessType = BusinessType.OTHER)
    public AjaxResult sendEmailVerificationCode(String email) {
        if (StringUtils.isEmpty(email)){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请输入邮箱");
        }else {
            //去除头尾空格
            email = email.trim();
        }
        return toAjax(userInfoService.sendEmailVerificationCode(email));
    }

    /**
     * 获取用户信息
     */
    @GetMapping(value = "getUserInfo")
    public AjaxResult getUserInfo(){
        return AjaxResult.success(userInfoService.selectUserInfoById(SecurityUtils.getUserId()));
    }

    /**
     * 用户修改头像
     */
    @PostMapping(value = "/modifyAvatar")
    @RepeatSubmit
    @Log(title = "用户修改头像", businessType = BusinessType.OTHER)
    public AjaxResult modifyAvatar(String avatar) {
        if (StringUtils.isEmpty(avatar)){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请上传用户头像");
        }
        return toAjax(userInfoService.modifyAvatar(avatar));
    }

    /**
     * 修改用户信息
     */
    @PostMapping(value = "modifyUserInfo")
    @RepeatSubmit
    @Log(title = "修改用户信息", businessType = BusinessType.OTHER,dict = UserInfoLogDict.class,
            saveParamNames = {"nickName","userAccount"})
    public AjaxResult modifyUserInfo(String nickName,String userAccount) {
        if (StringUtils.isEmpty(nickName)){
            throw new LangException(HintConstants.PARAM_NULL,"请输入用户昵称");
        }
        if (StringUtils.isEmpty(userAccount)){
            throw new LangException(HintConstants.PARAM_NULL,"请输入用户账号");
        }else {
            //去除头尾空格
            userAccount = userAccount.trim();
        }
        return toAjax(userInfoService.modifyUserInfo(nickName,userAccount));
    }

    /**
     * 获取个人中心面板数据
     */
    @GetMapping(value = "personCenterPanelData")
    public AjaxResult personCenterPanelData(){
        return userInfoService.personCenterPanelData(null);
    }

    /**
     * 用户钱包余额(不取折合)
     */
    @GetMapping(value = "userBalance")
    public AjaxResult userBalance(Long currencyId) {
        return userInfoService.userBalance(currencyId);
    }

    /**
     * 用户钱包余额折合信息
     */
    @GetMapping(value = "userBalanceConvert")
    public AjaxResult userBalanceConvert(Integer isPlatformCurrency) {
        return userInfoService.userBalanceConvert(isPlatformCurrency);
    }

//    /**
//     * 用户实名认证申请
//     */
//    @PostMapping(value = "userAuth")
//    @RepeatSubmit
//    @Log(title = "用户实名认证申请", businessType = BusinessType.OTHER)
//    public AjaxResult userAuth(@RequestBody UserInfo userInfo) {
//        //认证方式
//        Integer authMethod = userInfo.getAuthMethod();
//        if (authMethod == null){
//            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择认证方式");
//        }
//        if (authMethod.equals(0)){
//            if (StringUtils.isEmpty(userInfo.getIdCard())){
//                return AjaxResult.error(HintConstants.PARAM_NULL,"请输入身份证号码");
//            }
//        }
//        if (StringUtils.isEmpty(userInfo.getRealName())){
//            return AjaxResult.error(HintConstants.PARAM_NULL,"请输入真实姓名");
//        }
//        if (StringUtils.isEmpty(userInfo.getImg1Key())){
//            return AjaxResult.error(HintConstants.PARAM_NULL,"请上传证件正面图片");
//        }
//        if (StringUtils.isEmpty(userInfo.getImg2Key())){
//            return AjaxResult.error(HintConstants.PARAM_NULL,"请上传证件反面图片");
//        }
////        if (StringUtils.isEmpty(userInfo.getImg3Key())){
////            return AjaxResult.error(HintConstants.PARAM_NULL,"请上传手持身份证图片");
////        }
//        return toAjax(userInfoService.userAuth(userInfo));
//    }

    /**
     * 修改用户登录密码
     */

    @PostMapping(value = "updateUserPwd")
    @RepeatSubmit
    @Log(title = "用户修改登录密码", businessType = BusinessType.OTHER)
    public AjaxResult updateUserPwd(String oldPwd, String newPwd) {
        if (StringUtils.isEmpty(oldPwd)){
            throw new LangException(HintConstants.PARAM_NULL,"请输入原密码");
        }
        if (StringUtils.isEmpty(newPwd)){
            throw new LangException(HintConstants.PARAM_NULL,"请输入新密码");
        }
        if (StringUtils.isNotEmpty(newPwd) && newPwd.length() < 6){
            List<Object> params = new ArrayList<>();
            params.add(6);
            throw new LangException("hint_88",params,"请输入至少" + 6 + "位数密码");
        }
        return userInfoService.updateUserPwd(oldPwd, newPwd);
    }

    /**
     * 用户是否已经设置提现密码
     */
    @GetMapping(value = "isSetWithPwd")
    public AjaxResult isSetWithPwd(){
        String userWithPwd = userInfoService.selectUserWithPwdByUserId(SecurityUtils.getUserId());
        if (StringUtils.isEmpty(userWithPwd)){
            return AjaxResult.success().put("isSetWithPwd",0);
        }
        return AjaxResult.success().put("isSetWithPwd",1);
    }

    /**
     * 修改用户提现密码
     */

    @PostMapping(value = "updateUserWithPwd")
    @RepeatSubmit
    @Log(title = "用户修改提现密码", businessType = BusinessType.OTHER)
    public AjaxResult updateUserWithPwd(String oldPwd, String newPwd) {
        if (StringUtils.isEmpty(newPwd)){
            throw new LangException(HintConstants.PARAM_NULL,"请输入新密码");
        }
        if (StringUtils.isNotEmpty(newPwd) && newPwd.length() < 6){
            List<Object> params = new ArrayList<>();
            params.add(6);
            throw new LangException("hint_88",params,"请输入至少" + 6 + "位数密码");
        }
        return userInfoService.updateUserWithPwd(oldPwd, newPwd);
    }

    /**
     * 进入/退出模拟交易模式
     */

    @GetMapping(value = "changeLoginModel")
    @RepeatSubmit
    @Log(title = "进入/退出模拟交易模式", businessType = BusinessType.OTHER)
    public AjaxResult changeLoginModel() {
        return AjaxResult.success(userInfoService.changeLoginModel());
    }

    /**
     * 绑定邮箱
     */

    @PostMapping(value = "bindEmail")
    @RepeatSubmit
    @Log(title = "绑定邮箱", businessType = BusinessType.OTHER)
    public AjaxResult bindEmail(String email, String yzmCode) {
        return AjaxResult.success(userInfoService.bindEmail(email, yzmCode));
    }
}
