package com.ruoyi.system.service.impl;


import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.domain.UserCommissionRecord;
import com.ruoyi.system.domain.UserRecharge;
import com.ruoyi.system.domain.UserTeamLevelLine;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.IUserAmountService;
import com.ruoyi.system.service.IUserTeamLevelLineService;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.utils.currencyExchangeRate.ExchangeRateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户团队关系网（用户代理线）Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-09-09
 */
@Service
public class UserTeamLevelLineServiceImpl implements IUserTeamLevelLineService
{
    @Resource
    private UserTeamLevelLineMapper userTeamLevelLineMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserRechargeMapper userRechargeMapper;

    @Resource
    private PlatformCurrencyMapper platformCurrencyMapper;

    @Resource
    private UserCommissionRecordMapper userCommissionRecordMapper;

    @Autowired
    private IUserAmountService userAmountService;
    /**
     * 查询用户团队关系网（用户代理线）
     * 
     * @param id 用户团队关系网（用户代理线）主键
     * @return 用户团队关系网（用户代理线）
     */
    @Override
    public UserTeamLevelLine selectUserTeamLevelLineById(Long id)
    {
        return userTeamLevelLineMapper.selectUserTeamLevelLineById(id);
    }

    /**
     * 查询用户团队关系网（用户代理线）列表
     * 
     * @param userTeamLevelLine 用户团队关系网（用户代理线）
     * @return 用户团队关系网（用户代理线）
     */
    @Override
    public List<UserTeamLevelLine> selectUserTeamLevelLineList(UserTeamLevelLine userTeamLevelLine)
    {
        return userTeamLevelLineMapper.selectUserTeamLevelLineList(userTeamLevelLine);
    }

    /**
     * 新增用户团队关系网（用户代理线）
     * 
     * @param userTeamLevelLine 用户团队关系网（用户代理线）
     * @return 结果
     */
    @Override
    public int insertUserTeamLevelLine(UserTeamLevelLine userTeamLevelLine)
    {
        userTeamLevelLine.setCreateTime(new Date());
        return userTeamLevelLineMapper.insertUserTeamLevelLine(userTeamLevelLine);
    }

    /**
     * 修改用户团队关系网（用户代理线）
     * 
     * @param userTeamLevelLine 用户团队关系网（用户代理线）
     * @return 结果
     */
    @Override
    public int updateUserTeamLevelLine(UserTeamLevelLine userTeamLevelLine)
    {
        return userTeamLevelLineMapper.updateUserTeamLevelLine(userTeamLevelLine);
    }

    /**
     * 批量删除用户团队关系网（用户代理线）
     * 
     * @param ids 需要删除的用户团队关系网（用户代理线）主键
     * @return 结果
     */
    @Override
    public int deleteUserTeamLevelLineByIds(Long[] ids)
    {
        return userTeamLevelLineMapper.deleteUserTeamLevelLineByIds(ids);
    }

    /**
     * 删除用户团队关系网（用户代理线）信息
     * 
     * @param id 用户团队关系网（用户代理线）主键
     * @return 结果
     */
    @Override
    public int deleteUserTeamLevelLineById(Long id)
    {
        return userTeamLevelLineMapper.deleteUserTeamLevelLineById(id);
    }

    /**
     * 更新团队等级关系
     * @param userId 用户id
     * @param supUserId 上级用户id
     * @param updateType 更新类型：0：用户新增更新  1：用户删除更新 2：变更代理线更新
     * @return
     */
    public int updateUserTeamLevelLine(Long userId,Long supUserId,int updateType){
        int count = 0;
        if (updateType == 0){
            count = updateUserTeamLevelLineByInsertUser(userId,supUserId);
        }else if (updateType == 1){
            count = updateUserTeamLevelLineByDeleteUser(userId,supUserId);
        }else if (updateType == 2){
            count = updateUserTeamLevelLineByChangeSuperior(userId,supUserId);
        }
        validate();
        return count;
    }


