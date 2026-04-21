package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.StakingProduct;
import com.ruoyi.system.service.IStakingProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 质押产品配置Controller
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
@RestController
@RequestMapping("/system/stakingProduct")
public class StakingProductController extends BaseController
{
    @Autowired
    private IStakingProductService stakingProductService;

    /**
     * 查询质押产品配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:stakingProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(StakingProduct stakingProduct)
    {
        startPage();
        startOrderBy("a.sort is null,a.sort");
        List<StakingProduct> list = stakingProductService.selectStakingProductList(stakingProduct);
        return getDataTable(list);
    }

    /**
     * 导出质押产品配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:stakingProduct:export')")
    @Log(title = "质押产品配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StakingProduct stakingProduct)
    {
        List<StakingProduct> list = stakingProductService.selectStakingProductList(stakingProduct);
        ExcelUtil<StakingProduct> util = new ExcelUtil<StakingProduct>(StakingProduct.class);
        util.exportExcel(response, list, "质押产品配置数据");
    }

    /**
     * 获取质押产品配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:stakingProduct:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stakingProductService.selectStakingProductById(id));
    }

    /**
     * 新增质押产品配置
     */
    @PreAuthorize("@ss.hasPermi('system:stakingProduct:add')")
    @Log(title = "质押产品配置", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody StakingProduct stakingProduct)
    {
        if (StringUtils.isEmpty(stakingProduct.getStakingName())){
            throw new ServiceException("请输入质押产品名称");
        }
        if (StringUtils.isEmpty(stakingProduct.getStakingImg())){
            throw new ServiceException("请上传质押产品图标");
        }
        if (stakingProduct.getMinPrice() == null){
            throw new ServiceException("请输入质押产品单笔最低购买金额");
        }
        if (stakingProduct.getMinPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("质押产品单笔最低购买金额必须大于0");
        }
        if (stakingProduct.getMaxPrice() == null){
            throw new ServiceException("请输入质押产品单笔最高购买金额");
        }
        if (stakingProduct.getMaxPrice().compareTo(stakingProduct.getMinPrice()) < 0){
            throw new ServiceException("质押产品单笔最高购买金额必须大于等于单笔最低购买金额");
        }
        if (stakingProduct.getStakingTime() != null && stakingProduct.getStakingTime() <= 0){
            throw new ServiceException("质押天数必须大于0");
        }
        if (stakingProduct.getFloatDailyIncomeMaxRate() == null){
            throw new ServiceException("请输入质押产品浮动收益范围");
        }
        if (stakingProduct.getFloatDailyIncomeMinRate() == null){
            throw new ServiceException("请输入质押产品浮动收益范围");
        }
        if (stakingProduct.getFloatDailyIncomeMinRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("质押产品浮动最低收益率必须大于等于0");
        }
        if (stakingProduct.getFloatDailyIncomeMaxRate().compareTo(stakingProduct.getFloatDailyIncomeMinRate()) < 0){
            throw new ServiceException("质押产品浮动最高收益率必须大于等于最低收益率");
        }
        if (stakingProduct.getFixedIncomeRate() == null){
            throw new ServiceException("请输入质押产品固定收益率");
        }
        if (stakingProduct.getFixedIncomeRate().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("质押产品固定收益率必须大于0");
        }
        if (stakingProduct.getBreakContractRate() == null){
            throw new ServiceException("请输入质押产品提前赎回违约金收费率");
        }
        if (stakingProduct.getBreakContractRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("质押产品提前赎回违约金收费率必须大于等于0");
        }
        if (stakingProduct.getUserAmountLimit() == null){
            throw new ServiceException("请输入购买质押产品的用户余额最低要求");
        }
        //开放数量
        BigDecimal openQuantity = stakingProduct.getOpenQuantity();
        if (openQuantity != null && openQuantity.compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("开放数量必须大于0");
        }
        //剩余数量
        BigDecimal remainingQuantity = stakingProduct.getRemainingQuantity();
        if (remainingQuantity != null && remainingQuantity.compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("剩余数量不允许小于0");
        }
        if (openQuantity != null && remainingQuantity != null){
            if (remainingQuantity.compareTo(openQuantity) > 0){
                throw new ServiceException("剩余数量不允许大于开放数量");
            }
        }
        if (stakingProduct.getCurrencyId() == null){
            throw new ServiceException("请选择质押币种");
        }
        return toAjax(stakingProductService.insertStakingProduct(stakingProduct));
    }

