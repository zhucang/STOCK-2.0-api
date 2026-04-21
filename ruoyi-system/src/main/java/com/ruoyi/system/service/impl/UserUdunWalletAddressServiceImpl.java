package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.UserUdunWalletAddress;
import com.ruoyi.system.mapper.UserUdunWalletAddressMapper;
import com.ruoyi.system.service.IUdunRechargeService;
import com.ruoyi.system.service.IUserUdunWalletAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 优盾加密货币钱包信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-05
 */
@Service
public class UserUdunWalletAddressServiceImpl implements IUserUdunWalletAddressService 
{
    @Resource
    private UserUdunWalletAddressMapper userUdunWalletAddressMapper;

    @Autowired
    private IUdunRechargeService udunRechargeService;

    /**
     * 查询优盾加密货币钱包信息
     * 
     * @param id 优盾加密货币钱包信息主键
     * @return 优盾加密货币钱包信息
     */
    @Override
    public UserUdunWalletAddress selectUserUdunWalletAddressById(Long id)
    {
        return userUdunWalletAddressMapper.selectUserUdunWalletAddressById(id);
    }

    /**
     * 查询优盾加密货币钱包信息列表
     * 
     * @param userUdunWalletAddress 优盾加密货币钱包信息
     * @return 优盾加密货币钱包信息
     */
    @Override
    public List<UserUdunWalletAddress> selectUserUdunWalletAddressList(UserUdunWalletAddress userUdunWalletAddress)
    {
        return userUdunWalletAddressMapper.selectUserUdunWalletAddressList(userUdunWalletAddress);
    }

    /**
     * 新增优盾加密货币钱包信息
     * 
     * @param userUdunWalletAddress 优盾加密货币钱包信息
     * @return 结果
     */
    @Override
    public int insertUserUdunWalletAddress(UserUdunWalletAddress userUdunWalletAddress)
    {
        //主币种编号
        String mainCoinType = userUdunWalletAddress.getMainCoinType();
        //钱包地址
        String walletAddress = userUdunWalletAddress.getWalletAddress();
        //币种名称
        String coinName = userUdunWalletAddress.getCoinName();
        //验证是否支持该币种
        List<JSONObject> merchantSupportCoins = udunRechargeService.getMerchantSupportCoins();
        if (merchantSupportCoins.stream().filter(a->mainCoinType.equals(a.getString("mainCoinType")) && coinName.equals(a.getString("name"))).count() <= 0){
            throw new ServiceException("请检查是否支持此币种");
        }
        //搜索已绑定过的该币种的优盾钱包
        UserUdunWalletAddress search = new UserUdunWalletAddress();
        search.setUserId(userUdunWalletAddress.getUserId());
        search.setCoinName(coinName);
        search.setMainCoinType(mainCoinType);
        List<UserUdunWalletAddress> userUdunWalletAddresses = userUdunWalletAddressMapper.selectUserUdunWalletAddressList(search);
        //如果已经绑定此币种编号的钱包
        if (userUdunWalletAddresses.size() > 0){
            throw new ServiceException("该用户已绑定过该币种的钱包");
        }
        //验证钱包唯一性
        search = new UserUdunWalletAddress();
        search.setCoinName(coinName);
        search.setWalletAddress(walletAddress);
        search.setMainCoinType(mainCoinType);
        userUdunWalletAddresses = userUdunWalletAddressMapper.selectUserUdunWalletAddressList(search);
        if (userUdunWalletAddresses.size() > 0){
            UserUdunWalletAddress vo = userUdunWalletAddresses.get(0);
            if (vo.getUserId().equals(userUdunWalletAddress.getUserId())){
                throw new ServiceException("此账户已经绑定过钱包地址"+coinName+":"+walletAddress);
            }else {
                throw new ServiceException(coinName+":"+walletAddress+"已被其他账户绑定");
            }
        }
        userUdunWalletAddress.setCreateTime(DateUtils.getNowDate());
        return userUdunWalletAddressMapper.insertUserUdunWalletAddress(userUdunWalletAddress);
    }

