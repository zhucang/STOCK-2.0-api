package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.LoanProductLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.LoanProduct;
import com.ruoyi.system.service.ILoanProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 贷款产品配置Controller
 * 
 * @author ruoyi
 * @date 2024-05-21
 */
@RestController
@RequestMapping("/system/loanProduct")
public class LoanProductController extends BaseController
{
    @Autowired
    private ILoanProductService loanProductService;

    /**
     * 查询贷款产品配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(LoanProduct loanProduct)
    {
        startPage();
        startOrderBy("a.sort is null,a.sort");
        List<LoanProduct> list = loanProductService.selectLoanProductList(loanProduct);
        return getDataTable(list);
    }

    /**
     * 导出贷款产品配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanProduct:export')")
    @Log(title = "贷款产品配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LoanProduct loanProduct)
    {
        List<LoanProduct> list = loanProductService.selectLoanProductList(loanProduct);
        ExcelUtil<LoanProduct> util = new ExcelUtil<LoanProduct>(LoanProduct.class);
        util.exportExcel(response, list, "贷款产品配置数据");
    }

    /**
     * 获取贷款产品配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:loanProduct:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(loanProductService.selectLoanProductById(id));
    }

    /**
     * 新增贷款产品配置
     */
    @PreAuthorize("@ss.hasPermi('system:loanProduct:add')")
    @Log(title = "新增贷款产品配置", businessType = BusinessType.INSERT,dict = LoanProductLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody LoanProduct loanProduct)
    {
        if (StringUtils.isEmpty(loanProduct.getProductName())){
            throw new ServiceException("请输入产品名称");
        }
        if (loanProduct.getMinPrice() == null){
            throw new ServiceException("请输入最小金额");
        }
        if (loanProduct.getMinPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("最小金额必须大于0");
        }
        if (loanProduct.getMaxPrice() == null){
            throw new ServiceException("请输入最大金额");
        }
        if (loanProduct.getMaxPrice().compareTo(loanProduct.getMinPrice()) < 0){
            throw new ServiceException("最大金额必须大于等于最小金额");
        }
        if (loanProduct.getInterestFreeDays() == null){
            throw new ServiceException("请输入贷款免息天数");
        }
        if (loanProduct.getInterestFreeDays() < 0){
            throw new ServiceException("贷款免息天数不允许小于0");
        }
        if (loanProduct.getLoanDays() == null){
            throw new ServiceException("请输入贷款天数");
        }
        if (loanProduct.getLoanDays() <= 0){
            throw new ServiceException("贷款天数必须大于0");
        }
        if (loanProduct.getLoanDays() < loanProduct.getInterestFreeDays()){
            throw new ServiceException("免息天数不允许大于贷款天数");
        }
        if (loanProduct.getLoanDailyRate() == null){
            throw new ServiceException("请输入贷款每日利息率");
        }
        if (loanProduct.getLoanDailyRate().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("贷款每日利息率必须大于0");
        }
        if (loanProduct.getBreakContractDailyRate() == null){
            throw new ServiceException("请输入违约每日利息率");
        }
        if (loanProduct.getBreakContractDailyRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("违约每日利息率不允许小于0");
        }
        if (loanProduct.getMaxLoanCount() == null){
            throw new ServiceException("请输入最大贷款次数");
        }
        if (loanProduct.getMaxLoanCount() <= 0){
            throw new ServiceException("最大贷款次数必须大于0");
        }
        if (loanProduct.getInterestSettlementMethod() == null){
            throw new ServiceException("请选择利息结算方式");
        }
        if (!loanProduct.getInterestSettlementMethod().equals(0) && !loanProduct.getInterestSettlementMethod().equals(1)){
            throw new ServiceException("利息结算方式错误");
        }
        if (loanProduct.getCurrencyId() == null){
            throw new ServiceException("请输入贷款币种");
        }
        return toAjax(loanProductService.insertLoanProduct(loanProduct));
    }

    /**
     * 修改贷款产品配置
     */
    @PreAuthorize("@ss.hasPermi('system:loanProduct:edit')")
    @Log(title = "修改贷款产品配置", businessType = BusinessType.UPDATE,dict = LoanProductLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody LoanProduct loanProduct)
    {
        if (loanProduct.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(loanProduct.getProductName())){
            throw new ServiceException("请输入产品名称");
        }
        if (loanProduct.getMinPrice() == null){
            throw new ServiceException("请输入最小金额");
        }
        if (loanProduct.getMinPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("最小金额必须大于0");
        }
        if (loanProduct.getMaxPrice() == null){
            throw new ServiceException("请输入最大金额");
        }
        if (loanProduct.getMaxPrice().compareTo(loanProduct.getMinPrice()) < 0){
            throw new ServiceException("最大金额必须大于等于最小金额");
        }
        if (loanProduct.getInterestFreeDays() == null){
            throw new ServiceException("请输入贷款免息天数");
        }
        if (loanProduct.getInterestFreeDays() < 0){
            throw new ServiceException("贷款免息天数不允许小于0");
        }
        if (loanProduct.getLoanDays() == null){
            throw new ServiceException("请输入贷款天数");
        }
        if (loanProduct.getLoanDays() <= 0){
            throw new ServiceException("贷款天数必须大于0");
        }
        if (loanProduct.getLoanDays() < loanProduct.getInterestFreeDays()){
            throw new ServiceException("免息天数不允许大于贷款天数");
        }
        if (loanProduct.getLoanDailyRate() == null){
            throw new ServiceException("请输入贷款每日利息率");
        }
        if (loanProduct.getLoanDailyRate().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("贷款每日利息率必须大于0");
        }
        if (loanProduct.getBreakContractDailyRate() == null){
            throw new ServiceException("请输入违约每日利息率");
        }
        if (loanProduct.getBreakContractDailyRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("违约每日利息率不允许小于0");
        }
        if (loanProduct.getMaxLoanCount() == null){
            throw new ServiceException("请输入最大贷款次数");
        }
        if (loanProduct.getMaxLoanCount() <= 0){
            throw new ServiceException("最大贷款次数必须大于0");
        }
        if (loanProduct.getInterestSettlementMethod() == null){
            throw new ServiceException("请选择利息结算方式");
        }
        if (!loanProduct.getInterestSettlementMethod().equals(0) && !loanProduct.getInterestSettlementMethod().equals(1)){
            throw new ServiceException("利息结算方式错误");
        }
        if (loanProduct.getCurrencyId() == null){
            throw new ServiceException("请输入贷款币种");
        }
        return toAjax(loanProductService.updateLoanProduct(loanProduct));
    }

    /**
     * 修改贷款产品多语言
     */
    @PreAuthorize("@ss.hasPermi('system:loanProduct:edit')")
    @Log(title = "修改贷款产品多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateProductNameLang")
    public AjaxResult updateProductNameLang(@RequestBody LoanProduct loanProduct)
    {
        if (loanProduct.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(loanProduct.getProductNameLang().getZh())){
            throw new ServiceException("请输入产品称");
        }
        return toAjax(loanProductService.updateProductNameLang(loanProduct.getId(),loanProduct.getProductNameLang()));
    }

    /**
     * 删除贷款产品配置
     */
    @PreAuthorize("@ss.hasPermi('system:loanProduct:remove')")
    @Log(title = "删除贷款产品配置", businessType = BusinessType.DELETE,dict = LoanProductLogDict.class,
            saveParamNames = {"id","productName","loanProducts"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(loanProductService.deleteLoanProductByIds(ids));
    }
}
