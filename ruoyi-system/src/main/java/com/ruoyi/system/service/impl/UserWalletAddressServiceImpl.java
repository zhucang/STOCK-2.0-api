package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.domain.UserWalletAddress;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.mapper.UserWalletAddressMapper;
import com.ruoyi.system.service.IPlatformCurrencyService;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserWalletAddressService;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.utils.telegram.TelegramUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户虚拟货币钱包地址Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-29
 */
@Service
public class UserWalletAddressServiceImpl implements IUserWalletAddressService 
{
    @Resource
    private UserWalletAddressMapper userWalletAddressMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private ISwitchSetService switchSetService;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 查询用户虚拟货币钱包地址
     * 
     * @param id 用户虚拟货币钱包地址主键
     * @return 用户虚拟货币钱包地址
     */
    @Override
    @Cacheable(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.ENTITY,key = "#id")
    public UserWalletAddress selectUserWalletAddressById(Long id)
    {
        return userWalletAddressMapper.selectUserWalletAddressById(id);
    }

    /**
     * 查询用户虚拟货币钱包地址列表
     * 
     * @param userWalletAddress 用户虚拟货币钱包地址
     * @return 用户虚拟货币钱包地址
     */
    @Override
    @Cacheable(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.LIST,key = "#userWalletAddress.cacheableKey()")
    public List<UserWalletAddress> selectUserWalletAddressList(UserWalletAddress userWalletAddress)
    {
        return userWalletAddressMapper.selectUserWalletAddressList(userWalletAddress);
    }

