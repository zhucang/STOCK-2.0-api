package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.UserBibiAssets;
import com.ruoyi.system.service.IUserBibiAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/userBibiAssets")
public class ApiUserBibiAssetsController extends BaseController
{
    @Autowired
    private IUserBibiAssetsService userBibiAssetsService;

    /**
     * 查询用户币币资产列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserBibiAssets userBibiAssets)
    {
        userBibiAssets.setUserId(SecurityUtils.getUserId());
        startPage();
        List<UserBibiAssets> list = userBibiAssetsService.selectUserBibiAssetsList(userBibiAssets);
        userBibiAssetsService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 查询用户币币资产信息
     */
    @GetMapping("/getUserBibiAssets")
    public AjaxResult getUserBibiAssets(String productCode, Integer productType)
    {
        UserBibiAssets userBibiAssets = userBibiAssetsService.getUserBibiAssets(SecurityUtils.getUserId(), productCode, productType);
        return AjaxResult.success(userBibiAssets);
    }
}