    /**
     * 修改质押产品配置
     */
    @PreAuthorize("@ss.hasPermi('system:stakingProduct:edit')")
    @Log(title = "质押产品配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody StakingProduct stakingProduct)
    {
        if (stakingProduct.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(stakingProduct.getStakingName())){
            throw new ServiceException("请输入质押产品名称");
        }
        if (StringUtils.isEmpty(stakingProduct.getStakingImg())){
            throw new ServiceException("请上传质押产品图标");
        }
        if (stakingProduct.getMinPrice() == null){
            throw new ServiceException("请输入质押产品单笔最低购买金额");
        }
        if (stakingProduct.getMinPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("质押产品单笔最低购买金额必须大于0");
        }
        if (stakingProduct.getMaxPrice() == null){
            throw new ServiceException("请输入质押产品单笔最高购买金额");
        }
        if (stakingProduct.getMaxPrice().compareTo(stakingProduct.getMinPrice()) < 0){
            throw new ServiceException("质押产品单笔最高购买金额必须大于等于单笔最低购买金额");
        }
        if (stakingProduct.getStakingTime() != null && stakingProduct.getStakingTime() <= 0){
            throw new ServiceException("质押天数必须大于0");
        }
        if (stakingProduct.getFloatDailyIncomeMaxRate() == null){
            throw new ServiceException("请输入质押产品浮动收益范围");
        }
        if (stakingProduct.getFloatDailyIncomeMinRate() == null){
            throw new ServiceException("请输入质押产品浮动收益范围");
        }
        if (stakingProduct.getFloatDailyIncomeMinRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("质押产品浮动最低收益率必须大于等于0");
        }
        if (stakingProduct.getFloatDailyIncomeMaxRate().compareTo(stakingProduct.getFloatDailyIncomeMinRate()) < 0){
            throw new ServiceException("质押产品浮动最高收益率必须大于等于最低收益率");
        }
        if (stakingProduct.getFixedIncomeRate() == null){
            throw new ServiceException("请输入质押产品固定收益率");
        }
        if (stakingProduct.getFixedIncomeRate().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("质押产品固定收益率必须大于0");
        }
        if (stakingProduct.getBreakContractRate() == null){
            throw new ServiceException("请输入质押产品提前赎回违约金收费率");
        }
        if (stakingProduct.getBreakContractRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("质押产品提前赎回违约金收费率必须大于等于0");
        }
        if (stakingProduct.getUserAmountLimit() == null){
            throw new ServiceException("请输入购买质押产品的用户余额最低要求");
        }
        //开放数量
        BigDecimal openQuantity = stakingProduct.getOpenQuantity();
        if (openQuantity != null && openQuantity.compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("开放数量必须大于0");
        }
        //剩余数量
        BigDecimal remainingQuantity = stakingProduct.getRemainingQuantity();
        if (remainingQuantity != null && remainingQuantity.compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("剩余数量不允许小于0");
        }
        if (openQuantity != null && remainingQuantity != null){
            if (remainingQuantity.compareTo(openQuantity) > 0){
                throw new ServiceException("剩余数量不允许大于开放数量");
            }
        }
        if (stakingProduct.getCurrencyId() == null){
            throw new ServiceException("请选择质押币种");
        }
        return toAjax(stakingProductService.updateStakingProduct(stakingProduct));
    }

    /**
     * 修改质押名称多语言
     */
    @PreAuthorize("@ss.hasPermi('system:stakingProduct:edit')")
    @Log(title = "修改质押名称多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "/updateStakingNameLang")
    public AjaxResult updateStakingNameLang(@RequestBody StakingProduct stakingProduct)
    {
        if (stakingProduct.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(stakingProduct.getStakingNameLang().getZh())){
            throw new ServiceException("请输入质押名称");
        }
        return toAjax(stakingProductService.updateStakingNameLang(stakingProduct.getId(),stakingProduct.getStakingNameLang()));
    }

    /**
     * 删除质押产品配置
     */
    @PreAuthorize("@ss.hasPermi('system:stakingProduct:remove')")
    @Log(title = "质押产品配置", businessType = BusinessType.DELETE)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stakingProductService.deleteStakingProductByIds(ids));
    }
}
