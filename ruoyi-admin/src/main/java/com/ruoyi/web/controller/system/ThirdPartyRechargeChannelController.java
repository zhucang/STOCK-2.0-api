package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.ThirdPartyRechargeChannelLogDict;
import com.ruoyi.system.domain.ThirdPartyRechargeChannel;
import com.ruoyi.system.service.IThirdPartyRechargeChannelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 第三方充值通道配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
@RestController
@RequestMapping("/system/thirdPartyRechargeChannel")
public class ThirdPartyRechargeChannelController extends BaseController
{
    @Autowired
    private IThirdPartyRechargeChannelService thirdPartyRechargeChannelService;

    /**
     * 查询第三方充值通道配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:thirdPartyRechargeChannel:list')")
    @GetMapping("/list")
    public TableDataInfo list(ThirdPartyRechargeChannel thirdPartyRechargeChannel)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<ThirdPartyRechargeChannel> list = thirdPartyRechargeChannelService.selectThirdPartyRechargeChannelList(thirdPartyRechargeChannel);
        return getDataTable(list);
    }

    /**
     * 获取第三方充值通道配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:thirdPartyRechargeChannel:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(thirdPartyRechargeChannelService.selectThirdPartyRechargeChannelById(id));
    }

    /**
     * 新增第三方充值通道配置
     */
    @PreAuthorize("@ss.hasPermi('system:thirdPartyRechargeChannel:add')")
    @Log(title = "新增第三方充值通道配置", businessType = BusinessType.INSERT,dict = ThirdPartyRechargeChannelLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody ThirdPartyRechargeChannel thirdPartyRechargeChannel)
    {
        if (thirdPartyRechargeChannel.getChannelName() == null){
            throw new ServiceException("请输入通道名称");
        }
        if (thirdPartyRechargeChannel.getChannelImg() == null){
            throw new ServiceException("请上传通道图片");
        }
        if (thirdPartyRechargeChannel.getJumpUrl() == null){
            throw new ServiceException("请输入跳转路径");
        }
        return toAjax(thirdPartyRechargeChannelService.insertThirdPartyRechargeChannel(thirdPartyRechargeChannel));
    }

    /**
     * 修改第三方充值通道配置
     */
    @PreAuthorize("@ss.hasPermi('system:thirdPartyRechargeChannel:edit')")
    @Log(title = "修改第三方充值通道配置", businessType = BusinessType.UPDATE,dict = ThirdPartyRechargeChannelLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody ThirdPartyRechargeChannel thirdPartyRechargeChannel)
    {
        if (thirdPartyRechargeChannel.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (thirdPartyRechargeChannel.getChannelName() == null){
            throw new ServiceException("请输入通道名称");
        }
        if (thirdPartyRechargeChannel.getChannelImg() == null){
            throw new ServiceException("请上传通道图片");
        }
        if (thirdPartyRechargeChannel.getJumpUrl() == null){
            throw new ServiceException("请输入跳转路径");
        }
        return toAjax(thirdPartyRechargeChannelService.updateThirdPartyRechargeChannel(thirdPartyRechargeChannel));
    }

    /**
     * 修改第三方充值通道名称多语言
     */
    @PreAuthorize("@ss.hasPermi('system:thirdPartyRechargeChannel:edit')")
    @Log(title = "修改第三方充值通道名称多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateChannelNameLang")
    public AjaxResult updateChannelNameLang(@RequestBody ThirdPartyRechargeChannel thirdPartyRechargeChannel)
    {
        if (thirdPartyRechargeChannel.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(thirdPartyRechargeChannel.getChannelNameLang().getZh())){
            throw new ServiceException("请输入通道名称");
        }
        return toAjax(thirdPartyRechargeChannelService.updateChannelNameLang(thirdPartyRechargeChannel.getId(),thirdPartyRechargeChannel.getChannelNameLang()));
    }

    /**
     * 删除第三方充值通道配置
     */
    @PreAuthorize("@ss.hasPermi('system:thirdPartyRechargeChannel:remove')")
    @Log(title = "删除第三方充值通道配置", businessType = BusinessType.DELETE,dict = ThirdPartyRechargeChannelLogDict.class,
            saveParamNames = {"id","channelName","thirdPartyRechargeChannels"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(thirdPartyRechargeChannelService.deleteThirdPartyRechargeChannelByIds(ids));
    }
}