    /**
     * 新增用户更新团队等级关系
     * @param userId 用户id
     * @param supUserId 上级用户id
     * @return
     */
    int updateUserTeamLevelLineByInsertUser(Long userId,Long supUserId){
        //即将新增的代理线
        List<UserTeamLevelLine> newUserTeamLevelLineList = new ArrayList<>();

        //插入用户与上级的团队等级关系
        UserTeamLevelLine userTeamLevelLine = new UserTeamLevelLine();
        userTeamLevelLine.setUserId(userId);
        userTeamLevelLine.setSupUserId(supUserId);
        userTeamLevelLine.setTeamLevel(1);
        userTeamLevelLine.setCreateTime(new Date());
        newUserTeamLevelLineList.add(userTeamLevelLine);
        //获取上级用户的所有上级线
        List<UserTeamLevelLine> userTeamLevelLines = userTeamLevelLineMapper.getSupTeamLine(supUserId,null,0);
        //插入用户与上级用户的所有上级的团队等级关系
        for (int i = 0; i < userTeamLevelLines.size(); i++) {
            UserTeamLevelLine userTeamLevelLineVo = userTeamLevelLines.get(i);
            userTeamLevelLineVo.setUserId(userId);
            userTeamLevelLineVo.setTeamLevel(userTeamLevelLineVo.getTeamLevel()+1);
            userTeamLevelLineVo.setCreateTime(new Date());
            newUserTeamLevelLineList.add(userTeamLevelLineVo);
        }
        int count = userTeamLevelLineMapper.insertUserTeamLevelLines(newUserTeamLevelLineList);
        if (count != newUserTeamLevelLineList.size()){
            return 0;
        }
        return 1;
    }

    /**
     * 删除用户更新团队等级关系
     * @param userId 用户id
     * @param supUserId 上级用户id
     * @return
     */
    int updateUserTeamLevelLineByDeleteUser(Long userId,Long supUserId){
        //代理id:如果有上级用户(上级无代理则不变，上级有代理，则要把所有下级的代理变成新代理)
        //所有下级团队关系信息
        List<UserTeamLevelLine> lowerTeamLine = userTeamLevelLineMapper.getLowerTeamLine(userId, null, 0);
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //如果有上级用户
        if (supUserId != null){
            //清空用户supUserId;
            userInfo.getParams().put("cleanSupUserId",0);
            int updateUserInfo = userInfoMapper.updateUserInfo(userInfo);
            if (updateUserInfo <= 0){
                throw new ServiceException("系统繁忙");
            }
            //上级用户信息
            UserInfo supUserInfo = userInfoMapper.selectUserInfoById(userInfo.getSupUserId());
            //如果有上级用户，并且上级用户有代理,则将当前用户所有下级的代理信息变更为其上级用户的代理信息
            if (supUserInfo.getAgentId() != null){
                userInfoMapper.replaceAgentIdAndAgentName(userInfo.getAgentId(),supUserInfo.getAgentId(),supUserInfo.getAgentName(),supUserInfo.getNickName(),lowerTeamLine.stream().map(UserTeamLevelLine::getUserId).collect(Collectors.toList()));
            }
        }
        //清空当前用户自身的代理信息
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        userInfoMapper.replaceAgentIdAndAgentName(userInfo.getAgentId(),null,null,null,userIds);
        //直推下级人数
        long directLowersCount = lowerTeamLine.stream().filter(a -> a.getTeamLevel().equals(1)).count();
        //更新用户信息表的上级用户id
        //删除用户后其下级的上级用户id变更为此用户的上级用户id
        int updateSupUserId = userInfoMapper.replaceSupUserId(userId, supUserId);
        if (directLowersCount != updateSupUserId){
            throw new ServiceException("系统繁忙");
        }
        //所有上级团队关系信息
        List<UserTeamLevelLine> supTeamLine = userTeamLevelLineMapper.getSupTeamLine(userId, null, 0);
        //更新团队关系网
        //清空用户的团队关系网
        int cleanUserTeamLevelLineByUserId = userTeamLevelLineMapper.cleanUserTeamLevelLineByUserId(userId);
        //此用户涉及到的关系网数量
        int teamLineInvolvedCount = lowerTeamLine.size() + supTeamLine.size();
        if (teamLineInvolvedCount != cleanUserTeamLevelLineByUserId){
            throw new ServiceException("系统繁忙");
        }
        //下级团队ids
        List<Long> lowerTeamUserIds = lowerTeamLine.stream().map(UserTeamLevelLine::getUserId).collect(Collectors.toList());
        //上级团队ids
        List<Long> supTeamUserIds = supTeamLine.stream().map(UserTeamLevelLine::getSupUserId).collect(Collectors.toList());
        //如果上下都有团队
        if (lowerTeamUserIds.size() != 0 && supTeamUserIds.size() != 0){
            int connectUpperAndLowerTeam = userTeamLevelLineMapper.connectUpperAndLowerTeam(lowerTeamUserIds, supTeamUserIds);
            if (connectUpperAndLowerTeam != lowerTeamUserIds.size()*supTeamUserIds.size()){
                throw new RuntimeException("系统繁忙");
            }
        }
        return 1;
    }

