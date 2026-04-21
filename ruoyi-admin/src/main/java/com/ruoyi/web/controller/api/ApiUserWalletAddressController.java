package com.ruoyi.web.controller.api;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserWalletAddressLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.UserWalletAddress;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserWalletAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户虚拟货币钱包信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-14
 */
@RestController
@RequestMapping("/api/userWalletAddress")
public class ApiUserWalletAddressController extends BaseController
{
    @Autowired
    private IUserWalletAddressService userWalletAddressService;

    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 查询用户虚拟货币钱包信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserWalletAddress userWalletAddress)
    {
        userWalletAddress.setUserId(SecurityUtils.getUserId());
        startPage();
        List<UserWalletAddress> list = userWalletAddressService.selectUserWalletAddressList(userWalletAddress);
        return getDataTable(list);
    }

    /**
     * 新增用户虚拟货币钱包信息
     */
    @RepeatSubmit
    @PostMapping("addUserWalletAddress")
    @Log(title = "用户添加虚拟货币钱包信息", businessType = BusinessType.OTHER,dict = UserWalletAddressLogDict.class,
            saveParamNames = {"id","currencyId","currencyName","walletAddress","walletAddressType"})
    public AjaxResult addUserWalletAddress(@RequestBody UserWalletAddress userWalletAddress)
    {
        userWalletAddress.setUserId(SecurityUtils.getUserId());
        if (StringUtils.isEmpty(userWalletAddress.getWalletAddress())){
            throw new LangException(HintConstants.PARAM_NULL,"请输入钱包地址");
        }
        if (userWalletAddress.getCurrencyId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择钱包币种");
        }
        //虚拟钱包是否必须上传钱包收款码
        Integer switchStatus101 = switchSetService.selectSwitchStatusById(101L);
        if (switchStatus101.equals(0)){
            if (StringUtils.isEmpty(userWalletAddress.getWalletReceiptQrCode())){
                throw new LangException("hint_87","请上传钱包收款码");
            }
        }
        return toAjax(userWalletAddressService.userAddWalletAddress(userWalletAddress));
    }

    /**
     * 修改用户虚拟货币钱包信息
     */
    @RepeatSubmit
    @PostMapping("modifyUserWalletAddress")
    @Log(title = "用户修改虚拟货币钱包信息", businessType = BusinessType.OTHER,dict = UserWalletAddressLogDict.class,
            saveParamNames = {"id","currencyId","currencyName","walletAddress","walletAddressType"})
    public AjaxResult modifyUserWalletAddress(@RequestBody UserWalletAddress userWalletAddress)
    {
        userWalletAddress.setUserId(SecurityUtils.getUserId());
        if (userWalletAddress.getId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(userWalletAddress.getWalletAddress())){
            throw new LangException(HintConstants.PARAM_NULL,"请输入钱包地址");
        }
        if (userWalletAddress.getCurrencyId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择钱包币种");
        }
        //虚拟钱包是否必须上传钱包收款码
        Integer switchStatus101 = switchSetService.selectSwitchStatusById(101L);
        if (switchStatus101.equals(0)){
            if (StringUtils.isEmpty(userWalletAddress.getWalletReceiptQrCode())){
                throw new LangException("hint_87","请上传钱包收款码");
            }
        }
        return toAjax(userWalletAddressService.userModifyWalletAddress(userWalletAddress));
    }

    /**
     * 删除用户虚拟货币钱包信息
     */
    @PostMapping("/deleteUserWalletAddress")
    @Log(title = "用户删除虚拟货币钱包信息", businessType = BusinessType.OTHER,dict = UserWalletAddressLogDict.class,
            saveParamNames = {"id","currencyId","currencyName","walletAddress","walletAddressType"})
    public AjaxResult deleteUserWalletAddress(Long userWalletAddressId)
    {
        //是否允许用户删除虚拟钱包开关
        Integer allowUserDeleteWalletAddressSwitch = switchSetService.selectSwitchStatusById(51L);
        if (!allowUserDeleteWalletAddressSwitch.equals(0)){
            throw new LangException("hint_57","不允许删除钱包地址");
        }
        //钱包信息
        UserWalletAddress userWalletAddress = userWalletAddressService.selectUserWalletAddressById(userWalletAddressId);
        //日志记录钱包信息
        HttpUtils.getRequestLogParams().put("userWalletAddress", JSONObject.toJSONString(userWalletAddress));
        //校验用户信息
        if (!userWalletAddress.getUserId().equals(SecurityUtils.getUserId())){
            throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        return toAjax(userWalletAddressService.deleteUserWalletAddressById(userWalletAddressId));
    }
}
