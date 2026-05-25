package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.service.TokenService;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.UserApiKeyUtils;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.utils.currencyExchangeRate.ExchangeRateUtil;
import com.ruoyi.system.utils.email.SendEmailUtils;
import com.ruoyi.system.utils.sms.SmsSendUtils;
import com.ruoyi.system.utils.sms.SmsSendUtils2;
import com.ruoyi.system.utils.sms.SmsSendUtils3;
import com.ruoyi.system.utils.telegram.TelegramUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService
{

    private static Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private TokenService tokenService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Autowired
    private IUserTeamLevelLineService userTeamLevelLineService;

    @Resource
    private UserTeamLevelLineMapper userTeamLevelLineMapper;

    @Autowired
    private IIpBlackListService ipBlackListService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IBonusConfigService bonusConfigService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Resource
    private FastTradeOrderMapper fastTradeOrderMapper;

    @Autowired
    private IUserStockPositionService userStockPositionService;

    @Autowired
    private IUserCryptocurrencyPositionService userCryptocurrencyPositionService;

    @Autowired
    private IUserFuturesPositionService userFuturesPositionService;

    @Autowired
    private IUserForexPositionService userForexPositionService;

    @Autowired
    private IUserPointChangeRecordService userPointChangeRecordService;

    @Autowired
    private IAgentTeamLevelLineService agentTeamLevelLineService;

    @Autowired
    private IUserWinningsChangeRecordService userWinningsChangeRecordService;

    @Autowired
    private IUserFastTradeControlService userFastTradeControlService;

    @Autowired
    private IUserAuthRecordService userAuthRecordService;

    @Autowired
    private ISiteInfoService siteInfoService;

    @Autowired
    private IUserDmAmountChangeRecordService userDmAmountChangeRecordService;

    @Autowired
    private ICurrencyExchangeRateService currencyExchangeRateService;

    @Autowired
    private IUserVipLevelConfigService userVipLevelConfigService;

    @Autowired
    private IVipExperienceValueService vipExperienceValueService;

    @Autowired
    private IUserBibiAssetsService userBibiAssetsService;

    @Resource
    private UserApiKeyMapper userApiKeyMapper;

    /**
     * 查询用户信息
     * 
     * @param id 用户信息主键
     * @return 用户信息
     */
    @Override
    public UserInfo selectUserInfoById(Long id)
    {
        return userInfoMapper.selectUserInfoById(id);
    }

    /**
     * 查询用户信息列表
     * 
     * @param userInfo 用户信息
     * @return 用户信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserInfo> selectUserInfoList(UserInfo userInfo)
    {
        return userInfoMapper.selectUserInfoList(userInfo);
    }

    /**
     * 填充其他信息
     * @param userInfos 用户信息
     */
    @Override
    public void fillOtherInfo(List<UserInfo> userInfos){
        fillAgentLine(userInfos);
        fillSupUserLine(userInfos);
        fillCurrentOnlineStatus(userInfos);
        fillUserAmount(userInfos);
        fillUserFastTradeControlInfo(userInfos);
        fillUserBibiAssets(userInfos);
    }

    /**
     * 填充代理线
     * @param userInfos 用户信息
     */

    public void fillAgentLine(List<UserInfo> userInfos){
        //用户的代理集合
        List<Long> agentIds = userInfos.stream().map(UserInfo::getAgentId).distinct().collect(Collectors.toList());
        //取这些代理各自的上级，取N级
        AgentTeamLevelLine agentTeamLevelLine = new AgentTeamLevelLine();
        agentTeamLevelLine.setTeamLevel(3);
        agentTeamLevelLine.getParams().put("userIds",agentIds);
        agentTeamLevelLine.getParams().put("queryType",0);
        PageUtils.orderBy("team_level");
        List<AgentTeamLevelLine> agentTeamLevelLines = agentTeamLevelLineService.selectAgentTeamLevelLineList(agentTeamLevelLine);
        //上级团队信息map
        Map<Long, List<AgentTeamLevelLine>> agentTeamMap = agentTeamLevelLines.stream().collect(Collectors.groupingBy(a -> a.getUserId()));
        //获取这些代理的信息
        agentIds.addAll(agentTeamLevelLines.stream().map(AgentTeamLevelLine::getSupUserId).distinct().collect(Collectors.toList()));
        SysUser sysUser = new SysUser();
        sysUser.getParams().put("userIds",agentIds);
        sysUser.getParams().put("agentData",1);
        Map<Long, SysUser> agentUsersMap = sysUserMapper.selectUserList(sysUser).stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
        //遍历
        for (int i = 0; i < userInfos.size(); i++) {
            //用户信息
            UserInfo userInfo = userInfos.get(i);
            //代理id
            Long agentId = userInfo.getAgentId();
            //代理线
            if (agentUsersMap.get(agentId) == null){
                //如果代理信息不存在
                continue;
            }
            String agentLine = agentUsersMap.get(agentId).getUserName();
            //代理的上级线
            List<AgentTeamLevelLine> lines = agentTeamMap.get(agentId);
            if (lines != null){
                //遍历
                for (int j = 0; j < lines.size(); j++) {
                    //代理线信息
                    AgentTeamLevelLine agentTeamLevelLineVo = lines.get(j);
                    if (agentTeamLevelLineVo != null){
                        SysUser agentUser = agentUsersMap.get(agentTeamLevelLineVo.getSupUserId());
                        if (agentUser != null){
                            agentLine = agentUser.getUserName() + "——" + agentLine;
                        }
                    }

                }
            }
            userInfo.setAgentLine(agentLine);
        }
    }

    /**
     * 填充上级用户线
     * @param userInfos 用户信息
     */
    public void fillSupUserLine(List<UserInfo> userInfos){
        //用户上级线使用用户ID展示
        Integer switchStatus100 = switchSetService.selectSwitchStatusById(100L);
        for (int i = 0; i < userInfos.size(); i++) {
            //获取用户上级线
            PageHelper.orderBy("team_level");
            UserTeamLevelLine userTeamLevelLine = new UserTeamLevelLine();
            userTeamLevelLine.setUserId(userInfos.get(i).getId());
            List<UserTeamLevelLine> userTeamLevelLines = userTeamLevelLineService.selectUserTeamLevelLineList(userTeamLevelLine);
            //上级线基础
            String base = userInfos.get(i).getUserAccount();
            if (switchStatus100.equals(0)){
                base = String.valueOf(userInfos.get(i).getUserNo());
            }
            if (userTeamLevelLines.size() > 0){
                List<Long> supUserIds = userTeamLevelLines.stream().map(UserTeamLevelLine::getSupUserId).collect(Collectors.toList());
                UserInfo search = new UserInfo();
                search.getParams().put("userIds",supUserIds);
                Map<Long, UserInfo> userInfoMap = userInfoMapper.selectUserInfoList(search).stream().collect(Collectors.toMap(a -> a.getId(), a -> a));
                for (int j = 0; j < userTeamLevelLines.size(); j++) {
                    //用户id
                    Long userId = userTeamLevelLines.get(j).getSupUserId();
                    if (switchStatus100.equals(0)){
                        String userNo = String.valueOf(userInfoMap.get(userId).getUserNo());
                        base = userNo + ">>" + base;
                    }else {
                        String userAccount = userInfoMap.get(userId).getUserAccount();
                        base = userAccount + ">>" + base;
                    }
                }
            }
            userInfos.get(i).setSupUserLine(base);
        }
    }

    /**
     * 填充用户钱包信息
     * @param userInfos
     */
    public void fillUserAmount(List<UserInfo> userInfos){
        //平台币种
        PlatformCurrency platformCurrency = new PlatformCurrency();
        platformCurrency.setStatus(0);
        PageUtils.orderBy("sort is null,sort");
        List<PlatformCurrency> platformCurrencies = platformCurrencyService.selectPlatformCurrencyList(platformCurrency);
        PageUtils.clearPage();
        for (int i = 0; i < userInfos.size(); i++) {
            //用户id
            Long userId = userInfos.get(i).getId();
            //查询用户各货币余额信息列表
            List<UserAmount> userAmounts = userAmountService.selectUserAmountListByUserId(userId,platformCurrencies);
            //有余额的
            List<UserAmount> userAmountsUsed = userAmounts.stream().filter(a -> a.getAmount().compareTo(BigDecimal.ZERO) != 0 || a.getFrozenAmount().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());
            //有余额钱包的数量
            int userAmountsUsedSize = userAmountsUsed.size();
            if (userAmountsUsedSize >= 5){
                userInfos.get(i).setUserAmounts(userAmountsUsed);
            }else {
                //没有有余额的
                List<UserAmount> userAmountsUnUsed = userAmounts.stream().filter(a -> a.getAmount().compareTo(BigDecimal.ZERO) == 0 && a.getFrozenAmount().compareTo(BigDecimal.ZERO) == 0).collect(Collectors.toList());
                int num = 5 - userAmountsUsedSize;
                if (userAmountsUnUsed.size() < num){
                    num = userAmountsUnUsed.size();
                }
                Map<Long, UserAmount> mapUnUsed = userAmountsUnUsed.subList(0,num).stream().collect(Collectors.toMap(a -> a.getCurrencyId(), a -> a));
                Map<Long, UserAmount> mapUsed = userAmountsUsed.stream().collect(Collectors.toMap(a -> a.getCurrencyId(), a -> a));
                mapUsed.putAll(mapUnUsed);
                userAmountsUsed.clear();
                for (int j = 0; j < userAmounts.size(); j++) {
                    UserAmount userAmount = userAmounts.get(j);
                    if (mapUsed.get(userAmount.getCurrencyId()) != null){
                        userAmountsUsed.add(userAmount);
                    }
                }
                userInfos.get(i).setUserAmounts(userAmountsUsed);
            }
        }
    }

    /**
     * 填充在线状态
     * @param userInfos 用户信息
     */
    public void fillCurrentOnlineStatus(List<UserInfo> userInfos){
        for (int i = 0; i < userInfos.size(); i++) {
            //此用户当前登录的token
            String token = redisCache.getCacheMapValue("app", String.valueOf(userInfos.get(i).getId()));
            if (tokenService.validateIsLogin(token)){
                userInfos.get(i).setCurrentOnline(1);
            }else {
                userInfos.get(i).setCurrentOnline(0);
            }
        }
    }

    /**
     * 填充用户单控信息
     * @param userInfos
     */
    public void fillUserFastTradeControlInfo(List<UserInfo> userInfos){
        //用户ids
        List<Long> userIds = userInfos.stream().map(UserInfo::getId).collect(Collectors.toList());
        //查询极速交易用户单控参数
        UserFastTradeControl userFastTradeControl = new UserFastTradeControl();
        userFastTradeControl.getParams().put("userIds",userIds);
        List<UserFastTradeControl> userFastTradeControls = userFastTradeControlService.selectUserFastTradeControlList(userFastTradeControl);
        //用户单控信息map
        Map<Long, UserFastTradeControl> userFastTradeControlMap = userFastTradeControls.stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
        for (int i = 0; i < userInfos.size(); i++) {
            //用户id
            Long userId = userInfos.get(i).getId();
            //用户单控信息
            UserFastTradeControl userFastTradeControlVo = userFastTradeControlMap.get(userId);
            if (userFastTradeControlVo == null){
                userFastTradeControlVo = new UserFastTradeControl();
                //未控
                userFastTradeControlVo.setControlType(0);
            }
            userInfos.get(i).getParams().put("userFastTradeControl",userFastTradeControlVo);
        }
    }

    /**
     * 填充用户币币资产信息
     * @param userInfos
     */
    public void fillUserBibiAssets(List<UserInfo> userInfos){
        //用户ids
        List<Long> userIds = userInfos.stream().map(UserInfo::getId).collect(Collectors.toList());
        //查询用户币币资产信息
        UserBibiAssets userBibiAssets = new UserBibiAssets();
        userBibiAssets.getParams().put("userIds",userIds);
        List<UserBibiAssets> userBibiAssetsList = userBibiAssetsService.selectUserBibiAssetsList(userBibiAssets);
        userBibiAssetsService.fillOtherInfo(userBibiAssetsList);
        //用户币币资产信息map
        Map<Long, List<UserBibiAssets>> userBibiAssetsMap = userBibiAssetsList.stream().filter(a->a.getBibiAmount().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.groupingBy(a -> a.getUserId()));
        for (int i = 0; i < userInfos.size(); i++) {
            //用户id
            Long userId = userInfos.get(i).getId();
            //用户币币资产信息
            List<UserBibiAssets> userBibiAssetsVo = userBibiAssetsMap.get(userId);
            if (userBibiAssetsVo == null){
                userBibiAssetsVo = new ArrayList<UserBibiAssets>();
            }
            userInfos.get(i).getParams().put("userBibiAssets",userBibiAssetsVo);
        }
    }

    /**
     * 新增用户信息
     * 
     * @param userInfo 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUserInfo(UserInfo userInfo)
    {
        //用户登陆账号
        String userAccount = userInfo.getUserAccount();
        //账号最小位数限制
        Integer userAccountMinLimit = CacheUtils.getOtherValueByKey("user_account_min_limit", Integer.class);
        if (userAccountMinLimit != null && userAccount.length() < userAccountMinLimit){
            throw new ServiceException("登陆账号长度最小为"+userAccountMinLimit);
        }
        //根据账号查询用户
        UserInfo byUserAccount = userInfoMapper.userLogin(userAccount);
        if (byUserAccount != null) {
            throw new ServiceException("登陆账号已存在");
        }
        //用户手机号
        String phone = userInfo.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            String[] split = phone.split("-");
            if (split.length != 2 || !StringUtils.isNumeric(split[0]) || !StringUtils.isNumeric(split[1])){
                throw new ServiceException("请输入正确的手机号");
            }
            //手机号最小位数限制
            Integer userPhoneMinLimit = CacheUtils.getOtherValueByKey("user_phone_min_limit", Integer.class);
            if (userPhoneMinLimit != null && split[1].length() < userPhoneMinLimit){
                throw new ServiceException("手机号长度最小为"+userPhoneMinLimit);
            }
            UserInfo byPhone = userInfoMapper.userLogin(phone);
            if (byPhone != null) {
                throw new ServiceException("手机号已注册");
            }
        }
        //用户邮箱
        String email = userInfo.getEmail();
        if (StringUtils.isNotEmpty(email)){
            if (!email.contains("@") || !email.contains(".")){
                throw new ServiceException("请输入正确的邮箱");
            }
            UserInfo byEmail = userInfoMapper.userLogin(email);
            if (byEmail != null) {
                throw new ServiceException("邮箱号已注册");
            }
        }

        UserInfo userInfoVo = new UserInfo();
        userInfoVo.setAccountType(userInfo.getAccountType());
        userInfoVo.setUserAccount(userAccount);
        userInfoVo.setPhone(userInfo.getPhone());
        userInfoVo.setEmail(userInfo.getEmail());
        userInfoVo.setNickName(userInfo.getNickName());
        userInfoVo.setUserPwd(SecurityUtils.encryptPassword(userInfo.getUserPwd()));
        userInfoVo.setWithPwd(SecurityUtils.encryptPassword(userInfo.getWithPwd()));
        userInfoVo.setStatus(userInfo.getStatus());
        userInfoVo.setIsLock(userInfo.getIsLock());
        userInfoVo.setRegTime(new Date());
        String ipAddr = IpUtils.getIpAddr();
        userInfoVo.setRegIp(ipAddr);
        userInfoVo.setRegAddress(IpUtils.getAddressByIp(ipAddr));
        if (userInfo.getAgentId() != null) {
            //代理信息
            SysUser agentUser = sysUserMapper.selectUserById(userInfo.getAgentId());
            if (agentUser != null && agentUser.getDelFlag().equals("0")){
                userInfoVo.setAgentId(userInfo.getAgentId());
                userInfoVo.setAgentName(agentUser.getUserName());
                userInfoVo.setAgentNickName(agentUser.getNickName());
            }else {
                throw new ServiceException("系统繁忙");
            }
        }
        //生成邀请码
        String inviteCode;
        while (true){
            inviteCode = "0"+ CodeUtils.generateInviteCode(CacheUtils.getOtherValueByKey("user_inviteCode_number",Integer.class));
            UserInfo byInviteCode = userInfoMapper.selectUserInfoByInviteCode(inviteCode);
            if (byInviteCode == null){
                break;
            }
        }
        userInfoVo.setInviteCode(inviteCode);
        userInfoVo.setVipLevel(CacheUtils.getOtherValueByKey("new_user_default_vip_level",Integer.class));
//        userInfoVo.setAvatar(siteInfoService.selectSiteInfoById(1L).getSiteLogo());
        int insertCount = userInfoMapper.insertUserInfo(userInfoVo);
        if (insertCount <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录未加密的密码
        userInfoVo.setWithPwd(userInfo.getWithPwd());
        userInfoVo.setUserPwd(userInfo.getUserPwd());
        //日志记录用户信息
        HttpUtils.getRequestLogParams().put("userInfoVo", JSONObject.toJSONString(userInfoVo));
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("id",userInfoVo.getId());
        //新用户默认单控状态
        String newUserFastTradeDefaultControl = CacheUtils.getOtherValueByKey("newUser_fastTrade_default_control",String.class);
        if (StringUtils.isNotEmpty(newUserFastTradeDefaultControl)){
            UserFastTradeControl userFastTradeControl = new UserFastTradeControl();
            userFastTradeControl.setUserId(userInfoVo.getId());
            userFastTradeControl.setControlType(Integer.valueOf(newUserFastTradeDefaultControl));
            int updateUserFastTradeControl = userFastTradeControlService.updateUserFastTradeControl(userFastTradeControl);
            if (updateUserFastTradeControl == 0){
                throw new ServiceException("设置用户默认单控状态异常");
            }
        }
        return 1;
    }

    /**
     * 修改用户信息
     * 
     * @param userInfo 用户信息
     * @return 结果
     */
    @Override
    public int updateUserInfo(UserInfo userInfo)
    {
        //用户id
        Long userId = userInfo.getId();
        //验证邮箱号是否存在
        String email = userInfo.getEmail();
        if (StringUtils.isNotEmpty(email)){
            if (!email.contains("@") || !email.contains(".")){
                throw new ServiceException("请输入正确的邮箱");
            }
            UserInfo byEmail = userInfoMapper.userLogin(userInfo.getEmail());
            if (byEmail != null && !userId.equals(byEmail.getId())) {
                throw new ServiceException("邮箱号已存在");
            }
        }else {
            userInfo.getParams().put("emailNull","emailNull");
        }
        //验证手机号是否存在
        String phone = userInfo.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            String[] split = phone.split("-");
            if (split.length != 2 || !StringUtils.isNumeric(split[0]) || !StringUtils.isNumeric(split[1])){
                throw new ServiceException("请输入正确的手机号");
            }
            //手机号最小位数限制
            Integer userPhoneMinLimit = CacheUtils.getOtherValueByKey("user_phone_min_limit", Integer.class);
            if (userPhoneMinLimit != null && split[1].length() < userPhoneMinLimit){
                throw new ServiceException("手机号长度最小为"+userPhoneMinLimit);
            }
            UserInfo byPhone = userInfoMapper.userLogin(phone);
            if (byPhone != null && !userId.equals(byPhone.getId())) {
                throw new ServiceException("手机号已存在");
            }
        }else {
            userInfo.getParams().put("phoneNull","phoneNull");
        }
        //验证登陆账号是否存在
        String userAccount = userInfo.getUserAccount();
        if (StringUtils.isNotEmpty(userAccount)){
            //去除头尾空格
            userAccount = userAccount.trim();
            //账号最小位数限制
            Integer userAccountMinLimit = CacheUtils.getOtherValueByKey("user_account_min_limit", Integer.class);
            if (userAccountMinLimit != null && userAccount.length() < userAccountMinLimit){
                throw new ServiceException("登陆账号长度最小为"+userAccountMinLimit);
            }
            UserInfo byUserAccount = userInfoMapper.userLogin(userAccount);
            if (byUserAccount != null && !userId.equals(byUserAccount.getId())) {
                throw new ServiceException("登陆账号已经存在");
            }
        }
        //旧的用户信息
        UserInfo userInfoOld = userInfoMapper.selectUserInfoById(userId);
        if (userInfo.getAgentId() != null) {
            //如果修改了代理
            if (!userInfo.getAgentId().equals(userInfoOld.getAgentId())){
                //代理信息
                SysUser agentUser = sysUserMapper.selectUserById(userInfo.getAgentId());
                if (agentUser != null && agentUser.getDelFlag().equals("0")) {
                    userInfo.setAgentName(agentUser.getUserName());
                    userInfo.setAgentNickName(agentUser.getNickName());
                }else {
                    throw new ServiceException("系统繁忙");
                }
                //修改了代理，变成代理的直推用户，无上级用户
                userInfo.getParams().put("cleanSupUserId",0);
                //获取当前用户的所有下级团队关系信息
                List<UserTeamLevelLine> lowerTeamLine = userTeamLevelLineMapper.getLowerTeamLine(userId, null, 0);
                //所有下级用户的用户id
                List<Long> lowerTeamUserIds = lowerTeamLine.stream().map(UserTeamLevelLine::getUserId).collect(Collectors.toList());
                //更新所有下级的代理为新上级的代理
                int replaceAgentIdAndAgentName = userInfoMapper.replaceAgentIdAndAgentName(userInfoOld.getAgentId(), agentUser.getUserId(), agentUser.getUserName(),agentUser.getNickName(),lowerTeamUserIds);
                if (lowerTeamUserIds.size() != replaceAgentIdAndAgentName){
                    throw new ServiceException("系统繁忙");
                }
                //获取当前用户的所有上级团队关系信息
                List<UserTeamLevelLine> supTeamLine = userTeamLevelLineMapper.getSupTeamLine(userId, null, 0);
                //需要删除的团队关系信息
                List<Long> deleteIds = new ArrayList<>();
                //清理当前用户的所有上级团队关系信息
                deleteIds.addAll(supTeamLine.stream().map(UserTeamLevelLine::getId).collect(Collectors.toList()));
                //清理下级与当前用户旧上级的团队关系信息
                for (int i = 0; i < lowerTeamLine.size(); i++) {
                    //此下级与当前用户的团队等级差
                    Integer teamLevel = lowerTeamLine.get(i).getTeamLevel();
                    //清理团队等级大于与当前用户的团队等级差的数据（下级与当前用户旧上级的团队关系信息）
                    deleteIds.addAll(userTeamLevelLineMapper.getSupTeamLine(lowerTeamLine.get(i).getUserId(), null, 0).stream().filter(a -> a.getTeamLevel() > teamLevel).map(UserTeamLevelLine::getId).collect(Collectors.toList()));
                }
                //需要清理的数据数量
                int deleteCount = deleteIds.size();
                if (deleteCount > 0 ){
                    //清理需要清理的数据
                    int deleteUserTeamLevelLineByIds = userTeamLevelLineMapper.deleteUserTeamLevelLineByIds(deleteIds.toArray(new Long[deleteCount]));
                    if (deleteUserTeamLevelLineByIds != deleteCount){
                        throw new ServiceException("系统繁忙");
                    }
                }
            }
        }

        //修改前打码量
        BigDecimal needOrderAmountBefore = userInfoOld.getNeedOrderAmount();
        //修改后打码量
        BigDecimal needOrderAmountAfter = userInfo.getNeedOrderAmount();
        //如果有修改打码量
        if (needOrderAmountAfter != null && needOrderAmountAfter.compareTo(needOrderAmountBefore) != 0){
            //变更打码量
            BigDecimal dmAmount = needOrderAmountAfter.subtract(needOrderAmountBefore);
            //插入打码量变更记录
            UserDmAmountChangeRecord userDmAmountChangeRecord = new UserDmAmountChangeRecord();
            userDmAmountChangeRecord.setUserId(userId);
            userDmAmountChangeRecord.setOrderAmount(dmAmount);
            userDmAmountChangeRecord.setDmMultiples(BigDecimal.ZERO);
            userDmAmountChangeRecord.setDmAmount(dmAmount);
            userDmAmountChangeRecord.setDmAmountBefore(needOrderAmountBefore);
            userDmAmountChangeRecord.setDmAmountAfter(needOrderAmountAfter);
            userDmAmountChangeRecord.setCreateTime(new Date());
            userDmAmountChangeRecord.setUpdateBy(SecurityUtils.getUsername());
            userDmAmountChangeRecord.setOrderType(3);
            int insertUserDmAmountChangeRecord = userDmAmountChangeRecordService.insertUserDmAmountChangeRecord(userDmAmountChangeRecord);
            if (insertUserDmAmountChangeRecord <= 0){
                throw new ServiceException("系统繁忙");
            }
        }

        UserInfo userInfoVo = new UserInfo();
        userInfoVo.setId(userId);
        userInfoVo.setAgentId(userInfo.getAgentId());
        userInfoVo.setAgentName(userInfo.getAgentName());
        userInfoVo.setAgentNickName(userInfo.getAgentNickName());
        userInfoVo.setUserAccount(userAccount);
        userInfoVo.setNickName(userInfo.getNickName());
        userInfoVo.setPhone(userInfo.getPhone());
        userInfoVo.setEmail(userInfo.getEmail());
        userInfoVo.setWithPwd(SecurityUtils.encryptPassword(userInfo.getWithPwd()));
        userInfoVo.setUserPwd(SecurityUtils.encryptPassword(userInfo.getUserPwd()));
        userInfoVo.setIsLock(userInfo.getIsLock());
        userInfoVo.setStatus(userInfo.getStatus());
        userInfoVo.setNeedOrderAmount(userInfo.getNeedOrderAmount());
        userInfoVo.setRemark(userInfo.getRemark());
        userInfoVo.setParams(userInfo.getParams());
        int updateCount = userInfoMapper.updateUserInfo(userInfoVo);
        if (updateCount <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录未加密的密码
        userInfoVo.setWithPwd(userInfo.getWithPwd());
        userInfoVo.setUserPwd(userInfo.getUserPwd());
        //日志记录用户信息
        HttpUtils.getRequestLogParams().put("userInfoVo",JSONObject.toJSONString(userInfoVo));
        return 1;
    }

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的用户信息主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserInfoByIds(Long[] ids)
    {
        UserInfo userInfo = new UserInfo();
        Map<String, Object> params = new HashMap<>();
        params.put("userIds",Arrays.asList(ids));
        userInfo.setParams(params);
        List<UserInfo> userInfos = userInfoMapper.selectUserInfoList(userInfo);
        //日志记录用户信息
        HttpUtils.getRequestLogParams().put("JSONArray:userInfos", JSONObject.toJSONString(userInfos));
        for (int i = 0; i < userInfos.size(); i++) {
            //用户信息
            UserInfo userInfoVo = userInfos.get(i);
            //用户id
            Long userId = userInfoVo.getId();
            int updateUserTeamLevelLine = userTeamLevelLineService.updateUserTeamLevelLine(userId, userInfoVo.getSupUserId(), 1);
            if(updateUserTeamLevelLine <= 0){
                throw new RuntimeException("系统繁忙");
            }
        }
        return userInfoMapper.deleteUserInfoByIds(ids);
    }

    /**
     * 修改用户资金（用户资金入款/扣款）
     * @param userId 用户id
     * @param amt 金额
     * @param direction 0：入款 1：扣款
     * @param currencyId 币种id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAmt(Long userId, BigDecimal amt, Integer direction, Long currencyId, String remark){
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null) {
            throw new ServiceException("用户不存在");
        }
        //上分下分自动转化平台默认交易币种
        Integer selectSwitchStatusById66 = switchSetService.selectSwitchStatusById(66L);
        if (selectSwitchStatusById66.equals(0)){
            //平台默认交易币种
            Long defaultTradeCurrencyId = CacheUtil.getOtherValueByKey("default_trade_currency_id", Long.class);
            if (defaultTradeCurrencyId == null){
                throw new ServiceException("获取平台默认交易币种信息异常");
            }
            if (!defaultTradeCurrencyId.equals(currencyId)){
                //获取平台币种
                PlatformCurrency platformCurrency = new PlatformCurrency();
                platformCurrency.setStatus(0);
                List<PlatformCurrency> platformCurrencies = platformCurrencyService.selectPlatformCurrencyList(platformCurrency);
                //币种map
                Map<Long, PlatformCurrency> platformCurrencyMap = platformCurrencies.stream().collect(Collectors.toMap(a -> a.getId(), a -> a));
                ExchangeRateUtil.fillExchangeRate(platformCurrencies);
                //汇率
                BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,defaultTradeCurrencyId,platformCurrencyMap).get("exchangeRate");
                //折合
                amt = amt.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                currencyId = defaultTradeCurrencyId;
            }
        }
        //订单号
        String orderCode = CodeUtils.generateOrderCode("userPointChangeRecord");
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        //用户钱包余额信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //变更前金额
        BigDecimal userAmountBefore = userAmount.getAmount();
        //如果是扣钱
        if (direction.equals(1)){
            if (userAmountBefore.compareTo(amt) < 0){
                throw new ServiceException("扣款失败, 资金不足");
            }
            amt = amt.negate();
        }else {
            //如果是上分
            //打码量计算模式（0：用户充值 1：用户充值+上分 2：用户充值+上分+彩金）
            SwitchSet switchSet81 = switchSetService.selectSwitchSetById(81L);
            if (switchSet81 != null && switchSet81.getStatus() >= 1){
                //更新打码量
                //打码倍数
                BigDecimal userDefaultDmMultiples = CacheUtils.getOtherValueByKey("user_default_dm_multiples",BigDecimal.class);
                if (userDefaultDmMultiples != null && userDefaultDmMultiples.compareTo(BigDecimal.ZERO) >= 0){
                    //汇率
                    BigDecimal exchangeRate = currencyExchangeRateService.getExchangeInfo(currencyId,3L).get("exchangeRate");
                    //折合USDT
                    BigDecimal USDTValue = exchangeRate.multiply(amt);
                    //新增打码量
                    BigDecimal dmAmt = userDefaultDmMultiples.multiply(USDTValue).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    if (dmAmt.compareTo(BigDecimal.ZERO) > 0){
                        //原先的打码量
                        BigDecimal needOrderAmount = userInfo.getNeedOrderAmount();
                        if (needOrderAmount.compareTo(BigDecimal.ZERO) < 0){
                            needOrderAmount = BigDecimal.ZERO;
                        }
                        UserInfo userInfoVo = new UserInfo();
                        userInfoVo.setId(userInfo.getId());
                        userInfoVo.setNeedOrderAmount(needOrderAmount.add(dmAmt));
                        int updateUser = userInfoMapper.updateUserInfo(userInfoVo);
                        if (updateUser <= 0) {
                            throw new ServiceException("系统繁忙");
                        }
                        //插入打码量变更记录
                        UserDmAmountChangeRecord userDmAmountChangeRecord = new UserDmAmountChangeRecord();
                        userDmAmountChangeRecord.setUserId(userId);
                        userDmAmountChangeRecord.setOrderAmount(USDTValue);
                        userDmAmountChangeRecord.setDmMultiples(userDefaultDmMultiples);
                        userDmAmountChangeRecord.setDmAmount(dmAmt);
                        userDmAmountChangeRecord.setDmAmountBefore(needOrderAmount);
                        userDmAmountChangeRecord.setDmAmountAfter(needOrderAmount.add(dmAmt));
                        userDmAmountChangeRecord.setCreateTime(new Date());
                        userDmAmountChangeRecord.setUpdateBy(SecurityUtils.getUsername());
                        userDmAmountChangeRecord.setOrderType(0);
                        int insertUserDmAmountChangeRecord = userDmAmountChangeRecordService.insertUserDmAmountChangeRecord(userDmAmountChangeRecord);
                        if (insertUserDmAmountChangeRecord <= 0){
                            throw new ServiceException("系统繁忙");
                        }
                    }
                }
            }

            //与USDT汇率
            BigDecimal exchangeRate = currencyExchangeRateService.getExchangeInfo(currencyId,3L).get("exchangeRate");
            //折合USDT
            BigDecimal USDTValue = exchangeRate.multiply(amt);

            //用户当前vip等级
            Integer vipLevel = userInfo.getVipLevel();

            //用户上分计算VIP等级
            Integer selectSwitchStatus115 = switchSetService.selectSwitchStatusById(115L);
            if (selectSwitchStatus115.equals(0)){
                //更新会员等级
                //VIP经验值更新前
                BigDecimal vipExperienceValueBefore = vipExperienceValueService.getUserCurrentVipExperienceValue(userId);
                //本次充值经验值
                BigDecimal experienceValue = USDTValue;
                //VIP经验值更新后
                BigDecimal vipExperienceValueAfter = vipExperienceValueBefore.add(experienceValue);
                //匹配的用户vip等级
                UserVipLevelConfig userVipLevelConfigNew = userVipLevelConfigService.selectUserVipLevelConfigByRechargeAmount(vipExperienceValueAfter);
                //如果匹配的vip等级与当前等级不符，则更新vip等级
                if (userVipLevelConfigNew != null && !userVipLevelConfigNew.getVipLevel().equals(vipLevel)){
                    UserInfo userInfoVo = new UserInfo();
                    userInfoVo.setId(userInfo.getId());
                    userInfoVo.setVipLevel(userVipLevelConfigNew.getVipLevel());
                    int count = userInfoMapper.updateUserInfo(userInfoVo);
                    if (count <= 0){
                        throw new ServiceException("系统繁忙");
                    }
                }
                //新增VIP经验值记录
                VipExperienceValue vipExperienceValue = new VipExperienceValue();
                vipExperienceValue.setUserId(userId);
                vipExperienceValue.setRelateOrderCode(orderCode);
                vipExperienceValue.setExperienceValue(experienceValue);
                vipExperienceValue.setExperienceValueBefore(vipExperienceValueBefore);
                vipExperienceValue.setExperienceValueAfter(vipExperienceValueAfter);
                vipExperienceValue.setCreateTime(new Date());
                int insertVipExperienceValue = vipExperienceValueService.insertVipExperienceValue(vipExperienceValue);
                if (insertVipExperienceValue <= 0){
                    throw new ServiceException("插入VIP经验值记录异常");
                }
            }
        }
        //变更后金额
        BigDecimal userAmountAfter = userAmountBefore.add(amt);
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new ServiceException("系统繁忙");
        }

        //添加账户流水
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userInfo.getId());
        //入款
        if (direction.equals(0)){
            userBillDetail.setOrderClass(2);
            userBillDetail.setDeType("系统入款");
            userBillDetail.setDeSummary("系统入款成功");
        }else {
            userBillDetail.setDeType("系统扣款");
            userBillDetail.setDeSummary("系统扣款成功");
            userBillDetail.setOrderClass(3);
        }
        userBillDetail.setOrderAmount(amt);
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(null);
        userBillDetail.setCurrencyId(userAmount.getCurrencyId());
        int count = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (count <= 0) {
            throw new ServiceException("系统繁忙");
        }

        //添加上分下分记录
        UserPointChangeRecord userPointChangeRecord = new UserPointChangeRecord();
        userPointChangeRecord.setUserId(userId);
        userPointChangeRecord.setOrderCode(orderCode);
        userPointChangeRecord.setOrderAmount(amt);
        userPointChangeRecord.setOrderType(direction);
        userPointChangeRecord.setCreateTime(new Date());
        userPointChangeRecord.setOperatorName(SecurityUtils.getUsername());
        userPointChangeRecord.setCurrencyId(currencyId);
        userPointChangeRecord.setUserAmountBefore(userAmountBefore);
        userPointChangeRecord.setUserAmountAfter(userAmountAfter);
        userPointChangeRecord.setRemark(remark);
        int insertUserPointChangeRecord = userPointChangeRecordService.insertUserPointChangeRecord(userPointChangeRecord);
        if (insertUserPointChangeRecord <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录上下分记录信息
        HttpUtils.getRequestLogParams().put("userPointChangeRecord",JSONObject.toJSONString(userPointChangeRecord));
        return 1;
    }

    /**
     * 用户彩金操作
     * @param userId 用户id
     * @param amt 金额
     * @param direction 0：彩金赠送（系统充值） 1：彩金赠送（福利彩金） 2：彩金回收
     * @param currencyId 币种id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWinnings(Long userId, BigDecimal amt, Integer direction,Long currencyId){
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null) {
            throw new ServiceException("用户不存在");
        }
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        //订单号
        String orderCode = CodeUtils.generateOrderCode("userWinningsChangeRecord");
        //用户钱包余额信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //变更前金额
        BigDecimal userAmountBefore = userAmount.getAmount();
        //如果是扣钱
        if (direction.equals(2)) {
            if (userAmountBefore.compareTo(amt) < 0) {
                throw new ServiceException("扣款失败, 资金不足");
            }
            //获取用户某币种的可回收彩金金额
            BigDecimal canRecoveredWinningsAmount = userBillDetailService.getTotalOutWinningsByUserId(userId,currencyId);
            if (canRecoveredWinningsAmount.compareTo(amt) < 0){
                throw new ServiceException("此币种彩金可回收金额" + canRecoveredWinningsAmount);
            }
            amt = amt.negate();
        }else {
            //如果是赠送彩金
            //打码量计算模式（0：用户充值 1：用户充值+上分 2：用户充值+上分+彩金）
            SwitchSet switchSet81 = switchSetService.selectSwitchSetById(81L);
            if (switchSet81 != null && switchSet81.getStatus().equals(2)){
                //更新打码量
                //打码倍数
                BigDecimal userDefaultDmMultiples = CacheUtils.getOtherValueByKey("user_default_dm_multiples",BigDecimal.class);
                if (userDefaultDmMultiples != null && userDefaultDmMultiples.compareTo(BigDecimal.ZERO) >= 0){
                    //汇率
                    BigDecimal exchangeRate = currencyExchangeRateService.getExchangeInfo(currencyId,3L).get("exchangeRate");
                    //折合USDT
                    BigDecimal USDTValue = exchangeRate.multiply(amt);
                    //新增打码量
                    BigDecimal dmAmt = userDefaultDmMultiples.multiply(USDTValue).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    if (dmAmt.compareTo(BigDecimal.ZERO) > 0){
                        //原先的打码量
                        BigDecimal needOrderAmount = userInfo.getNeedOrderAmount();
                        if (needOrderAmount.compareTo(BigDecimal.ZERO) < 0){
                            needOrderAmount = BigDecimal.ZERO;
                        }
                        UserInfo userInfoVo = new UserInfo();
                        userInfoVo.setId(userInfo.getId());
                        userInfoVo.setNeedOrderAmount(needOrderAmount.add(dmAmt));
                        int updateUser = userInfoMapper.updateUserInfo(userInfoVo);
                        if (updateUser <= 0) {
                            throw new ServiceException("系统繁忙");
                        }
                        //插入打码量变更记录
                        UserDmAmountChangeRecord userDmAmountChangeRecord = new UserDmAmountChangeRecord();
                        userDmAmountChangeRecord.setUserId(userId);
                        userDmAmountChangeRecord.setOrderAmount(USDTValue);
                        userDmAmountChangeRecord.setDmMultiples(userDefaultDmMultiples);
                        userDmAmountChangeRecord.setDmAmount(dmAmt);
                        userDmAmountChangeRecord.setDmAmountBefore(needOrderAmount);
                        userDmAmountChangeRecord.setDmAmountAfter(needOrderAmount.add(dmAmt));
                        userDmAmountChangeRecord.setCreateTime(new Date());
                        userDmAmountChangeRecord.setUpdateBy(SecurityUtils.getUsername());
                        userDmAmountChangeRecord.setOrderType(2);
                        int insertUserDmAmountChangeRecord = userDmAmountChangeRecordService.insertUserDmAmountChangeRecord(userDmAmountChangeRecord);
                        if (insertUserDmAmountChangeRecord <= 0){
                            throw new ServiceException("系统繁忙");
                        }
                    }
                }
            }
            //与USDT汇率
            BigDecimal exchangeRate = currencyExchangeRateService.getExchangeInfo(currencyId,3L).get("exchangeRate");
            //折合USDT
            BigDecimal USDTValue = exchangeRate.multiply(amt);

            //用户当前vip等级
            Integer vipLevel = userInfo.getVipLevel();

            //用户上分计算VIP等级
            Integer selectSwitchStatus116 = switchSetService.selectSwitchStatusById(116L);
            if (selectSwitchStatus116.equals(0)){
                //更新会员等级
                //VIP经验值更新前
                BigDecimal vipExperienceValueBefore = vipExperienceValueService.getUserCurrentVipExperienceValue(userId);
                //本次充值经验值
                BigDecimal experienceValue = USDTValue;
                //VIP经验值更新后
                BigDecimal vipExperienceValueAfter = vipExperienceValueBefore.add(experienceValue);
                //匹配的用户vip等级
                UserVipLevelConfig userVipLevelConfigNew = userVipLevelConfigService.selectUserVipLevelConfigByRechargeAmount(vipExperienceValueAfter);
                //如果匹配的vip等级与当前等级不符，则更新vip等级
                if (userVipLevelConfigNew != null && !userVipLevelConfigNew.getVipLevel().equals(vipLevel)){
                    UserInfo userInfoVo = new UserInfo();
                    userInfoVo.setId(userInfo.getId());
                    userInfoVo.setVipLevel(userVipLevelConfigNew.getVipLevel());
                    int count = userInfoMapper.updateUserInfo(userInfoVo);
                    if (count <= 0){
                        throw new ServiceException("系统繁忙");
                    }
                }
                //新增VIP经验值记录
                VipExperienceValue vipExperienceValue = new VipExperienceValue();
                vipExperienceValue.setUserId(userId);
                vipExperienceValue.setRelateOrderCode(orderCode);
                vipExperienceValue.setExperienceValue(experienceValue);
                vipExperienceValue.setExperienceValueBefore(vipExperienceValueBefore);
                vipExperienceValue.setExperienceValueAfter(vipExperienceValueAfter);
                vipExperienceValue.setCreateTime(new Date());
                int insertVipExperienceValue = vipExperienceValueService.insertVipExperienceValue(vipExperienceValue);
                if (insertVipExperienceValue <= 0){
                    throw new ServiceException("插入VIP经验值记录异常");
                }
            }
        }
        //变更后金额
        BigDecimal userAmountAfter = userAmountBefore.add(amt);
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new ServiceException("系统繁忙");
        }

        String type = null;
        Integer orderClass = null;
        if (direction.equals(0)){
            type = "彩金赠送(系统充值)";
            orderClass = 40;
        }else if (direction.equals(1)){
            type = "彩金赠送(福利彩金)";
            orderClass = 41;
        }else if (direction.equals(2)){
            type = "彩金回收";
            orderClass = 42;
        }

        //添加账户流水
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userInfo.getId());
        userBillDetail.setDeType(type);
        userBillDetail.setDeSummary(type);
        userBillDetail.setOrderAmount(amt);
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(null);
        userBillDetail.setOrderClass(orderClass);
        userBillDetail.setCurrencyId(currencyId);
        int count = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (count <= 0) {
            throw new ServiceException("系统繁忙");
        }

        //添加彩金出入记录
        UserWinningsChangeRecord userWinningsChangeRecord = new UserWinningsChangeRecord();
        userWinningsChangeRecord.setUserId(userId);
        userWinningsChangeRecord.setOrderCode(orderCode);
        userWinningsChangeRecord.setOrderAmount(amt);
        userWinningsChangeRecord.setOrderType(direction);
        userWinningsChangeRecord.setCreateTime(new Date());
        userWinningsChangeRecord.setOperatorName(SecurityUtils.getUsername());
        userWinningsChangeRecord.setCurrencyId(currencyId);
        userWinningsChangeRecord.setUserAmountBefore(userAmountBefore);
        userWinningsChangeRecord.setUserAmountAfter(userAmountAfter);
        int insertUserPointChangeRecord = userWinningsChangeRecordService.insertUserWinningsChangeRecord(userWinningsChangeRecord);
        if (insertUserPointChangeRecord <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录彩金出入记录信息
        HttpUtils.getRequestLogParams().put("userWinningsChangeRecord",JSONObject.toJSONString(userWinningsChangeRecord));
        return 1;
    }

    /**
     * 设置用户等级
     * @param userId 用户id
     * @param vipLevel vip等级
     * @return
     */
    public AjaxResult setUserVipLevel(Long userId,Integer vipLevel){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setVipLevel(vipLevel);
        int count = userInfoMapper.updateUserInfo(userInfo);
        if (count <= 0){
            return AjaxResult.error("系统费繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 设置信用分
     * @param userId 用户id
     * @param creditScore
     * @return
     */
    public AjaxResult setUserCreditScore(Long userId,Integer creditScore){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setCreditScore(creditScore);
        int count = userInfoMapper.updateUserInfo(userInfo);
        if (count <= 0){
            return AjaxResult.error("系统费繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 设置用户可提现状态
     * @param userId 用户id
     * @param isCanWithdraw 是否能提现：0：是 1：否
     * @return
     */
    public AjaxResult setUserIsCanWithdraw(Long userId,Integer isCanWithdraw){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setIsCanWithdraw(isCanWithdraw);
        int count = userInfoMapper.updateUserInfo(userInfo);
        if (count <= 0){
            return AjaxResult.error("系统繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 设置用户极速输、赢收益率
     * @param userId 用户id
     * @param fastTradeWinProfitRatio 极速赢收益率
     * @param fastTradeLoseProfitRatio 极速输收益率
     * @return
     */
    @Override
    public AjaxResult setFastTradeWinOrLoseProfitRatio(Long userId,BigDecimal fastTradeWinProfitRatio,BigDecimal fastTradeLoseProfitRatio){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setFastTradeWinProfitRatio(fastTradeWinProfitRatio);
        userInfo.setFastTradeLoseProfitRatio(fastTradeLoseProfitRatio);
        int count = userInfoMapper.updateUserInfo(userInfo);
        if (count <= 0){
            return AjaxResult.error("系统费繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 设置用户是否是代理员
     * @param userId 用户id
     * @param isAgent 是否设置为代理员 0：是 1：否
     * @return
     */
    @Override
    public AjaxResult setIsAgent(Long userId,Integer isAgent){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setIsAgent(isAgent);
        int count = userInfoMapper.updateUserInfo(userInfo);
        if (count <= 0){
            return AjaxResult.error("系统费繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 冻结、解冻资金
     * @param userId 用户id
     * @param currencyId 币种id
     * @param amount 金额
     * @param operationType 操作类型 0：冻结 1：解冻
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int freezeAndUnfreezeFunds(Long userId,Long currencyId,BigDecimal amount,Integer operationType){
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        //钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //冻结
        if (operationType.equals(0)){
            //余额变更前
            BigDecimal amountBefore = userAmount.getAmount();
            if (amountBefore.compareTo(amount) < 0){
                throw new ServiceException("余额不足");
            }
            //余额变更后
            BigDecimal amountAfter = amountBefore.subtract(amount);
            //冻结金额变更前
            BigDecimal frozenAmountBefore = userAmount.getFrozenAmount();
            //冻结金额变更后
            BigDecimal frozenAmountAfter = frozenAmountBefore.add(amount);
            userAmount.setAmount(amountAfter);
            userAmount.setFrozenAmount(frozenAmountAfter);
            //更新钱包余额
            int count = userAmountService.updateUserAmount(userAmount);
            if (count <= 0){
                throw new ServiceException("系统繁忙");
            }
            //用户流水记录
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userInfo.getId());
            userBillDetail.setDeType("资金冻结");
            userBillDetail.setDeSummary("资金冻结");
            userBillDetail.setOrderAmount(amount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(amountBefore);
            userBillDetail.setAmountAfter(amountAfter);
            userBillDetail.setRelateOrderId(null);
            userBillDetail.setOrderClass(53);
            userBillDetail.setCurrencyId(currencyId);
            int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insert <= 0) {
                throw new ServiceException("系统繁忙");
            }
            //日志记录流水记录
            HttpUtils.getRequestLogParams().put("userBillDetail",JSONObject.toJSONString(userBillDetail));
            //日志记录冻结金额变更前
            HttpUtils.getRequestLogParams().put("frozenAmountBefore",frozenAmountBefore);
            //日志记录冻结金额变更后
            HttpUtils.getRequestLogParams().put("frozenAmountAfter",frozenAmountAfter);
        }else {
            //冻结金额变更前
            BigDecimal frozenAmountBefore = userAmount.getFrozenAmount();
            if (frozenAmountBefore.compareTo(amount) < 0){
                throw new ServiceException("冻结中的金额只有"+frozenAmountBefore);
            }
            //允许解冻金额
            BigDecimal allowUnfreezeAmount =userBillDetailService.getAllowUnfreezeAmountByUserId(userId,currencyId);
            if (allowUnfreezeAmount.compareTo(amount) < 0){
                throw new ServiceException("允许冻结中的金额只有"+allowUnfreezeAmount);
            }
            //冻结金额变更后
            BigDecimal frozenAmountAfter = frozenAmountBefore.subtract(amount);
            //余额变更前
            BigDecimal amountBefore = userAmount.getAmount();
            //余额变更后
            BigDecimal amountAfter = amountBefore.add(amount);
            userAmount.setAmount(amountAfter);
            userAmount.setFrozenAmount(frozenAmountAfter);
            //更新钱包余额
            int count = userAmountService.updateUserAmount(userAmount);
            if (count <= 0){
                throw new ServiceException("系统繁忙");
            }
            //用户流水记录
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userInfo.getId());
            userBillDetail.setDeType("资金解冻");
            userBillDetail.setDeSummary("资金解冻");
            userBillDetail.setOrderAmount(amount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(amountBefore);
            userBillDetail.setAmountAfter(amountAfter);
            userBillDetail.setRelateOrderId(null);
            userBillDetail.setOrderClass(54);
            userBillDetail.setCurrencyId(currencyId);
            int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insert <= 0) {
                throw new ServiceException("系统繁忙");
            }
            //日志记录流水记录
            HttpUtils.getRequestLogParams().put("userBillDetail",JSONObject.toJSONString(userBillDetail));
            //日志记录冻结金额变更前
            HttpUtils.getRequestLogParams().put("frozenAmountBefore",frozenAmountBefore);
            //日志记录冻结金额变更后
            HttpUtils.getRequestLogParams().put("frozenAmountAfter",frozenAmountAfter);
        }
        return 1;
    }

//    /**
//     * 用户实名审核
//     * @param userAuthRecord 用户信息
//     * @return
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public int authApprove(UserAuthRecord userAuthRecord){
//        //用户id
//        Long userId = userAuthRecord.getUserId();
//        //用户信息
//        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
//        if (userInfo == null){
//            throw new ServiceException("获取用户信息异常");
//        }
//        //认证状态
//        Integer authStatus = userAuthRecord.getAuthStatus();
//        //认证等级
//        Integer authLevel = userAuthRecord.getAuthLevel();
//        //如果状态是未认证
//        if (authStatus.equals(-1)){
//            UserAuthRecord search = new UserAuthRecord();
//            search.setUserId(userId);
//            search.setAuthLevel(authLevel);
//            //该用户此等级所有的申请记录
//            List<UserAuthRecord> userAuthRecords = userAuthRecordService.selectUserAuthRecordList(search);
//            if (userAuthRecords.size() == 0){
//                return 1;
//            }
//            int deleteUserAuthRecordByIds = userAuthRecordService.deleteUserAuthRecordByIds(userAuthRecords.stream().map(UserAuthRecord::getId).collect(Collectors.toList()).toArray(new Long[]{}));
//            if (deleteUserAuthRecordByIds != userAuthRecords.size()){
//                throw new ServiceException("系统繁忙");
//            }
//            userInfo.setRealName("");
//            //初级实名认证
//            if (authLevel.equals(0)){
//                userInfo.setAuthStatusJunior(0);
//            }else if (authLevel.equals(1)){
//                //高级实名认证
//                userInfo.setAuthStatusSenior(0);
//            }else {
//                throw new ServiceException("认证等级错误");
//            }
//            //更新用户信息
//            int updateUserInfo = userInfoMapper.updateUserInfo(userInfo);
//            if (updateUserInfo <= 0){
//                throw new ServiceException("系统繁忙");
//            }
//        }else {
//            //用户该等级的实名认证信息
//            UserAuthRecord lastOne = userAuthRecordService.selectLastOne(userId, authLevel);
//            //如果有实名认证申请过
//            if (lastOne.getId() != null){
//                if (lastOne.getId() == null && !lastOne.getId().equals(userAuthRecord.getId())){
//                    throw new ServiceException("系统繁忙");
//                }
//                //更新用户实名认证信息
//                userAuthRecord.setApproveTime(new Date());
//                userAuthRecord.setApproveName(SecurityUtils.getUsername());
//                int updateUserAuthRecord = userAuthRecordService.updateUserAuthRecord(userAuthRecord);
//                if (updateUserAuthRecord <= 0){
//                    throw new ServiceException("系统繁忙");
//                }
//            }else {
//                if (userAuthRecord.getId() != null){
//                    throw new ServiceException("系统繁忙");
//                }
//                //新增用户实名认证信息
//                userAuthRecord.setApproveTime(new Date());
//                userAuthRecord.setApproveName(SecurityUtils.getUsername());
//                int insertUserAuthRecord = userAuthRecordService.insertUserAuthRecord(userAuthRecord);
//                if (insertUserAuthRecord <= 0){
//                    throw new ServiceException("系统繁忙");
//                }
//            }
//            userInfo.setRealName(userAuthRecord.getRealName());
//            //初级实名认证
//            if (authLevel.equals(0)){
//                userInfo.setAuthStatusJunior(authStatus+1);
//            }else if (authLevel.equals(1)){
//                //高级实名认证
//                userInfo.setAuthStatusSenior(authStatus+1);
//            }else {
//                throw new ServiceException("认证等级错误");
//            }
//            //更新用户信息
//            int updateUserInfo = userInfoMapper.updateUserInfo(userInfo);
//            if (updateUserInfo <= 0){
//                throw new ServiceException("系统繁忙");
//            }
//        }
//        return 1;
//    }

    /**
     * 用户实名审核
     * @param userAuthRecord 用户信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int authApprove(UserAuthRecord userAuthRecord){
        //用户id
        Long userId = userAuthRecord.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null){
            throw new ServiceException("获取用户信息异常");
        }
        //认证状态
        Integer authStatus = userAuthRecord.getAuthStatus();
        //认证等级
        Integer authLevel = userAuthRecord.getAuthLevel();
        //如果状态是未认证
        if (authStatus.equals(-1)){
            UserAuthRecord search = new UserAuthRecord();
            search.setUserId(userId);
            search.setAuthLevel(authLevel);
            //该用户此等级所有的申请记录
            List<UserAuthRecord> userAuthRecords = userAuthRecordService.selectUserAuthRecordList(search);
            if (userAuthRecords.size() == 0){
                return 1;
            }
            int deleteUserAuthRecordByIds = userAuthRecordService.deleteUserAuthRecordByIds(userAuthRecords.stream().map(UserAuthRecord::getId).collect(Collectors.toList()).toArray(new Long[]{}));
            if (deleteUserAuthRecordByIds != userAuthRecords.size()){
                throw new ServiceException("系统繁忙");
            }
            userInfo.setRealName("");
            //初级实名认证
            if (authLevel.equals(0)){
                userInfo.setAuthStatusJunior(0);
            }else if (authLevel.equals(1)){
                //高级实名认证
                userInfo.setAuthStatusSenior(0);
            }else {
                throw new ServiceException("认证等级错误");
            }
            //更新用户信息
            int updateUserInfo = userInfoMapper.updateUserInfo(userInfo);
            if (updateUserInfo <= 0){
                throw new ServiceException("系统繁忙");
            }
        }else {
            //用户该等级的实名认证信息
            UserAuthRecord lastOne = userAuthRecordService.selectLastOne(userId, authLevel);
            //如果有实名认证申请过
            if (lastOne.getId() != null){
                if (userAuthRecord.getId() == null || !lastOne.getId().equals(userAuthRecord.getId())){
                    throw new ServiceException("系统繁忙");
                }
                //更新用户实名认证信息
                userAuthRecord.setApproveTime(new Date());
                userAuthRecord.setApproveName(SecurityUtils.getUsername());
                int updateUserAuthRecord = userAuthRecordService.updateUserAuthRecord(userAuthRecord);
                if (updateUserAuthRecord <= 0){
                    throw new ServiceException("系统繁忙");
                }
            }else {
                if (userAuthRecord.getId() != null){
                    throw new ServiceException("系统繁忙");
                }
                //新增用户实名认证信息
                userAuthRecord.setApproveTime(new Date());
                userAuthRecord.setApproveName(SecurityUtils.getUsername());
                int insertUserAuthRecord = userAuthRecordService.insertUserAuthRecord(userAuthRecord);
                if (insertUserAuthRecord <= 0){
                    throw new ServiceException("系统繁忙");
                }
            }
            userInfo.setRealName(userAuthRecord.getRealName());
            //初级实名认证
            if (authLevel.equals(0)){
                userInfo.setAuthStatusJunior(authStatus+1);
            }else if (authLevel.equals(1)){
                //高级实名认证
                userInfo.setAuthStatusSenior(authStatus+1);
            }else {
                throw new ServiceException("认证等级错误");
            }
            //更新用户信息
            int updateUserInfo = userInfoMapper.updateUserInfo(userInfo);
            if (updateUserInfo <= 0){
                throw new ServiceException("系统繁忙");
            }
        }
        return 1;
    }


    /**
     * 用户登陆
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult userLogin(String userAccount,String userPwd){
        //登录ip
        String ip = IpUtils.getIpAddr(HttpUtils.getHttpServletRequest());
        //获取黑名单是否有此ip
        IpBlackList ipBlackList = ipBlackListService.selectIpBlackListByIp(ip);
        if (ipBlackList != null){
            return AjaxResult.error("hint_53","此ip禁止登录");
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
                    return AjaxResult.error("hint_35","该地区被限制登录");
                }
            }
        }
        //香港澳门地区登录限制开关
        Integer restrictAreaGat = switchSetService.selectSwitchStatusById(31L);
        if ((restrictAreaGat != null && restrictAreaGat == 0)){
            String[] privinces = {"香港", "澳门","台湾"};
            for (String privince : privinces) {
                if (addressInfo.contains(privince)) {
                    return AjaxResult.error("hint_35","该地区被限制登录");
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
        UserInfo userInfo = userInfoMapper.userLogin(userAccount);
        if (userInfo != null){
            if (!userInfo.getStatus().equals(0)) {
                return AjaxResult.error("hint_accountLocked","登陆失败, 账户被锁定");
            }
            //验证密码
            //密码错误，则记录错误次数
            String mapKey = "appLoginPwdErrorCount:";
            //密码错误
            if (!SecurityUtils.matchesPassword(userPwd,userInfo.getUserPwd())){
                //获取已登录错误次数
                Integer loginPwdErrorCount = redisCache.getCacheMapValue(mapKey,String.valueOf(userInfo.getId()));
                //记录登录错误次数
                if (loginPwdErrorCount == null){
                    redisCache.setCacheMapValue(mapKey,String.valueOf(userInfo.getId()),1);
                }else {
                    //已经错误次数
                    Integer alreadyWrongCount = loginPwdErrorCount + 1;
                    //登录最多允许错误次数
                    Integer maxLoginErrorCount = CacheUtils.getOtherValueByKey("max_login_error_count", Integer.class);
                    //如果错误次数超过允许错误的次数
                    if (alreadyWrongCount >= maxLoginErrorCount){
                        //禁用相关账号
                        UserInfo userInfoVo = new UserInfo();
                        userInfoVo.setId(userInfo.getId());
                        userInfoVo.setStatus(1);
                        int updateUserInfo = userInfoMapper.updateUserInfo(userInfoVo);
                        if (updateUserInfo <= 0){
                            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
                        }
                        redisCache.deleteCacheMapValue(mapKey,String.valueOf(userInfo.getId()));
                    }else {
                        redisCache.setCacheMapValue(mapKey,String.valueOf(userInfo.getId()),alreadyWrongCount);
                    }
                }
                return AjaxResult.error("hint_loginErrorAccountNotExist","登陆失败，用户名密码错误");
            }else {
                redisCache.deleteCacheMapValue(mapKey,String.valueOf(userInfo.getId()));
            }
        }else {
            return AjaxResult.error("hint_loginErrorAccountNotExist","登陆失败，用户名密码错误");
        }

        //重新获取详细信息
        userInfo = userInfoMapper.selectUserInfoById(userInfo.getId());
        if (Integer.valueOf(1).equals(userInfo.getAccountType()) && !isTouristLoginIpAllowed(ip)) {
            return AjaxResult.error("hint_53","此ip禁止登录");
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

        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        ajax.put("userInfo", userInfo);
        return ajax;
    }

    private boolean isTouristLoginIpAllowed(String ip) {
        String whiteList = CacheUtils.getOtherValueByKey("tourist_login_ip_white_list", String.class);
        if (StringUtils.isEmpty(whiteList)) {
            return true;
        }
        Set<String> allowIps = Arrays.stream(whiteList.split("[,;，；\\s]+"))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet());
        return allowIps.contains(ip);
    }

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPwd 用户密码
     * @param inviteCode 填写的邀请码
     * @param yzmCode 注册验证码
     * @param regType 注册类型 0：手机注册 1：邮箱注册
     * @param uuid uuid
     * @param imgValidateCode 图片验证码
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult reg(String userAccount,String userPwd,String inviteCode,String yzmCode,Integer regType,String uuid,String imgValidateCode){
        //账号最小位数限制
        Integer userAccountMinLimit = CacheUtils.getOtherValueByKey("user_account_min_limit", Integer.class);
        if (userAccountMinLimit != null && userAccount.length() < userAccountMinLimit){
            List<Object> params = new ArrayList<>();
            params.add(userAccountMinLimit);
            return AjaxResult.error("hint_45",params,"登陆账号长度最小为"+userAccountMinLimit);
        }
        //注册验证码开关
        Integer registerValidateCodeSwitch = null;
        //注册验证码模式
        Integer registerValidateCodeModel = null;
        //万能验证码key
        String universalKey = null;
        //注册图片验证码开关
        Integer registerImgValidateSwitch = null;
        //手机注册
        if (regType.equals(0)){
            //验证手机号格式
            String[] split = userAccount.split("-");
            if (split.length != 2 || !StringUtils.isNumeric(split[0]) || !StringUtils.isNumeric(split[1])){
                return AjaxResult.error("hint_41","请输入正确的手机号码");
            }
            //账号最小位数限制
            Integer userPhoneMinLimit = CacheUtils.getOtherValueByKey("user_phone_min_limit", Integer.class);
            if (userPhoneMinLimit != null && split[1].length() < userPhoneMinLimit){
                List<Object> params = new ArrayList<>();
                params.add(userPhoneMinLimit);
                return AjaxResult.error("hint_46",params,"手机号长度最小为"+userPhoneMinLimit);
            }
            registerValidateCodeSwitch = switchSetService.selectSwitchStatusById(3L);
            if (registerValidateCodeSwitch != null && registerValidateCodeSwitch.equals(0)){
                registerValidateCodeModel = switchSetService.selectSwitchStatusById(6L);
                universalKey = "universalKey_mobileRegister";
            }
            registerImgValidateSwitch = switchSetService.selectSwitchStatusById(54L);
        }else {
            //邮箱注册
            //验证邮箱格式
            if (!userAccount.contains("@") || !userAccount.contains(".")){
                return AjaxResult.error("hint_42","请输入正确的邮箱");
            }
            registerValidateCodeSwitch = switchSetService.selectSwitchStatusById(4L);
            if (registerValidateCodeSwitch != null && registerValidateCodeSwitch.equals(0)){
                registerValidateCodeModel = switchSetService.selectSwitchStatusById(7L);
                universalKey = "universalKey_emailRegister";
            }
            registerImgValidateSwitch = switchSetService.selectSwitchStatusById(55L);
        }

        //如果注册图片验证码开关开启
        if (registerImgValidateSwitch.equals(0))
        {
            if (StringUtils.isEmpty(uuid)){
                return AjaxResult.error(HintConstants.SYSTEM_BUSY,"请传入uuid");
            }
            if (StringUtils.isEmpty(imgValidateCode)){
                return AjaxResult.error(HintConstants.PARAM_NULL,"请传入验证码");
            }
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null)
            {
                return AjaxResult.error("hint_60","图片验证码失效");
            }
            if (!captcha.equalsIgnoreCase(imgValidateCode))
            {
                return AjaxResult.error("hint_61","图片验证码错误");
            }
        }

        //如果注册验证码开关开启
        if (registerValidateCodeModel != null){
            //真实验证码模式
            if (registerValidateCodeModel.equals(2)){
                //真实验证码
                String realCode = redisCache.getCacheObject("verificationCode:" + userAccount);
                if (!yzmCode.equals(realCode)){
                    return AjaxResult.error("hint_validateCodeWrong","验证码错误");
                }
            }
            //真实验证码模式+万能验证码模式
            else if(registerValidateCodeModel.equals(3)){
                //万能验证码
                String key = CacheUtils.getOtherValueByKey(universalKey,String.class);
                if (!key.equals(yzmCode)){
                    //万能验证码错误,则核对真实验证码
                    //真实验证码
                    String realCode = redisCache.getCacheObject("verificationCode:" + userAccount);
                    if (!yzmCode.equals(realCode)){
                        return AjaxResult.error("hint_validateCodeWrong","验证码错误");
                    }
                }
            }
            //万能验证码模式
            else if (registerValidateCodeModel.equals(4)){
                //万能验证码
                String key = CacheUtils.getOtherValueByKey(universalKey,String.class);
                if (!key.equals(yzmCode)){
                    return AjaxResult.error("hint_validateCodeWrong","验证码错误");
                }
            }
        }

        // 注册IP
        String ipAddr = IpUtils.getIpAddr();
        // 相同ip最多注册数量
        Integer sameIpMaxRegNum = CacheUtils.getOtherValueByKey("same_ip_max_reg_num", Integer.class);
        if (sameIpMaxRegNum != null && sameIpMaxRegNum > 0) {
            UserInfo userInfo = new UserInfo();
            userInfo.setRegIp(ipAddr);
            List<UserInfo> userInfos = userInfoMapper.selectUserInfoList(userInfo);
            if (userInfos.size() >= sameIpMaxRegNum) {
                throw new LangException("hint_97", "此IP注册数量达到上限");
            }
        }

        //代理id
        Long agentId = null;
        //代理名称
        String agentName = null;
        //代理昵称
        String agentNickName = null;
        //上级用户id
        Long supUserId = null;
        //注册邀请码开关 2 不显示，3 显示且选填，4 显示且必填
        Integer registerInvitationCodSwitch = switchSetService.selectSwitchStatusById(14L);
        // 必填邀请码
        if (registerInvitationCodSwitch.equals(4) && StringUtils.isEmpty(inviteCode)) {
            throw new LangException(HintConstants.PARAM_NULL, "请输入邀请码");
        }
        //如果用户填写了邀请码
        if (StringUtils.isNotEmpty(inviteCode)){
            //如果是用户邀请码
            if (inviteCode.startsWith("U") || inviteCode.startsWith("0")){
                //上级用户信息
                UserInfo supUser = userInfoMapper.selectUserInfoByInviteCode(inviteCode);
                if (supUser == null){
                    return AjaxResult.error("hint_40","注册失败, 无效的邀请码");
                }
                if (supUser.getStatus().equals(1)) {
                    return AjaxResult.error("hint_40","注册失败, 此邀请码账户已被禁用");
                }
                if (supUser.getAccountType().equals(2)){
                    throw new LangException("hint_40","此邀请码是模拟账号邀请码");
                }
                if (supUser.getIsAgent().equals(1)){
                    //是否默认所有会员为代理员开关
                    Integer allUserDefaultAgent = switchSetService.selectSwitchStatusById(53L);
                    if (allUserDefaultAgent == null || !allUserDefaultAgent.equals(0)){
                        return AjaxResult.error("hint_40","注册失败, 无效的邀请码");
                    }
                }
                //如果该用户有代理
                if (supUser.getAgentId() != null){
                    //上级用户的上级代理
                    SysUser supUserAgent = sysUserMapper.selectUserById(supUser.getAgentId());
                    if (supUserAgent != null){
                        if (supUserAgent.getStatus().equals("1")) {
                            return AjaxResult.error("hint_40","此邀请码账户的代理已被禁用");
                        }
                    }
                    agentId = supUserAgent.getUserId();
                    agentName = supUserAgent.getUserName();
                    agentNickName = supUserAgent.getNickName();
                }
                supUserId = supUser.getId();
            }else {
                //如果是代理邀请码
                SysUser agentUser = sysUserMapper.selectUserByInviteCode(inviteCode);
                if (agentUser == null) {
                    return AjaxResult.error("hint_40","注册失败, 无效的邀请码");
                }
                if (agentUser.getStatus().equals("1")) {
                    return AjaxResult.error("hint_40","注册失败, 无效的邀请码");
                }
                agentId = agentUser.getUserId();
                agentName = agentUser.getUserName();
                agentNickName = agentUser.getNickName();
            }
        }

        //获取用户信息
        UserInfo userInfo = userInfoMapper.userLogin(userAccount);
        if (userInfo != null) {
            throw new LangException("hint_47","账号已经注册");
        }
        UserInfo userInfoVo = new UserInfo();
        userInfoVo.setAgentId(agentId);
        userInfoVo.setAgentName(agentName);
        userInfoVo.setAgentNickName(agentNickName);
        if (regType.equals(0)) {
            //账号是否去掉区号开关
            Integer removeAreaCode = switchSetService.selectSwitchStatusById(46L);
            if (removeAreaCode != null && removeAreaCode.equals(0)){
                userInfo = userInfoMapper.userLogin(userAccount.split("-")[1]);
                if (userInfo != null) {
                    return AjaxResult.error("hint_47","账号已经注册");
                }
                userInfoVo.setUserAccount(userAccount.split("-")[1]);
            }else {
                userInfoVo.setUserAccount(userAccount);
            }
            userInfoVo.setPhone(userAccount);
            userAccount = userAccount.split("-")[1];
        }else {
            userInfoVo.setUserAccount(userAccount);
            userInfoVo.setEmail(userAccount);
        }
        userInfoVo.setNickName(userAccount);
        //生成邀请码
        while (true){
            inviteCode = "0"+ CodeUtils.generateInviteCode(CacheUtils.getOtherValueByKey("user_inviteCode_number",Integer.class));
            UserInfo byInviteCode = userInfoMapper.selectUserInfoByInviteCode(inviteCode);
            if (byInviteCode == null){
                break;
            }
        }
        userInfoVo.setInviteCode(inviteCode);
        userInfoVo.setVipLevel(CacheUtils.getOtherValueByKey("new_user_default_vip_level",Integer.class));
        userInfoVo.setUserPwd(SecurityUtils.encryptPassword(userPwd));
        userInfoVo.setSupUserId(supUserId);
        userInfoVo.setAccountType(0);
        userInfoVo.setRegTime(new Date());
        userInfoVo.setRegIp(ipAddr);
        userInfoVo.setRegAddress(IpUtils.getAddressByIp(ipAddr));
//        userInfoVo.setAvatar(siteInfoService.selectSiteInfoById(1L).getSiteLogo());
        int insertCount = userInfoMapper.insertUserInfo(userInfoVo);
        if (insertCount <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("id",userInfoVo.getId());
        //日志记录用户账号
        HttpUtils.getRequestLogParams().put("userAccount",userInfoVo.getUserAccount());
        //如果是用户推荐注册
        if (supUserId != null){
            //记录用户团队等级关系
            int saveUserTeamLevelLine = userTeamLevelLineService.updateUserTeamLevelLine(userInfoVo.getId(),supUserId,0);
            if (saveUserTeamLevelLine <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
        }
        //新用户默认单控状态
        String newUserFastTradeDefaultControl = CacheUtils.getOtherValueByKey("newUser_fastTrade_default_control",String.class);
        if (StringUtils.isNotEmpty(newUserFastTradeDefaultControl)){
            UserFastTradeControl userFastTradeControl = new UserFastTradeControl();
            userFastTradeControl.setUserId(userInfoVo.getId());
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
            userAmount.setUserId(userInfoVo.getId());
            userAmount.setCurrencyId(bonusConfigVo.getCurrencyId());
            userAmount.setAmount(bonusAmount);
            int insertUserAmount = userAmountService.insertUserAmount(userAmount);
            if (insertUserAmount <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
            //用户流水记录
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userInfoVo.getId());
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
            userWinningsChangeRecord.setUserId(userInfoVo.getId());
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
                    "⏰时间：" + userInfoVo.getRegTime() + "\n" +
                    "ID: " + userInfoVo.getUserNo() + "\n" +
                    "用户账号: " + userInfoVo.getUserAccount() + "\n" +
                    "用户昵称: " + userInfoVo.getNickName() + "\n" +
                    "邀请码: " + userInfoVo.getInviteCode() + "\n" +
                    "所属代理: " + userInfoVo.getAgentId() + "/" + userInfoVo.getAgentName() + "\n" +
                    "代理昵称: " + userInfoVo.getAgentNickName() + "\n" +
                    "注册地址: " + userInfoVo.getRegAddress() + "\n" +
                    "已注册成功!";
            TelegramUtils.sendAsyncMessage(telegramMsg, "default", "default");
        }
        return AjaxResult.success();
    }

    /**
     * 获取国际区号列表
     */
    @Override
    @Cacheable(value = "cacheable:phoneAreaCode",key = "'getPhoneAreaCodeList'")
    public List getPhoneAreaCodeList(){
        InputStream resourceAsStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            resourceAsStream = this.getClass().getResourceAsStream("/common/phoneAreaCode.json");
            inputStreamReader = new InputStreamReader(resourceAsStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String result = "";
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                result = result + str;
            }
            JSONArray jsonArray = JSONArray.parseArray(result);
            return jsonArray;
        } catch (IOException e) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取区号列表异常");
        }finally {
            try {
                if (resourceAsStream != null){
                    resourceAsStream.close();
                }
                if (inputStreamReader != null){
                    inputStreamReader.close();
                }
                if (bufferedReader != null){
                    bufferedReader.close();
                }
            }catch (Exception e){

            }
        }
    }

    /**
     * 发送手机验证码
     * @param phone 手机号
     * @return
     */
    @Override
    public int sendPhoneVerificationCode(String phone) {
        try {
            //获取验证码发送格式
            String[] split = phone.split("-");
            //如果以0开头，去掉0
            if (split[1].startsWith("0")){
                split[1] = split[1].substring(1);
            }
            //发送至第三方请求的手机号码
            String phoneNumber = split[0] + split[1];
            //日志记录发送至第三方请求的手机号码
            HttpUtils.getRequestLogParams().put("requestNumber",phoneNumber);
            //发送短信appKey
            String smsAppKey = CacheUtils.getOtherValueByKey("sms_appKey", String.class);
            //秘钥参数
            String[] params = smsAppKey.split(",");
            //验证码
            String code = "";
            if(params.length == 4){
                String smsMethod = params[0];
                if ("1".equals(smsMethod)){
                    code = SmsSendUtils.sendMessage(phoneNumber);
                }else if ("2".equals(smsMethod)){
                    code = SmsSendUtils2.sendSms(phoneNumber);
                }else if ("3".equals(smsMethod)){
                    code = SmsSendUtils3.sendSms(phoneNumber);
                }else {
                    throw new LangException(HintConstants.SYSTEM_BUSY,"请设置正确的验证码发送方式");
                }
            }else {
                throw new LangException(HintConstants.SYSTEM_BUSY,"请更新至最新版");
            }
            //有效时间5分钟
            redisCache.setCacheObject("verificationCode:"+phone,code,10, TimeUnit.MINUTES);
            //日志记录验证码
            HttpUtils.getRequestLogParams().put("验证码",code);
        } catch (Exception e) {
            throw new LangException(HintConstants.SYSTEM_BUSY,e.getMessage());
        }
        return 1;
    }

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @return
     */
    @Override
    public int sendEmailVerificationCode(String email) {
        try {
            String code = SendEmailUtils.sendEmail(email);
            //有效时间5分钟
            redisCache.setCacheObject("verificationCode:"+email,code,10, TimeUnit.MINUTES);
            HttpUtils.getRequestLogParams().put("验证码",code);
        } catch (Exception e) {
            throw new LangException(HintConstants.SYSTEM_BUSY,e.getMessage());
        }
        return 1;
    }

    /**
     * 用户修改头像
     * @param avatar 头像
     * @return
     */
    @Override
    public int modifyAvatar(String avatar){
        //当前登录用户id
        Long userId = SecurityUtils.getUserId();
        UserInfo userInfoVo = new UserInfo();
        userInfoVo.setId(userId);
        userInfoVo.setAvatar(avatar);
        int count = userInfoMapper.updateUserInfo(userInfoVo);
        if (count <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }


    /**
     * 修改用户信息
     * @param nickName 用户昵称
     * @param userAccount 用户账号
     * @return
     */
    @Override
    public int modifyUserInfo(String nickName,String userAccount){
        //是否允许用户自行修改用户信息
        Integer selectSwitchStatusById80 = switchSetService.selectSwitchStatusById(80L);
        if (!selectSwitchStatusById80.equals(0)){
            throw new LangException(HintConstants.SYSTEM_BUSY,"禁止修改信息");
        }
        //当前登录用户id
        Long userId = SecurityUtils.getUserId();
        //账号最小位数限制
        Integer userAccountMinLimit = CacheUtils.getOtherValueByKey("user_account_min_limit", Integer.class);
        if (userAccountMinLimit != null && userAccount.length() < userAccountMinLimit){
            List<Object> param = new ArrayList<>();
            param.add(userAccountMinLimit);
            throw new LangException("hint_45",param,"登陆账号长度最小为"+userAccountMinLimit);
        }
        //根据账号获取用户信息
        UserInfo byUserAccount = userInfoMapper.userLogin(userAccount);
        if (byUserAccount != null && !byUserAccount.getId().equals(userId)){
            throw new LangException("hint_47","账号已存在");
        }

        UserInfo userInfoVo = new UserInfo();
        userInfoVo.setId(userId);
        userInfoVo.setNickName(nickName);
        userInfoVo.setUserAccount(userAccount);
        int count = userInfoMapper.updateUserInfo(userInfoVo);
        if (count <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }

    /**
     * 个人中心面板数据
     * @return
     */
    @Override
    public AjaxResult personCenterPanelData(Long userId){
        if (userId == null) {
            userId = SecurityUtils.getUserId();
        }
        //用户余额折合USDT
        BigDecimal userBalanceConvert = BigDecimal.ZERO;
        //冻结保证金
        BigDecimal allMarginAmount = BigDecimal.ZERO;
        //总浮动盈亏
        BigDecimal profitAndLose = BigDecimal.ZERO;
        //极速交易总盈利
        BigDecimal fastTradeWin = BigDecimal.ZERO;
        //极速交易总亏损
        BigDecimal fastTradeLose = BigDecimal.ZERO;
        //今日浮动盈亏
        BigDecimal profitAndLoseToday = BigDecimal.ZERO;

        //平台币种
        PlatformCurrency platformCurrency = new PlatformCurrency();
        platformCurrency.setStatus(0);
        List<PlatformCurrency> platformCurrencies = platformCurrencyService.selectPlatformCurrencyList(platformCurrency);
        ExchangeRateUtil.fillExchangeRate(platformCurrencies);
        Map<Long, PlatformCurrency> map = platformCurrencies.stream().collect(Collectors.toMap(PlatformCurrency::getId, a -> a));
        //用户所有币种余额
        List<UserAmount> userAmounts = userAmountService.selectUserAmountListByUserId(userId,platformCurrencies);
        for (int i = 0; i < userAmounts.size(); i++) {
            //钱包信息
            UserAmount userAmount = userAmounts.get(i);
            //钱包币种
            Long currencyId = userAmount.getCurrencyId();
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,3L,map).get("exchangeRate");
            //折合USDT
            BigDecimal USDTValue = userAmount.getAmount().multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            userBalanceConvert = userBalanceConvert.add(USDTValue);
        }

        //冻结保证金
        //股票总保证金
        BigDecimal stockMarginAmount = userStockPositionService.getAllMarginAmountAmountByUserId(userId,null);
        //加密货币总保证金
        BigDecimal cryptocurrencyMarginAmount = userCryptocurrencyPositionService.getAllMarginAmountAmountByUserId(userId,null);
        //期货总保证金
        BigDecimal futuresMarginAmount = userFuturesPositionService.getAllMarginAmountAmountByUserId(userId,null);
        //外汇总保证金
        BigDecimal forexPosition = userForexPositionService.getAllMarginAmountAmountByUserId(userId,null);
        //总保证金
        allMarginAmount = stockMarginAmount.add(cryptocurrencyMarginAmount).add(futuresMarginAmount).add(forexPosition);

        //获取已经结算的订单
        FastTradeOrder fastTradeOrder = new FastTradeOrder();
        fastTradeOrder.setUserId(userId);
        fastTradeOrder.setOrderStatus(1);
        List<FastTradeOrder> fastTradeOrders = fastTradeOrderMapper.selectFastTradeOrderList(fastTradeOrder);
        //极速交易盈亏
        profitAndLose = fastTradeOrders.stream().map(FastTradeOrder::getOrderProfit).reduce(BigDecimal.ZERO,BigDecimal::add);
        //
        fastTradeWin = fastTradeOrders.stream().filter(a -> a.getOrderProfit().compareTo(BigDecimal.ZERO) > 0).map(FastTradeOrder::getOrderProfit).reduce(BigDecimal.ZERO,BigDecimal::add);
        //
        fastTradeLose = fastTradeOrders.stream().filter(a -> a.getOrderProfit().compareTo(BigDecimal.ZERO) < 0).map(FastTradeOrder::getOrderProfit).reduce(BigDecimal.ZERO,BigDecimal::add);
        //今日极速交易盈亏
        profitAndLoseToday = fastTradeOrders.stream().filter(a->DateUtils.getDate().equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,a.getOrderTime()))).map(FastTradeOrder::getOrderProfit).reduce(BigDecimal.ZERO,BigDecimal::add);


        Map<String, Object> result = new HashMap<>();
        result.put("userBalanceConvert",userBalanceConvert);
        result.put("allMarginAmount",allMarginAmount);
        result.put("profitAndLose",profitAndLose);
        result.put("profitAndLoseToday",profitAndLoseToday);
        result.put("fastTradeWin",fastTradeWin);
        result.put("fastTradeLose",fastTradeLose);
        return AjaxResult.success(result);
    }

    /**
     * 用户钱包余额
     * @param currencyId 币种id
     * @return
     */
    @Override
    public AjaxResult userBalance(Long currencyId){
        //用户id
        Long userId = UserApiKeyUtils.getUserId();
        //钱包信息列表
        List<UserAmount> result = new ArrayList<>();
        //查询全部
        if (currencyId == null){
            //平台币种
            PlatformCurrency platformCurrency = new PlatformCurrency();
            platformCurrency.setStatus(0);
            List<PlatformCurrency> platformCurrencies = platformCurrencyService.selectPlatformCurrencyList(platformCurrency);
            //map
            Map<Long, PlatformCurrency> userAmountMap = platformCurrencies.stream().collect(Collectors.toMap(a->a.getId(), a -> a));
            List<UserAmount> userAmounts = userAmountService.selectUserAmountListByUserId(userId, platformCurrencies);
            userAmounts.stream().forEach(a->{
                a.setAmount(a.getAmount().setScale(userAmountMap.get(a.getCurrencyId()).getSavaScale(), RoundingMode.DOWN));
            });
            result.addAll(userAmounts);
        }else {
            //查询单个
            UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
            PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
            userAmount.setAmount(userAmount.getAmount().setScale(platformCurrency.getSavaScale(), RoundingMode.DOWN));
            result.add(userAmount);
        }
        return AjaxResult.success(result);
    }

    /**
     * 用户钱包余额折合信息
     * @param isPlatformCurrency 资产类型 0：账户资产 1：两融资产
     * @return
     */
    @Override
    public AjaxResult userBalanceConvert(Integer isPlatformCurrency){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //获取启用的币种
        PlatformCurrency platformCurrency = new PlatformCurrency();
        platformCurrency.setIsPlatformCurrency(isPlatformCurrency);
        platformCurrency.setStatus(0);
        PageHelper.orderBy("sort is null,sort");
        //平台所有启用的币种
        List<PlatformCurrency> platformCurrencies = platformCurrencyService.selectPlatformCurrencyList(platformCurrency);
        PageHelper.clearPage();
        ExchangeRateUtil.fillExchangeRate(platformCurrencies);
        Map<Long, PlatformCurrency> map = platformCurrencies.stream().collect(Collectors.toMap(PlatformCurrency::getId, a -> a));
        //result
        List<Map<String, Object>> result = new ArrayList<>();
        //用户所有币种钱包信息
        List<UserAmount> userAmounts = userAmountService.selectUserAmountListByUserId(userId, platformCurrencies);
        for (int i = 0; i < userAmounts.size(); i++) {
            //钱包信息
            UserAmount userAmount = userAmounts.get(i);
            //币种id
            Long currencyId = userAmount.getCurrencyId();
            //账户资金
            BigDecimal userAmt = userAmount.getAmount();
            //冻结金额
            BigDecimal frozenAmount = userAmount.getFrozenAmount();
            //灵活资金金额
            BigDecimal flexibleInvestmentFunds = userAmount.getFlexibleInvestmentFunds();
            //币种信息
            PlatformCurrency platformCurrencyFrom = map.get(currencyId);
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,3L,map).get("exchangeRate");
            //折合USDT
            BigDecimal USDTValue = userAmt.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //冻结保证金
            BigDecimal marginAmount = BigDecimal.ZERO;
            //是否统一交易币种
            Integer selectSwitchStatusById56 = switchSetService.selectSwitchStatusById(56L);
            //如果是统一交易币种
            if (selectSwitchStatusById56.equals(0)){
                if (currencyId.equals(3L)){
                    BigDecimal marginAmountStock = userStockPositionService.getAllMarginAmountAmountByUserId(userId,null);
                    BigDecimal marginAmountCryptocurrency = userCryptocurrencyPositionService.getAllMarginAmountAmountByUserId(userId,null);
                    BigDecimal marginAmountFutures = userFuturesPositionService.getAllMarginAmountAmountByUserId(userId,null);
                    BigDecimal marginAmountForex = userForexPositionService.getAllMarginAmountAmountByUserId(userId,null);
                    marginAmount = marginAmountStock.add(marginAmountCryptocurrency).add(marginAmountFutures).add(marginAmountForex);
                }
            }else {
                if (currencyId == 1L){
                    marginAmount = userStockPositionService.getAllMarginAmountAmountByUserId(userId,null);
                }else if (currencyId == 2L){
                    marginAmount = userCryptocurrencyPositionService.getAllMarginAmountAmountByUserId(userId,null);
                }else if (currencyId == 7L){
                    marginAmount = userFuturesPositionService.getAllMarginAmountAmountByUserId(userId,null);
                }else if (currencyId == 4L){
                    marginAmount = userForexPositionService.getAllMarginAmountAmountByUserId(userId,null);
                }
            }
            //保留小数位数
            Integer savaScale = platformCurrencyFrom.getSavaScale();
            Map<String, Object> balanceInfoMap = new HashMap<>();
            balanceInfoMap.put("currencyId",platformCurrencyFrom.getId());
            balanceInfoMap.put("currencyName",platformCurrencyFrom.getCurrencyNameLang());
            balanceInfoMap.put("currencyImg",platformCurrencyFrom.getCurrencyImg());
            balanceInfoMap.put("userAmt",userAmt.setScale(savaScale, RoundingMode.DOWN).toPlainString());
            balanceInfoMap.put("USDTValue",USDTValue.setScale(savaScale, RoundingMode.DOWN).toPlainString());
            balanceInfoMap.put("marginAmount",marginAmount.setScale(savaScale, RoundingMode.DOWN).toPlainString());
            balanceInfoMap.put("frozenAmount",frozenAmount.setScale(savaScale, RoundingMode.DOWN).toPlainString());
            balanceInfoMap.put("flexibleInvestmentFunds",flexibleInvestmentFunds.setScale(savaScale, RoundingMode.DOWN).toPlainString());
            result.add(balanceInfoMap);
        }
        return AjaxResult.success(result);
    }

    /**
     * 用户实名认证
     * @param userInfo
     * @return
     */
    @Override
    public int userAuth(UserInfo userInfo){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //用户信息
        UserInfo userInfoVo = userInfoMapper.selectUserInfoById(userId);
        if (userInfoVo == null){
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //实名认证通过后不可修改实名信息开关
        Integer canNotModifiedAuthInfoAfterAuthed = switchSetService.selectSwitchStatusById(19L);
        if (canNotModifiedAuthInfoAfterAuthed.equals(0)){
            if (userInfoVo.getIsActive().equals(2)){
                throw new LangException(HintConstants.SYSTEM_BUSY,"实名认证提交后不允许修改实名信息");
            }
        }
        //实名认证审核中不可修改实名信息开关
        Integer canNotModifiedAuthInfoUnderAuthing = switchSetService.selectSwitchStatusById(59L);
        if (canNotModifiedAuthInfoUnderAuthing.equals(0)){
            if (userInfoVo.getIsActive().equals(1)){
                throw new LangException(HintConstants.SYSTEM_BUSY,"实名认证提交后不允许修改实名信息");
            }
        }
        //如果有填写身份证号码
        if (StringUtils.isNotEmpty(userInfo.getIdCard())){
            //身份证位数限制开关
            Integer idCardNumberDigitLimit = switchSetService.selectSwitchStatusById(17L);
            if (idCardNumberDigitLimit.equals(0)){
                //身份证位数
                Integer digit = CacheUtils.getOtherValueByKey("idCardNumber.digit",Integer.class);
                if (digit != null && userInfo.getIdCard().length() != digit){
                    throw new LangException("hint_correctIDNumberError","请输入正确的身份证号码");
                }
            }
        }

        //是否已实名 0：未实名 1：审核中 2：审核通过 3：审核驳回
        Integer isActive = 1;
        //账号类型： 0：真实用户 1：游客
        Integer accountType = userInfoVo.getAccountType();
        if (accountType.equals(0)){
            //用户实名认证自动通过审核开关
            Integer userAuthAutoApproveSwitch = switchSetService.selectSwitchStatusById(57L);
            if (userAuthAutoApproveSwitch.equals(0)){
                isActive = 2;
            }
        }else if (accountType.equals(1)){
            //游客实名认证自动通过审核开关
            Integer userAuthAutoApproveSwitch = switchSetService.selectSwitchStatusById(65L);
            if (userAuthAutoApproveSwitch.equals(0)){
                isActive = 2;
            }
        }else {
            throw new LangException(HintConstants.SYSTEM_ERR,"用户账号类型异常");
        }

        userInfoVo = new UserInfo();
        userInfoVo.setId(userId);
        userInfoVo.setRealName(userInfo.getRealName());
        userInfoVo.setIdCard(userInfo.getIdCard());
        userInfoVo.setImg1Key(userInfo.getImg1Key());
        userInfoVo.setImg2Key(userInfo.getImg2Key());
        userInfoVo.setImg3Key(userInfo.getImg3Key());
        userInfoVo.setAuthMethod(userInfo.getAuthMethod());
        userInfoVo.setAuthMethod(userInfo.getAuthMethod());
        userInfoVo.setIsActive(isActive);
        int count = userInfoMapper.updateUserInfo(userInfoVo);
        if (count <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }

    /**
     * 修改用户登录密码
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return
     */
    @Override
    public AjaxResult updateUserPwd(String oldPwd, String newPwd){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //获取该用户的原密码
        String userPwd = userInfoMapper.selectUserPwdByUserId(userId);
        if (!SecurityUtils.matchesPassword(oldPwd,userPwd)){
            throw new LangException("hint_pwdWrong","原密码错误");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setUserPwd(SecurityUtils.encryptPassword(newPwd));
        int count = userInfoMapper.updateUserInfo(userInfo);
        if (count <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 查询用户提现密码
     * @param userId 用户id
     * @return
     */
    @Override
    public String selectUserWithPwdByUserId(Long userId){
        return userInfoMapper.selectUserWithPwdByUserId(userId);
    }

    /**
     * 修改用户提现密码
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return
     */
    @Override
    public AjaxResult updateUserWithPwd(String oldPwd, String newPwd){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //获取该用户的原密码
        String userWithPwd = userInfoMapper.selectUserWithPwdByUserId(userId);
        if (StringUtils.isNotEmpty(userWithPwd)){
            if (!SecurityUtils.matchesPassword(oldPwd,userWithPwd)){
                throw new LangException("hint_pwdWrong","原密码错误");
            }
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setWithPwd(SecurityUtils.encryptPassword(newPwd));
        int count = userInfoMapper.updateUserInfo(userInfo);
        if (count <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 变更用户的上级
     * @param supUserId 上级用户id
     * @param userId 用户id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeSuperior(Long supUserId, Long userId){
        return userTeamLevelLineService.updateUserTeamLevelLine(userId, supUserId, 2);
    }

    /**
     * 进入/退出模拟交易模式
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map changeLoginModel(){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //获取关联账号
        UserInfo vo = new UserInfo();
        vo.setRelateUserId(userId);
        List<UserInfo> userInfos = userInfoMapper.selectUserInfoList(vo);
        if (userInfos.size() == 1) {
            //如果已经有关联账号
            vo = userInfos.get(0);
        }else {
            //如果没有关联账号
            //创建关联账号
            //用户信息
            UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
            //new
            UserInfo relateUser = new UserInfo();
            //邀请码
            String inviteCode = CodeUtils.generateInviteCode(CacheUtils.getOtherValueByKey("user_inviteCode_number",Integer.class));
            String userAccount = "demo"+inviteCode;
            relateUser.setUserAccount(userAccount);
            relateUser.setNickName(userAccount);
            relateUser.setUserPwd(SecurityUtils.encryptPassword(CodeUtils.generateOrderCode("pwd")));
            relateUser.setInviteCode("0"+inviteCode);
            relateUser.setAccountType(2);
            relateUser.setAvatar(userInfo.getAvatar());
            relateUser.setRegTime(new Date());
            relateUser.setRelateUserId(userId);
            relateUser.setVipLevel(6);
            int insertUserInfo = userInfoMapper.insertUserInfo(relateUser);
            if (insertUserInfo <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"新增模拟账号异常");
            }
            userInfo.setRelateUserId(relateUser.getId());
            int updateUserInfo = userInfoMapper.updateUserInfo(userInfo);
            if (updateUserInfo <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"更新用户关联账号ID异常");
            }
            Long currencyId = CacheUtil.getOtherValueByKey("default_trade_currency_id", Long.class);
            if (currencyId == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取平台默认交易币种异常");
            }
            //赋予初始金额
            UserAmount userAmount = userAmountService.getUserAmount(relateUser.getId(), currencyId);
            //初始金额
            BigDecimal demoAccountInitBalance = CacheUtil.getOtherValueByKey("demo_account_init_balance", BigDecimal.class);
            if (demoAccountInitBalance == null){
                demoAccountInitBalance = new BigDecimal("10000");
            }
            userAmount.setAmount(demoAccountInitBalance);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"更新模拟用户余额异常");
            }
            //获取模拟账户单控配置
            String allDemoAccountFastTradeControl = CacheUtils.getOtherValueByKey("allDemoAccount_fastTrade_control", String.class);
            if (StringUtils.isNotEmpty(allDemoAccountFastTradeControl)){
                UserFastTradeControl userFastTradeControl = new UserFastTradeControl();
                userFastTradeControl.setControlType(Integer.valueOf(allDemoAccountFastTradeControl));
                userFastTradeControl.setUserId(relateUser.getId());
                userFastTradeControlService.updateUserFastTradeControl(userFastTradeControl);
            }
            vo = relateUser;
        }

        //创建token
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(vo.getId());
        loginUser.setAppUser(vo);
        String token = tokenService.createToken(loginUser);
        //resullt
        Map<Object, Object> result = new HashMap<>();
        result.put(Constants.TOKEN, token);
        result.put("userInfo", vo);
        return result;
    }

    /**
     * 重置模拟账号余额
     * @param userId 用户ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resetDemoAccountBalance(Long userId){
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //如果不是模拟账号
        if (!userInfo.getAccountType().equals(2)){
            //模拟账号ID
            userId = userInfo.getRelateUserId();
            if (userId == null){
                throw new ServiceException("该账号还未绑定模拟账户");
            }
        }
        //平台默认交易币种ID
        Long currencyId = CacheUtil.getOtherValueByKey("default_trade_currency_id", Long.class);
        if (currencyId == null){
            throw new ServiceException("获取平台默认交易币种异常");
        }
        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //初始金额
        BigDecimal demoAccountInitBalance = CacheUtil.getOtherValueByKey("demo_account_init_balance", BigDecimal.class);
        if (demoAccountInitBalance == null){
            demoAccountInitBalance = new BigDecimal("10000");
        }
        //余额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //余额变更后
        BigDecimal userAmountAfter = demoAccountInitBalance;
        //差额
        BigDecimal subtractAmount = userAmountAfter.subtract(userAmountBefore);
        //如果余额相同
        if (subtractAmount.compareTo(BigDecimal.ZERO) == 0){
            return 1;
        }else {
            //更新用户余额
            userAmount.setAmount(demoAccountInitBalance);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"更新模拟用户余额异常");
            }
            Integer direction = subtractAmount.compareTo(BigDecimal.ZERO) > 0 ? 0 : 1;
            //添加账户流水
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            //入款
            if (direction.equals(0)){
                userBillDetail.setOrderClass(2);
                userBillDetail.setDeType("系统入款");
                userBillDetail.setDeSummary("系统入款成功");
            }else {
                userBillDetail.setDeType("系统扣款");
                userBillDetail.setDeSummary("系统扣款成功");
                userBillDetail.setOrderClass(3);
            }
            userBillDetail.setOrderAmount(subtractAmount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(null);
            userBillDetail.setCurrencyId(userAmount.getCurrencyId());
            int count = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (count <= 0) {
                throw new ServiceException("系统繁忙");
            }
            //添加上分下分记录
            UserPointChangeRecord userPointChangeRecord = new UserPointChangeRecord();
            userPointChangeRecord.setUserId(userId);
            userPointChangeRecord.setOrderCode(CodeUtils.generateOrderCode("userPointChangeRecord"));
            userPointChangeRecord.setOrderAmount(subtractAmount);
            userPointChangeRecord.setOrderType(direction);
            userPointChangeRecord.setCreateTime(new Date());
            userPointChangeRecord.setOperatorName(SecurityUtils.getUsername());
            userPointChangeRecord.setCurrencyId(currencyId);
            userPointChangeRecord.setUserAmountBefore(userAmountBefore);
            userPointChangeRecord.setUserAmountAfter(userAmountAfter);
            int insertUserPointChangeRecord = userPointChangeRecordService.insertUserPointChangeRecord(userPointChangeRecord);
            if (insertUserPointChangeRecord <= 0) {
                throw new ServiceException("系统繁忙");
            }

        }
        return 1;
    }

    /**
     * 绑定邮箱
     * @param email 邮箱
     * @return
     */
    public int bindEmail(String email, String yzmCode) {
        //验证email格式
        //验证邮箱格式
        if (!email.contains("@") || !email.contains(".")){
            throw new LangException("hint_42", "请输入正确的邮箱");
        }
        //用户ID
        Long userId = SecurityUtils.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null || !userInfo.getIsDel().equals(0)){
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        if (StringUtils.isNotEmpty(userInfo.getEmail())) {
            throw new LangException(HintConstants.SYSTEM_BUSY, "已绑定过邮箱");
        }
        //真实验证码
        String realCode = redisCache.getCacheObject("verificationCode:" + email);
        if (yzmCode == null) {
            yzmCode = "";
        }
        if (!yzmCode.equals(realCode)){
            throw new LangException("hint_validateCodeWrong","验证码错误");
        }
        userInfo.setEmail(email);
        int updateUserInfo = userInfoMapper.updateUserInfo(userInfo);
        if (updateUserInfo == 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY, "更新用户信息异常");
        }
        return 1;
    }
}
