package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.FinancialProductLogDict;
import com.ruoyi.system.domain.FinancialProduct;
import com.ruoyi.system.service.IFinancialProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 理财产品配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-26
 * 日志已优化
 */
@RestController
@RequestMapping("/system/financialProduct")
public class FinancialProductController extends BaseController
{
    @Autowired
    private IFinancialProductService financialProductService;

    /**
     * 查询理财产品配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:financialProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinancialProduct financialProduct)
    {
        startPage();
        startOrderBy("a.sort is null,a.sort");
        List<FinancialProduct> list = financialProductService.selectFinancialProductList(financialProduct);
        return getDataTable(list);
    }

    /**
     * 获取理财产品配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:financialProduct:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(financialProductService.selectFinancialProductById(id));
    }

    /**
     * 新增理财产品配置
     */
    @PreAuthorize("@ss.hasPermi('system:financialProduct:add')")
    @Log(title = "新增理财产品配置", businessType = BusinessType.INSERT,dict = FinancialProductLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody FinancialProduct financialProduct)
    {
        if (StringUtils.isEmpty(financialProduct.getFinancialName())){
            throw new ServiceException("请输入理财产品名称");
        }
        if (StringUtils.isEmpty(financialProduct.getFinancialImg())){
            throw new ServiceException("请上传理财产品图标");
        }
        if (financialProduct.getMinPrice() == null){
            throw new ServiceException("请输入理财产品单笔最低购买金额");
        }
        if (financialProduct.getMinPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("理财产品单笔最低购买金额必须大于0");
        }
        if (financialProduct.getMaxPrice() == null){
            throw new ServiceException("请输入理财产品单笔最高购买金额");
        }
        if (financialProduct.getMaxPrice().compareTo(financialProduct.getMinPrice()) < 0){
            throw new ServiceException("理财产品单笔最高购买金额必须大于等于单笔最低购买金额");
        }
        if (financialProduct.getFinancialTime() == null){
            throw new ServiceException("请输入理财产品持仓天数");
        }
        if (financialProduct.getFinancialTime() <= 0){
            throw new ServiceException("理财产品持仓天数必须大于0");
        }
        if (financialProduct.getFloatDailyIncomeMaxRate() == null){
            throw new ServiceException("请输入理财产品浮动收益范围");
        }
        if (financialProduct.getFloatDailyIncomeMinRate() == null){
            throw new ServiceException("请输入理财产品浮动收益范围");
        }
        if (financialProduct.getFloatDailyIncomeMinRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("理财产品浮动最低收益率必须大于等于0");
        }
        if (financialProduct.getFloatDailyIncomeMaxRate().compareTo(financialProduct.getFloatDailyIncomeMinRate()) < 0){
            throw new ServiceException("理财产品浮动最高收益率必须大于等于最低收益率");
        }
        if (financialProduct.getFixedIncomeRate() == null){
            throw new ServiceException("请输入理财产品固定收益率");
        }
        if (financialProduct.getFixedIncomeRate().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("理财产品固定收益率必须大于0");
        }
        if (financialProduct.getBreakContractRate() == null){
            throw new ServiceException("请输入理财产品提前赎回违约金收费率");
        }
        if (financialProduct.getBreakContractRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("理财产品提前赎回违约金收费率必须大于等于0");
        }
        if (financialProduct.getUserAmountLimit() == null){
            throw new ServiceException("请输入购买理财产品的用户余额最低要求");
        }
        if (financialProduct.getSettleMethod() == null){
            throw new ServiceException("请选择结算方式");
        }
        if (!financialProduct.getSettleMethod().equals(0) && !financialProduct.getSettleMethod().equals(1)){
            throw new ServiceException("结算方式错误");
        }
        //开放数量
        BigDecimal openQuantity = financialProduct.getOpenQuantity();
        if (openQuantity != null && openQuantity.compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("开放数量必须大于0");
        }
        //剩余数量
        BigDecimal remainingQuantity = financialProduct.getRemainingQuantity();
        if (remainingQuantity != null && remainingQuantity.compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("剩余数量不允许小于0");
        }
        if (openQuantity != null && remainingQuantity != null){
            if (remainingQuantity.compareTo(openQuantity) > 0){
                throw new ServiceException("剩余数量不允许大于开放数量");
            }
        }
        if (financialProduct.getCurrencyId() == null){
            throw new ServiceException("请选择理财币种");
        }
        return toAjax(financialProductService.insertFinancialProduct(financialProduct));
    }

