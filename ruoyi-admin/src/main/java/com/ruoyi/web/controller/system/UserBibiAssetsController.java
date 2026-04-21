package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.UserBibiAssets;
import com.ruoyi.system.service.IUserBibiAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户币币资产Controller
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
@RestController
@RequestMapping("/system/userBibiAssets")
public class UserBibiAssetsController extends BaseController
{
    @Autowired
    private IUserBibiAssetsService userBibiAssetsService;

    /**
     * 查询用户币币资产列表
     */
    @PreAuthorize("@ss.hasPermi('system:userBibiAssets:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserBibiAssets userBibiAssets)
    {
        startPage();
        startOrderBy("case when bibi_amount = 0 then 1 else 0 end");
        List<UserBibiAssets> list = userBibiAssetsService.selectUserBibiAssetsList(userBibiAssets);
        userBibiAssetsService.fillOtherInfo(list);
        return getDataTable(list);
    }

//    /**
//     * 导出用户币币资产列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:userBibiAssets:export')")
//    @Log(title = "用户币币资产", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, UserBibiAssets userBibiAssets)
//    {
//        List<UserBibiAssets> list = userBibiAssetsService.selectUserBibiAssetsList(userBibiAssets);
//        ExcelUtil<UserBibiAssets> util = new ExcelUtil<UserBibiAssets>(UserBibiAssets.class);
//        util.exportExcel(response, list, "用户币币资产数据");
//    }

    /**
     * 获取用户币币资产详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userBibiAssetsService.selectUserBibiAssetsById(id));
    }

//    /**
//     * 新增用户币币资产
//     */
//    @PreAuthorize("@ss.hasPermi('system:userBibiAssets:add')")
//    @Log(title = "用户币币资产", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody UserBibiAssets userBibiAssets)
//    {
//        return toAjax(userBibiAssetsService.insertUserBibiAssets(userBibiAssets));
//    }
//
//    /**
//     * 修改用户币币资产
//     */
//    @PreAuthorize("@ss.hasPermi('system:userBibiAssets:edit')")
//    @Log(title = "用户币币资产", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody UserBibiAssets userBibiAssets)
//    {
//        return toAjax(userBibiAssetsService.updateUserBibiAssets(userBibiAssets));
//    }
//
//    /**
//     * 删除用户币币资产
//     */
//    @PreAuthorize("@ss.hasPermi('system:userBibiAssets:remove')")
//    @Log(title = "用户币币资产", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(userBibiAssetsService.deleteUserBibiAssetsByIds(ids));
//    }
}
