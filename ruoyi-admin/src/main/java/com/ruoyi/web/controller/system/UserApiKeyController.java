package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.UserApiKey;
import com.ruoyi.system.service.IUserApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 应用秘钥apiKeyController
 * 
 * @author ruoyi
 * @date 2025-02-26
 */
@RestController
@RequestMapping("/system/userApiKey")
public class UserApiKeyController extends BaseController
{
    @Autowired
    private IUserApiKeyService userApiKeyService;

    /**
     * 查询应用秘钥apiKey列表
     */
    @PreAuthorize("@ss.hasPermi('system:userApiKey:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserApiKey userApiKey)
    {
        startPage();
        startOrderBy("id desc");
        List<UserApiKey> list = userApiKeyService.selectUserApiKeyList(userApiKey);
        return getDataTable(list);
    }

    /**
     * 导出应用秘钥apiKey列表
     */
    @PreAuthorize("@ss.hasPermi('system:userApiKey:export')")
    @Log(title = "应用秘钥apiKey", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserApiKey userApiKey)
    {
        List<UserApiKey> list = userApiKeyService.selectUserApiKeyList(userApiKey);
        ExcelUtil<UserApiKey> util = new ExcelUtil<UserApiKey>(UserApiKey.class);
        util.exportExcel(response, list, "应用秘钥apiKey数据");
    }

    /**
     * 获取应用秘钥apiKey详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userApiKey:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userApiKeyService.selectUserApiKeyById(id));
    }

    /**
     * 新增应用秘钥apiKey
     */
    @PreAuthorize("@ss.hasPermi('system:userApiKey:add')")
    @Log(title = "应用秘钥apiKey", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserApiKey userApiKey)
    {

        if (userApiKey.getUserId() == null){
            throw new ServiceException("请选择需要生成秘钥的用户");
        }
        userApiKey.setAppId(UUID.randomUUID().toString());
        userApiKey.setApiKey(UUID.randomUUID().toString());
        return toAjax(userApiKeyService.insertUserApiKey(userApiKey));
    }

//    /**
//     * 修改应用秘钥apiKey
//     */
//    @PreAuthorize("@ss.hasPermi('system:userApiKey:edit')")
//    @Log(title = "应用秘钥apiKey", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody UserApiKey userApiKey)
//    {
//        return toAjax(userApiKeyService.updateUserApiKey(userApiKey));
//    }

    /**
     * 删除应用秘钥apiKey
     */
    @PreAuthorize("@ss.hasPermi('system:userApiKey:remove')")
    @Log(title = "应用秘钥apiKey", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userApiKeyService.deleteUserApiKeyByIds(ids));
    }
}
