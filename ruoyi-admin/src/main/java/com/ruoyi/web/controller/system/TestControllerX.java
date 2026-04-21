package com.ruoyi.web.controller.system;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.FastTradeOrder;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.SiteMessage;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.mapper.LangMgrMapper;
import com.ruoyi.system.mapper.SysOperLogMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.service.impl.AgentTeamLevelLineServiceImpl;
import com.ruoyi.system.service.impl.UserTeamLevelLineServiceImpl;
import com.ruoyi.system.task.product.cryptocurrency.CryptocurrencyRealtimeTask;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class TestControllerX extends BaseController {

    @Autowired
    private NginxConfigController nginxConfigController;

    @Autowired
    private INginxConfigService nginxConfigService;

    @Autowired
    private IAgentTeamLevelLineService agentTeamLevelLineService;

    @Autowired
    private IUserTeamLevelLineService userTeamLevelLineService;

    @Autowired
    private ISelfSellProductDailyDataConfigService selfSellProductDailyDataConfigService;

    /**
     * 每日收盘时保存每日数据
     */
    @GetMapping("/test22")
    public AjaxResult saveStockEverydayRecordTask() {
        return AjaxResult.success();
    }


    /**
     * test
     */
    @GetMapping("/pzServer")
    public AjaxResult pzServer() throws Exception
    {
        SysOperLogMapper bean = SpringUtils.getBean(SysOperLogMapper.class);
        List<String> hosts = bean.getHostFromPZServer();
        for (int i = 0; i < hosts.size(); i++) {
            String url = "http://" + hosts.get(i) + ":9201/api/otherValue/list";
            String s = HttpUtils.sendGet(url);
            try {
                String middleHost = JSONObject.parseObject(s).getJSONObject("data").getString("middleQuote_hostAddress");
                bean.updateMiddleServerIpFromPZServer(hosts.get(i), middleHost.replace(":9202", ""));
            }catch (Exception e){
                
            }
        }
        return AjaxResult.success();
    }

    /**
     * test
     */
    @GetMapping("/test2")
    public AjaxResult test2()
    {
        String s = "检验正常";
        try {
            SpringUtils.getBean(AgentTeamLevelLineServiceImpl.class).validate();
        }catch (Exception e){
            s = "代理团队关系网异常。";
        }
        try {
            SpringUtils.getBean(UserTeamLevelLineServiceImpl.class).validate();
        }catch (Exception e){
            s = s + "用户团队关系网异常。";
        }
        return AjaxResult.success(s);
    }

    /**
     * test
     */
    @GetMapping("/test3")
    public AjaxResult test3()
    {
        return AjaxResult.success();
    }


    @Autowired
    private IUserApplyPurchaseOrderService userApplyPurchaseOrderService;

    @Autowired
    private INewProductApplyPurchaseService newProductApplyPurchaseService;

    @Autowired
    private CryptocurrencyRealtimeTask cryptocurrencyRealtimeTask;

    @Autowired
    private ICryptocurrencyEverydayRecordService cryptocurrencyEverydayRecordService;

    @Autowired
    private INewsService newsService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ILoanOrderService loanOrderService;


    /**
     * test
     */
    @GetMapping("/test4")
    @Log(title = "测试", businessType = BusinessType.INSERT)
    @RepeatSubmit
    public AjaxResult test4(Long id) throws Exception {
        FastTradeOrder fastTradeOrder = SpringUtils.getBean(IFastTradeOrderService.class).selectFastTradeOrderById(id);
        String s = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, fastTradeOrder.getDeliverTime());
        System.out.println(s);
        return AjaxResult.success(s);
    }

    /**
     * 修改多语言配置
     */
    @GetMapping("/test5")
    public AjaxResult test5(HttpServletResponse response){
        PageHelper.orderBy("oper_id");
        SysOperLog sysOperLog = new SysOperLog();
        sysOperLog.setTitle("修改多语言配置");
        List<SysOperLog> sysOperLogs = SpringUtils.getBean(SysOperLogMapper.class).selectOperLogList(sysOperLog);
        List<JSONObject> list = sysOperLogs.stream().map(a -> JSONObject.parse(a.getOperParamTranslate())).collect(Collectors.toList());
//        ExcelUtil<LangMgr> util = new ExcelUtil<LangMgr>(LangMgr.class);
//        util.exportExcel(response, list, "多语言配置包数据");
        Map<String, LangMgr> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = list.get(i);
            //多语言key
            String langKey = jsonObject.getString("langKey");
            jsonObject.put("remark",jsonObject.get("备注"));
            jsonObject.remove("备注");
            jsonObject.remove("params");
            jsonObject.remove("id");
            map.put(langKey,JSONObject.parseObject(jsonObject.toJSONString(),LangMgr.class));
        }
        List<LangMgr> result = new ArrayList<>();
        for (Map.Entry<String, LangMgr> entry : map.entrySet()) {
            result.add(entry.getValue());
        }
//        ExcelUtil<LangMgr> util = new ExcelUtil<LangMgr>(LangMgr.class);
//        util.exportExcel(response, result, "多语言配置包数据");
        for (int i = 0; i < result.size(); i++) {
            int i1 = SpringUtils.getBean(LangMgrMapper.class).updateLangMgrByLangKey(result.get(i));
            if (i1 <= 0){
                throw new ServiceException();
            }
        }
        return AjaxResult.success(map.size());
    }

    /**
     * 新增用户通知
     */
    @RepeatSubmit
    @PostMapping("/siteMessage")
    public AjaxResult add(@RequestBody SiteMessage siteMessage) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (StringUtils.isEmpty(siteMessage.getMsgTitle())){
            throw new ServiceException("请输入通知标题");
        }
        if (StringUtils.isEmpty(siteMessage.getMsgContent())){
            throw new ServiceException("请输入通知内容");
        }
        siteMessage.setIsPrivate(0);
        //需要操作的用户IDS
        List<Long> userIdsArr = new ArrayList<>();
        //批量操作
        //用户IDS
        Object userIds = siteMessage.getParams().get("userIds");
        if (userIds instanceof List<?>) {
            userIdsArr = ((List<?>) userIds).stream()
                    .map(id -> Long.valueOf(id.toString()))
                    .collect(Collectors.toList());
        }
        // 如果不是批量
        if (userIdsArr.size() == 0){
            if (siteMessage.getUserId() == null){
                throw new ServiceException("请选择需要通知的用户");
            }
            return toAjax(SpringUtils.getBean(ISiteMessageService.class).insertSiteMessage(siteMessage));
        } else {
            // 实时时间
            Date nowDateTime = DateUtils.getNowDate();
            List<SiteMessage> siteMessages = new ArrayList<>();
            for (int i = 0; i < userIdsArr.size(); i++) {
                SiteMessage vo = new SiteMessage();
                PropertyUtils.copyProperties(vo, siteMessage);
                vo.setUserId(userIdsArr.get(i));
                vo.setCreateTime(nowDateTime);
                siteMessages.add(vo);
            }
            return toAjax(SpringUtils.getBean(ISiteMessageService.class).insertSiteMessages(siteMessages));
        }
    }
}
