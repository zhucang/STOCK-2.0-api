package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.BankPropertyConfig;
import com.ruoyi.system.service.IBankPropertyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 银行卡参数字段配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-23
 * 已优化
 */
@RestController
@RequestMapping("/api/bankPropertyConfig")
public class ApiBankPropertyConfigController extends BaseController
{
    @Autowired
    private IBankPropertyConfigService bankPropertyConfigService;

    /**
     * 查询银行卡参数字段配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BankPropertyConfig bankPropertyConfig)
    {
        startPage();
        startOrderBy("sort is null,sort");
        bankPropertyConfig.setIsVisible(0);
        if (bankPropertyConfig.getConfigType() == null){
            bankPropertyConfig.setConfigType(0);
        }
        List<BankPropertyConfig> list = bankPropertyConfigService.selectBankPropertyConfigList(bankPropertyConfig);
        return getDataTable(list);
    }
}
