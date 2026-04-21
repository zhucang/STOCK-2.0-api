package com.ruoyi.web.controller.common;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.uuid.Seq;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.system.domain.UserVipLevelConfig;
import com.ruoyi.system.domain.VipExperienceValue;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.mapper.UserVipLevelConfigMapper;
import com.ruoyi.system.mapper.VipExperienceValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用请求处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/common")
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private RedisCache redisCache;

    private static final String FILE_DELIMETER = ",";

    /**
     * 通用下载请求
     * 
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     * @param file 文件
     * @param category 类别
     * @return
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file,String category)
    {
        if (StringUtils.isEmpty(category)){
            throw new RuntimeException("path empty");
        }
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath() + "/" + category;
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files)
            {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = RuoYiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求(Base64)（单个）
     * @param imgStr 图片字符串
     * @param category 类别
     * @return
     */
    @PostMapping("/uploadFileByBase64")
    public AjaxResult uploadFileByBase64(String imgStr,String category)
    {
        if (StringUtils.isEmpty(category)){
            throw new RuntimeException("path empty");
        }
        try
        {
            //文件名称
            String fileName = Seq.getId(Seq.uploadSeqType) + ".jpg";
            //文件日期 (yyyy/MM/dd)
            String datePath = DateUtils.datePath();
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath() + "/" + category + "/" + datePath + "/" + fileName;
            //base64编码字符串转换为图片并存入本地
            base64StrToImage(imgStr, filePath);
            fileName = filePath.replace(RuoYiConfig.getUploadPath(),Constants.RESOURCE_PREFIX + "/" + "upload");
            // 上传并返回新文件名称
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", fileName);
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * base64编码字符串转换为图片,并写入文件
     *
     * @param imgStr base64编码字符串
     * @param path   图片路径
     * @return
     */
    public static void base64StrToImage(String imgStr, String path) throws IOException {
        if (imgStr == null){
            throw new RuntimeException("获取文件数据失败");
        }
        BASE64Decoder decoder = new BASE64Decoder();
        // 解密
        byte[] b = decoder.decodeBuffer(imgStr);
        // 处理数据
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        //文件夹不存在则自动创建
        File tempFile = new File(path);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
            out.write(b);
            out.flush();
        }catch (Exception e){
            throw e;
        }finally {
            out.close();
        }
    }

    /**
     * 清空cacheable缓存
     */
    @GetMapping("/cleanAllCacheable")
    public AjaxResult cleanAllCacheable()
    {
        redisCache.deleteObject(redisCache.keys("cacheable:*"));
        return AjaxResult.success();
    }

    /**
     * VIP数据修复
     */
    @GetMapping("/fixVipData")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult fixVipData()
    {
        UserInfoMapper userInfoMapper = SpringUtils.getBean(UserInfoMapper.class);
        UserVipLevelConfigMapper userVipLevelConfigMapper = SpringUtils.getBean(UserVipLevelConfigMapper.class);
        VipExperienceValueMapper vipExperienceValueMapper = SpringUtils.getBean(VipExperienceValueMapper.class);

        UserInfo userInfo = userInfoMapper.selectUserInfoById(1L);
        Date regTime = userInfo.getRegTime();
        VipExperienceValue vipExperienceValue = new VipExperienceValue();
        vipExperienceValue.setEndTime(regTime);
        //所有未清理残留数据
        List<VipExperienceValue> vipExperienceValues = vipExperienceValueMapper.selectVipExperienceValueList(vipExperienceValue);
        Long[] ids = vipExperienceValues.stream().map(a -> a.getId()).collect(Collectors.toList()).toArray(new Long[]{});
        if (ids.length > 0){
            vipExperienceValueMapper.deleteVipExperienceValueByIds(ids);
        }
        //重新统计新数据
        PageUtils.orderBy("id");
        vipExperienceValues = vipExperienceValueMapper.selectVipExperienceValueList(new VipExperienceValue());
        //根据id分组
        Map<Long, List<VipExperienceValue>> group = vipExperienceValues.stream().collect(Collectors.groupingBy(a -> a.getUserId()));

        for (Map.Entry<Long, List<VipExperienceValue>> entry : group.entrySet()) {
            //vip经验值数据
            List<VipExperienceValue> vipExperienceValueList = entry.getValue();
            //检验第一条数据,如果数据不正常,则重新调整数据
            if (vipExperienceValueList.get(0).getExperienceValueBefore().compareTo(BigDecimal.ZERO) != 0){
                BigDecimal experienceValueBefore = BigDecimal.ZERO;
                //遍历
                for (int i = 0; i < vipExperienceValueList.size(); i++) {
                    VipExperienceValue vo = vipExperienceValueList.get(i);
                    //经验值
                    BigDecimal experienceValue = vo.getExperienceValue();
                    BigDecimal experienceValueAfter = experienceValueBefore.add(experienceValue);
                    vo.setExperienceValueBefore(experienceValueBefore);
                    vo.setExperienceValueAfter(experienceValueAfter);
                    int updateVipExperienceValue = vipExperienceValueMapper.updateVipExperienceValue(vo);
                    if (updateVipExperienceValue == 0){
                        throw new ServiceException();
                    }
                    experienceValueBefore = experienceValueAfter;
                    //用户ID
                    Long userId = entry.getKey();
                    //更新会员vip等级
                    userInfo = userInfoMapper.selectUserInfoById(userId);
                    //用户当前vip等级
                    Integer vipLevel = userInfo.getVipLevel();
                    //匹配的用户vip等级
                    UserVipLevelConfig userVipLevelConfigNew = userVipLevelConfigMapper.selectUserVipLevelConfigByRechargeAmount(experienceValueBefore);
                    if (userVipLevelConfigNew == null && i == 0){
                        userVipLevelConfigNew = new UserVipLevelConfig();
                        userVipLevelConfigNew.setVipLevel(0);
                    }
                    //如果匹配的vip等级与当前等级不符，则更新vip等级
                    if (userVipLevelConfigNew != null && !userVipLevelConfigNew.getVipLevel().equals(vipLevel)){
                        UserInfo userInfoVo = new UserInfo();
                        userInfoVo.setId(userInfo.getId());
                        userInfoVo.setVipLevel(userVipLevelConfigNew.getVipLevel());
                        int count = userInfoMapper.updateUserInfo(userInfoVo);
                        if (count <= 0){
                            throw new ServiceException("系统繁忙");
                        }
                    }
                }
            }
        }
        return AjaxResult.success();
    }
}
