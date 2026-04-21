package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.vo.UdunRechargeOrder;
import com.ruoyi.system.service.IUdunRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 优盾充值Controller
 *
 * @author ruoyi
 * @date 2023-10-30
 *  *  * cache待优化
 */
@RestController
@RequestMapping("/api/udunRecharge")
public class ApiUdunRechargeController extends BaseController {

    @Autowired
    private IUdunRechargeService udunRechargeService;

    /**
     * 查询udun充值商户支持币种
     */
    @GetMapping("/getMerchantSupportCoins")
    public TableDataInfo getMerchantSupportCoins() {
        return getDataTable(udunRechargeService.getMerchantSupportCoins());
    }

    /**
     * 用户发起优盾支付
     */
    @RepeatSubmit
    @PostMapping(value = "getPayWalletAddress")
    @Log(title = "用户发起优盾支付", businessType = BusinessType.OTHER,
            saveParamNames = {"主币种编号","钱包地址","币种名称","回调URL"})
    public AjaxResult getPayWalletAddress(String mainCoinType,String name) {
        if(StringUtils.isEmpty(mainCoinType)){
            throw new LangException(HintConstants.PARAM_NULL,"请选择需要充值的币种");
        }
        if (StringUtils.isEmpty(name)){
            throw new LangException(HintConstants.PARAM_NULL,"请选择需要充值的币种");
        }
        return AjaxResult.success(udunRechargeService.getPayWalletAddress(mainCoinType,name));
    }

    /**
     * 优盾支付成功回调
     */
    @RepeatSubmit
    @PostMapping(value = "udunRechargeCallUrl")
    @Log(title = "优盾支付成功回调", businessType = BusinessType.OTHER,
            saveParamNames = {"充值状态","币种类型","主币种编号","钱包地址","金额","手续费","交易ID","充值订单号","币种名称"})
    public AjaxResult udunRechargeCallUrl(UdunRechargeOrder udunRechargeOrder) {
        udunRechargeService.udunRechargeCallUrl(udunRechargeOrder);
        return AjaxResult.success();
    }
}