    /**
     * 变更上级更新团队等级关系
     * @param userId 用户id
     * @param supUserId 上级用户id
     * @return
     */
    int updateUserTeamLevelLineByChangeSuperior(Long userId,Long supUserId){
        //验证即将设置的新上级是当前用户自己
        if (userId.equals(supUserId)){
            throw new ServiceException("即将设置的新上级是当前用户自己");
        }
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null || userInfo.getIsDel().equals(1)){
            throw new ServiceException("获取用户信息异常");
        }
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("用户编号", userInfo.getUserNo());
        //日志记录用户账号
        HttpUtils.getRequestLogParams().put("用户账号", userInfo.getUserAccount());
        //上级信息
        UserInfo supUser = userInfoMapper.selectUserInfoById(supUserId);
        if (supUser == null || supUser.getIsDel().equals(1)){
            throw new ServiceException("上级不存在");
        }
        //日志记录上级id
        HttpUtils.getRequestLogParams().put("上级编号", supUser.getUserNo());
        //日志记录上级账号
        HttpUtils.getRequestLogParams().put("上级账号", supUser.getUserAccount());
        //获取上级用户的所有上级团队关系信息
        List<UserTeamLevelLine> supTeamLineBySupUser = userTeamLevelLineMapper.getSupTeamLine(supUserId, null, 0);
        //验证新的上级是否是当前用户的下级
        if (supTeamLineBySupUser.stream().filter(a->a.getSupUserId().equals(userId)).count() > 0){
            throw new ServiceException("即将设置的新上级是当前用户的下级！");
        }
        //更新当前用户的上级用户id
        userInfo.setSupUserId(supUserId);
        int updateUser = userInfoMapper.updateUserInfo(userInfo);
        if (updateUser <= 0){
            throw new ServiceException("系统繁忙");
        }
        //获取当前用户的所有下级团队关系信息
        List<UserTeamLevelLine> lowerTeamLine = userTeamLevelLineMapper.getLowerTeamLine(userId, null, 0);
        //所有下级用户的用户id
        List<Long> lowerTeamUserIds = lowerTeamLine.stream().map(UserTeamLevelLine::getUserId).collect(Collectors.toList());
        //更新自己及所有下级的代理为新上级的代理
        lowerTeamUserIds.add(userId);
        int replaceAgentIdAndAgentName = userInfoMapper.replaceAgentIdAndAgentName(userInfo.getAgentId(), supUser.getAgentId(), supUser.getAgentName(),supUser.getNickName(),lowerTeamUserIds);
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

