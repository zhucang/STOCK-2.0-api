package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.AppConfigLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.ZipUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.AppConfig;
import com.ruoyi.system.service.IAppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * app配置Controller
 * 
 * @author ruoyi
 * @date 2023-12-20
 * 日志优化完成
 */
@RestController
@RequestMapping("/system/appConfig")
public class AppConfigController extends BaseController
{
    @Autowired
    private IAppConfigService appConfigService;

    /**
     * 查询app配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:appConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(AppConfig appConfig)
    {
        startPage();
        List<AppConfig> list = appConfigService.selectAppConfigList(appConfig);
        return getDataTable(list);
    }

    /**
     * 导出app配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:appConfig:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, AppConfig appConfig)
    {
        List<AppConfig> list = appConfigService.selectAppConfigList(appConfig);
        ExcelUtil<AppConfig> util = new ExcelUtil<AppConfig>(AppConfig.class);
        util.exportExcel(response, list, "app配置数据");
    }

    /**
     * 导入app配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:appConfig:import')")
    @Log(title = "导入app配置列表", businessType = BusinessType.IMPORT)
    @RepeatSubmit
    @PostMapping("/import")
    public AjaxResult importData(@RequestParam("file") MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<AppConfig> util = new ExcelUtil<AppConfig>(AppConfig.class);
        List<AppConfig> list = util.importExcel(file.getInputStream());
        String message = appConfigService.importAppConfig(list, updateSupport);
        return AjaxResult.success(message);
    }

    /**
     * 导出app配置图片
     */
    @PreAuthorize("@ss.hasPermi('system:appConfig:export')")
    @PostMapping("/exportImg")
    public void exportImg(HttpServletResponse response, AppConfig appConfig){
        //app配置列表
        List<AppConfig> list = appConfigService.selectAppConfigList(appConfig);
        //图片路径列表
        List<String> imgList = list.stream().map(AppConfig::getImgUrl).collect(Collectors.toList());
        List<String> imgList2 = list.stream().map(AppConfig::getImgUrl2).collect(Collectors.toList());
        imgList.addAll(imgList2);
        try {
            //需要导出的图片文件列表
            List<File> files = new ArrayList<>();
            //导出的图片压缩zip后的文件路径
            Map<String, String> srcFilesPaths = new HashMap<>();
            //真实上传文件路径
            String uploadPath = RuoYiConfig.getUploadPath() + "/";
            //遍历
            for (int i = 0; i < imgList.size(); i++) {
                //图片路径
                String imgUrl = imgList.get(i);
                //资源上传文件路径
                String mappingUploadPath = Constants.RESOURCE_PREFIX + "/upload/";
                //存在图片
                if (StringUtils.isNotEmpty(imgUrl) && imgUrl.contains(mappingUploadPath)){
                    //真实路径
                    String realUrl = imgUrl.replace(mappingUploadPath,uploadPath);
                    //读取图片文件
                    File file = new File(realUrl);
                    //文件名
                    String name = file.getName();
                    //path
                    String path = realUrl.replace(uploadPath, "");
                    //添加对应图片压缩zip后的文件路径
                    srcFilesPaths.put(name,path);
                    //加入图片文件
                    files.add(file);
                }
            }
            //过渡文件夹url
            String destZipFileUrl = uploadPath + "fileTransition";
            File destZipFile = new File(destZipFileUrl);
            if (!destZipFile.exists()){
                destZipFile.mkdirs();
            }
            //文件名称
            String fileName = "appConfigImg.zip";
            //zip下载地址
            String downloadPath = destZipFileUrl + "/" + fileName;
            //生成zip文件
            ZipUtils.toZip(files.toArray(new File[files.size()]),new File(downloadPath),srcFilesPaths);
            //响应输出zip文件
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, fileName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }catch (Exception e){
            throw new ServiceException("下载资源异常");
        }
    }

    /**
     * 导入app配置图片
     */
    @PreAuthorize("@ss.hasPermi('system:appConfig:import')")
    @Log(title = "导入app配置图片", businessType = BusinessType.IMPORT)
    @RepeatSubmit
    @PostMapping("/importImg")
    public AjaxResult importImg(@RequestParam("file") MultipartFile file, boolean updateSupport) throws Exception
    {
        //解压zip文件
        ZipUtils.unZipFiles(ZipUtils.multipartFileToFile(file),RuoYiConfig.getUploadPath() + "/");
        return AjaxResult.success();
    }

    /**
     * 获取app配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:appConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(appConfigService.selectAppConfigById(id));
    }

    /**
     * 新增app配置
     */
    @PreAuthorize("@ss.hasPermi('system:appConfig:add')")
    @Log(title = "新增app配置", businessType = BusinessType.INSERT,dict = AppConfigLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody AppConfig appConfig)
    {
        return toAjax(appConfigService.insertAppConfig(appConfig));
    }

    /**
     * 修改app配置
     */
    @PreAuthorize("@ss.hasPermi('system:appConfig:edit')")
    @Log(title = "修改app配置", businessType = BusinessType.UPDATE,dict = AppConfigLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody AppConfig appConfig)
    {
        if (appConfig.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        return toAjax(appConfigService.updateAppConfig(appConfig));
    }

    /**
     * 删除app配置
     */
    @PreAuthorize("@ss.hasPermi('system:appConfig:remove')")
    @Log(title = "删除app配置", businessType = BusinessType.DELETE,dict = AppConfigLogDict.class)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(appConfigService.deleteAppConfigByIds(ids));
    }
}