    /**
     * 新增用户虚拟货币钱包地址
     * 
     * @param userWalletAddress 用户虚拟货币钱包地址
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.LIST,allEntries = true)
    public int insertUserWalletAddress(UserWalletAddress userWalletAddress)
    {
        //虚拟钱包是否必须上传钱包收款码
        Integer switchStatus101 = switchSetService.selectSwitchStatusById(101L);
        if (switchStatus101.equals(0)){
            if (StringUtils.isEmpty(userWalletAddress.getWalletReceiptQrCode())){
                throw new ServiceException("请上传钱包收款码");
            }
        }
        //用户id
        Long userId = userWalletAddress.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null){
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //获取此用户绑定的所有钱包个数
        int userBindWalletCount = userWalletAddressMapper.getUserBindWalletCount(userId, null,null);
        //单用户钱包地址允许绑定个数
        Integer userMaxWalletAddressCountAllCurrency = CacheUtils.getOtherValueByKey("user_max_wallet_address_count_all_currency", Integer.class);
        if (userMaxWalletAddressCountAllCurrency != null && !userMaxWalletAddressCountAllCurrency.equals(0) && userBindWalletCount >= userMaxWalletAddressCountAllCurrency){
            throw new ServiceException("钱包总绑定数量已达上限");
        }
        //币种id
        Long currencyId = userWalletAddress.getCurrencyId();
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        if (!platformCurrency.getStatus().equals(0)){
            throw new ServiceException("此币种已禁用");
        }
        //钱包地址类型
        String walletAddressType = userWalletAddress.getWalletAddressType();
        //获取此用户绑定的该币种的钱包个数
        int userBindWalletCountByCurrencyId = userWalletAddressMapper.getUserBindWalletCount(userId, currencyId,walletAddressType);
        //单用户单币种钱包地址允许绑定个数
        Integer userMaxWalletAddressCountEveryCurrency = CacheUtils.getOtherValueByKey("user_max_wallet_address_count_every_currency", Integer.class);
        if (userMaxWalletAddressCountEveryCurrency != null && !userMaxWalletAddressCountEveryCurrency.equals(0) && userBindWalletCountByCurrencyId >= userMaxWalletAddressCountEveryCurrency){
            throw new ServiceException("此币种钱包绑定数量已达上限");
        }
        //如果币种有分多种钱包
        if (StringUtils.isNotEmpty(platformCurrency.getWalletAddressType())){
            if (StringUtils.isEmpty(walletAddressType)){
                throw new ServiceException("请选择钱包地址类型");
            }
            //检验钱包地址类型是否正确
            if (!Arrays.asList(platformCurrency.getWalletAddressType().split("/")).contains(walletAddressType)){
                throw new ServiceException("钱包地址类型错误");
            }
        }else {
            userWalletAddress.setWalletAddressType(null);
        }
        //是否设置钱包地址唯一性
        Integer selectSwitchStatus109 = switchSetService.selectSwitchStatusById(109L);
        if (selectSwitchStatus109.equals(0)){
            //检验钱包地址是否已被绑定
            //钱包地址
            String walletAddress = userWalletAddress.getWalletAddress();
            UserWalletAddress search = new UserWalletAddress();
            search.setWalletAddress(walletAddress);
            search.setCurrencyId(currencyId);
            search.setWalletAddressType(StringUtils.isNotEmpty(walletAddressType)?walletAddressType:null);
            List<UserWalletAddress> userWalletAddresses = userWalletAddressMapper.selectUserWalletAddressList(search);
            if (userWalletAddresses.size() > 0){
                UserWalletAddress vo = userWalletAddresses.get(0);
                if (vo.getUserId().equals(userWalletAddress.getUserId())){
                    throw new ServiceException("此账户已经绑定过钱包地址"+platformCurrency.getCurrencyName()+":"+walletAddress);
                }else {
                    throw new ServiceException(platformCurrency.getCurrencyName()+":"+walletAddress+"已被其他账户绑定");
                }
            }
        }
        userWalletAddress.setCreateTime(DateUtils.getNowDate());
        int count = userWalletAddressMapper.insertUserWalletAddress(userWalletAddress);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
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

        //telegram通知（绑定钱包地址）
        Integer switchStatusById134 = switchSetService.selectSwitchStatusById(134L);
        if (switchStatusById134.equals(0)) {
            //telegram消息
            String telegramMsg = "\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89绑定钱包地址\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\n" +
                    "⏰时间：" + userWalletAddress.getCreateTime() + "\n" +
                    "ID: " + userInfo.getUserNo() + "\n" +
                    "用户账号: " + userInfo.getUserAccount() + "\n" +
                    "用户昵称: " + userInfo.getNickName() + "\n" +
                    "用户备注: " + userInfo.getRemark() + "\n" +
                    "邀请码: " + userInfo.getInviteCode() + "\n" +
                    "所属代理: " + userInfo.getAgentId() + "/" + userInfo.getAgentName() + "\n" +
                    "代理昵称: " + userInfo.getAgentNickName() + "\n" +
                    "钱包地址: " + userWalletAddress.getWalletAddress() + "\n" +
                    "钱包币种: " + platformCurrency.getCurrencyName() + (StringUtils.isNotEmpty(walletAddressType) ? walletAddressType : "");
            TelegramUtils.sendAsyncMessage(telegramMsg, "default", "default");
        }
        return 1;
    }

    /**
     * 修改用户虚拟货币钱包地址
     * 
     * @param userWalletAddress 用户虚拟货币钱包地址
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.ENTITY,key = "#userWalletAddress.id"),
            @CacheEvict(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.LIST,allEntries = true)})
    public int updateUserWalletAddress(UserWalletAddress userWalletAddress)
    {
        //虚拟钱包是否必须上传钱包收款码
        Integer switchStatus101 = switchSetService.selectSwitchStatusById(101L);
        if (switchStatus101.equals(0)){
            if (StringUtils.isEmpty(userWalletAddress.getWalletReceiptQrCode())){
                throw new ServiceException("请上传钱包收款码");
            }
        }
        //钱包信息
        UserWalletAddress userWalletAddressVo = userWalletAddressMapper.selectUserWalletAddressById(userWalletAddress.getId());
        //用户id
        Long userId = userWalletAddress.getUserId();
        if (userId != null){
            if (!userWalletAddressVo.getUserId().equals(userId)){
                throw new ServiceException("不允许修改钱包的绑定用户");
            }
        }else {
            userId = userWalletAddressVo.getUserId();
        }
        //币种id
        Long currencyId = userWalletAddress.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        if (!platformCurrency.getStatus().equals(0)){
            throw new ServiceException("此币种已禁用");
        }
        //钱包地址类型
        String walletAddressType = userWalletAddress.getWalletAddressType();
        //如果币种变更
        if (!userWalletAddressVo.getCurrencyId().equals(currencyId) || !(StringUtils.isEmpty(walletAddressType)?"":walletAddressType).equals(StringUtils.isEmpty(userWalletAddressVo.getWalletAddressType())?"":userWalletAddressVo.getWalletAddressType())){
            //获取此用户绑定的该币种的钱包个数
            int userBindWalletCountByCurrencyId = userWalletAddressMapper.getUserBindWalletCount(userId, currencyId,walletAddressType);
            //单用户单币种钱包地址允许绑定个数
            Integer userMaxWalletAddressCountEveryCurrency = CacheUtils.getOtherValueByKey("user_max_wallet_address_count_every_currency", Integer.class);
            if (userMaxWalletAddressCountEveryCurrency != null && !userMaxWalletAddressCountEveryCurrency.equals(0) && userBindWalletCountByCurrencyId >= userMaxWalletAddressCountEveryCurrency){
                throw new ServiceException("此币种钱包绑定数量已达上限");
            }
        }
        //如果币种有分多种钱包
        if (StringUtils.isNotEmpty(platformCurrency.getWalletAddressType())){
            if (StringUtils.isEmpty(walletAddressType)){
                throw new ServiceException("请选择钱包地址类型");
            }
            //检验钱包地址类型是否正确
            if (!Arrays.asList(platformCurrency.getWalletAddressType().split("/")).contains(walletAddressType)){
                throw new ServiceException("钱包地址类型错误");
            }
        }else {
            userWalletAddress.setWalletAddressType("");
        }
        //是否设置钱包地址唯一性
        Integer selectSwitchStatus109 = switchSetService.selectSwitchStatusById(109L);
        if (selectSwitchStatus109.equals(0)){
            //检验钱包地址是否已被绑定
            //钱包地址
            String walletAddress = userWalletAddress.getWalletAddress();
            UserWalletAddress search = new UserWalletAddress();
            search.setWalletAddress(walletAddress);
            search.setCurrencyId(currencyId);
            search.setWalletAddressType(StringUtils.isNotEmpty(walletAddressType)?walletAddressType:null);
            List<UserWalletAddress> userWalletAddresses = userWalletAddressMapper.selectUserWalletAddressList(search);
            if (userWalletAddresses.size() > 0){
                UserWalletAddress vo = userWalletAddresses.get(0);
                if (!vo.getId().equals(userWalletAddress.getId())){
                    if (vo.getUserId().equals(userWalletAddress.getUserId())){
                        throw new ServiceException("此账户已经绑定过钱包地址"+platformCurrency.getCurrencyName()+":"+walletAddress);
                    }else {
                        throw new ServiceException(platformCurrency.getCurrencyName()+":"+walletAddress+"已被其他账户绑定");
                    }
                }
            }
        }
        userWalletAddress.setUpdateTime(DateUtils.getNowDate());
        int count = userWalletAddressMapper.updateUserWalletAddress(userWalletAddress);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 批量删除用户虚拟货币钱包地址
     * 
     * @param ids 需要删除的用户虚拟货币钱包地址主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.LIST,allEntries = true)})
    public int deleteUserWalletAddressByIds(Long[] ids)
    {
        UserWalletAddress search = new UserWalletAddress();
        search.getParams().put("ids",Arrays.asList(ids));
        List<UserWalletAddress> userWalletAddresses = userWalletAddressMapper.selectUserWalletAddressList(search);
        //日志记录钱包信息列表
        HttpUtils.getRequestLogParams().put("JSONArray:userWalletAddresses", JSONObject.toJSONString(userWalletAddresses));
        return userWalletAddressMapper.deleteUserWalletAddressByIds(ids);
    }

    /**
     * 删除用户虚拟货币钱包地址信息
     * 
     * @param id 用户虚拟货币钱包地址主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.LIST,allEntries = true)})
    public int deleteUserWalletAddressById(Long id)
    {
        return userWalletAddressMapper.deleteUserWalletAddressById(id);
    }

    /**
     * 用户添加虚拟货币钱包地址
     *
     * @param userWalletAddress 用户虚拟货币钱包地址
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.LIST,allEntries = true)
    public int userAddWalletAddress(UserWalletAddress userWalletAddress)
    {
        //用户id
        Long userId = userWalletAddress.getUserId();
        //获取此用户绑定的所有钱包个数
        int userBindWalletCount = userWalletAddressMapper.getUserBindWalletCount(userId, null,null);
        //单用户钱包地址允许绑定个数
        Integer userMaxWalletAddressCountAllCurrency = CacheUtils.getOtherValueByKey("user_max_wallet_address_count_all_currency", Integer.class);
        if (userMaxWalletAddressCountAllCurrency != null && !userMaxWalletAddressCountAllCurrency.equals(0) && userBindWalletCount >= userMaxWalletAddressCountAllCurrency){
            throw new LangException("hint_72","钱包总绑定数量已达上限");
        }
        //币种id
        Long currencyId = userWalletAddress.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取币种信息异常");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        if (!platformCurrency.getStatus().equals(0)){
            throw new LangException(HintConstants.SYSTEM_BUSY,"此币种已禁用");
        }
        //钱包地址类型
        String walletAddressType = userWalletAddress.getWalletAddressType();
        //获取此用户绑定的该币种的钱包个数
        int userBindWalletCountByCurrencyId = userWalletAddressMapper.getUserBindWalletCount(userId, currencyId,walletAddressType);
        //单用户单币种钱包地址允许绑定个数
        Integer userMaxWalletAddressCountEveryCurrency = CacheUtils.getOtherValueByKey("user_max_wallet_address_count_every_currency", Integer.class);
        if (userMaxWalletAddressCountEveryCurrency != null && !userMaxWalletAddressCountEveryCurrency.equals(0) && userBindWalletCountByCurrencyId >= userMaxWalletAddressCountEveryCurrency){
            throw new LangException("hint_72","此币种钱包绑定数量已达上限");
        }
        //如果币种有分多种钱包
        if (StringUtils.isNotEmpty(platformCurrency.getWalletAddressType())){
            if (StringUtils.isEmpty(walletAddressType)){
                throw new LangException(HintConstants.SYSTEM_BUSY,"请选择钱包地址类型");
            }
            //检验钱包地址类型是否正确
            if (!Arrays.asList(platformCurrency.getWalletAddressType().split("/")).contains(walletAddressType)){
                throw new LangException(HintConstants.SYSTEM_ERR,"钱包地址类型错误");
            }
        }
        //是否设置钱包地址唯一性
        Integer selectSwitchStatus109 = switchSetService.selectSwitchStatusById(109L);
        if (selectSwitchStatus109.equals(0)){
            //检验钱包地址是否已被绑定
            //钱包地址
            String walletAddress = userWalletAddress.getWalletAddress();
            UserWalletAddress search = new UserWalletAddress();
            search.setWalletAddress(walletAddress);
            search.setCurrencyId(currencyId);
            search.setWalletAddressType(StringUtils.isNotEmpty(walletAddressType)?walletAddressType:null);
            List<UserWalletAddress> userWalletAddresses = userWalletAddressMapper.selectUserWalletAddressList(search);
            if (userWalletAddresses.size() > 0){
                UserWalletAddress vo = userWalletAddresses.get(0);
                if (vo.getUserId().equals(userWalletAddress.getUserId())){
                    List<Object> params = new ArrayList<>();
                    params.add(platformCurrency.getCurrencyName());
                    params.add(walletAddress);
                    throw new LangException("hint_6",params,"此账户已经绑定过钱包地址"+platformCurrency.getCurrencyName()+":"+walletAddress);
                }else {
                    List<Object> params = new ArrayList<>();
                    params.add(platformCurrency.getCurrencyName());
                    params.add(walletAddress);
                    throw new LangException("hint_7",params,platformCurrency.getCurrencyName()+":"+walletAddress+"已被其他账户绑定");
                }
            }
        }
        userWalletAddress.setCreateTime(DateUtils.getNowDate());
        int count = userWalletAddressMapper.insertUserWalletAddress(userWalletAddress);
        if (count <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }

    /**
     * 用户编辑虚拟货币钱包地址
     *
     * @param userWalletAddress 用户虚拟货币钱包地址
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.ENTITY,key = "#userWalletAddress.id"),
            @CacheEvict(value = CacheableKey.USER_WALLET_ADDRESS + CacheableKey.LIST,allEntries = true)})
    public int userModifyWalletAddress(UserWalletAddress userWalletAddress)
    {
        //用户id
        Long userId = userWalletAddress.getUserId();
        //钱包信息
        UserWalletAddress userWalletAddressVo = userWalletAddressMapper.selectUserWalletAddressById(userWalletAddress.getId());
        if (!userWalletAddressVo.getUserId().equals(userId)){
            throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //币种id
        Long currencyId = userWalletAddress.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取币种信息异常");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        if (!platformCurrency.getStatus().equals(0)){
            throw new LangException(HintConstants.SYSTEM_BUSY,"此币种已禁用");
        }
        //钱包地址类型
        String walletAddressType = userWalletAddress.getWalletAddressType();
        //如果币种变更
        if (!userWalletAddressVo.getCurrencyId().equals(userWalletAddress.getCurrencyId()) || !(StringUtils.isEmpty(walletAddressType)?"":walletAddressType).equals(StringUtils.isEmpty(userWalletAddressVo.getWalletAddressType())?"":userWalletAddressVo.getWalletAddressType())){
            //获取此用户绑定的该币种的钱包个数
            int userBindWalletCountByCurrencyId = userWalletAddressMapper.getUserBindWalletCount(userId, currencyId,walletAddressType);
            //单用户单币种钱包地址允许绑定个数
            Integer userMaxWalletAddressCountEveryCurrency = CacheUtils.getOtherValueByKey("user_max_wallet_address_count_every_currency", Integer.class);
            if (userMaxWalletAddressCountEveryCurrency != null && !userMaxWalletAddressCountEveryCurrency.equals(0) && userBindWalletCountByCurrencyId >= userMaxWalletAddressCountEveryCurrency){
                throw new LangException("hint_72","此币种钱包绑定数量已达上限");
            }
        }
        //如果币种有分多种钱包
        if (StringUtils.isNotEmpty(platformCurrency.getWalletAddressType())){
            if (StringUtils.isEmpty(walletAddressType)){
                throw new LangException(HintConstants.SYSTEM_BUSY,"请选择钱包地址类型");
            }
            //检验钱包地址类型是否正确
            if (!Arrays.asList(platformCurrency.getWalletAddressType().split("/")).contains(walletAddressType)){
                throw new LangException(HintConstants.SYSTEM_ERR,"钱包地址类型错误");
            }
        }
        //是否设置钱包地址唯一性
        Integer selectSwitchStatus109 = switchSetService.selectSwitchStatusById(109L);
        if (selectSwitchStatus109.equals(0)){
            //检验钱包地址是否已被绑定
            //钱包地址
            String walletAddress = userWalletAddress.getWalletAddress();
            UserWalletAddress search = new UserWalletAddress();
            search.setWalletAddress(walletAddress);
            search.setCurrencyId(currencyId);
            search.setWalletAddressType(StringUtils.isNotEmpty(walletAddressType)?walletAddressType:null);
            List<UserWalletAddress> userWalletAddresses = userWalletAddressMapper.selectUserWalletAddressList(search);
            if (userWalletAddresses.size() > 0){
                UserWalletAddress vo = userWalletAddresses.get(0);
                if (!vo.getId().equals(userWalletAddress.getId())){
                    if (vo.getUserId().equals(userWalletAddress.getUserId())){
                        List<Object> params = new ArrayList<>();
                        params.add(platformCurrency.getCurrencyName());
                        params.add(walletAddress);
                        throw new LangException("hint_6",params,"此账户已经绑定过钱包地址"+platformCurrency.getCurrencyName()+":"+walletAddress);
                    }else {
                        List<Object> params = new ArrayList<>();
                        params.add(platformCurrency.getCurrencyName());
                        params.add(walletAddress);
                        throw new LangException("hint_7",params,platformCurrency.getCurrencyName()+":"+walletAddress+"已被其他账户绑定");
                    }
                }
            }
        }
        userWalletAddress.setUpdateTime(DateUtils.getNowDate());
        int count = userWalletAddressMapper.updateUserWalletAddress(userWalletAddress);
        if (count <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }
}
