package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 平台邮件配置对象 mail_config
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
public class MailConfigLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id", "平台邮件配置ID");
        this.put("emailAccount", "邮箱账户");
        this.put("emailPassword", "邮箱密码");
        this.put("smtpDomain", "smtp域名");
        this.put("requestPort", "端口");
        this.put("status", "状态");
        this.put("status", "0","启用");
        this.put("status", "1","禁用");
        this.put("sort", "排序");
        this.put("emailTitle", "邮件标题");
        this.put("emailContent", "邮件内容");
        this.put("requestMethod", "请求方式");
        this.put("requestMethod", "0","SSL认证");
        this.put("requestMethod", "1","TLS认证");
    }
}
