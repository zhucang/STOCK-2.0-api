package com.ruoyi.system.service.impl;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.ZipUtils;
import com.ruoyi.system.domain.ClientTemplate;
import com.ruoyi.system.domain.ClientVersion;
import com.ruoyi.system.mapper.ClientVersionMapper;
import com.ruoyi.system.service.IClientTemplateService;
import com.ruoyi.system.service.IClientVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 客户端版本管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-01-08
 */
@Service
public class ClientVersionServiceImpl implements IClientVersionService 
{
    @Resource
    private ClientVersionMapper clientVersionMapper;

    @Autowired
    private IClientTemplateService clientTemplateService;

    /**
     * 查询客户端版本管理
     * 
     * @param id 客户端版本管理主键
     * @return 客户端版本管理
     */
    @Override
    public ClientVersion selectClientVersionById(Long id)
    {
        return clientVersionMapper.selectClientVersionById(id);
    }

    /**
     * 查询客户端版本管理列表
     * 
     * @param clientVersion 客户端版本管理
     * @return 客户端版本管理
     */
    @Override
    public List<ClientVersion> selectClientVersionList(ClientVersion clientVersion)
    {
        return clientVersionMapper.selectClientVersionList(clientVersion);
    }

    /**
     * 新增客户端版本管理
     * 
     * @param clientVersion 客户端版本管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertClientVersion(ClientVersion clientVersion)
    {
        //模板id
        Long clientTemplateId = clientVersion.getClientTemplateId();
        //模板信息
        ClientTemplate clientTemplate = clientTemplateService.selectClientTemplateById(clientTemplateId);
        if (clientTemplate == null){
            throw new ServiceException("获取模板信息异常");
        }
        //文件上传真实基础路径
        String uploadPath = RuoYiConfig.getUploadPath();
        //文件上传映射基础路径
        String mappingUploadPath = Constants.RESOURCE_PREFIX + "/upload";
        //h5压缩包文件映射路径
        String compressedPackageNameH5 = clientVersion.getCompressedPackageNameH5();
        if (StringUtils.isNotEmpty(compressedPackageNameH5)){
            compressedPackageNameH5 = compressedPackageNameH5.replace(mappingUploadPath,uploadPath);
            //h5资源文件
            File h5File = new File(compressedPackageNameH5);
            if (!h5File.exists()){
                throw new ServiceException("请检查h5资源是否上传成功");
            }
        }
        //app压缩包文件映射路径
        String compressedPackageNameApp = clientVersion.getCompressedPackageNameApp();
        if (StringUtils.isNotEmpty(compressedPackageNameApp)){
            compressedPackageNameApp = compressedPackageNameApp.replace(mappingUploadPath,uploadPath);
            //app资源文件
            File appFile = new File(compressedPackageNameApp);
            if (!appFile.exists()){
                throw new ServiceException("请检查app资源是否上传成功");
            }
        }
        clientVersion.setCreateTime(DateUtils.getNowDate());
        int insertClientVersion = clientVersionMapper.insertClientVersion(clientVersion);
        if (insertClientVersion <= 0){
            throw new ServiceException("系统繁忙");
        }
        int updateVersion = updateVersion(clientVersion.getId());
        if (updateVersion <= 0){
            throw new ServiceException("更新版本异常");
        }
        return 1;
    }

    /**
     * 更新版本
     * @param clientVersionId 客户端版本id
     * @return
     */
    @Override
    public int updateVersion(Long clientVersionId){
        //版本信息
        ClientVersion clientVersion = clientVersionMapper.selectClientVersionById(clientVersionId);
        //文件上传真实基础路径
        String uploadPath = RuoYiConfig.getUploadPath();
        //文件上传映射基础路径
        String mappingUploadPath = Constants.RESOURCE_PREFIX + "/upload";
        //h5压缩包文件映射路径
        String compressedPackageNameH5 = clientVersion.getCompressedPackageNameH5().replace(mappingUploadPath,uploadPath);
        //h5资源文件
        File h5File = new File(compressedPackageNameH5);
        if (!h5File.exists()){
            throw new ServiceException("获取此版本资源文件异常");
        }
        //解压文件至系统资源文件存放路径
        //系统资源文件存放路径
        String fileStoragePath = clientVersion.getFileStoragePath();
        if (StringUtils.isEmpty(fileStoragePath)){
            throw new ServiceException("获取资源文件存放路径异常");
        }
        try {
            ZipUtils.unZipFiles(h5File,fileStoragePath);
        }catch (Exception e){
            throw new ServiceException("解压文件异常");
        }
        return 1;
    }

    /**
     * 修改客户端版本管理
     * 
     * @param clientVersion 客户端版本管理
     * @return 结果
     */
    @Override
    public int updateClientVersion(ClientVersion clientVersion)
    {
        return clientVersionMapper.updateClientVersion(clientVersion);
    }

    /**
     * 批量删除客户端版本管理
     * 
     * @param ids 需要删除的客户端版本管理主键
     * @return 结果
     */
    @Override
    public int deleteClientVersionByIds(Long[] ids)
    {
        return clientVersionMapper.deleteClientVersionByIds(ids);
    }

    /**
     * 删除客户端版本管理信息
     * 
     * @param id 客户端版本管理主键
     * @return 结果
     */
    @Override
    public int deleteClientVersionById(Long id)
    {
        return clientVersionMapper.deleteClientVersionById(id);
    }
}
