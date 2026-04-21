package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserBankLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BankPropertyConfig;
import com.ruoyi.system.domain.UserBank;
import com.ruoyi.system.service.IBankPropertyConfigService;
import com.ruoyi.system.service.IUserBankService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户银行卡Controller
 * 
 * @author ruoyi
 * @date 2023-11-09
 */
@RestController
@RequestMapping("/system/userBank")
public class UserBankController extends BaseController
{
    @Autowired
    private IUserBankService userBankService;

    @Autowired
    private IBankPropertyConfigService bankPropertyConfigService;

    /**
     * 查询用户银行卡列表
     */
    @PreAuthorize("@ss.hasPermi('system:userBank:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserBank userBank)
    {
        if (userBank.getUserId() == null){
            return getDataTable(new ArrayList<>());
//            throw new ServiceException("请选择需要查看银行卡信息的用户");
        }
        startPage();
        List<UserBank> list = userBankService.selectUserBankList(userBank);
        return getDataTable(list);
    }

    /**
     * 获取用户银行卡详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userBank:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userBankService.selectUserBankById(id));
    }

    /**
     * 新增用户银行卡
     */
    @PreAuthorize("@ss.hasPermi('system:userBank:add')")
    @Log(title = "新增用户银行卡", businessType = BusinessType.INSERT,dict = UserBankLogDict.class,
            saveParamNames = {"id","bankName","bankNo","bankOpenAddress","holder","bankPhone","bankCountry","abaCode","swift","postCode","userRealAddress","accountType"})
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody UserBank userBank)
    {
        if (userBank.getUserId() == null){
            throw new ServiceException("请选择需要新增用户银行卡的用户");
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
                //取字段
                Object property = PropertyUtils.getProperty(userBank, propertyName);
                //如果是必填
                if (isRequire.equals(0)){
                    if (property == null || StringUtils.isEmpty(String.valueOf(property))){
                        throw new ServiceException("请输入"+bankPropertyConfigVo.getPropertyDesc());
                    }
                }
            } catch (Exception e) {
                if (e instanceof ServiceException){
                    throw (ServiceException)e;
                }else {
                    throw new ServiceException();
                }
            }
        }
        return toAjax(userBankService.insertUserBank(userBank));
    }

    /**
     * 修改用户银行卡
     */
    @PreAuthorize("@ss.hasPermi('system:userBank:edit')")
    @Log(title = "修改用户银行卡", businessType = BusinessType.UPDATE,dict = UserBankLogDict.class,
            saveParamNames = {"id","bankName","bankNo","bankOpenAddress","holder","bankPhone","bankCountry","abaCode","swift","postCode","userRealAddress","accountType"})
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody UserBank userBank)
    {
        if (userBank.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (userBank.getUserId() == null){
            throw new ServiceException("请选择需要新增用户银行卡的用户");
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
                //取字段
                Object property = PropertyUtils.getProperty(userBank, propertyName);
                //如果是必填
                if (isRequire.equals(0)){
                    if (property == null || StringUtils.isEmpty(String.valueOf(property))){
                        throw new ServiceException("请输入"+bankPropertyConfigVo.getPropertyDesc());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return toAjax(userBankService.updateUserBank(userBank));
    }

    /**
     * 删除用户银行卡
     */
    @PreAuthorize("@ss.hasPermi('system:userBank:remove')")
    @Log(title = "删除用户银行卡", businessType = BusinessType.DELETE,dict = UserBankLogDict.class,
            saveParamNames = {"id","userNo","bankName","bankNo","bankOpenAddress","holder","bankPhone","bankCountry","abaCode","swift","postCode","userRealAddress","accountType","userBanks"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userBankService.deleteUserBankByIds(ids));
    }
}