    /**
     * 修改理财产品配置
     */
    @PreAuthorize("@ss.hasPermi('system:financialProduct:edit')")
    @Log(title = "修改理财产品配置", businessType = BusinessType.UPDATE,dict = FinancialProductLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody FinancialProduct financialProduct)
    {
        if (financialProduct.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(financialProduct.getFinancialName())){
            throw new ServiceException("请输入理财产品名称");
        }
        if (StringUtils.isEmpty(financialProduct.getFinancialImg())){
            throw new ServiceException("请上传理财产品图标");
        }
        if (financialProduct.getMinPrice() == null){
            throw new ServiceException("请输入理财产品单笔最低购买金额");
        }
        if (financialProduct.getMinPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("理财产品单笔最低购买金额必须大于0");
        }
        if (financialProduct.getMaxPrice() == null){
            throw new ServiceException("请输入理财产品单笔最高购买金额");
        }
        if (financialProduct.getMaxPrice().compareTo(financialProduct.getMinPrice()) < 0){
            throw new ServiceException("理财产品单笔最高购买金额必须大于等于单笔最低购买金额");
        }
        if (financialProduct.getFinancialTime() == null){
            throw new ServiceException("请输入理财产品持仓天数");
        }
        if (financialProduct.getFinancialTime() <= 0){
            throw new ServiceException("理财产品持仓天数必须大于0");
        }
        if (financialProduct.getFloatDailyIncomeMaxRate() == null){
            throw new ServiceException("请输入理财产品浮动收益范围");
        }
        if (financialProduct.getFloatDailyIncomeMinRate() == null){
            throw new ServiceException("请输入理财产品浮动收益范围");
        }
        if (financialProduct.getFloatDailyIncomeMinRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("理财产品浮动最低收益率必须大于等于0");
        }
        if (financialProduct.getFloatDailyIncomeMaxRate().compareTo(financialProduct.getFloatDailyIncomeMinRate()) < 0){
            throw new ServiceException("理财产品浮动最高收益率必须大于等于最低收益率");
        }
        if (financialProduct.getFixedIncomeRate() == null){
            throw new ServiceException("请输入理财产品固定收益率");
        }
        if (financialProduct.getFixedIncomeRate().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("理财产品固定收益率必须大于0");
        }
        if (financialProduct.getBreakContractRate() == null){
            throw new ServiceException("请输入理财产品提前赎回违约金收费率");
        }
        if (financialProduct.getBreakContractRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("理财产品提前赎回违约金收费率必须大于等于0");
        }
        if (financialProduct.getUserAmountLimit() == null){
            throw new ServiceException("请输入购买理财产品的用户余额最低要求");
        }
        if (financialProduct.getSettleMethod() == null){
            throw new ServiceException("请选择结算方式");
        }
        if (!financialProduct.getSettleMethod().equals(0) && !financialProduct.getSettleMethod().equals(1)){
            throw new ServiceException("结算方式错误");
        }
        //开放数量
        BigDecimal openQuantity = financialProduct.getOpenQuantity();
        if (openQuantity != null && openQuantity.compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("开放数量必须大于0");
        }
        //剩余数量
        BigDecimal remainingQuantity = financialProduct.getRemainingQuantity();
        if (remainingQuantity != null && remainingQuantity.compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("剩余数量不允许小于0");
        }
        if (openQuantity != null && remainingQuantity != null){
            if (remainingQuantity.compareTo(openQuantity) > 0){
                throw new ServiceException("剩余数量不允许大于开放数量");
            }
        }
        if (financialProduct.getCurrencyId() == null){
            throw new ServiceException("请选择理财币种");
        }
        return toAjax(financialProductService.updateFinancialProduct(financialProduct));
    }

    /**
     * 修改理财名称多语言
     */
    @PreAuthorize("@ss.hasPermi('system:financialProduct:edit')")
    @Log(title = "修改理财名称多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateFinancialNameLang")
    public AjaxResult updateFinancialNameLang(@RequestBody FinancialProduct financialProduct)
    {
        if (financialProduct.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(financialProduct.getFinancialNameLang().getZh())){
            throw new ServiceException("请输入理财名称");
        }
        return toAjax(financialProductService.updateFinancialNameLang(financialProduct.getId(),financialProduct.getFinancialNameLang()));
    }

    /**
     * 删除理财产品配置
     */
    @PreAuthorize("@ss.hasPermi('system:financialProduct:remove')")
    @Log(title = "删除理财产品配置", businessType = BusinessType.DELETE,dict = FinancialProductLogDict.class,
    saveParamNames = {"id","financialName","financialProducts"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(financialProductService.deleteFinancialProductByIds(ids));
    }
}
