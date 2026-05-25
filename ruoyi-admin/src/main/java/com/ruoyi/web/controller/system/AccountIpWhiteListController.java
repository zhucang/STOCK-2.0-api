package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.AccountIpWhiteList;
import com.ruoyi.system.service.IAccountIpWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 账号ip白名单Controller
 *
 * @author ruoyi
 * @date 2026-05-25
 */
@RestController
@RequestMapping("/system/accountIpWhiteList")
public class AccountIpWhiteListController extends BaseController
{
    @Autowired
    private IAccountIpWhiteListService accountIpWhiteListService;

    /**
     * 查询账号ip白名单列表
     */
    @PreAuthorize("@ss.hasPermi('system:accountIpWhiteList:list')")
    @GetMapping("/list")
    public TableDataInfo list(AccountIpWhiteList accountIpWhiteList)
    {
        startPage();
        startOrderBy("id desc");
        List<AccountIpWhiteList> list = accountIpWhiteListService.selectAccountIpWhiteListList(accountIpWhiteList);
        return getDataTable(list);
    }

    /**
     * 获取账号ip白名单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:accountIpWhiteList:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(accountIpWhiteListService.selectAccountIpWhiteListById(id));
    }

    /**
     * 新增账号ip白名单
     */
    @PreAuthorize("@ss.hasPermi('system:accountIpWhiteList:add')")
    @Log(title = "新增账号ip白名单", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody AccountIpWhiteList accountIpWhiteList)
    {
        validate(accountIpWhiteList, false);
        return toAjax(accountIpWhiteListService.insertAccountIpWhiteList(accountIpWhiteList));
    }

    /**
     * 修改账号ip白名单
     */
    @PreAuthorize("@ss.hasPermi('system:accountIpWhiteList:edit')")
    @Log(title = "修改账号ip白名单", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody AccountIpWhiteList accountIpWhiteList)
    {
        validate(accountIpWhiteList, true);
        return toAjax(accountIpWhiteListService.updateAccountIpWhiteList(accountIpWhiteList));
    }

    /**
     * 删除账号ip白名单
     */
    @PreAuthorize("@ss.hasPermi('system:accountIpWhiteList:remove')")
    @Log(title = "删除账号ip白名单", businessType = BusinessType.DELETE)
    @RepeatSubmit
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(accountIpWhiteListService.deleteAccountIpWhiteListByIds(ids));
    }

    private void validate(AccountIpWhiteList accountIpWhiteList, boolean checkId)
    {
        if (checkId && accountIpWhiteList.getId() == null)
        {
            throw new ServiceException("请选择需要修改的选项");
        }
        if (accountIpWhiteList.getAccountType() == null && accountIpWhiteList.getAccountId() == null)
        {
            throw new ServiceException("账号类型和账号ID至少填写一个");
        }
        if (StringUtils.isEmpty(accountIpWhiteList.getIpAddress()))
        {
            throw new ServiceException("请输入ip");
        }
    }
}
