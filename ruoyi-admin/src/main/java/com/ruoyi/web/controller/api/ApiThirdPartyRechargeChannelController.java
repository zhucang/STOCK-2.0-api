package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.ThirdPartyRechargeChannel;
import com.ruoyi.system.service.IThirdPartyRechargeChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 第三方充值通道配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
@RestController
@RequestMapping("/api/thirdPartyRechargeChannel")
public class ApiThirdPartyRechargeChannelController extends BaseController
{
    @Autowired
    private IThirdPartyRechargeChannelService thirdPartyRechargeChannelService;

    /**
     * 查询第三方充值通道配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ThirdPartyRechargeChannel thirdPartyRechargeChannel)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<ThirdPartyRechargeChannel> list = thirdPartyRechargeChannelService.selectThirdPartyRechargeChannelList(thirdPartyRechargeChannel);
        return getDataTable(list);
    }
}
