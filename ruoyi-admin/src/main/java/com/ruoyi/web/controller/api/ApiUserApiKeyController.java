package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.UserApiKey;
import com.ruoyi.system.service.IUserApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 应用秘钥apiKeyController
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
@RestController
@RequestMapping("/api/userApiKey")
public class ApiUserApiKeyController extends BaseController
{
    @Autowired
    private IUserApiKeyService userApiKeyService;

    /**
     * 查询应用秘钥apiKey列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserApiKey userApiKey)
    {
        userApiKey.setUserId(SecurityUtils.getUserId());
        startPage();
        List<UserApiKey> list = userApiKeyService.selectUserApiKeyList(userApiKey);
        return getDataTable(list);
    }

    /**
     * 新增应用秘钥apiKey
     */
    @Log(title = "应用秘钥apiKey", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody UserApiKey userApiKey)
    {
        userApiKey.setUserId(SecurityUtils.getUserId());
        userApiKey.setAppId(UUID.randomUUID().toString());
        userApiKey.setApiKey(UUID.randomUUID().toString());
        return toAjax(userApiKeyService.insertUserApiKey(userApiKey));
    }

//    /**
//     * 修改应用秘钥apiKey
//     */
//    @PreAuthorize("@ss.hasPermi('system:key:edit')")
//    @Log(title = "应用秘钥apiKey", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody UserApiKey userApiKey)
//    {
//        return toAjax(userApiKeyService.updateUserApiKey(userApiKey));
//    }

    /**
     * 删除应用秘钥apiKey
     */
    @PreAuthorize("@ss.hasPermi('system:key:remove')")
    @Log(title = "应用秘钥apiKey", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userApiKeyService.deleteUserApiKeyByIds(ids));
    }
}
