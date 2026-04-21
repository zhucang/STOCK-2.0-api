package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.NewProductApplyPurchase;
import com.ruoyi.system.service.INewProductApplyPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 新股新币申购初始配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-30
 */
@RestController
@RequestMapping("/system/newProductApplyPurchase")
public class NewProductApplyPurchaseController extends BaseController
{
    @Autowired
    private INewProductApplyPurchaseService newProductApplyPurchaseService;

    /**
     * 查询新股新币申购初始配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:newProductApplyPurchase:list')")
    @GetMapping("/list")
    public TableDataInfo list(NewProductApplyPurchase newProductApplyPurchase)
    {
        if (newProductApplyPurchase.getProductType() == null){
            throw new ServiceException("请选择产品类型");
        }
        startPage();
        List<NewProductApplyPurchase> list = newProductApplyPurchaseService.selectNewProductApplyPurchaseList(newProductApplyPurchase);
        return getDataTable(list);
    }

    /**
     * 获取新股新币申购初始配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:newProductApplyPurchase:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(newProductApplyPurchaseService.selectNewProductApplyPurchaseById(id));
    }

    /**
     * 新增新股新币申购初始配置
     */
    @PreAuthorize("@ss.hasPermi('system:newProductApplyPurchase:add')")
    @Log(title = "新增新股新币申购初始配置", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody NewProductApplyPurchase newProductApplyPurchase)
    {
        if (newProductApplyPurchase.getSelfSellProductId() == null){
            throw new ServiceException("请选择自营产品");
        }
        if (newProductApplyPurchase.getListingPrice() == null){
            throw new ServiceException("请输入上市价格");
        }
        if (newProductApplyPurchase.getListingPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("上市价格必须大于0");
        }
//        if (newProductApplyPurchase.getListingQuantity() == null){
//            throw new ServiceException("请输入上市数量");
//        }
//        if (newProductApplyPurchase.getListingQuantity() <= 0){
//            throw new ServiceException("上市数量必须大于0");
//        }
        //上市数量
        Integer listingQuantity = newProductApplyPurchase.getListingQuantity();
        if (listingQuantity != null && listingQuantity <= 0){
            throw new ServiceException("上市数量必须大于0");
        }
        if (newProductApplyPurchase.getListingDayIncreaseRate() == null){
            throw new ServiceException("请输入上市当天涨幅率");
        }
        if (newProductApplyPurchase.getListingDayIncreaseRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("上市当天涨幅率不允许小于0");
        }
        if (newProductApplyPurchase.getUserAmountLimit() == null){
            throw new ServiceException("请输入用户余额最低要求");
        }
        if (newProductApplyPurchase.getUserAmountLimit().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("用户余额最低要求不允许小于0");
        }
        if (newProductApplyPurchase.getApplyPurchaseStartDate() == null){
            throw new ServiceException("请选择申购开始日期");
        }
        if (newProductApplyPurchase.getApplyPurchaseEndDate() == null){
            throw new ServiceException("请选择申购结束日期");
        }
        if (newProductApplyPurchase.getApplyPurchaseStartDate().after(newProductApplyPurchase.getApplyPurchaseEndDate())){
            throw new ServiceException("申购开始日期不允许比申购结束日期晚");
        }
//        if (newProductApplyPurchase.getRemainingQuantity() == null){
//            throw new ServiceException("请输入剩余数量");
//        }
//        if (newProductApplyPurchase.getRemainingQuantity() < 0){
//            throw new ServiceException("剩余数量不允许小于0");
//        }
//        if (newProductApplyPurchase.getRemainingQuantity() > newProductApplyPurchase.getListingQuantity()){
//            throw new ServiceException("剩余数量不允许大于上市数量");
//        }
        //剩余数量
        Integer remainingQuantity = newProductApplyPurchase.getRemainingQuantity();
        if (remainingQuantity != null && remainingQuantity < 0){
            throw new ServiceException("剩余数量不允许小于0");
        }
        if (listingQuantity != null && remainingQuantity != null){
            if (remainingQuantity > listingQuantity){
                throw new ServiceException("剩余数量不允许大于上市数量");
            }
        }
        if (newProductApplyPurchase.getListingStartDate() == null){
            throw new ServiceException("请选择上市开始时间");
        }
        if (newProductApplyPurchase.getListingEndDate() == null){
            throw new ServiceException("请选择上市结束时间");
        }
        if (newProductApplyPurchase.getListingStartDate().after(newProductApplyPurchase.getListingEndDate())){
            throw new ServiceException("上市开始日期不允许比上市结束日期晚");
        }
        if (newProductApplyPurchase.getApplyPurchaseEndDate().after(newProductApplyPurchase.getListingStartDate())){
            throw new ServiceException("上市开始日期必须晚于申购结束日期");
        }
        if (newProductApplyPurchase.getListedDayIncreaseRate() == null){
            throw new ServiceException("请输入上市后每天涨幅率");
        }
        if (newProductApplyPurchase.getListedDayIncreaseRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("上市后每天涨幅不允许小于0");
        }
        if (newProductApplyPurchase.getApplyPurchaseMin() == null){
            throw new ServiceException("请输入单笔申购最低数量");
        }
        if (newProductApplyPurchase.getApplyPurchaseMin().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("单笔申购最低数量不允许小于等于0");
        }
        if (newProductApplyPurchase.getApplyPurchaseMax() == null){
            throw new ServiceException("请输入单笔申购最高数量");
        }
        if (newProductApplyPurchase.getApplyPurchaseMax().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("单笔申购最高数量不允许小于等于0");
        }
        if (newProductApplyPurchase.getApplyPurchaseMin().compareTo(newProductApplyPurchase.getApplyPurchaseMax()) == 1){
            throw new ServiceException("单笔申购最低数量不允许大于单笔申购最高数量");
        }
        if (newProductApplyPurchase.getLockupPeriod() != null && newProductApplyPurchase.getLockupPeriod() < 0){
            throw new ServiceException("锁仓期限不允许小于0");
        }
//        //申购开始日期必须晚于今天
//        if (newProductApplyPurchase.getApplyPurchaseStartDate().before(DateUtils.getEndOfDay(new Date()))){
//            throw new ServiceException("申购开始日期不允许早于今天");
//        }
        return toAjax(newProductApplyPurchaseService.insertNewProductApplyPurchase(newProductApplyPurchase));
    }

