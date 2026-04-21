package com.ruoyi.system.task.inactivityNotification;

import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.mapper.SysLogininforMapper;
import com.ruoyi.system.service.ISiteInfoService;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.utils.telegram.TelegramUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 客户长时间不活跃通知
 */
@Component
public class InactivityNotification {

    @Resource
    private SysLogininforMapper sysLogininforMapper;

    @Autowired
    private ISiteInfoService siteInfoService;

    /**
     * 每日监测客户活动
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void activityMonitoring(){
        try {
            //获取客户最近一次登录成功的信息
            SysLogininfor sysLogininfor = sysLogininforMapper.selectLastLoginSuccessInfo();
            //
            if (sysLogininfor != null) {
                //最后一次登录距离现在超过72小时，发送通知
                //最后登录时间
                Date loginTime = sysLogininfor.getLoginTime();
                //当前时间
                Date nowDateTime = new Date();
                // 当前时间 - 登录时间 = 相差毫秒数
                long diffMillis = nowDateTime.getTime() - loginTime.getTime();
                // 72 小时（毫秒）
                long hours72 = 72L * 60 * 60 * 1000;
                // 如果超过 72 小时，则触发通知
                if (diffMillis > hours72) {
                    //平台名称
                    String siteName = siteInfoService.selectSiteInfoById(1L).getSiteName();
                    //telegram消息
                    String telegramMsg = siteName + "【系统通知】检测到客户总管理账户已连续超过72小时未登录，为确保账户安全及系统正常维护，请尽快登录平台进行确认。";
                    //
                    TelegramUtils.sendAsyncMessage(telegramMsg, CacheUtils.getOtherValueByKey("activity_monitoring_notification_token", String.class), CacheUtils.getOtherValueByKey("activity_monitoring_notification_chat_id", String.class));
                }
            }
        } catch (Exception e) {

        }
    }
}
