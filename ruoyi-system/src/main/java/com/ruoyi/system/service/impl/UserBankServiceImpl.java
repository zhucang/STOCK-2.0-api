package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.UserBank;
import com.ruoyi.system.mapper.UserBankMapper;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserBankService;
import com.ruoyi.system.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 用户银行卡Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-09
 */
@Service
public class UserBankServiceImpl implements IUserBankService 
{
    @Resource
    private UserBankMapper userBankMapper;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 查询用户银行卡
     * 
     * @param id 用户银行卡主键
     * @return 用户银行卡
     */
    @Override
    @Cacheable(value = CacheableKey.USER_BANK + CacheableKey.ENTITY,key = "#id")
    public UserBank selectUserBankById(Long id)
    {
        return userBankMapper.selectUserBankById(id);
    }

    /**
     * 查询用户银行卡列表
     * 
     * @param userBank 用户银行卡
     * @return 用户银行卡
     */
    @Override
    @Cacheable(value = CacheableKey.USER_BANK + CacheableKey.LIST,key = "#userBank.cacheableKey()")
    public List<UserBank> selectUserBankList(UserBank userBank)
    {
        return userBankMapper.selectUserBankList(userBank);
    }

    /**
     * 新增用户银行卡
     * 
     * @param userBank 用户银行卡
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.USER_BANK + CacheableKey.LIST,allEntries = true)
    public int insertUserBank(UserBank userBank)
    {
        //银行卡号
        String bankNo = userBank.getBankNo();
        //是否设置银行卡号唯一性
        Integer selectSwitchStatus110 = switchSetService.selectSwitchStatusById(110L);
        if (selectSwitchStatus110.equals(0)){
            if (StringUtils.isNotEmpty(bankNo)){
                UserBank search = new UserBank();
                search.setBankNo(bankNo);
                List<UserBank> userBanks = userBankMapper.selectUserBankList(search);
                if (userBanks.size() > 0){
                    throw new ServiceException("银行信息已存在，不要重复添加");
                }
            }
        }
        //用户id
        Long userId = userBank.getUserId();
        //用户信息
        UserInfo userInfo = userInfoService.selectUserInfoById(userId);
        //是否开启初级实名认证
        Integer selectSwitchStatusById68 = switchSetService.selectSwitchStatusById(68L);
//        //如果用户未进行初级实名认证
//        if (selectSwitchStatusById68.equals(0) && !userInfo.getAuthStatusJunior().equals(2)){
//            throw new ServiceException("请先完成初级实名认证");
//        }
        //是否开启高级实名认证
        Integer selectSwitchStatusById75 = switchSetService.selectSwitchStatusById(75L);
//        //如果用户未进行高级实名认证
//        if (selectSwitchStatusById75.equals(0) && !userInfo.getAuthStatusSenior().equals(2)){
//            throw new ServiceException("请先完成高级实名认证");
//        }
        //如果有开启实名认证
        if (selectSwitchStatusById75.equals(0) || selectSwitchStatusById68.equals(0)) {
            //是否要求银行卡持有人名称与实名认证真实姓名相符
            Integer selectSwitchStatusById = switchSetService.selectSwitchStatusById(77L);
            if (selectSwitchStatusById.equals(0)){
                if (StringUtils.isEmpty(userInfo.getRealName())){
                    throw new ServiceException("请先完成实名认证");
                }
                if (!userBank.getHolder().equals(userInfo.getRealName())){
                    throw new ServiceException("银行卡持有人名称与实名认证真实姓名不符");
                }
            }
        }
        //是否允许绑定多张银行卡开关
        Integer switchStatus = switchSetService.selectSwitchStatusById(48L);
        //如果不允许绑定多张
        if (!switchStatus.equals(0)){
            //用户已拥有银行卡数量
            int userBankCardCount = userBankMapper.getUserBankCardCountByUserId(userId);
            if (userBankCardCount > 0){
                throw new RuntimeException("最多允许绑定一张银行卡");
            }
        }
        userBank.setCreateTime(DateUtils.getNowDate());
        int count = userBankMapper.insertUserBank(userBank);
        if (count <= 0){
            throw new RuntimeException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改用户银行卡
     * 
     * @param userBank 用户银行卡
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.USER_BANK + CacheableKey.ENTITY,key = "#userBank.id"),
            @CacheEvict(value = CacheableKey.USER_BANK + CacheableKey.LIST,allEntries = true)})
    public int updateUserBank(UserBank userBank)
    {
        //银行卡号
        String bankNo = userBank.getBankNo();
        //是否设置银行卡号唯一性
        Integer selectSwitchStatus110 = switchSetService.selectSwitchStatusById(110L);
        if (selectSwitchStatus110.equals(0)){
            if (StringUtils.isNotEmpty(bankNo)){
                UserBank search = new UserBank();
                search.setBankNo(bankNo);
                List<UserBank> userBanks = userBankMapper.selectUserBankList(search);
                if (userBanks.size() > 0){
                    UserBank vo = userBanks.get(0);
                    if (!userBank.getId().equals(vo.getId())){
                        throw new ServiceException("银行信息已存在，不要重复添加");
                    }
                }
            }
        }
        //用户id
        Long userId = userBank.getUserId();
        //用户信息
        UserInfo userInfo = userInfoService.selectUserInfoById(userId);
        //是否开启初级实名认证
        Integer selectSwitchStatusById68 = switchSetService.selectSwitchStatusById(68L);
//        //如果用户未进行初级实名认证
//        if (selectSwitchStatusById68.equals(0) && !userInfo.getAuthStatusJunior().equals(2)){
//            throw new ServiceException("请先完成初级实名认证");
//        }
        //是否开启高级实名认证
        Integer selectSwitchStatusById75 = switchSetService.selectSwitchStatusById(75L);
//        //如果用户未进行高级实名认证
//        if (selectSwitchStatusById75.equals(0) && !userInfo.getAuthStatusSenior().equals(2)){
//            throw new ServiceException("请先完成高级实名认证");
//        }
        //如果有开启实名认证
        if (selectSwitchStatusById75.equals(0) || selectSwitchStatusById68.equals(0)) {
            //是否要求银行卡持有人名称与实名认证真实姓名相符
            Integer selectSwitchStatusById = switchSetService.selectSwitchStatusById(77L);
            if (selectSwitchStatusById.equals(0)){
                if (StringUtils.isEmpty(userInfo.getRealName())){
                    throw new ServiceException("请先完成实名认证");
                }
                if (!userBank.getHolder().equals(userInfo.getRealName())){
                    throw new ServiceException("银行卡持有人名称与实名认证真实姓名不符");
                }
            }
        }
        return userBankMapper.updateUserBank(userBank);
    }

    /**
     * 批量删除用户银行卡
     * 
     * @param ids 需要删除的用户银行卡主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.USER_BANK + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.USER_BANK + CacheableKey.LIST,allEntries = true)})
    public int deleteUserBankByIds(Long[] ids)
    {
        UserBank search = new UserBank();
        search.getParams().put("ids", Arrays.asList(ids));
        List<UserBank> userBanks = userBankMapper.selectUserBankList(search);
        //日志记录用户银行卡信息
        HttpUtils.getRequestLogParams().put("JSONArray:userBanks", JSONObject.toJSONString(userBanks));
        return userBankMapper.deleteUserBankByIds(ids);
    }

    /**
     * 删除用户银行卡信息
     * 
     * @param id 用户银行卡主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.USER_BANK + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.USER_BANK + CacheableKey.LIST,allEntries = true)})
    public int deleteUserBankById(Long id)
    {
        //银行卡信息
        UserBank userBank = userBankMapper.selectUserBankById(id);
        if (userBank == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取银行卡信息异常");
        }
        if (!SecurityUtils.getUserId().equals(userBank.getUserId())){
            throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //日志记录银行卡信息
        HttpUtils.getRequestLogParams().put("userBank", JSONObject.toJSONString(userBank));
        return userBankMapper.deleteUserBankById(id);
    }

    /**
     * 用户添加银行卡
     *
     * @param userBank 用户银行卡
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.USER_BANK + CacheableKey.LIST,allEntries = true)
    public int addUserBankCard(UserBank userBank)
    {
        //银行卡号
        String bankNo = userBank.getBankNo();
        //是否设置银行卡号唯一性
        Integer selectSwitchStatus110 = switchSetService.selectSwitchStatusById(110L);
        if (selectSwitchStatus110.equals(0)){
            if (StringUtils.isNotEmpty(bankNo)){
                UserBank search = new UserBank();
                search.setBankNo(bankNo);
                List<UserBank> userBanks = userBankMapper.selectUserBankList(search);
                if (userBanks.size() > 0){
                    throw new LangException("hint_BankInformationAlreadyExists","银行信息已存在，不要重复添加");
                }
            }
        }
        //用户id
        Long userId = userBank.getUserId();
        //用户信息
        UserInfo userInfo = userInfoService.selectUserInfoById(userId);
        //是否开启初级实名认证
        Integer selectSwitchStatusById68 = switchSetService.selectSwitchStatusById(68L);
//        //如果用户未进行初级实名认证
//        if (selectSwitchStatusById68.equals(0) && !userInfo.getAuthStatusJunior().equals(2)){
//            throw new LangException(HintConstants.AUTH_FIRST_JUNIOR,"请先完成初级实名认证");
//        }
        //是否开启高级实名认证
        Integer selectSwitchStatusById75 = switchSetService.selectSwitchStatusById(75L);
//        //如果用户未进行高级实名认证
//        if (selectSwitchStatusById75.equals(0) && !userInfo.getAuthStatusSenior().equals(2)){
//            throw new LangException(HintConstants.AUTH_FIRST_SENIOR,"请先完成高级实名认证");
//        }
        //如果有开启实名认证
        if (selectSwitchStatusById75.equals(0) || selectSwitchStatusById68.equals(0)) {
            //是否要求银行卡持有人名称与实名认证真实姓名相符
            Integer selectSwitchStatusById = switchSetService.selectSwitchStatusById(77L);
            if (selectSwitchStatusById.equals(0)){
                if (StringUtils.isEmpty(userInfo.getRealName())){
                    throw new LangException(HintConstants.AUTH_FIRST,"请先完成实名认证");
                }
                if (!userBank.getHolder().equals(userInfo.getRealName())){
                    throw new LangException("hint_70","银行卡持有人名称与实名认证真实姓名不符");
                }
            }
        }
        //是否允许绑定多张银行卡开关
        Integer switchStatus = switchSetService.selectSwitchStatusById(48L);
        //如果不允许绑定多张
        if (!switchStatus.equals(0)){
            //用户已拥有银行卡数量
            int userBankCardCount = userBankMapper.getUserBankCardCountByUserId(userId);
            if (userBankCardCount > 0){
               throw new LangException("hint_55","最多允许绑定一张银行卡");
            }
        }
        userBank.setCreateTime(DateUtils.getNowDate());
        int count = userBankMapper.insertUserBank(userBank);
        if (count <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }

    /**
     * 用户修改银行卡
     *
     * @param userBank 用户银行卡
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.USER_BANK + CacheableKey.ENTITY,key = "#userBank.id"),
            @CacheEvict(value = CacheableKey.USER_BANK + CacheableKey.LIST,allEntries = true)})
    public int updateUserBankCard(UserBank userBank){
        //是否允许用户修改银行卡信息
        Integer switchStatus = switchSetService.selectSwitchStatusById(5L);
        if (!switchStatus.equals(0)){
            throw new LangException("hint_updateBankContactCustomerService","如需修改银行卡信息请联系客服");
        }
        //银行卡号
        String bankNo = userBank.getBankNo();
        //是否设置银行卡号唯一性
        Integer selectSwitchStatus110 = switchSetService.selectSwitchStatusById(110L);
        if (selectSwitchStatus110.equals(0)){
            if (StringUtils.isNotEmpty(bankNo)){
                UserBank search = new UserBank();
                search.setBankNo(bankNo);
                List<UserBank> userBanks = userBankMapper.selectUserBankList(search);
                if (userBanks.size() > 0){
                    UserBank vo = userBanks.get(0);
                    if (!userBank.getId().equals(vo.getId())){
                        throw new LangException("hint_BankInformationAlreadyExists","银行信息已存在，不要重复添加");
                    }
                }
            }
        }
        //用户id
        Long userId = userBank.getUserId();
        //用户信息
        UserInfo userInfo = userInfoService.selectUserInfoById(userId);
        //是否开启初级实名认证
        Integer selectSwitchStatusById68 = switchSetService.selectSwitchStatusById(68L);
//        //如果用户未进行初级实名认证
//        if (selectSwitchStatusById68.equals(0) && !userInfo.getAuthStatusJunior().equals(2)){
//            throw new LangException(HintConstants.AUTH_FIRST_JUNIOR,"请先完成初级实名认证");
//        }
        //是否开启高级实名认证
        Integer selectSwitchStatusById75 = switchSetService.selectSwitchStatusById(75L);
//        //如果用户未进行高级实名认证
//        if (selectSwitchStatusById75.equals(0) && !userInfo.getAuthStatusSenior().equals(2)){
//            throw new LangException(HintConstants.AUTH_FIRST_SENIOR,"请先完成高级实名认证");
//        }
        //如果有开启实名认证
        if (selectSwitchStatusById75.equals(0) || selectSwitchStatusById68.equals(0)) {
            //是否要求银行卡持有人名称与实名认证真实姓名相符
            Integer selectSwitchStatusById = switchSetService.selectSwitchStatusById(77L);
            if (selectSwitchStatusById.equals(0)){
                if (StringUtils.isEmpty(userInfo.getRealName())){
                    throw new LangException(HintConstants.AUTH_FIRST,"请先完成实名认证");
                }
                if (!userBank.getHolder().equals(userInfo.getRealName())){
                    throw new LangException("hint_70","银行卡持有人名称与实名认证真实姓名不符");
                }
            }
        }
        int count = userBankMapper.updateUserBank(userBank);
        if (count <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }

}
