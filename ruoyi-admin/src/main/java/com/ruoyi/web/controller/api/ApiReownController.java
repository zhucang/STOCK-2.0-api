package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserInfoLogDict;
import com.ruoyi.common.service.TokenService;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.EthereumLoginUtils;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.utils.telegram.TelegramUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * reown钱包地址授权controller
 */
@RestController
@RequestMapping("/api/reown")
public class ApiReownController {

    private static Logger log = LoggerFactory.getLogger(ApiReownController.class);

    @Autowired
    private RedisCache redisCache;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IUserFastTradeControlService userFastTradeControlService;

    @Autowired
    private IBonusConfigService bonusConfigService;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Autowired
    private IUserWinningsChangeRecordService userWinningsChangeRecordService;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IIpBlackListService ipBlackListService;

    @Autowired
    private IAccountIpWhiteListService accountIpWhiteListService;

//    @RepeatSubmit
//    @GetMapping("/getNonce")
//    public AjaxResult getNonce(String address) {
//        String nonce = java.util.UUID.randomUUID().toString();
//        String key= "reown:" + address;
//        //3分钟有效时间
//        redisCache.setCacheObject(key, nonce, 3, TimeUnit.MINUTES);
//        return AjaxResult.success().put("nonce", nonce);
//    }

    @GetMapping("/getNonce")
    public AjaxResult getNonce(String address) {
        // EIP-4361 规定 Nonce 必须为纯字母数字 (alphanum)，移除 UUID 的连字符
        String nonce = java.util.UUID.randomUUID().toString().replace("-", "");
        String key = "reown:" + address;
        // 3分钟有效时间
        redisCache.setCacheObject(key, nonce, 3, TimeUnit.MINUTES);
        return AjaxResult.success().put("nonce", nonce);
    }

