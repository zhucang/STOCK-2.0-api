package com.ruoyi.system.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.UserBillDetail;
import com.ruoyi.system.domain.UserDmAmountChangeRecord;
import com.ruoyi.system.domain.UserRecharge;
import com.ruoyi.system.domain.UserUdunWalletAddress;
import com.ruoyi.system.domain.vo.UdunRechargeOrder;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.mapper.UserRechargeMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.utils.udun.UdunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UdunRechargeServiceImpl implements IUdunRechargeService {

    @Autowired
    private IUserUdunWalletAddressService userUdunWalletAddressService;

    @Autowired
    private IUserAmountService userAmountService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IUserDmAmountChangeRecordService userDmAmountChangeRecordService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Resource
    private UserRechargeMapper userRechargeMapper;

    /**
     * 查询udun充值商户支持币种
     * @return
     */
    @Override
    public List getMerchantSupportCoins(){
        //币种
        List<SysDictData> udunCoin = DictUtils.getDictCache("udun_coin");
        //result
        List<JSONObject> result = udunCoin.stream().map(a -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", a.getDictLabel());
            jsonObject.put("coinType", a.getDictValue().split(",")[0]);
            jsonObject.put("mainCoinType", a.getDictValue().split(",")[1]);
            jsonObject.put("platFormCurrencyId", a.getDictValue().split(",")[2]);
            return jsonObject;
        }).collect(Collectors.toList());
        return result;
    }


    /**
     * 获取优盾支付钱包地址
     * @param mainCoinType 币种编号
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserUdunWalletAddress getPayWalletAddress(String mainCoinType,String name){
        //日志记录主币种编号
        HttpUtils.getRequestLogParams().put("主币种编号",mainCoinType);
        //用户id
        Long userId = SecurityUtils.getUserId();
        //验证是否支持该币种
        List<JSONObject> merchantSupportCoins = getMerchantSupportCoins();
        if (merchantSupportCoins.stream().filter(a->mainCoinType.equals(a.getString("mainCoinType")) && name.equals(a.getString("name"))).count() <= 0){
            throw new LangException("hint_67", "不支持此币种");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("币种名称",name);
        //搜索已绑定过的该币种的优盾钱包
        UserUdunWalletAddress search = new UserUdunWalletAddress();
        search.setUserId(userId);
        search.setCoinName(name);
        search.setMainCoinType(mainCoinType);
        List<UserUdunWalletAddress> userUdunWalletAddresses = userUdunWalletAddressService.selectUserUdunWalletAddressList(search);
        //如果还未绑定此币种编号的钱包
        if (userUdunWalletAddresses.size() <= 0){
            //生成钱包地址
            //钱包地址
            String address;
            try {
                //回调URL
                String callUrl = HttpUtils.getHttpServletRequest().getRequestURL().toString().replace("http://", "https://").replace("getPayWalletAddress", "udunRechargeCallUrl");
                //获取在线支付的钱包地址
                address = UdunUtils.createAddress(mainCoinType, callUrl);
                //日志记录回调URL
                HttpUtils.getRequestLogParams().put("回调URL",callUrl);
            }catch (Exception e){
                throw new LangException(HintConstants.SYSTEM_BUSY,e.getMessage());
            }
            UserUdunWalletAddress userUdunWalletAddress = new UserUdunWalletAddress();
            userUdunWalletAddress.setUserId(userId);
            userUdunWalletAddress.setMainCoinType(mainCoinType);
            userUdunWalletAddress.setCoinName(name);
            userUdunWalletAddress.setWalletAddress(address);
            userUdunWalletAddress.setCreateTime(new Date());
            int insertUserUdunWalletAddress = userUdunWalletAddressService.addUserUdunWalletAddress(userUdunWalletAddress);
            if (insertUserUdunWalletAddress <= 0){
                throw new LangException(HintConstants.SYSTEM_BUSY,"新增用户优盾钱包异常");
            }
            //日志记录钱包地址
            HttpUtils.getRequestLogParams().put("钱包地址",address);
            return userUdunWalletAddress;

        }else {
            UserUdunWalletAddress userUdunWalletAddress = userUdunWalletAddresses.get(0);
            //日志记录钱包地址
            HttpUtils.getRequestLogParams().put("钱包地址",userUdunWalletAddress.getWalletAddress());
            return userUdunWalletAddress;
        }
    }

    /**
     * 优盾支付成功回调
     * @param udunRechargeOrder
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void udunRechargeCallUrl(UdunRechargeOrder udunRechargeOrder){
        //body参数
        String bodyString = udunRechargeOrder.getBody();
        //body参数
        JSONObject body = JSONObject.parseObject(bodyString);
        //充值状态 3：成功 4：失败
        String status = body.getString("status");
        //交易类型 1：充币 2：提币
        String tradeType = body.getString("tradeType");
        //如果不是充币
        if (!"1".equals(tradeType)){
            throw new ServiceException("交易类型不是充币");
        }
        //币种类型
        String coinType = body.getString("coinType");
        //主币种编号
        String mainCoinType = body.getString("mainCoinType");
        //钱包地址
        String address = body.getString("address");
        //精度
        Integer decimals = Integer.valueOf(body.getString("decimals"));
        //金额
        BigDecimal amount = new BigDecimal(body.getString("amount")).divide(new BigDecimal(10).pow(decimals),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
//        //手续费
//        BigDecimal fee = new BigDecimal(body.getString("fee")).divide(new BigDecimal(10).pow(decimals),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //交易id
        String tradeId = body.getString("tradeId");

        //商户支持币种列表
        List<JSONObject> merchantSupportCoins = getMerchantSupportCoins();
        //商户支持币种map
        Map<String, JSONObject> map = merchantSupportCoins.stream().collect(Collectors.toMap(a -> a.getString("coinType") + "," + a.getString("mainCoinType"), a -> a));
        //充值币种信息
        JSONObject rechargeCurrencyInfo = map.get(coinType + "," + mainCoinType);
        if (rechargeCurrencyInfo == null){
            throw new ServiceException("请检查是否支持此币种");
        }
        //币种名称
        String coinName = rechargeCurrencyInfo.getString("name");

        //日志记录详情
        HttpUtils.getRequestLogParams().put("充值状态","3".equals(status) ? "充值成功":"充值失败");
        HttpUtils.getRequestLogParams().put("币种类型",coinType);
        HttpUtils.getRequestLogParams().put("主币种编号",mainCoinType);
        HttpUtils.getRequestLogParams().put("币种名称",coinName);
        HttpUtils.getRequestLogParams().put("钱包地址",address);
        HttpUtils.getRequestLogParams().put("金额",amount.stripTrailingZeros().toPlainString());
//        HttpUtils.getRequestLogParams().put("手续费",amount.stripTrailingZeros().toPlainString());
        HttpUtils.getRequestLogParams().put("交易ID",tradeId);

        UserRecharge searchRechargeOrder = new UserRecharge();
        searchRechargeOrder.setRemark("交易ID："+tradeId);
        List<UserRecharge> userRecharges = userRechargeMapper.selectUserRechargeList(searchRechargeOrder);
        if (userRecharges.size() > 0){
            throw new ServiceException("无需重复回调");
        }

        //获取该钱包地址绑定的用户
        UserUdunWalletAddress search = new UserUdunWalletAddress();
        search.setWalletAddress(address);
        search.setCoinName(coinName);
        search.setMainCoinType(mainCoinType);
        List<UserUdunWalletAddress> userUdunWalletAddresses = userUdunWalletAddressService.selectUserUdunWalletAddressList(search);
        if (userUdunWalletAddresses.size() <= 0){
            throw new ServiceException("该钱包地址未绑定用户");
        }
        //用户id
        Long userId = userUdunWalletAddresses.get(0).getUserId();
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("userId",userId);

        //如果充值成功
        if ("3".equals(status)){
            //时间戳
            String timestamp = udunRechargeOrder.getTimestamp();
            //随机数
            String nonce = udunRechargeOrder.getNonce();
            //签名
            String sign = udunRechargeOrder.getSign();
            //商户秘钥
            String merchantKey = UdunUtils.merchantKey;
            if (merchantKey == null){
                UdunUtils.init();
                merchantKey = UdunUtils.merchantKey;
            }
            if (!sign.equals(SecureUtil.md5(bodyString + merchantKey + nonce + timestamp).toLowerCase())){
                throw new ServiceException("sign不对应");
            }
            //平台币种id
            Long currencyId = null;
            //币种名称
            String currencyName = null;
            try {
                //平台币种id
                currencyId = rechargeCurrencyInfo.getLong("platFormCurrencyId");
                if (currencyId == null){
                    throw new ServiceException();
                }
                //币种名称
                currencyName = rechargeCurrencyInfo.getString("name");
                if (StringUtils.isEmpty(currencyName)){
                    throw new ServiceException();
                }
            }catch (Exception e){
                throw new ServiceException("获取充值币种信息异常，请检查是否正常配置");
            }
            //更新用户钱包余额
            UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
            //余额变更钱
            BigDecimal userAmountBefore = userAmount.getAmount();
            //实际金额
//            BigDecimal realAmount = amount.subtract(fee);
            BigDecimal realAmount = amount;
            //余额变更后
            BigDecimal userAmountAfter = userAmountBefore.add(realAmount);
            //更新用户余额
            userAmount.setAmount(userAmountAfter);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new ServiceException("更新用户余额异常");
            }
            try{
                //用户信息
                UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
                if (userInfo == null){
                    throw new RuntimeException("获取用户信息异常");
                }
                //更新打码量
                //打码倍数
                BigDecimal userDefaultDmMultiples = CacheUtils.getOtherValueByKey("user_default_dm_multiples",BigDecimal.class);
                if (userDefaultDmMultiples != null && userDefaultDmMultiples.compareTo(BigDecimal.ZERO) >= 0){
                    //新增打码量
                    BigDecimal dmAmt = userDefaultDmMultiples.multiply(realAmount).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
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
                            throw new RuntimeException("系统繁忙");
                        }
                        //插入打码量变更记录
                        UserDmAmountChangeRecord userDmAmountChangeRecord = new UserDmAmountChangeRecord();
                        userDmAmountChangeRecord.setUserId(userId);
                        userDmAmountChangeRecord.setOrderAmount(realAmount);
                        userDmAmountChangeRecord.setDmMultiples(userDefaultDmMultiples);
                        userDmAmountChangeRecord.setDmAmount(dmAmt);
                        userDmAmountChangeRecord.setDmAmountBefore(needOrderAmount);
                        userDmAmountChangeRecord.setDmAmountAfter(needOrderAmount.add(dmAmt));
                        userDmAmountChangeRecord.setCreateTime(new Date());
                        userDmAmountChangeRecord.setUpdateBy(SecurityUtils.getUsername());
                        userDmAmountChangeRecord.setOrderType(1);
                        int insertUserDmAmountChangeRecord = userDmAmountChangeRecordService.insertUserDmAmountChangeRecord(userDmAmountChangeRecord);
                        if (insertUserDmAmountChangeRecord <= 0){
                            throw new ServiceException("系统繁忙");
                        }
                    }
                }
            }catch (Exception e){

            }

            //新增充值订单信息
            UserRecharge userRecharge = new UserRecharge();
            userRecharge.setUserId(userId);
            userRecharge.setOrderCode(CodeUtils.generateOrderCode("R"));
            userRecharge.setRechargeAmount(realAmount);
            userRecharge.setOrderStatus(1);
            userRecharge.setCreateTime(new Date());
            userRecharge.setPayTime(new Date());
            userRecharge.setOperatorName("自动通过");
            userRecharge.setPayChannelName("优盾支付");
            userRecharge.setPayChannelId(0L);
            userRecharge.setCurrencyId(currencyId);
            userRecharge.setRechargeImg(null);
            userRecharge.setRechargeMsg(null);
            userRecharge.setUserAmountBefore(userAmountBefore);
            userRecharge.setUserAmountAfter(userAmountAfter);
            userRecharge.setRechargeMethod(1);
//            String remark = "充值币种："+currencyName+",充值金额："+amount+"，手续费："+fee+"，实际到账："+realAmount+"，钱包地址："+address;
            String remark = "充值币种："+currencyName+",充值金额："+amount+"，实际到账："+realAmount+"，钱包地址："+address+"，交易ID："+tradeId;
            userRecharge.setRemark(remark);
            //日志记录充值订单号
            HttpUtils.getRequestLogParams().put("充值订单号",userRecharge.getOrderCode());
            //日志记录币种名称
            HttpUtils.getRequestLogParams().put("币种名称",currencyName);
            int updateUserRecharge = userRechargeMapper.insertUserRecharge(userRecharge);
            if (updateUserRecharge <= 0){
                throw new ServiceException("插入充值订单信息异常");
            }

            //流水明细
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("用户优盾支付");
            userBillDetail.setDeSummary("用户优盾支付成功");
            userBillDetail.setOrderAmount(realAmount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(userRecharge.getId());
            userBillDetail.setOrderClass(0);
            userBillDetail.setCurrencyId(userAmount.getCurrencyId());
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail <= 0) {
                throw new ServiceException("插入流水明细异常");
            }
        }else {
            //充值失败
            throw new ServiceException("充值失败");
        }
    }
}
