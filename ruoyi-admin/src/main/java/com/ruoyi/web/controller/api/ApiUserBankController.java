package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserBankLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BankPropertyConfig;
import com.ruoyi.system.domain.UserBank;
import com.ruoyi.system.service.IBankPropertyConfigService;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserBankService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户银行卡Controller
 * 
 * @author ruoyi
 * @date 2023-11-09
 */
@RestController
@RequestMapping("/api/userBank")
public class ApiUserBankController extends BaseController
{
    @Autowired
    private IUserBankService userBankService;

    @Autowired
    private IBankPropertyConfigService bankPropertyConfigService;

    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 查询用户银行卡列表
     */
    @GetMapping("/list")
    public AjaxResult list(UserBank userBank)
    {
        userBank.setUserId(SecurityUtils.getUserId());
        startPage();
        List<UserBank> list = userBankService.selectUserBankList(userBank);
        return AjaxResult.success(getDataTable(list));
    }

    /**
     * 新增用户银行卡
     */
    @Log(title = "用户添加银行卡", businessType = BusinessType.OTHER,dict = UserBankLogDict.class,
            saveParamNames = {"id","bankName","bankNo","bankOpenAddress","holder","bankPhone","bankCountry","abaCode","swift","postCode","userRealAddress","accountType"})
    @RepeatSubmit
    @PostMapping(value = "addUserBankCard")
    public AjaxResult addUserBankCard(@RequestBody UserBank userBank)
    {
        userBank.setUserId(SecurityUtils.getUserId());
        //银行卡字段配置信息
        BankPropertyConfig bankPropertyConfig = new BankPropertyConfig();
        bankPropertyConfig.setIsVisible(0);
        bankPropertyConfig.setConfigType(0);
        List<BankPropertyConfig> bankPropertyConfigs = bankPropertyConfigService.selectBankPropertyConfigList(bankPropertyConfig);
        for (int i = 0; i < bankPropertyConfigs.size(); i++) {
            //配置信息
            BankPropertyConfig bankPropertyConfigVo = bankPropertyConfigs.get(i);
            //字段名称
            String propertyName = bankPropertyConfigVo.getPropertyName();
            //是否必填
            Integer isRequire = bankPropertyConfigVo.getIsRequire();
            try {
                //取字段
                Object property = PropertyUtils.getProperty(userBank, propertyName);
                //如果是必填
                if (isRequire.equals(0)){
                    if (property == null || StringUtils.isEmpty(String.valueOf(property))){
                        throw new LangException(HintConstants.PARAM_NULL,"请输入"+bankPropertyConfigVo.getPropertyDesc());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return toAjax(userBankService.addUserBankCard(userBank));
    }

    /**
     * 修改用户银行卡
     */
    @RepeatSubmit
    @PostMapping(value = "updateUserBankCard")
    @Log(title = "用户修改银行卡", businessType = BusinessType.OTHER,dict = UserBankLogDict.class,
            saveParamNames = {"id","bankName","bankNo","bankOpenAddress","holder","bankPhone","bankCountry","abaCode","swift","postCode","userRealAddress","accountType"})
    public AjaxResult updateUserBankCard(@RequestBody UserBank userBank)
    {
        if (userBank.getId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择需要修改的选项");
        }
        if (userBank.getUserId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请传入验证信息");
        }
        //用户id
        Long userId = SecurityUtils.getUserId();
        //此银行卡旧信息
        UserBank userBankVo = userBankService.selectUserBankById(userBank.getId());
        if (!userBankVo.getUserId().equals(userId) || !userBank.getUserId().equals(userId)){
            throw new ServiceException("用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //银行卡字段配置信息
        BankPropertyConfig bankPropertyConfig = new BankPropertyConfig();
        bankPropertyConfig.setIsVisible(0);
        bankPropertyConfig.setConfigType(0);
        List<BankPropertyConfig> bankPropertyConfigs = bankPropertyConfigService.selectBankPropertyConfigList(bankPropertyConfig);
        for (int i = 0; i < bankPropertyConfigs.size(); i++) {
            //配置信息
            BankPropertyConfig bankPropertyConfigVo = bankPropertyConfigs.get(i);
            //字段名称
            String propertyName = bankPropertyConfigVo.getPropertyName();
            //是否必填
            Integer isRequire = bankPropertyConfigVo.getIsRequire();
            try {
                if (propertyName.equals("swift")){
                    propertyName = "swift";
                }
                //取字段
                Object property = PropertyUtils.getProperty(userBank, propertyName);
                //如果是必填
                if (isRequire.equals(0)){
                    if (property == null || StringUtils.isEmpty(String.valueOf(property))){
                        throw new LangException(HintConstants.PARAM_NULL,"请输入"+bankPropertyConfigVo.getPropertyDesc());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return toAjax(userBankService.updateUserBankCard(userBank));
    }

    /**
     * 删除用户银行卡
     */
    @RepeatSubmit
    @PostMapping("/userDeleteBankCard")
    @Log(title = "用户删除银行卡", businessType = BusinessType.OTHER,dict = UserBankLogDict.class,
            saveParamNames = {"id","bankName","bankNo","bankOpenAddress","holder","bankPhone","bankCountry","abaCode","swift","postCode","userRealAddress","accountType"})
    public AjaxResult userDeleteBankCard(Long userBankId)
    {
        //是否允许删除银行卡开关
        Integer allowDeleteBankCardSwitch = switchSetService.selectSwitchStatusById(50L);
        if (!allowDeleteBankCardSwitch.equals(0)){
            throw new LangException("hint_56","不允许删除银行卡");
        }
        return toAjax(userBankService.deleteUserBankById(userBankId));
    }
}