    @Transactional(rollbackFor = Exception.class)
    @RepeatSubmit
    @PostMapping("/verify")
    @Log(title = "钱包授权登录/注册", businessType = BusinessType.OTHER, dict = UserInfoLogDict.class)
    public AjaxResult verify(@RequestBody VerifyRequest req) throws Exception {
        //地址
        String address = req.getAddress();
        //消息
        String message = req.getMessage();
        //签名hex
        String signature = req.getSignature();
        //链命名空间
        String chainNamespace = req.getChainNamespace();
        //nonce
        String nonce = req.getNonce();
        // 账号
        String account = req.getAccount();
        //
        if (StringUtils.isEmpty(address) || StringUtils.isEmpty(message) || StringUtils.isEmpty(signature) || StringUtils.isEmpty(chainNamespace)) {
            throw new LangException(HintConstants.PARAM_NULL, "缺少参数");
        }
        //key
        String key= "reown:" + address;
        //
        String nonceCache = redisCache.getCacheObject(key);
        if (StringUtils.isEmpty(nonceCache)) {
            throw new LangException(HintConstants.SYSTEM_BUSY, "授权失败");
        } else {
            redisCache.deleteObject(key);
        }
        //验证nonce
        if (!nonceCache.equals(nonce)) {
            throw new LangException(HintConstants.SYSTEM_BUSY, "nonce err");
        }

        boolean verified = EthereumLoginUtils.verify(
                "https://bsc-dataseed.binance.org/",
                address,
                message,
                signature
        );

        if (!verified) {
            throw new RuntimeException("签名验证失败");
        }

        address = account;
        //
        UserInfo userInfo = new UserInfo();
        userInfo.setDappId(address);
        List<UserInfo> userInfos = userInfoMapper.selectUserInfoList(userInfo);
        //如果未绑定账号
        if (userInfos.size() == 0) {
            userInfo.setUserAccount(address);
            userInfo.setNickName(address);
            userInfo.setUserPwd(SecurityUtils.encryptPassword("123456"));
            String inviteCode;
            //生成邀请码
            while (true){
                inviteCode = "0"+ CodeUtils.generateInviteCode(CacheUtils.getOtherValueByKey("user_inviteCode_number",Integer.class));
                UserInfo byInviteCode = userInfoMapper.selectUserInfoByInviteCode(inviteCode);
                if (byInviteCode == null){
                    break;
                }
            }
            userInfo.setInviteCode(inviteCode);
            userInfo.setVipLevel(CacheUtils.getOtherValueByKey("new_user_default_vip_level",Integer.class));
            userInfo.setAccountType(0);
            userInfo.setRegTime(new Date());
            String ipAddr = IpUtils.getIpAddr();
            userInfo.setRegIp(ipAddr);
            userInfo.setRegAddress(IpUtils.getAddressByIp(ipAddr));
            int insertCount = userInfoMapper.insertUserInfo(userInfo);
            if (insertCount <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY, "新增用户信息异常");
            }
            //新用户默认单控状态
            String newUserFastTradeDefaultControl = CacheUtils.getOtherValueByKey("newUser_fastTrade_default_control",String.class);
            if (StringUtils.isNotEmpty(newUserFastTradeDefaultControl)){
                UserFastTradeControl userFastTradeControl = new UserFastTradeControl();
                userFastTradeControl.setUserId(userInfo.getId());
                userFastTradeControl.setControlType(Integer.valueOf(newUserFastTradeDefaultControl));
                int updateUserFastTradeControl = userFastTradeControlService.updateUserFastTradeControl(userFastTradeControl);
                if (updateUserFastTradeControl == 0){
                    throw new ServiceException("设置用户默认单控状态异常");
                }
            }
            //注册彩金
            BonusConfig bonusConfig = new BonusConfig();
            bonusConfig.setStartTime(new Date());
            bonusConfig.setEndTime(new Date());
            List<BonusConfig> bonusConfigs = bonusConfigService.selectBonusConfigList(bonusConfig);
            for (int i = 0; i < bonusConfigs.size(); i++) {
                BonusConfig bonusConfigVo = bonusConfigs.get(i);
                //彩金金额
                BigDecimal bonusAmount = bonusConfigVo.getBonusAmount();
                if (bonusAmount.compareTo(BigDecimal.ZERO) <= 0){
                    continue;
                }
                UserAmount userAmount = new UserAmount();
                userAmount.setUserId(userInfo.getId());
                userAmount.setCurrencyId(bonusConfigVo.getCurrencyId());
                userAmount.setAmount(bonusAmount);
                int insertUserAmount = userAmountService.insertUserAmount(userAmount);
                if (insertUserAmount <= 0){
                    throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
                }
                //用户流水记录
                UserBillDetail userBillDetail = new UserBillDetail();
                userBillDetail.setUserId(userInfo.getId());
                userBillDetail.setDeType("注册赠送彩金");
                userBillDetail.setDeSummary("注册赠送彩金");
                userBillDetail.setOrderAmount(bonusAmount);
                userBillDetail.setOrderTime(new Date());
                userBillDetail.setAmountBefore(BigDecimal.ZERO);
                userBillDetail.setAmountAfter(bonusAmount);
                userBillDetail.setRelateOrderId(null);
                userBillDetail.setOrderClass(56);
                userBillDetail.setCurrencyId(userAmount.getCurrencyId());
                int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
                if (insert <= 0) {
                    throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
                }
                //添加彩金出入记录
                UserWinningsChangeRecord userWinningsChangeRecord = new UserWinningsChangeRecord();
                userWinningsChangeRecord.setUserId(userInfo.getId());
                userWinningsChangeRecord.setOrderCode(CodeUtils.generateOrderCode("userWinningsChangeRecord"));
                userWinningsChangeRecord.setOrderAmount(bonusAmount);
                userWinningsChangeRecord.setOrderType(4);
                userWinningsChangeRecord.setCreateTime(new Date());
                userWinningsChangeRecord.setOperatorName("注册彩金");
                userWinningsChangeRecord.setCurrencyId(userAmount.getCurrencyId());
                userWinningsChangeRecord.setUserAmountBefore(BigDecimal.ZERO);
                userWinningsChangeRecord.setUserAmountAfter(bonusAmount);
                int insertUserPointChangeRecord = userWinningsChangeRecordService.insertUserWinningsChangeRecord(userWinningsChangeRecord);
                if (insertUserPointChangeRecord <= 0) {
                    throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
                }
            }

            //telegram通知（新增注册）
            Integer switchStatusById129 = switchSetService.selectSwitchStatusById(129L);
            if (switchStatusById129.equals(0)) {

                //telegram消息
                String telegramMsg = "\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89新增注册\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\n" +
                        "⏰时间：" + userInfo.getRegTime() + "\n" +
                        "ID: " + userInfo.getUserNo() + "\n" +
                        "用户账号: " + userInfo.getUserAccount() + "\n" +
                        "用户昵称: " + userInfo.getNickName() + "\n" +
                        "邀请码: " + userInfo.getInviteCode() + "\n" +
                        "所属代理: " + userInfo.getAgentId() + "/" + userInfo.getAgentName() + "\n" +
                        "代理昵称: " + userInfo.getAgentNickName() + "\n" +
                        "注册地址: " + userInfo.getRegAddress() + "\n" +
                        "已注册成功!";
                TelegramUtils.sendAsyncMessage(telegramMsg, "default", "default");
            }

            //重新获取详细信息
            LoginUser loginUser = new LoginUser();
            loginUser.setUserId(userInfo.getId());
            loginUser.setAppUser(userInfo);
            loginUser.setIpaddr(ipAddr);
            loginUser.setLoginLocation(userInfo.getRegAddress());
            String token = tokenService.createToken(loginUser);

            //记录免登录信息
            UserInfo userInfoVo = new UserInfo();
            userInfoVo.setId(userInfo.getId());;
            userInfoVo.setNoLoginInfo(token);
            userInfoVo.setLastLoginIp(ipAddr+"("+userInfo.getRegAddress()+")");
            userInfoVo.setLastLoginTime(new Date());
            int count = userInfoMapper.updateUserInfo(userInfoVo);
            if (count <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }

            //日志记录用户id
            HttpUtils.getRequestLogParams().put("userId",userInfo.getId());
            //日志记录用户账号
            HttpUtils.getRequestLogParams().put("userAccount",userInfo.getUserAccount());

            AjaxResult ajax = AjaxResult.success();
            ajax.put(Constants.TOKEN, token);
            ajax.put("userInfo", userInfo);
            //记录登陆日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userInfo.getId(),userInfo.getUserAccount(),Constants.APP_LOGIN, ajax.isSuccess() ? Constants.LOGIN_SUCCESS:Constants.LOGIN_FAIL, String.valueOf(ajax.get(AjaxResult.MSG_TAG))));
            return ajax;
        } else {
            //已绑定账号
            //登录
            //登录ip
            String ip = IpUtils.getIpAddr(HttpUtils.getHttpServletRequest());
            //获取黑名单是否有此ip
            IpBlackList ipBlackList = ipBlackListService.selectIpBlackListByIp(ip);
            if (ipBlackList != null){
                throw new LangException("hint_53","此ip禁止登录");
            }
            //ip所在地
            String addressInfo = null;
            try {
                addressInfo = IpUtils.getAddressByIp(ip);
            } catch (Exception e) {
                log.error("ip解析地区异常,ip:"+ip);
            }
            //如果没有解析出地址，不允许登陆
            if (StringUtils.isEmpty(addressInfo)){
//            throw new LangException(HintConstants.SYSTEM_BUSY,"登陆异常，未解析出登录地址");
            }
            //中国地区登录限制开关
            Integer restrictAreaChina = switchSetService.selectSwitchStatusById(30L);
            if ((restrictAreaChina != null && restrictAreaChina == 0)){
                String[] privinces = {"北京", "天津", "河北省", "山西", "内蒙", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆"};
                for (String privince : privinces) {
                    if (addressInfo.contains(privince)) {
                        throw new LangException("hint_35","该地区被限制登录");
                    }
                }
            }
            //香港澳门地区登录限制开关
            Integer restrictAreaGat = switchSetService.selectSwitchStatusById(31L);
            if ((restrictAreaGat != null && restrictAreaGat == 0)){
                String[] privinces = {"香港", "澳门","台湾"};
                for (String privince : privinces) {
                    if (addressInfo.contains(privince)) {
                        throw new LangException("hint_35","该地区被限制登录");
                    }
                }
            }
            //国外地区登录限制开关
            Integer restrictAreaOverseas = switchSetService.selectSwitchStatusById(105L);
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
                    throw new LangException("hint_35","该地区被限制登录");
                }
            }

