package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.WithdrawChannelConfigLogDict;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.WithdrawChannelConfig;
import com.ruoyi.system.service.IWithdrawChannelConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提现通道配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@RestController
@RequestMapping("/system/withdrawChannelConfig")
public class WithdrawChannelConfigController extends BaseController
{
    @Autowired
    private IWithdrawChannelConfigService withdrawChannelConfigService;

    /**
     * 查询提现通道配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawChannelConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(WithdrawChannelConfig withdrawChannelConfig)
    {
        startPage();
        startOrderBy("a.sort is null,a.sort");
        List<WithdrawChannelConfig> list = withdrawChannelConfigService.selectWithdrawChannelConfigList(withdrawChannelConfig);
        return getDataTable(list);
    }

    /**
     * 获取提现通道配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawChannelConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(withdrawChannelConfigService.selectWithdrawChannelConfigById(id));
    }

    /**
     * 新增提现通道配置
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawChannelConfig:add')")
    @Log(title = "新增提现通道配置", businessType = BusinessType.INSERT,dict = WithdrawChannelConfigLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody WithdrawChannelConfig withdrawChannelConfig)
    {
        if (withdrawChannelConfig.getWithdrawMinAmount() == null){
            throw new ServiceException("请输入最低提现金额");
        }
        if (withdrawChannelConfig.getWithdrawMinAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("最低提现金额必须大于0");
        }
        if (withdrawChannelConfig.getWithdrawMaxAmount() == null){
            throw new ServiceException("请输入最高提现金额");
        }
        if (withdrawChannelConfig.getWithdrawMaxAmount().compareTo(withdrawChannelConfig.getWithdrawMinAmount()) < 0){
            throw new ServiceException("最高提现金额不能小于最低提现金额");
        }
        if (withdrawChannelConfig.getCurrencyId() == null){
            throw new ServiceException("请选择提现通道的提现币种");
        }
        if (StringUtils.isEmpty(withdrawChannelConfig.getChannelName())){
            throw new ServiceException("请输入提现通道名称");
        }
        if (withdrawChannelConfig.getHandingFeeMethod() == null){
            throw new ServiceException("请选择提现通道的手续费模式");
        }
        Integer withdrawType = withdrawChannelConfig.getWithdrawType();
        if (withdrawType == null){
            throw new ServiceException("请选择提现通道的提现类型");
        }
        if (!withdrawType.equals(0) && !withdrawType.equals(1) && !withdrawType.equals(2)){
            throw new ServiceException("提现通道的提现类型错误");
        }
        if (withdrawChannelConfig.getHandingFeeFixedAmount() == null){
            throw new ServiceException("请输入提现固定手续费金额");
        }
        if (withdrawChannelConfig.getHandingFeeFixedAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("提现固定手续费金额不允许小于0");
        }
        if (withdrawChannelConfig.getHandingFeeRate() == null){
            throw new ServiceException("请输入提现手续费费率");
        }
        if (withdrawChannelConfig.getHandingFeeRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("提现手续费费率不允许小于0");
        }
        if (withdrawChannelConfig.getWithdrawCount() == null){
            throw new ServiceException("请输入该通道的每日提现次数");
        }
        if (withdrawChannelConfig.getWithdrawCount() < 0){
            throw new ServiceException("每日提现次数不允许小于0");
        }
        if (withdrawChannelConfig.getWithdrawStartTime() == null){
            throw new ServiceException("请输入每日提现开始时间");
        }
        if (withdrawChannelConfig.getWithdrawEndTime() == null){
            throw new ServiceException("请输入每日提现结束时间");
        }
        if (DateUtils.compTime(withdrawChannelConfig.getWithdrawStartTime(),withdrawChannelConfig.getWithdrawEndTime())){
            throw new ServiceException("提现结束时间不允许比开始时间早");
        }
        return toAjax(withdrawChannelConfigService.insertWithdrawChannelConfig(withdrawChannelConfig));
    }

    /**
     * 修改提现通道配置
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawChannelConfig:edit')")
    @Log(title = "修改提现通道配置", businessType = BusinessType.UPDATE,dict = WithdrawChannelConfigLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody WithdrawChannelConfig withdrawChannelConfig)
    {
        if (withdrawChannelConfig.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (withdrawChannelConfig.getWithdrawMinAmount() == null){
            throw new ServiceException("请输入最低提现金额");
        }
        if (withdrawChannelConfig.getWithdrawMinAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("最低提现金额必须大于0");
        }
        if (withdrawChannelConfig.getWithdrawMaxAmount() == null){
            throw new ServiceException("请输入最高提现金额");
        }
        if (withdrawChannelConfig.getWithdrawMaxAmount().compareTo(withdrawChannelConfig.getWithdrawMinAmount()) < 0){
            throw new ServiceException("最高提现金额不能小于最低提现金额");
        }
        if (withdrawChannelConfig.getCurrencyId() == null){
            throw new ServiceException("请选择提现通道的提现币种");
        }
        if (StringUtils.isEmpty(withdrawChannelConfig.getChannelName())){
            throw new ServiceException("请输入提现通道名称");
        }
        if (withdrawChannelConfig.getHandingFeeMethod() == null){
            throw new ServiceException("请选择提现通道的手续费模式");
        }
        Integer withdrawType = withdrawChannelConfig.getWithdrawType();
        if (withdrawType == null){
            throw new ServiceException("请选择提现通道的提现类型");
        }
        if (!withdrawType.equals(0) && !withdrawType.equals(1) && !withdrawType.equals(2)){
            throw new ServiceException("提现通道的提现类型错误");
        }
        if (withdrawChannelConfig.getHandingFeeFixedAmount() == null){
            throw new ServiceException("请输入提现固定手续费金额");
        }
        if (withdrawChannelConfig.getHandingFeeFixedAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("提现固定手续费金额不允许小于0");
        }
        if (withdrawChannelConfig.getHandingFeeRate() == null){
            throw new ServiceException("请输入提现手续费费率");
        }
        if (withdrawChannelConfig.getHandingFeeRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("提现手续费费率不允许小于0");
        }
        if (withdrawChannelConfig.getWithdrawCount() == null){
            throw new ServiceException("请输入该通道的每日提现次数");
        }
        if (withdrawChannelConfig.getWithdrawCount() < 0){
            throw new ServiceException("每日提现次数不允许小于0");
        }
        if (withdrawChannelConfig.getWithdrawStartTime() == null){
            throw new ServiceException("请输入每日提现开始时间");
        }
        if (withdrawChannelConfig.getWithdrawEndTime() == null){
            throw new ServiceException("请输入每日提现结束时间");
        }
        if (DateUtils.compTime(withdrawChannelConfig.getWithdrawStartTime(),withdrawChannelConfig.getWithdrawEndTime())){
            throw new ServiceException("提现结束时间不允许比开始时间早");
        }
        return toAjax(withdrawChannelConfigService.updateWithdrawChannelConfig(withdrawChannelConfig));
    }

    /**
     * 修改提现通道名称多语言配置
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawChannelConfig:edit')")
    @Log(title = "修改提现通道配置名称多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateChannelNameLang")
    public AjaxResult updateChannelNameLang(@RequestBody WithdrawChannelConfig withdrawChannelConfig)
    {
        if (withdrawChannelConfig.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(withdrawChannelConfig.getChannelNameLang().getZh())){
            throw new ServiceException("请输入通道名称");
        }
        return toAjax(withdrawChannelConfigService.updateChannelNameLang(withdrawChannelConfig.getId(),withdrawChannelConfig.getChannelNameLang()));
    }

    /**
     * 删除提现通道配置
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawChannelConfig:remove')")
    @Log(title = "删除提现通道配置", businessType = BusinessType.DELETE,dict = WithdrawChannelConfigLogDict.class,
        saveParamNames = {"id","channelName","withdrawChannelConfigs"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(withdrawChannelConfigService.deleteWithdrawChannelConfigByIds(ids));
    }
}