    /**
     * 修改优盾加密货币钱包信息
     * 
     * @param userUdunWalletAddress 优盾加密货币钱包信息
     * @return 结果
     */
    @Override
    public int updateUserUdunWalletAddress(UserUdunWalletAddress userUdunWalletAddress)
    {
        //钱包信息
        UserUdunWalletAddress udunWalletAddressVo = userUdunWalletAddressMapper.selectUserUdunWalletAddressById(userUdunWalletAddress.getId());
        //用户id
        Long userId = userUdunWalletAddress.getUserId();
        if (!udunWalletAddressVo.getUserId().equals(userId)){
            throw new ServiceException("不允许修改钱包的绑定用户");
        }
        //主币种编号
        String mainCoinType = userUdunWalletAddress.getMainCoinType();
        //钱包地址
        String walletAddress = userUdunWalletAddress.getWalletAddress();
        //币种名称
        String coinName = userUdunWalletAddress.getCoinName();
        //验证是否支持该币种
        List<JSONObject> merchantSupportCoins = udunRechargeService.getMerchantSupportCoins();
        if (merchantSupportCoins.stream().filter(a->mainCoinType.equals(a.get("mainCoinType")) && coinName.equals(a.get("name"))).count() <= 0){
            throw new ServiceException("请检查是否支持此币种");
        }
        //搜索已绑定过的该币种的优盾钱包
        UserUdunWalletAddress search = new UserUdunWalletAddress();
        search.setUserId(userUdunWalletAddress.getUserId());
        search.setCoinName(coinName);
        search.setMainCoinType(mainCoinType);
        List<UserUdunWalletAddress> userUdunWalletAddresses = userUdunWalletAddressMapper.selectUserUdunWalletAddressList(search);
        //如果已经绑定此币种编号的钱包
        if (userUdunWalletAddresses.size() > 0){
            UserUdunWalletAddress vo = userUdunWalletAddresses.get(0);
            if (!vo.getId().equals(userUdunWalletAddress.getId())){
                throw new ServiceException("该用户已绑定过该币种的钱包");
            }
        }
        //验证钱包唯一性
        search = new UserUdunWalletAddress();
        search.setCoinName(coinName);
        search.setWalletAddress(walletAddress);
        search.setMainCoinType(mainCoinType);
        userUdunWalletAddresses = userUdunWalletAddressMapper.selectUserUdunWalletAddressList(search);
        if (userUdunWalletAddresses.size() > 0){
            UserUdunWalletAddress vo = userUdunWalletAddresses.get(0);
            if (!vo.getId().equals(userUdunWalletAddress.getId())){
                if (vo.getUserId().equals(userUdunWalletAddress.getUserId())){
                    throw new ServiceException("此账户已经绑定过钱包地址"+coinName+":"+walletAddress);
                }else {
                    throw new ServiceException(coinName+":"+walletAddress+"已被其他账户绑定");
                }
            }
        }
        return userUdunWalletAddressMapper.updateUserUdunWalletAddress(userUdunWalletAddress);
    }

    /**
     * 批量删除优盾加密货币钱包信息
     * 
     * @param ids 需要删除的优盾加密货币钱包信息主键
     * @return 结果
     */
    @Override
    public int deleteUserUdunWalletAddressByIds(Long[] ids)
    {
        UserUdunWalletAddress search = new UserUdunWalletAddress();
        search.getParams().put("ids", Arrays.asList(ids));
        List<UserUdunWalletAddress> userUdunWalletAddress = userUdunWalletAddressMapper.selectUserUdunWalletAddressList(search);
        //日志记录钱包信息列表
        HttpUtils.getRequestLogParams().put("JSONArray:userUdunWalletAddress", JSONObject.toJSONString(userUdunWalletAddress));
        return userUdunWalletAddressMapper.deleteUserUdunWalletAddressByIds(ids);
    }

    /**
     * 删除优盾加密货币钱包信息信息
     * 
     * @param id 优盾加密货币钱包信息主键
     * @return 结果
     */
    @Override
    public int deleteUserUdunWalletAddressById(Long id)
    {
        return userUdunWalletAddressMapper.deleteUserUdunWalletAddressById(id);
    }



    /**
     * 用户添加优盾钱包
     *
     * @param userUdunWalletAddress 优盾加密货币钱包信息
     * @return 结果
     */
    @Override
    public int addUserUdunWalletAddress(UserUdunWalletAddress userUdunWalletAddress)
    {
        userUdunWalletAddress.setCreateTime(DateUtils.getNowDate());
        return userUdunWalletAddressMapper.insertUserUdunWalletAddress(userUdunWalletAddress);
    }
}
