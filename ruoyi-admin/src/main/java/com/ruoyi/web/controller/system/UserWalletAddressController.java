package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserWalletAddressLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.UserWalletAddress;
import com.ruoyi.system.service.IUserWalletAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户虚拟货币钱包地址Controller
 * 
 * @author ruoyi
 * @date 2023-10-29
 */
@RestController
@RequestMapping("/system/userWalletAddress")
public class UserWalletAddressController extends BaseController
{
    @Autowired
    private IUserWalletAddressService userWalletAddressService;

    /**
     * 查询用户虚拟货币钱包地址列表
     */
    @PreAuthorize("@ss.hasPermi('system:userWalletAddress:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserWalletAddress userWalletAddress)
    {
        if (userWalletAddress.getUserId() == null){
            throw new ServiceException("请选择需要查看钱包地址信息的用户");
        }
        startPage();
        List<UserWalletAddress> list = userWalletAddressService.selectUserWalletAddressList(userWalletAddress);
        return getDataTable(list);
    }

    /**
     * 导出用户虚拟货币钱包地址列表
     */
    @PreAuthorize("@ss.hasPermi('system:userWalletAddress:export')")
    @Log(title = "导出用户虚拟货币钱包地址", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserWalletAddress userWalletAddress)
    {
        List<UserWalletAddress> list = userWalletAddressService.selectUserWalletAddressList(userWalletAddress);
        ExcelUtil<UserWalletAddress> util = new ExcelUtil<UserWalletAddress>(UserWalletAddress.class);
        util.exportExcel(response, list, "用户虚拟货币钱包地址数据");
    }

    /**
     * 获取用户虚拟货币钱包地址详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userWalletAddress:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userWalletAddressService.selectUserWalletAddressById(id));
    }

    /**
     * 新增用户虚拟货币钱包地址
     */
    @PreAuthorize("@ss.hasPermi('system:userWalletAddress:add')")
    @Log(title = "新增用户虚拟货币钱包地址", businessType = BusinessType.INSERT,dict = UserWalletAddressLogDict.class,
            saveParamNames = {"id","currencyId","currencyName","walletAddress","walletAddressType"})
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody UserWalletAddress userWalletAddress)
    {
        if (userWalletAddress.getUserId() == null){
            throw new ServiceException("请选择需要新增钱包的用户");
        }
        if (userWalletAddress.getCurrencyId() == null){
            throw new ServiceException("请选择钱包的币种");
        }
        if (StringUtils.isEmpty(userWalletAddress.getWalletAddress())){
            throw new ServiceException("请输入钱包地址");
        }
        return toAjax(userWalletAddressService.insertUserWalletAddress(userWalletAddress));
    }

    /**
     * 修改用户虚拟货币钱包地址
     */
    @PreAuthorize("@ss.hasPermi('system:userWalletAddress:edit')")
    @Log(title = "修改用户虚拟货币钱包地址", businessType = BusinessType.UPDATE,dict = UserWalletAddressLogDict.class,
            saveParamNames = {"id","currencyId","currencyName","walletAddress","walletAddressType"})
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody UserWalletAddress userWalletAddress)
    {
        if (userWalletAddress.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (userWalletAddress.getCurrencyId() == null){
            throw new ServiceException("请选择钱包的币种");
        }
        if (StringUtils.isEmpty(userWalletAddress.getWalletAddress())){
            throw new ServiceException("请输入钱包地址");
        }
        return toAjax(userWalletAddressService.updateUserWalletAddress(userWalletAddress));
    }

    /**
     * 删除用户虚拟货币钱包地址
     */
    @PreAuthorize("@ss.hasPermi('system:userWalletAddress:remove')")
    @Log(title = "删除用户虚拟货币钱包地址", businessType = BusinessType.DELETE,dict = UserWalletAddressLogDict.class,
            saveParamNames = {"id","userNo","currencyId","currencyName","walletAddress","walletAddressType","userWalletAddresses"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userWalletAddressService.deleteUserWalletAddressByIds(ids));
    }
}
