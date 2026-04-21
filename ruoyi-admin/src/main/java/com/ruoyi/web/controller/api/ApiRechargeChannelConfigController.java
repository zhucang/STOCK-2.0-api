package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BankPropertyConfig;
import com.ruoyi.system.domain.RechargeChannelConfig;
import com.ruoyi.system.mapper.RechargeChannelConfigMapper;
import com.ruoyi.system.service.IBankPropertyConfigService;
import com.ruoyi.system.service.IRechargeChannelConfigService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 充值通道配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@RestController
@RequestMapping("/api/rechargeChannelConfig")
public class ApiRechargeChannelConfigController extends BaseController
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
    @GetMapping("/list")
    public TableDataInfo list(RechargeChannelConfig rechargeChannelConfig)
    {
        startPage();
        startOrderBy("a.sort is null, a.sort");
        // 获取公共的充值通道
        rechargeChannelConfig.setStatus(0);
        List<RechargeChannelConfig> list = rechargeChannelConfigService.selectRechargeChannelConfigList(rechargeChannelConfig);
        if (list.size() > 0) {
            // 用户ID
            Long userId = SecurityUtils.getUserId();
            clearPage();
            // 获取用户定制充值通道信息
            RechargeChannelConfig rechargeChannelConfigSearch = new RechargeChannelConfig();
            rechargeChannelConfigSearch.setUserId(userId);
            List<RechargeChannelConfig> userChannel = rechargeChannelConfigService.selectRechargeChannelConfigList(rechargeChannelConfigSearch);
            //
            if (userChannel.size() > 0) {
                // 按 originalChannelId 建立映射
                Map<Long, RechargeChannelConfig> userChannelMap = userChannel.stream()
                        .filter(item -> item.getOriginalChannelId() != null)
                        .collect(Collectors.toMap(
                                RechargeChannelConfig::getOriginalChannelId,
                                Function.identity(),
                                (v1, v2) -> v1
                        ));
                // 覆盖公共通道
                List<RechargeChannelConfig> result = new ArrayList<>();
                for (RechargeChannelConfig publicChannel : list) {
                    RechargeChannelConfig customChannel = userChannelMap.get(publicChannel.getId());
                    if (customChannel != null) {
                        // 如果你的用户定制数据是一整条完整数据，直接替换即可
                        result.add(customChannel);
                    } else {
                        result.add(publicChannel);
                    }
                }
                list = result;
            }
        }

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
            //如果是电汇
            if (rechargeChannelConfigVo.getChannelType().equals(1)){
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
        }
        return getDataTable(list);
    }
}
