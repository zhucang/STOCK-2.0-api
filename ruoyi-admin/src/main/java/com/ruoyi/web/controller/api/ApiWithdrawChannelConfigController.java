package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.WithdrawChannelConfig;
import com.ruoyi.system.service.IWithdrawChannelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提现通道配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@RestController
@RequestMapping("/api/withdrawChannelConfig")
public class ApiWithdrawChannelConfigController extends BaseController
{
    @Autowired
    private IWithdrawChannelConfigService withdrawChannelConfigService;

    /**
     * 查询提现通道配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(WithdrawChannelConfig withdrawChannelConfig)
    {
        startPage();
        startOrderBy("a.sort is null,a.sort");
        withdrawChannelConfig.setStatus(0);
        List<WithdrawChannelConfig> list = withdrawChannelConfigService.selectWithdrawChannelConfigList(withdrawChannelConfig);
        return getDataTable(list);
    }
}
