package com.ruoyi.web.controller.api;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.logDict.UserRechargeLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.UserRecharge;
import com.ruoyi.system.domain.vo.UserOnlineRechargeOrderVo;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserRechargeService;
import com.ruoyi.system.utils.cache.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户充值订单Controller
 * 
 * @author ruoyi
 * @date 2023-10-30
 *  *  * cache待优化
 */
@RestController
@RequestMapping("/api/userRecharge")
public class ApiUserRechargeController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ApiUserRechargeController.class);

    @Autowired
    private IUserRechargeService userRechargeService;

    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 查询用户充值订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserRecharge userRecharge) {
        userRecharge.setUserId(SecurityUtils.getUserId());
        //app端充值记录是否展示彩金赠送记录
        Integer selectSwitchStatusById78 = switchSetService.selectSwitchStatusById(78L);
        //app端充值提现记录是否展示上分下分记录
        Integer selectSwitchStatusById89 = switchSetService.selectSwitchStatusById(89L);
        startPage();
        if (selectSwitchStatusById78.equals(0) || selectSwitchStatusById89.equals(0)) {
            startOrderBy("create_time desc");
            if (selectSwitchStatusById78.equals(0)) {
                userRecharge.getParams().put("unionWinnings", 0);
            }
            if (selectSwitchStatusById89.equals(0)) {
                userRecharge.getParams().put("unionUpPoint", 0);
            }
            List<UserRecharge> list = userRechargeService.selectUserRechargeListWithOthers(userRecharge);
            return getDataTable(list);
        }
        startOrderBy("id desc");
        List<UserRecharge> list = userRechargeService.selectUserRechargeList(userRecharge);
        return getDataTable(list);
    }

    /**
     * 用户充值
     */
    @RepeatSubmit
    @PostMapping(value = "inMoney")
    @Log(title = "用户充值", businessType = BusinessType.OTHER, dict = UserRechargeLogDict.class,
            saveParamNames = {"id", "currencyId", "currencyName", "orderCode", "rechargeAmount", "payChannelName", "payChannelId", "rechargeImg", "rechargeMethod"})
    public AjaxResult inMoney(BigDecimal rechargeAmount, Long payChannelId, String rechargeImg) {
        if (rechargeAmount == null) {
            throw new LangException(HintConstants.PARAM_NULL, "请输入充值金额");
        }
        if (payChannelId == null) {
            throw new LangException(HintConstants.PARAM_NULL, "请输入充值通道");
        }
        //是否要求用户充值上传凭证
        Integer selectSwitchStatusById79 = switchSetService.selectSwitchStatusById(79L);
        if (selectSwitchStatusById79.equals(0)) {
            if (StringUtils.isEmpty(rechargeImg)) {
                throw new LangException(HintConstants.PARAM_NULL, "请上传充值凭证");
            }
        }
        return toAjax(userRechargeService.inMoney(rechargeAmount, payChannelId, rechargeImg));
    }

    /**
     * 获取在线支付支持的法币
     */
    @GetMapping(value = "getOnlineRechargeSupportFiatsCurrency")
    public AjaxResult onlineRechargeSupportFiatsCurrency(String payType) {
        try {
            return AjaxResult.success(CacheUtils.getOtherValueByKey("onlineRechargeSupportFiatsCurrency_channel_" + payType.replace("C", ""), String.class).split(","));
        } catch (Exception e) {
            return AjaxResult.success(new String[0]);
        }
    }

    /**
     * 用户在线支付
     */
    @RepeatSubmit
    @PostMapping(value = "userOnlineRecharge")
    @Log(title = "用户在线支付", businessType = BusinessType.OTHER, dict = UserRechargeLogDict.class,
            saveParamNames = {"id", "currencyId", "currencyName", "orderCode", "rechargeAmount", "payChannelName", "payChannelId", "rechargeImg", "rechargeMethod", "remark"})
    public AjaxResult userOnlineRecharge(BigDecimal rechargeAmount, String payType, String fiat) {
        //用户id
        Long userId = SecurityUtils.getUserId();
        if (rechargeAmount == null) {
            throw new LangException(HintConstants.PARAM_NULL, "请输入充值金额");
        }
        if (StringUtils.isEmpty(payType)) {
            throw new LangException(HintConstants.PARAM_NULL, "请选择支付类型");
        }
        if (StringUtils.isEmpty(fiat)) {
            throw new LangException(HintConstants.PARAM_NULL, "请选择法币单位");
        }
        try {
            if (!Arrays.asList(CacheUtils.getOtherValueByKey("onlineRechargeSupportFiatsCurrency_channel_" + payType.replace("C", ""), String.class).split(",")).contains(fiat)) {
                throw new LangException("hint_67", "不支持此币种");
            }
        } catch (Exception e) {
            throw new LangException("hint_67", "不支持此币种");
        }
        try {
            return AjaxResult.success(userRechargeService.userOnlineRecharge(rechargeAmount, payType, fiat, userId));
        } catch (Exception e) {
            try {
                //第三方返还的错误消息
                String param = JSONObject.parseObject(e.getMessage()).getString("param");
                param = param.replace("minBuyAmount=", "").replace("maxBuyAmount=", "");
                String[] split = param.split(",");
                String minBuyAmount = split[0];
                String maxBuyAmount = split[1];
                List<Object> list = new ArrayList<>();
                list.add(minBuyAmount);
                list.add(maxBuyAmount);
                throw new LangException("hint_69", list, "单笔充值金额范围" + minBuyAmount + "~" + maxBuyAmount);
            } catch (Exception exception) {
                if (exception instanceof LangException) {
                    throw exception;
                } else {
                    throw new LangException("hint_68", "发起支付失败,原因：" + e.getMessage());
                }
            }
        }
    }

    /**
     * 在线支付成功异步回调
     */
    @RepeatSubmit
    @PostMapping(value = "receiveUserOnlineRechargeOrderState")
    @Log(title = "在线支付成功异步回调", businessType = BusinessType.OTHER)
    public String receiveUserOnlineRechargeOrderState(@RequestBody UserOnlineRechargeOrderVo userOnlineRechargeOrderVo) {
        return userRechargeService.receiveUserOnlineRechargeOrderState(userOnlineRechargeOrderVo);
    }
}
