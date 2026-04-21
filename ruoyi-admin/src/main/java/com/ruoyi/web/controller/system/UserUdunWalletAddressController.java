package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserUdunWalletAddressLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.UserUdunWalletAddress;
import com.ruoyi.system.service.IUserUdunWalletAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优盾加密货币钱包信息Controller
 * 
 * @author ruoyi
 * @date 2024-09-30
 */
@RestController
@RequestMapping("/system/userUdunWalletAddress")
public class UserUdunWalletAddressController extends BaseController
{
    @Autowired
    private IUserUdunWalletAddressService userUdunWalletAddressService;

    /**
     * 查询优盾加密货币钱包信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:userUdunWalletAddress:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserUdunWalletAddress userUdunWalletAddress)
    {
        startPage();
        startOrderBy("id desc");
        List<UserUdunWalletAddress> list = userUdunWalletAddressService.selectUserUdunWalletAddressList(userUdunWalletAddress);
        return getDataTable(list);
    }

    /**
     * 获取优盾加密货币钱包信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userUdunWalletAddress:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userUdunWalletAddressService.selectUserUdunWalletAddressById(id));
    }

    /**
     * 新增优盾加密货币钱包信息
     */
    @PreAuthorize("@ss.hasPermi('system:userUdunWalletAddress:add')")
    @Log(title = "新增优盾加密货币钱包信息", businessType = BusinessType.INSERT,dict = UserUdunWalletAddressLogDict.class,
            saveParamNames = {"id","mainCoinType","coinName","walletAddress"})
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody UserUdunWalletAddress userUdunWalletAddress)
    {
        if (userUdunWalletAddress.getUserId() == null){
            throw new ServiceException("请选择钱包绑定的用户");
        }
        if (StringUtils.isEmpty(userUdunWalletAddress.getMainCoinType())){
            throw new ServiceException("请选择币种");
        }
        if (StringUtils.isEmpty(userUdunWalletAddress.getCoinName())){
            throw new ServiceException("请选择币种");
        }
        if (StringUtils.isEmpty(userUdunWalletAddress.getWalletAddress())){
            throw new ServiceException("请输入钱包地址");
        }
        return toAjax(userUdunWalletAddressService.insertUserUdunWalletAddress(userUdunWalletAddress));
    }

    /**
     * 修改优盾加密货币钱包信息
     */
    @PreAuthorize("@ss.hasPermi('system:userUdunWalletAddress:edit')")
    @Log(title = "修改优盾加密货币钱包信息", businessType = BusinessType.UPDATE,dict = UserUdunWalletAddressLogDict.class,
            saveParamNames = {"id","mainCoinType","coinName","walletAddress"})
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody UserUdunWalletAddress userUdunWalletAddress)
    {
        if (userUdunWalletAddress.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (userUdunWalletAddress.getUserId() == null){
            throw new ServiceException("请选择钱包绑定的用户");
        }
        if (StringUtils.isEmpty(userUdunWalletAddress.getMainCoinType())){
            throw new ServiceException("请选择币种");
        }
        if (StringUtils.isEmpty(userUdunWalletAddress.getCoinName())){
            throw new ServiceException("请选择币种");
        }
        if (StringUtils.isEmpty(userUdunWalletAddress.getWalletAddress())){
            throw new ServiceException("请输入钱包地址");
        }
        return toAjax(userUdunWalletAddressService.updateUserUdunWalletAddress(userUdunWalletAddress));
    }

    /**
     * 删除优盾加密货币钱包信息
     */
    @PreAuthorize("@ss.hasPermi('system:userUdunWalletAddress:remove')")
    @Log(title = "删除优盾加密货币钱包信息", businessType = BusinessType.DELETE,dict = UserUdunWalletAddressLogDict.class,
            saveParamNames = {"id","userNo","mainCoinType","coinName","walletAddress","userUdunWalletAddress"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userUdunWalletAddressService.deleteUserUdunWalletAddressByIds(ids));
    }
}