            //用户信息
            userInfo = userInfos.get(0);
            if (!userInfo.getStatus().equals(0)) {
                throw new LangException("hint_accountLocked","登陆失败, 账户被锁定");
            }
            Integer appIpWhiteListSwitch = switchSetService.selectSwitchStatusById(137L);
            if (appIpWhiteListSwitch != null && appIpWhiteListSwitch.equals(0)
                    && userInfo.getAccountType() != null && userInfo.getAccountType().equals(1)
                    && !accountIpWhiteListService.isIpAllowed(userInfo.getAccountType(), userInfo.getId(), ip)){
                throw new LangException("当前账号未配置此ip白名单，禁止登录");
            }

            LoginUser loginUser = new LoginUser();
            loginUser.setUserId(userInfo.getId());
            loginUser.setAppUser(userInfo);
            loginUser.setIpaddr(ip);
            loginUser.setLoginLocation(addressInfo);
            String token = tokenService.createToken(loginUser);

            //记录免登录信息
            UserInfo userInfoVo = new UserInfo();
            userInfoVo.setId(userInfo.getId());;
            userInfoVo.setNoLoginInfo(token);
            userInfoVo.setLastLoginIp(ip+"("+addressInfo+")");
            userInfoVo.setLastLoginTime(new Date());
            int count = userInfoMapper.updateUserInfo(userInfoVo);
            if (count <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }

            //日志记录用户id
            HttpUtils.getRequestLogParams().put("userId",userInfo.getId());
            //日志记录用户账号
            HttpUtils.getRequestLogParams().put("userAccount",userInfo.getUserAccount());

            AjaxResult ajax = AjaxResult.success();
            ajax.put(Constants.TOKEN, token);
            ajax.put("userInfo", userInfo);
            //记录登陆日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userInfo.getId(),userInfo.getUserAccount(),Constants.APP_LOGIN, ajax.isSuccess() ? Constants.LOGIN_SUCCESS:Constants.LOGIN_FAIL, String.valueOf(ajax.get(AjaxResult.MSG_TAG))));
            return ajax;
        }
    }

    public static class VerifyRequest {
        private String address;
        private String nonce;
        private String message;
        private String signature;
        private String chainNamespace;
        private String chainId;
        private String account;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getChainNamespace() {
            return chainNamespace;
        }

        public void setChainNamespace(String chainNamespace) {
            this.chainNamespace = chainNamespace;
        }

        public String getChainId() {
            return chainId;
        }

        public void setChainId(String chainId) {
            this.chainId = chainId;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}
