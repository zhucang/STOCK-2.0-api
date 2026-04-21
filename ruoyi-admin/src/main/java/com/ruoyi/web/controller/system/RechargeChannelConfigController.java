package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.RechargeChannelConfigLogDict;
import com.ruoyi.system.domain.BankPropertyConfig;
import com.ruoyi.system.domain.RechargeChannelConfig;
import com.ruoyi.system.mapper.RechargeChannelConfigMapper;
import com.ruoyi.system.service.IBankPropertyConfigService;
import com.ruoyi.system.service.IRechargeChannelConfigService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 充值通道配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@RestController
@RequestMapping("/system/rechargeChannelConfig")
public class RechargeChannelConfigController extends BaseController
{
    @Autowired
    private IRechargeChannelConfigService rechargeChannelConfigService;

    @Autowired
    private IBankPropertyConfigService bankPropertyConfigService;

    @Resource
    private RechargeChannelConfigMapper rechargeChannelConfigMapper;

    /**
     * 查询充值通道配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeChannelConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(RechargeChannelConfig rechargeChannelConfig)
    {
        startPage();
        startOrderBy("a.sort is null, a.sort");
        List<RechargeChannelConfig> list = rechargeChannelConfigService.selectRechargeChannelConfigList(rechargeChannelConfig);

        TableDataInfo dataTable = getDataTable(list);
        BankPropertyConfig bankPropertyConfig = new BankPropertyConfig();
        bankPropertyConfig.setIsVisible(0);
        bankPropertyConfig.setConfigType(1);
        startOrderBy("sort is null,sort");
        List<BankPropertyConfig> bankPropertyConfigs = bankPropertyConfigService.selectBankPropertyConfigList(bankPropertyConfig);
        clearPage();
        //遍历取值
        for (int i = 0; i < list.size(); i++) {
            //充值通道配置信息
            RechargeChannelConfig rechargeChannelConfigVo = list.get(i);
            //银行卡参数配置列表
            List<BankPropertyConfig> bankPropertyConfigList = new ArrayList<>();
            //遍历取值
            for (int j = 0; j < bankPropertyConfigs.size(); j++) {
                //银行卡配置信息
                BankPropertyConfig bankPropertyConfigVo = new BankPropertyConfig();
                bankPropertyConfigVo.setPropertyName(bankPropertyConfigs.get(j).getPropertyName());
                bankPropertyConfigVo.setLangKey(bankPropertyConfigs.get(j).getLangKey());
                bankPropertyConfigVo.setIsVisible(bankPropertyConfigs.get(j).getIsVisible());
                //银行卡参数名称
                String propertyName = bankPropertyConfigVo.getPropertyName();
                try{
                    //相应的值
                    Object val = PropertyUtils.getProperty(rechargeChannelConfigVo, propertyName);
                    bankPropertyConfigVo.getParams().put("val",val);
                }catch (Exception e){

                }
                bankPropertyConfigList.add(bankPropertyConfigVo);
            }
            rechargeChannelConfigVo.getParams().put("bankPropertyConfigs",bankPropertyConfigList);
        }
        dataTable.getMapData().put("bankPropertyConfigs",bankPropertyConfigs.stream().map(a->{
            a.getParams().put("val",null);
            return a;
        }).collect(Collectors.toList()));
        return dataTable;
    }

    /**
     * 获取充值通道配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeChannelConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        RechargeChannelConfig rechargeChannelConfigVo = rechargeChannelConfigService.selectRechargeChannelConfigById(id);
        BankPropertyConfig bankPropertyConfig = new BankPropertyConfig();
        bankPropertyConfig.setIsVisible(0);
        bankPropertyConfig.setConfigType(1);
        startOrderBy("sort is null,sort");
        List<BankPropertyConfig> bankPropertyConfigs = bankPropertyConfigService.selectBankPropertyConfigList(bankPropertyConfig);
        clearPage();
        //遍历取值
        for (int j = 0; j < bankPropertyConfigs.size(); j++) {
            //银行卡配置信息
            BankPropertyConfig bankPropertyConfigVo = bankPropertyConfigs.get(j);
            //银行卡参数名称
            String propertyName = bankPropertyConfigVo.getPropertyName();
            try{
                //相应的值
                Object val = PropertyUtils.getProperty(rechargeChannelConfigVo, propertyName);
                bankPropertyConfigVo.getParams().put("val",val);
            }catch (Exception e){

            }
        }
        rechargeChannelConfigVo.getParams().put("bankPropertyConfigs",bankPropertyConfigs);
        return success(rechargeChannelConfigVo);
    }

    /**
     * 新增充值通道配置
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeChannelConfig:add')")
    @Log(title = "新增充值通道配置", businessType = BusinessType.INSERT,dict = RechargeChannelConfigLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody RechargeChannelConfig rechargeChannelConfig)
    {
        if (StringUtils.isEmpty(rechargeChannelConfig.getChannelName())){
            throw new ServiceException("请输入通道名称");
        }
        if (StringUtils.isEmpty(rechargeChannelConfig.getChannelImg())){
            throw new ServiceException("请输入通道图片");
        }
        if (rechargeChannelConfig.getChannelType() == null){
            throw new ServiceException("请选择通道类型");
        }
        if (!rechargeChannelConfig.getChannelType().equals(0) && !rechargeChannelConfig.getChannelType().equals(1) && !rechargeChannelConfig.getChannelType().equals(2)){
            throw new ServiceException("请选择正确的通道类型");
        }
        if (!rechargeChannelConfig.getChannelType().equals(2)){
            if (rechargeChannelConfig.getCurrencyId() == null){
                throw new ServiceException("请选择通道币种");
            }
            if (StringUtils.isEmpty(rechargeChannelConfig.getChannelAccount())){
                throw new ServiceException("请输入收款账号");
            }
            if (rechargeChannelConfig.getChannelMinLimit() == null){
                throw new ServiceException("请输入最小入金");
            }
            if (rechargeChannelConfig.getChannelMinLimit().compareTo(BigDecimal.ZERO) <= 0){
                throw new ServiceException("最小入金必须大于0");
            }
            if (rechargeChannelConfig.getChannelMaxLimit() == null){
                throw new ServiceException("请输入最大入金");
            }
            if (rechargeChannelConfig.getChannelMaxLimit().compareTo(rechargeChannelConfig.getChannelMinLimit()) == -1){
                throw new ServiceException("最大入金不允许小于最小入金");
            }
        }else {
            rechargeChannelConfig.setCurrencyId(0L);
        }
        return toAjax(rechargeChannelConfigService.insertRechargeChannelConfig(rechargeChannelConfig));
    }

    /**
     * 修改充值通道配置
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeChannelConfig:edit')")
    @Log(title = "修改充值通道配置", businessType = BusinessType.UPDATE,dict = RechargeChannelConfigLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody RechargeChannelConfig rechargeChannelConfig)
    {
        if (rechargeChannelConfig.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(rechargeChannelConfig.getChannelName())){
            throw new ServiceException("请输入通道名称");
        }
        if (StringUtils.isEmpty(rechargeChannelConfig.getChannelImg())){
            throw new ServiceException("请输入通道图片");
        }
        if (rechargeChannelConfig.getChannelType() == null){
            throw new ServiceException("请选择通道类型");
        }
        if (!rechargeChannelConfig.getChannelType().equals(0) && !rechargeChannelConfig.getChannelType().equals(1) && !rechargeChannelConfig.getChannelType().equals(2)){
            throw new ServiceException("请选择正确的通道类型");
        }
        if (!rechargeChannelConfig.getChannelType().equals(2)){
            if (rechargeChannelConfig.getCurrencyId() == null){
                throw new ServiceException("请选择通道币种");
            }
            if (StringUtils.isEmpty(rechargeChannelConfig.getChannelAccount())){
                throw new ServiceException("请输入收款账号");
            }
            if (rechargeChannelConfig.getChannelMinLimit() == null){
                throw new ServiceException("请输入最小入金");
            }
            if (rechargeChannelConfig.getChannelMinLimit().compareTo(BigDecimal.ZERO) <= 0){
                throw new ServiceException("最小入金必须大于0");
            }
            if (rechargeChannelConfig.getChannelMaxLimit() == null){
                throw new ServiceException("请输入最大入金");
            }
            if (rechargeChannelConfig.getChannelMaxLimit().compareTo(rechargeChannelConfig.getChannelMinLimit()) == -1){
                throw new ServiceException("最大入金不允许小于最小入金");
            }
        }else {
            rechargeChannelConfig.setCurrencyId(0L);
        }
        return toAjax(rechargeChannelConfigService.updateRechargeChannelConfig(rechargeChannelConfig));
    }

    /**
     * 修改充值通道名称多语言配置
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeChannelConfig:edit')")
    @Log(title = "修改充值通道名称多语言配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateChannelNameLang")
    public AjaxResult updateChannelNameLang(@RequestBody RechargeChannelConfig rechargeChannelConfig)
    {
        if (rechargeChannelConfig.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(rechargeChannelConfig.getChannelNameLang().getZh())){
            throw new ServiceException("请输入通道名称");
        }
        return toAjax(rechargeChannelConfigService.updateChannelNameLang(rechargeChannelConfig.getId(),rechargeChannelConfig.getChannelNameLang()));
    }

    /**
     * 删除充值通道配置
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeChannelConfig:remove')")
    @Log(title = "删除充值通道配置", businessType = BusinessType.DELETE,dict = RechargeChannelConfigLogDict.class,
            saveParamNames = {"id","channelName","rechargeChannelConfigs"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(rechargeChannelConfigService.deleteRechargeChannelConfigByIds(ids));
    }
}
