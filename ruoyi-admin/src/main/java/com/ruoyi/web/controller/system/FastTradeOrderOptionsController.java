package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.FastTradeOrderOptionsLogDict;
import com.ruoyi.system.domain.FastTradeOrderOptions;
import com.ruoyi.system.service.IFastTradeOrderOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 极速交易下单选项Controller
 * 
 * @author ruoyi
 * @date 2023-11-02
 * 日志已优化
 */
@RestController
@RequestMapping("/system/fastTradeOrderOptions")
public class FastTradeOrderOptionsController extends BaseController
{
    @Autowired
    private IFastTradeOrderOptionsService fastTradeOrderOptionsService;

    /**
     * 查询极速交易下单选项列表
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrderOptions:list')")
    @GetMapping("/list")
    public TableDataInfo list(FastTradeOrderOptions fastTradeOrderOptions)
    {
        startPage();
        startOrderBy("product_code,sort is null,sort");
        List<FastTradeOrderOptions> list = fastTradeOrderOptionsService.selectFastTradeOrderOptionsList(fastTradeOrderOptions);
        return getDataTable(list);
    }

    /**
     * 获取极速交易下单选项详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrderOptions:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fastTradeOrderOptionsService.selectFastTradeOrderOptionsById(id));
    }

    /**
     * 新增极速交易下单选项
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrderOptions:add')")
    @Log(title = "新增极速交易下单选项", businessType = BusinessType.INSERT,dict = FastTradeOrderOptionsLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody FastTradeOrderOptions fastTradeOrderOptions) throws Exception {
        if (fastTradeOrderOptions.getDurationValue() == null){
            throw new ServiceException("请输入下单选项的时长");
        }
        if (fastTradeOrderOptions.getDurationValue() <= 0){
            throw new ServiceException("下单选项的时长必须的大于0");
        }
        if (fastTradeOrderOptions.getDurationLabel() == null){
            throw new ServiceException("请输入下单选项的时间单位");
        }
        if (fastTradeOrderOptions.getUpWinProfitRatio() == null){
            throw new ServiceException("请输入下单选项的涨赢收益率");
        }
        if (fastTradeOrderOptions.getUpLoseProfitRatio() == null){
            throw new ServiceException("请输入下单选项的涨输扣除率");
        }
        if (fastTradeOrderOptions.getDownWinProfitRatio() == null){
            throw new ServiceException("请输入下单选项的跌赢收益率");
        }
        if (fastTradeOrderOptions.getDownLoseProfitRatio() == null){
            throw new ServiceException("请输入下单选项的跌输扣除率");
        }
        if (fastTradeOrderOptions.getUpWinProfitRatio().compareTo(BigDecimal.ZERO) <= 0 || fastTradeOrderOptions.getDownWinProfitRatio().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("收益率必须大于0");
        }
        if (fastTradeOrderOptions.getUpLoseProfitRatio().compareTo(BigDecimal.ZERO) <= 0 || fastTradeOrderOptions.getDownLoseProfitRatio().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("扣除率必须大于0");
        }
        if (fastTradeOrderOptions.getMinBuyAmount() == null){
            throw new ServiceException("请输入下单选项的最小买入金额");
        }
        if (fastTradeOrderOptions.getMinBuyAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("最小买入金额必须大于0");
        }
//        if (fastTradeOrderOptions.getMaxBuyAmount() == null){
//            throw new ServiceException()("请输入下单选项的最大买入金额");
//        }
//        if (fastTradeOrderOptions.getMinBuyAmount().compareTo(fastTradeOrderOptions.getMaxBuyAmount()) > 0){
//            throw new ServiceException()("最小买入金额不允许大于最大买入金额");
//        }
        return toAjax(fastTradeOrderOptionsService.insertFastTradeOrderOptions(fastTradeOrderOptions));
    }

    /**
     * 修改极速交易下单选项
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrderOptions:edit')")
    @Log(title = "修改极速交易下单选项", businessType = BusinessType.UPDATE,dict = FastTradeOrderOptionsLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody FastTradeOrderOptions fastTradeOrderOptions)
    {
        if (fastTradeOrderOptions.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (fastTradeOrderOptions.getProductCode() == null){
            throw new ServiceException("请选择玩法的所属产品");
        }
        if (fastTradeOrderOptions.getProductType() == null){
            throw new ServiceException("请选择产品类型");
        }
        if (fastTradeOrderOptions.getDurationValue() == null){
            throw new ServiceException("请输入下单选项的时长");
        }
        if (fastTradeOrderOptions.getDurationValue() <= 0){
            throw new ServiceException("下单选项的时长必须的大于0");
        }
        if (fastTradeOrderOptions.getDurationLabel() == null){
            throw new ServiceException("请输入下单选项的时间单位");
        }
        if (fastTradeOrderOptions.getUpWinProfitRatio() == null){
            throw new ServiceException("请输入下单选项的涨赢收益率");
        }
        if (fastTradeOrderOptions.getUpLoseProfitRatio() == null){
            throw new ServiceException("请输入下单选项的涨输扣除率");
        }
        if (fastTradeOrderOptions.getDownWinProfitRatio() == null){
            throw new ServiceException("请输入下单选项的跌赢收益率");
        }
        if (fastTradeOrderOptions.getDownLoseProfitRatio() == null){
            throw new ServiceException("请输入下单选项的跌输扣除率");
        }
        if (fastTradeOrderOptions.getUpWinProfitRatio().compareTo(BigDecimal.ZERO) <= 0 || fastTradeOrderOptions.getDownWinProfitRatio().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("收益率必须大于0");
        }
        if (fastTradeOrderOptions.getUpLoseProfitRatio().compareTo(BigDecimal.ZERO) <= 0 || fastTradeOrderOptions.getDownLoseProfitRatio().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("扣除率必须大于0");
        }
        if (fastTradeOrderOptions.getMinBuyAmount() == null){
            throw new ServiceException("请输入下单选项的最小买入金额");
        }
        if (fastTradeOrderOptions.getMinBuyAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("最小买入金额必须大于0");
        }
//        if (fastTradeOrderOptions.getMaxBuyAmount() == null){
//            throw new ServiceException()("请输入下单选项的最大买入金额");
//        }
//        if (fastTradeOrderOptions.getMinBuyAmount().compareTo(fastTradeOrderOptions.getMaxBuyAmount()) > 0){
//            throw new ServiceException()("最小买入金额不允许大于最大买入金额");
//        }
        return toAjax(fastTradeOrderOptionsService.updateFastTradeOrderOptions(fastTradeOrderOptions));
    }

    /**
     * 批量修改极速交易下单选项
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrderOptions:edit')")
    @Log(title = "批量修改极速交易下单选项", businessType = BusinessType.UPDATE,dict = FastTradeOrderOptionsLogDict.class,
            saveParamNames = {"ids","productCode","productType","durationValue","durationLabel","status","upWinProfitRatio","upLoseProfitRatio","downWinProfitRatio","downFluctuationRatio","minBuyAmount","maxBuyAmount","minUserAmount","profitRatioMethod","loseMoneyMethod","vipLevelLimit"})
    @RepeatSubmit
    @PostMapping("batchUpdate")
    public AjaxResult batchUpdate(@RequestBody FastTradeOrderOptions fastTradeOrderOptions)
    {
        if (fastTradeOrderOptions.getIds() == null || fastTradeOrderOptions.getIds().size() == 0){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (fastTradeOrderOptions.getDurationValue() == null){
            throw new ServiceException("请输入下单选项的时长");
        }
        if (fastTradeOrderOptions.getDurationValue() <= 0){
            throw new ServiceException("下单选项的时长必须的大于0");
        }
        if (fastTradeOrderOptions.getDurationLabel() == null){
            throw new ServiceException("请输入下单选项的时间单位");
        }
        if (fastTradeOrderOptions.getUpWinProfitRatio() == null){
            throw new ServiceException("请输入下单选项的涨赢收益率");
        }
        if (fastTradeOrderOptions.getUpLoseProfitRatio() == null){
            throw new ServiceException("请输入下单选项的涨输扣除率");
        }
        if (fastTradeOrderOptions.getDownWinProfitRatio() == null){
            throw new ServiceException("请输入下单选项的跌赢收益率");
        }
        if (fastTradeOrderOptions.getDownLoseProfitRatio() == null){
            throw new ServiceException("请输入下单选项的跌输扣除率");
        }
        if (fastTradeOrderOptions.getUpWinProfitRatio().compareTo(BigDecimal.ZERO) <= 0 || fastTradeOrderOptions.getDownWinProfitRatio().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("收益率必须大于0");
        }
        if (fastTradeOrderOptions.getUpLoseProfitRatio().compareTo(BigDecimal.ZERO) <= 0 || fastTradeOrderOptions.getDownLoseProfitRatio().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("扣除率必须大于0");
        }
        if (fastTradeOrderOptions.getMinBuyAmount() == null){
            throw new ServiceException("请输入下单选项的最小买入金额");
        }
        if (fastTradeOrderOptions.getMinBuyAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("最小买入金额必须大于0");
        }
//        if (fastTradeOrderOptions.getMaxBuyAmount() == null){
//            throw new ServiceException()("请输入下单选项的最大买入金额");
//        }
//        if (fastTradeOrderOptions.getMinBuyAmount().compareTo(fastTradeOrderOptions.getMaxBuyAmount()) > 0){
//            throw new ServiceException()("最小买入金额不允许大于最大买入金额");
//        }
        return toAjax(fastTradeOrderOptionsService.batchUpdateUpdateFastTradeOrderOptions(fastTradeOrderOptions));
    }

    /**
     * 删除极速交易下单选项
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrderOptions:remove')")
    @Log(title = "删除极速交易下单选项", businessType = BusinessType.DELETE,dict = FastTradeOrderOptionsLogDict.class,
            saveParamNames = {"id","productCode","productType","durationValue","durationLabel","fastTradeOrderOptions"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fastTradeOrderOptionsService.deleteFastTradeOrderOptionsByIds(ids));
    }

    /**
     * 清空极速交易下单选项
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrderOptions:remove')")
    @Log(title = "清空极速交易下单选项", businessType = BusinessType.DELETE,dict = FastTradeOrderOptionsLogDict.class,
            saveParamNames = {"productType"})
    @RepeatSubmit
    @GetMapping(value = "cleanOptions")
    public AjaxResult cleanOptions(Integer productType) {
        if (productType == null){
            throw new ServiceException("请选择产品类型");
        }
        return toAjax(fastTradeOrderOptionsService.cleanOptions(productType));
    }
}
