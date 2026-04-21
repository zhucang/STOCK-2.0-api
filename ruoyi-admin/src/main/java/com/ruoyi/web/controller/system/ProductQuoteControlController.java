package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.ProductQuoteControlLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.ProductQuoteControl;
import com.ruoyi.system.service.IProductQuoteControlService;
import com.ruoyi.system.utils.ProductQuoteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 产品短线行情控制Controller
 * 
 * @author ruoyi
 * @date 2024-01-11
 */
@RestController
@RequestMapping("/system/selfSellProductQuoteControl")
public class ProductQuoteControlController extends BaseController
{
    @Autowired
    private IProductQuoteControlService productQuoteControlService;

    /**fi
     * 查询产品短线行情控制列表
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProductQuoteControl:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductQuoteControl productQuoteControl)
    {
        if (productQuoteControl.getProductType() == null){
//            throw new ServiceException()("请选择产品类型");
            return getDataTable(new ArrayList<>());
        }
        startPage();
        List<ProductQuoteControl> list = productQuoteControlService.selectProductQuoteControlList(productQuoteControl);
        return getDataTable(list);
    }

    /**
     * 获取产品短线行情控制详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProductQuoteControl:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(productQuoteControlService.selectProductQuoteControlById(id));
    }

    /**
     * 新增产品短线行情控制
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProductQuoteControl:add')")
    @Log(title = "新增产品短线行情控制", businessType = BusinessType.INSERT,dict = ProductQuoteControlLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody ProductQuoteControl productQuoteControl)
    {
        if (productQuoteControl.getStartDelayTime() == null){
            throw new ServiceException("请输入启动延时时间");
        }
        if (productQuoteControl.getStartDelayTime() <= 0){
            throw new ServiceException("启动延时时间必须大于0");
        }
        if (StringUtils.isEmpty(productQuoteControl.getProductCode())){
            throw new ServiceException("请选择需要控制行情的自营产品");
        }
        if (productQuoteControl.getProductType() == null){
            throw new ServiceException("请选择产品类型");
        }
        if (productQuoteControl.getPresetPrice() == null){
            throw new ServiceException("请输入预设点位");
        }
        if (productQuoteControl.getPresetDuration() == null){
            throw new ServiceException("请输入预设时长");
        }
        if (productQuoteControl.getReturnDuration() == null){
            throw new ServiceException("请输入回归时长");
        }
        if (productQuoteControl.getReturnDuration() <= 0){
            throw new ServiceException("回归时长必须大于0");
        }
        if (productQuoteControl.getReturnDuration()*2 > productQuoteControl.getPresetDuration()){
            throw new ServiceException("预设时长至少需要是回归时长的两倍");
        }
        return toAjax(productQuoteControlService.insertProductQuoteControl(productQuoteControl));
    }

//    /**
//     * 修改产品短线行情控制
//     */
//    @PreAuthorize("@ss.hasPermi('system:selfSellProductQuoteControl:edit')")
//    @Log(title = "修改产品短线行情控制", businessType = BusinessType.UPDATE,dict = ProductQuoteControlLogDict.class)
//    @RepeatSubmit
//    @PutMapping
//    public AjaxResult edit(@RequestBody ProductQuoteControl productQuoteControl)
//    {
//        return toAjax(productQuoteControlService.updateProductQuoteControl(productQuoteControl));
//    }

    /**
     * 删除产品短线行情控制
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProductQuoteControl:remove')")
    @Log(title = "删除产品短线行情控制", businessType = BusinessType.DELETE,dict = ProductQuoteControlLogDict.class)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(productQuoteControlService.deleteProductQuoteControlByIds(ids));
    }

    @GetMapping("/getQuoteByProductCode")
    public AjaxResult getQuoteByProductCode(String productCode, Integer productType) {
        if (StringUtils.isEmpty(productCode)){
            return AjaxResult.error("请选择产品");
        }
        if (productType == null){
            return AjaxResult.error("请选择产品类型");
        }
        //行情map
        Map<String, TickerInfo> tickerInfoMap = null;
        if (productType.equals(1)){
            tickerInfoMap = ProductQuoteUtils.getStockQuote(productCode,true);
        }else if (productType.equals(2)){
            tickerInfoMap = ProductQuoteUtils.getCryptoCurrencyQuote(productCode,true);
        }else if (productType.equals(3)){
            tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCode);
        }else if (productType.equals(4)){
            tickerInfoMap = ProductQuoteUtils.getForexQuote(productCode);
        }else {
            return AjaxResult.error("产品类型错误");
        }
        TickerInfo tickerInfo = tickerInfoMap.get(productCode);
        if (new BigDecimal(tickerInfo.getNowPrice()).compareTo(BigDecimal.ZERO) <= 0){
            return AjaxResult.error("未获取到该产品行情信息");
        }
        return AjaxResult.success(tickerInfo);
    }
}