        //加入上级用户本身
        UserTeamLevelLine supUserTeamLevelLine = new UserTeamLevelLine();
        supUserTeamLevelLine.setSupUserId(supUserId);
        supUserTeamLevelLine.setTeamLevel(0);
        supTeamLineBySupUser.add(supUserTeamLevelLine);
        //当前时间
        Date nowDateTime = new Date();
        //即将新增的团队关系
        List<UserTeamLevelLine> userTeamLevelLines = new ArrayList<>();
        //遍历获取当前用户所有新上级与当前用户和当前用户所有下级的新团队关系
        for (int i = 0; i < supTeamLineBySupUser.size(); i++) {
            //上级信息
            UserTeamLevelLine sup = supTeamLineBySupUser.get(i);
            //获取新上级与原下级的团队关系信息
            for (int j = 0; j < lowerTeamLine.size(); j++) {
                //下级信息
                UserTeamLevelLine low = lowerTeamLine.get(j);
                //new
                UserTeamLevelLine vo = new UserTeamLevelLine();
                vo.setUserId(low.getUserId());
                vo.setSupUserId(sup.getSupUserId());
                vo.setTeamLevel(low.getTeamLevel()+sup.getTeamLevel()+1);
                vo.setCreateTime(nowDateTime);
                userTeamLevelLines.add(vo);
            }
            //获取新上级当前用户的团队关系信息
            UserTeamLevelLine vo = new UserTeamLevelLine();
            vo.setUserId(userId);
            vo.setSupUserId(sup.getSupUserId());
            vo.setTeamLevel(sup.getTeamLevel()+1);
            vo.setCreateTime(nowDateTime);
            userTeamLevelLines.add(vo);
        }
        //插入新团队关系信息
        int insertAgentTeamLevelLines = userTeamLevelLineMapper.insertUserTeamLevelLines(userTeamLevelLines);
        if (insertAgentTeamLevelLines != userTeamLevelLines.size()){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    public void validate(){
        UserInfo userInfo = new UserInfo();
        List<UserInfo> userInfos = userInfoMapper.selectUserInfoList(userInfo);
        //用户信息map
        Map<Long, UserInfo> userMap = userInfos.stream().collect(Collectors.toMap(a -> a.getId(), a -> a));
        for (int i = 0; i < userInfos.size(); i++) {
            //用户id
            Long userId = userInfos.get(i).getId();
            //上级用户id
            Long supUserId = userInfos.get(i).getSupUserId();
            //获取该用户的所有上级团队关系信息
            List<UserTeamLevelLine> supTeamLine = userTeamLevelLineMapper.getSupTeamLine(userId, null, 0);
            for (int j = 0; j < supTeamLine.size(); j++) {
                //上级用户id
                Long supUserIdValidate = supTeamLine.get(j).getSupUserId();
                if (!supUserId.equals(supUserIdValidate)){
                    throw new ServiceException("系统繁忙");
                }
                supUserId = userMap.get(supUserId).getSupUserId();
            }
        }
    }

    /**
     * 查询下级团队信息
     * @param userTeamLevelLine
     * @return
     */
    @Override
    public List<UserInfo> queryLowerTeamInfo(UserTeamLevelLine userTeamLevelLine){
        //用户id
        Long userId = userTeamLevelLine.getUserId();
        //N级团队信息
        List<UserTeamLevelLine> lowerTeamLine = userTeamLevelLineMapper.getLowerTeamLine(userId,  userTeamLevelLine.getTeamLevel(), 1);
        if (lowerTeamLine.size() == 0){
            return new ArrayList<>();
        }
        //下级团队ids
        List<Long> lowerTeamUserIds = lowerTeamLine.stream().map(UserTeamLevelLine::getUserId).collect(Collectors.toList());
        //下级团队信息List
        UserInfo userInfo = new UserInfo();
        Map<String, Object> params = new HashMap<>();
        params.put("userIds",lowerTeamUserIds);
        userInfo.setParams(params);
        List<UserInfo> users = userInfoMapper.selectUserInfoList(userInfo);
        //所有币种
        List<PlatformCurrency> platformCurrencies = platformCurrencyMapper.selectPlatformCurrencyList(new PlatformCurrency());
        ExchangeRateUtil.fillExchangeRate(platformCurrencies);
        Map<Long, PlatformCurrency> map = platformCurrencies.stream().collect(Collectors.toMap(PlatformCurrency::getId, a -> a));
        for (int i = 0; i < users.size(); i++) {
            //用户信息
            UserInfo userInfoVo = users.get(i);
            //用户id
            Long id = userInfoVo.getId();
            //充值金额（折合USDT）
            BigDecimal rechargeAmount = BigDecimal.ZERO;
            //获取其各币种充值金额，折合USDT计算总充值金额
            List<Long> userIds = new ArrayList<>();
            userIds.add(id);
            List<UserRecharge> userRecharges = userRechargeMapper.selectUserRechargeAmountAllCurrency(userIds,null,null);
            for (int j = 0; j < userRecharges.size(); j++) {
                //币种id
                Long currencyId = userRecharges.get(j).getCurrencyId();
                //充值金额
                BigDecimal payAmt = userRecharges.get(j).getRechargeAmount();
                //币种信息
                PlatformCurrency platformCurrencyFrom = map.get(currencyId);
                //USDT币种信息
                PlatformCurrency platformCurrencyTo = map.get(3L);
                if (platformCurrencyFrom != null && platformCurrencyTo != null){
                    //汇率
                    BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,3L,map).get("exchangeRate");
                    //折合USDT
                    BigDecimal USDTValue = payAmt.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    rechargeAmount = rechargeAmount.add(USDTValue);
                }
            }
            userInfoVo.setRechargeAmount(rechargeAmount);

            //用户余额
            BigDecimal userAmount = BigDecimal.ZERO;
            for (int j = 0; j < platformCurrencies.size(); j++) {
                PlatformCurrency platformCurrencyVo = platformCurrencies.get(j);
                //币种id
                Long currencyId = platformCurrencyVo.getId();
                //账户资金
                BigDecimal userAmt = userAmountService.getUserAmount(Long.valueOf(id), currencyId).getAmount();
                //币种信息
                PlatformCurrency platformCurrencyFrom = map.get(currencyId);
                //USDT币种信息
                PlatformCurrency platformCurrencyTo = map.get(3L);
                if (platformCurrencyFrom != null && platformCurrencyTo != null){
                    //汇率
                    BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,3L,map).get("exchangeRate");
                    //折合USDT
                    BigDecimal USDTValue = userAmt.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    userAmount = userAmount.add(USDTValue);
                }
            }
            userInfoVo.setUserAmount(userAmount);
        }
        return users;
    }

    /**
     * 我的团队数据仪表板
     * @param userTeamLevelLine 团队等级关系
     * @return
     */
    @Override
    public Map<String,Object> myTeamDataDashboard(UserTeamLevelLine userTeamLevelLine){
        //筛选开始时间
        Date startTime = userTeamLevelLine.getStartTime();
        //筛选结束时间
        Date endTime = userTeamLevelLine.getEndTime();

        //用户id
        Long userId = userTeamLevelLine.getUserId();
        //团队等级
        Integer systemTeamLevel = CacheUtils.getOtherValueByKey("team_userTeamLevel", Integer.class);
        if (systemTeamLevel == null){
            systemTeamLevel = 0;
        }

        //团队总充值
        BigDecimal teamTotalRechargeAmount = BigDecimal.ZERO;
        //累计佣金
        BigDecimal teamTotalCommissionAmount = BigDecimal.ZERO;
        //首冲人数
        Integer firstRechargePersonNum = 0;
        //直推人数
        Integer teamDirectPersonNum = 0;
        //团队人数
        Integer teamPersonNum = 0;
        //今日充值人数
        Integer rechargePersonNumToday = 0;

        //所有下级
        List<UserTeamLevelLine> lowerTeamLine = userTeamLevelLineMapper.getLowerTeamLine(userId, systemTeamLevel, 0);
        if (lowerTeamLine.size() != 0){
            //所有币种
            List<PlatformCurrency> platformCurrencies = platformCurrencyMapper.selectPlatformCurrencyList(new PlatformCurrency());
            ExchangeRateUtil.fillExchangeRate(platformCurrencies);
            Map<Long, PlatformCurrency> map = platformCurrencies.stream().collect(Collectors.toMap(PlatformCurrency::getId, a -> a));
            //用户ids
            List<Long> userIds = lowerTeamLine.stream().map(UserTeamLevelLine::getUserId).collect(Collectors.toList());
            //团队总充值
            List<UserRecharge> userRecharges = userRechargeMapper.selectUserRechargeAmountAllCurrency(userIds,startTime,endTime);
            for (int j = 0; j < userRecharges.size(); j++) {
                //币种id
                Long currencyId = userRecharges.get(j).getCurrencyId();
                //充值金额
                BigDecimal payAmt = userRecharges.get(j).getRechargeAmount();
                //币种信息
                PlatformCurrency platformCurrencyFrom = map.get(currencyId);
                //USDT币种信息
                PlatformCurrency platformCurrencyTo = map.get(3L);
                if (platformCurrencyFrom != null && platformCurrencyTo != null){
                    //汇率
                    BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,3L,map).get("exchangeRate");
                    //折合USDT
                    BigDecimal USDTValue = payAmt.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    teamTotalRechargeAmount = teamTotalRechargeAmount.add(USDTValue);
                }
            }

            //团队佣金
            List<UserCommissionRecord> userCommissionRecords = userCommissionRecordMapper.selectUserCommissionAmountAllCurrency(userIds,userId,0,startTime,endTime);
            for (int j = 0; j < userCommissionRecords.size(); j++) {
                //币种id
                Long currencyId = userCommissionRecords.get(j).getCurrencyId();
                //充值金额
                BigDecimal commissionAmount = userCommissionRecords.get(j).getCommissionAmount();
                //币种信息
                PlatformCurrency platformCurrencyFrom = map.get(currencyId);
                //USDT币种信息
                PlatformCurrency platformCurrencyTo = map.get(3L);
                if (platformCurrencyFrom != null && platformCurrencyTo != null){
                    //汇率
                    BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,3L,map).get("exchangeRate");
                    //折合USDT
                    BigDecimal USDTValue = commissionAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    teamTotalCommissionAmount = teamTotalCommissionAmount.add(USDTValue);
                }
            }
            //首冲人数
            firstRechargePersonNum = userRechargeMapper.getFirstRechargePersonNum(userIds);
            //今日充值人数
            rechargePersonNumToday = userRechargeMapper.getRechargePersonNumToday(userIds);

            //直推人数
            teamDirectPersonNum = (int)lowerTeamLine.stream().filter(a->a.getTeamLevel().equals(1)).count();
            //团队人数
            teamPersonNum = lowerTeamLine.size();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("teamTotalRechargeAmount",teamTotalRechargeAmount);
        map.put("teamTotalCommissionAmount",teamTotalCommissionAmount);
        map.put("firstRechargePersonNum",firstRechargePersonNum);
        map.put("teamDirectPersonNum",teamDirectPersonNum);
        map.put("teamPersonNum",teamPersonNum);
        map.put("rechargePersonNumToday",rechargePersonNumToday);
        return map;
    }
}
