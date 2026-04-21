package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.cmd.RunBatUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.security.filter.MyCorsFilter;
import com.ruoyi.system.domain.NginxConfig;
import com.ruoyi.system.service.INginxConfigService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * nginx配置转发Controller
 * 
 * @author ruoyi
 * @date 2024-04-23
 */
@RestController
@RequestMapping("/system/nginxConfig")
public class NginxConfigController extends BaseController
{
    @Autowired
    private INginxConfigService nginxConfigService;

    /**
     * 查询nginx配置转发列表
     */
    @PreAuthorize("@ss.hasPermi('system:nginxConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(NginxConfig nginxConfig)
    {
        startPage();
        List<NginxConfig> list = nginxConfigService.selectNginxConfigList(nginxConfig);
        return getDataTable(list);
    }

    /**
     * 导出nginx配置转发列表
     */
    @PreAuthorize("@ss.hasPermi('system:nginxConfig:export')")
    @Log(title = "nginx配置转发", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NginxConfig nginxConfig)
    {
        List<NginxConfig> list = nginxConfigService.selectNginxConfigList(nginxConfig);
        ExcelUtil<NginxConfig> util = new ExcelUtil<NginxConfig>(NginxConfig.class);
        util.exportExcel(response, list, "nginx配置转发数据");
    }

    /**
     * 获取nginx配置转发详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:nginxConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(nginxConfigService.selectNginxConfigById(id));
    }

    /**
     * 新增nginx配置转发
     */
    @PreAuthorize("@ss.hasPermi('system:nginxConfig:add')")
    @Log(title = "nginx配置转发", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NginxConfig nginxConfig)
    {
        if (StringUtils.isEmpty(nginxConfig.getServerName())){
            return AjaxResult.error("请输入服务名称");
        }
        if (nginxConfig.getConfigType() == null){
            return AjaxResult.error("请选择配置类型类型");
        }
        if (StringUtils.isEmpty(nginxConfig.getConfigContent())){
            return AjaxResult.error("请输入配置内容");
        }
        return toAjax(nginxConfigService.insertNginxConfig(nginxConfig));
    }

    /**
     * 修改nginx配置转发
     */
    @PreAuthorize("@ss.hasPermi('system:nginxConfig:edit')")
    @Log(title = "nginx配置转发", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NginxConfig nginxConfig)
    {
        if (nginxConfig.getId() == null){
            return AjaxResult.error("请选择徐璈修改的选项");
        }
        if (StringUtils.isEmpty(nginxConfig.getServerName())){
            return AjaxResult.error("请输入服务名称");
        }
        if (nginxConfig.getConfigType() == null){
            return AjaxResult.error("请选择配置类型类型");
        }
        if (StringUtils.isEmpty(nginxConfig.getConfigContent())){
            return AjaxResult.error("请输入配置内容");
        }
        return toAjax(nginxConfigService.updateNginxConfig(nginxConfig));
    }

    /**
     * 删除nginx配置转发
     */
    @PreAuthorize("@ss.hasPermi('system:nginxConfig:remove')")
    @Log(title = "nginx配置转发", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        if (Arrays.asList(ids).stream().filter(a->a<=7).count() > 0){
            return AjaxResult.error("不允许删除基础数据");
        }
        return toAjax(nginxConfigService.deleteNginxConfigByIds(ids));
    }

    /**
     * 重新加载nginx配置
     */
    @PreAuthorize("@ss.hasPermi('system:nginxConfig:reloadConfig')")
    @Log(title = "重新加载nginx配置", businessType = BusinessType.UPDATE)
    @GetMapping("/reloadConfig")
    public AjaxResult reloadConfig(HttpServletResponse response) throws Exception {
        return toAjax(reloadConfig());
    }

    public int reloadConfig() throws Exception{
        //host
        String host = SpringUtils.getRequiredProperty("middle.host");
        //如果是测试环境
        if ("localhost".equals(host) || "dev".equals(host)){
            reloadCorsFilter("*");
            return 1;
        }
        //ng配置
        List<NginxConfig> nginxConfigs = nginxConfigService.selectNginxConfigList(new NginxConfig());
        //重新加载域名白名单
        reloadCorsFilter(nginxConfigs.stream().map(NginxConfig::getServerName).collect(Collectors.joining(",")));
        //片段
        String fragments = nginxConfigs.stream().map(a -> a.getConfigContent().replace("${domain}", a.getServerName())).collect(Collectors.joining("\n"));
        BufferedReader in = null;
        BufferedWriter out = null;
        try {
            InputStream is=this.getClass().getResourceAsStream("/init/temp/nginx_temp.conf");
            in = new BufferedReader(new InputStreamReader(is));
            out = new BufferedWriter(new FileWriter("C:\\peizi-2.0\\environment\\nginx-1.20.2\\conf\\nginx.conf"));
            String str;
            while ((str = in.readLine()) != null) {
                if ("    ${content}".equals(str)){
                    str = fragments;
                }
                out.write(str);
                out.newLine();
            }
        } catch (IOException e) {
            throw new ServiceException("复刻配置模板失败");
        }finally {
            if (in != null){
                in.close();
            }
            if (out != null){
                out.close();
            }
        }
        //执行bat文件
        //bat文件路径
        String batPath = "/init/nginx_init.bat"; // 把你的bat脚本路径写在这里
        //bat文件流
        InputStream batFileInputStream = this.getClass().getResourceAsStream(batPath);
        if (batFileInputStream != null) {
            //过渡文件夹url
            String fileTransition = RuoYiConfig.getUploadPath() + "/fileTransition";
            File fileTransitionFile = new File(fileTransition);
            if (!fileTransitionFile.exists()){
                fileTransitionFile.mkdirs();
            }
            //执行的bat文件路径
            String exePath = fileTransition + "/" + "nginx_init.bat";
            FileUtils.copyInputStreamToFile(batFileInputStream,new File(exePath));
            batFileInputStream.close();
            try {
                RunBatUtils.callCmd(exePath);
            }catch (Exception e){
                throw new ServiceException("执行bat文件异常");
            }
        }else {
            throw new ServiceException("获取bat文件异常");
        }
        return 1;
    }

    /**
     * 重新加载服务器的域名白名单
     * @param allowedOriginPattern 跨域域名白名单
     * @return
     */
    public int reloadCorsFilter(String allowedOriginPattern){
        //重新设置服务器的域名白名单
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        //host
        if (StringUtils.isNotEmpty(allowedOriginPattern)){
            String[] domains = allowedOriginPattern.split(",");
            for (int i = 0; i < domains.length; i++) {
                //服务名称
                String serverName = domains[i];
                if ("*".equals(serverName) || serverName.contains("http://") || serverName.contains("https://") || serverName.contains("ws://") || serverName.contains("wss://")){
                    config.addAllowedOriginPattern(domains[i]);
                }else {
                    config.addAllowedOriginPattern("https://"+domains[i]);
                }
            }
        }
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        //重置跨域过滤
        MyCorsFilter myCorsFilter = SpringUtils.getBean(MyCorsFilter.class);
        myCorsFilter.setConfigSource(source);
        return 1;
    }
}
