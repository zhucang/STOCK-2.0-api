package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.SiteInfoLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.system.domain.SiteInfo;
import com.ruoyi.system.service.ISiteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 平台基本信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-13
 */
@RestController
@RequestMapping("/system/siteInfo")
public class SiteInfoController extends BaseController
{
    @Autowired
    private ISiteInfoService siteInfoService;

    /**
     * 查询平台基本信息列表
     */
    @GetMapping("/getSiteInfo")
    public AjaxResult getSiteInfo()
    {
        return AjaxResult.success(siteInfoService.selectSiteInfoById(1L));
    }

    /**
     * 修改平台基本信息
     */
    @PreAuthorize("@ss.hasPermi('system:siteInfo:updateSiteInfo')")
    @Log(title = "修改平台基本信息", businessType = BusinessType.UPDATE,dict = SiteInfoLogDict.class)
    @RepeatSubmit
    @PostMapping("/updateSiteInfo")
    public AjaxResult updateSiteInfo(@RequestBody SiteInfo siteInfo)
    {
        if (siteInfo.getId() == null){
            siteInfo.setId(1L);
        }
        if (StringUtils.isEmpty(siteInfo.getSiteName())){
            throw new ServiceException("请输入平台名称");
        }
        return AjaxResult.success(siteInfoService.updateSiteInfo(siteInfo));
    }

    /**
     * 上传描述文件
     */
    @PreAuthorize("@ss.hasPermi('system:siteInfo:updateSiteInfo')")
    @PostMapping("/updateDescribeFile")
    public AjaxResult updateDescribeFile(MultipartFile file, String domain, Integer fileType)
    {
        if (StringUtils.isEmpty(domain)){
            throw new ServiceException("请输入访问域名");
        }
        if (fileType == null){
            throw new ServiceException("请选择上传类型");
        }
        try {
            //文件名称
            String fileName = FileUploadUtils.upload("C:/web-vue/download", file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION,1);
            //下载访问路径
            String downLoadUrl = domain + "/download/" + fileName;
            SiteInfo siteInfo = new SiteInfo();
            siteInfo.setId(1L);
            if (fileType.equals(1)){
                siteInfo.setIosDownloadUrl(downLoadUrl);
            }else if (fileType.equals(2)){
                siteInfo.setAndroidDownloadUrl(downLoadUrl);
            }else if (fileType.equals(3)){
                siteInfo.setAndroidApkDownloadUrl(downLoadUrl);
            }else {
                throw new ServiceException("上传类型错误");
            }
            int count = siteInfoService.updateSiteInfo(siteInfo);
            if (count <= 0){
                throw new ServiceException("系统繁忙");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return AjaxResult.success();
    }

}
