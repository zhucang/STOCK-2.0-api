package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.UserProductOption;
import com.ruoyi.system.service.IUserProductOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户产品自选关联信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-01
 *  *  * cache待优化
 */
@RestController
@RequestMapping("/api/userProductOption")
public class ApiUserProductOptionController extends BaseController
{
    @Autowired
    private IUserProductOptionService userProductOptionService;

    /**
     * 查询用户产品自选关联信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserProductOption userProductOption)
    {
//        if (userProductOption.getProductType() == null){
//            return getDataTable(new ArrayList<>());
//            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择产品的类型");
//        }
        userProductOption.setUserId(SecurityUtils.getUserId());
        startPage();
        List<UserProductOption> list = userProductOptionService.selectUserProductOptionList(userProductOption);
        return getDataTable(list);
    }

    /**
     * 添加到自选产品
     */
    @RepeatSubmit
    @PostMapping(value = "addOption")
    public AjaxResult addOption(@RequestBody UserProductOption userProductOption) {
        userProductOption.setUserId(SecurityUtils.getUserId());
        if (userProductOption.getProductId() == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择需要添加自选的产品");
        }
        if (StringUtils.isEmpty(userProductOption.getProductCode())){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择需要添加自选的产品");
        }
        if (userProductOption.getProductType() == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择产品的类型");
        }
        return toAjax(userProductOptionService.insertUserProductOption(userProductOption));
    }

    /**
     * 删除自选产品
     */
    @RepeatSubmit
    @PostMapping(value = "delOption")
    public AjaxResult delOption(String productCode,Integer productType) {
        if (StringUtils.isEmpty(productCode)){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择需要删除自选的产品");
        }
        if (productType == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择产品的类型");
        }
        return userProductOptionService.delOption(productCode,productType);
    }

    /**
     * 批量删除自选产品
     */
    @RepeatSubmit
    @PostMapping(value = "batchDelOption")
    public AjaxResult batchDelOption(@RequestBody Long[] userProductOptionIds) {
        if (StringUtils.isEmpty(userProductOptionIds)){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择需要删除的选项");
        }
        return toAjax(userProductOptionService.deleteUserProductOptionByIds(userProductOptionIds));
    }
}
