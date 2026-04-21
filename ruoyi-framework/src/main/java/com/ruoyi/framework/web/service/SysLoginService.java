package com.ruoyi.framework.web.service;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.googleAuth.GoogleAuthenticatorUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.system.domain.IpBlackList;
import com.ruoyi.system.service.IIpBlackListService;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private SystemTokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IIpBlackListService ipBlackListService;

    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid,String googleValidateCode)
    {
        //登录ip
        String ip = IpUtils.getIpAddr(HttpUtils.getHttpServletRequest());
        //获取黑名单是否有此ip
        IpBlackList ipBlackList = ipBlackListService.selectIpBlackListByIp(ip);
        if (ipBlackList != null){
            throw new ServiceException("此ip禁止登录");
        }
        //ip所在地
        String addressInfo = null;
        try {
            addressInfo = IpUtils.getAddressByIp(ip);
        } catch (Exception e) {

        }
        //如果没有解析出地址，不允许登陆
        if (StringUtils.isEmpty(addressInfo)){
//            throw new ServiceException("登陆异常，未解析出登录地址");
        }
        //中国地区登录限制开关
        Integer restrictAreaChina = switchSetService.selectSwitchStatusById(32L);
        if ((restrictAreaChina != null && restrictAreaChina == 0)){
            String[] privinces = {"北京", "天津", "河北省", "山西", "内蒙", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆"};
            for (String privince : privinces) {
                if (addressInfo.contains(privince)) {
                    throw new ServiceException("该地区被限制登录");
                }
            }
        }
        //香港澳门地区登录限制开关
        Integer restrictAreaGat = switchSetService.selectSwitchStatusById(33L);
        if ((restrictAreaGat != null && restrictAreaGat == 0)){
            String[] privinces = {"香港", "澳门","台湾"};
            for (String privince : privinces) {
                if (addressInfo.contains(privince)) {
                    throw new ServiceException("该地区被限制登录");
                }
            }
        }
        //国外地区登录限制开关
        Integer restrictAreaOverseas = switchSetService.selectSwitchStatusById(106L);
        if (restrictAreaOverseas.equals(0)){
            String[] privinces = {"北京", "天津", "河北省", "山西", "内蒙", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆","香港", "澳门","台湾"};
            Boolean restrict = true;
            for (String privince : privinces) {
                if (addressInfo.contains(privince)) {
                    restrict = false;
                    break;
                }
            }
            if (restrict){
                throw new ServiceException("该地区被限制登录");
            }
        }

        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(null,username,Constants.WEB_LOGIN, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(null,username,Constants.WEB_LOGIN, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }

        //登录用户信息
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //谷歌验证器密钥
        String googleAuthSecurityKey = userService.getGoogleAuthSecurityKey(loginUser.getUserId());
        if (StringUtils.isNotEmpty(googleAuthSecurityKey)){
            if (StringUtils.isEmpty(googleValidateCode)){
                throw new ServiceException("请输入谷歌验证码");
            }
            // 根据密钥获取此刻的动态口令
            String realCode = GoogleAuthenticatorUtils.getTOTPCode(googleAuthSecurityKey);
            if (!realCode.equals(googleValidateCode)){
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(null,username,Constants.WEB_LOGIN, Constants.LOGIN_FAIL, "谷歌验证码错误"));
                throw new ServiceException("谷歌验证码错误");
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(null,username,Constants.WEB_LOGIN, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        recordLoginInfo(loginUser.getUserId());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(addressInfo);
        // 生成token
        return tokenService.createToken(loginUser);
    }


    /**
     * 校验验证码
     * 
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(null,username,Constants.WEB_LOGIN, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha))
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(null,username,Constants.WEB_LOGIN, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(null,username,Constants.WEB_LOGIN, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(null,username,Constants.WEB_LOGIN, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(null,username,Constants.WEB_LOGIN, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(null,username,Constants.WEB_LOGIN, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