    /**
     * 修改新股新币申购初始配置
     */
    @PreAuthorize("@ss.hasPermi('system:newProductApplyPurchase:edit')")
    @Log(title = "修改新股新币申购初始配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody NewProductApplyPurchase newProductApplyPurchase)
    {
        if (newProductApplyPurchase.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (newProductApplyPurchase.getSelfSellProductId() == null){
            throw new ServiceException("请选择自营产品");
        }
        if (newProductApplyPurchase.getListingPrice() == null){
            throw new ServiceException("请输入上市价格");
        }
        if (newProductApplyPurchase.getListingPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("上市价格必须大于0");
        }
//        if (newProductApplyPurchase.getListingQuantity() == null){
//            throw new ServiceException("请输入上市数量");
//        }
//        if (newProductApplyPurchase.getListingQuantity() <= 0){
//            throw new ServiceException("上市数量必须大于0");
//        }
        //上市数量
        Integer listingQuantity = newProductApplyPurchase.getListingQuantity();
        if (listingQuantity != null && listingQuantity <= 0){
            throw new ServiceException("上市数量必须大于0");
        }
        if (newProductApplyPurchase.getListingDayIncreaseRate() == null){
            throw new ServiceException("请输入上市当天涨幅率");
        }
        if (newProductApplyPurchase.getListingDayIncreaseRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("上市当天涨幅率不允许小于0");
        }
        if (newProductApplyPurchase.getUserAmountLimit() == null){
            throw new ServiceException("请输入用户余额最低要求");
        }
        if (newProductApplyPurchase.getUserAmountLimit().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("用户余额最低要求不允许小于0");
        }
        if (newProductApplyPurchase.getApplyPurchaseStartDate() == null){
            throw new ServiceException("请选择申购开始日期");
        }
        if (newProductApplyPurchase.getApplyPurchaseEndDate() == null){
            throw new ServiceException("请选择申购结束日期");
        }
        if (newProductApplyPurchase.getApplyPurchaseStartDate().after(newProductApplyPurchase.getApplyPurchaseEndDate())){
            throw new ServiceException("申购开始日期不允许比申购结束日期晚");
        }
//        if (newProductApplyPurchase.getRemainingQuantity() == null){
//            throw new ServiceException("请输入剩余数量");
//        }
//        if (newProductApplyPurchase.getRemainingQuantity() < 0){
//            throw new ServiceException("剩余数量不允许小于0");
//        }
//        if (newProductApplyPurchase.getRemainingQuantity() > newProductApplyPurchase.getListingQuantity()){
//            throw new ServiceException("剩余数量不允许大于上市数量");
//        }
        //剩余数量
        Integer remainingQuantity = newProductApplyPurchase.getRemainingQuantity();
        if (remainingQuantity != null && remainingQuantity < 0){
            throw new ServiceException("剩余数量不允许小于0");
        }
        if (listingQuantity != null && remainingQuantity != null){
            if (remainingQuantity > listingQuantity){
                throw new ServiceException("剩余数量不允许大于上市数量");
            }
        }
        if (newProductApplyPurchase.getListingStartDate() == null){
            throw new ServiceException("请选择上市开始时间");
        }
        if (newProductApplyPurchase.getListingEndDate() == null){
            throw new ServiceException("请选择上市结束时间");
        }
        if (newProductApplyPurchase.getListingStartDate().after(newProductApplyPurchase.getListingEndDate())){
            throw new ServiceException("上市开始日期不允许比上市结束日期晚");
        }
        if (newProductApplyPurchase.getApplyPurchaseEndDate().after(newProductApplyPurchase.getListingStartDate())){
            throw new ServiceException("上市开始日期必须晚于申购结束日期");
        }
        if (newProductApplyPurchase.getListedDayIncreaseRate() == null){
            throw new ServiceException("请输入上市后每天涨幅率");
        }
        if (newProductApplyPurchase.getListedDayIncreaseRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("上市后每天涨幅不允许小于0");
        }
        if (newProductApplyPurchase.getApplyPurchaseMin() == null){
            throw new ServiceException("请输入单笔申购最低数量");
        }
        if (newProductApplyPurchase.getApplyPurchaseMin().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("单笔申购最低数量不允许小于等于0");
        }
        if (newProductApplyPurchase.getApplyPurchaseMax() == null){
            throw new ServiceException("请输入单笔申购最高数量");
        }
        if (newProductApplyPurchase.getApplyPurchaseMax().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("单笔申购最高数量不允许小于等于0");
        }
        if (newProductApplyPurchase.getApplyPurchaseMin().compareTo(newProductApplyPurchase.getApplyPurchaseMax()) == 1){
            throw new ServiceException("单笔申购最低数量不允许大于单笔申购最高数量");
        }
//        if (newProductApplyPurchase.getLockupPeriod() != null && newProductApplyPurchase.getLockupPeriod() < 0){
//            throw new ServiceException("锁仓期限不允许小于0");
//        }
//        //申购开始日期必须晚于今天
//        if (newProductApplyPurchase.getApplyPurchaseStartDate().before(DateUtils.getEndOfDay(new Date()))){
//            throw new ServiceException("申购开始日期不允许早于今天");
//        }
        return toAjax(newProductApplyPurchaseService.updateNewProductApplyPurchase(newProductApplyPurchase));
    }

    /**
     * 删除新股新币申购初始配置
     */
    @PreAuthorize("@ss.hasPermi('system:newProductApplyPurchase:remove')")
    @Log(title = "删除新股新币申购初始配置", businessType = BusinessType.DELETE)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(newProductApplyPurchaseService.deleteNewProductApplyPurchaseByIds(ids));
    }
}
