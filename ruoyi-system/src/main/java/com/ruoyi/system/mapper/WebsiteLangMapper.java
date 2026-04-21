package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.WebsiteLang;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 官网多语言包Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-09
 */
public interface WebsiteLangMapper 
{
    /**
     * 查询官网多语言包
     * 
     * @param id 官网多语言包主键
     * @return 官网多语言包
     */
    public WebsiteLang selectWebsiteLangById(Long id);

    /**
     * 查询官网多语言包
     *
     * @param langKey 多语言key
     * @return 多语言配置包
     */
    public WebsiteLang selectWebsiteLangByLangKey(String langKey);

    /**
     * 查询官网多语言包列表
     * 
     * @param websiteLang 官网多语言包
     * @return 官网多语言包集合
     */
    public List<WebsiteLang> selectWebsiteLangList(WebsiteLang websiteLang);

    /**
     * 新增官网多语言包
     * 
     * @param websiteLang 官网多语言包
     * @return 结果
     */
    public int insertWebsiteLang(WebsiteLang websiteLang);

    /**
     * 修改官网多语言包
     * 
     * @param websiteLang 官网多语言包
     * @return 结果
     */
    public int updateWebsiteLang(WebsiteLang websiteLang);

    /**
     * 修改官网多语言包
     *
     * @param websiteLang 官网多语言包
     * @return 结果
     */
    public int updateWebsiteLangByLangKey(WebsiteLang websiteLang);

    /**
     * 批量替换多语言
     * @param from
     * @param to
     * @return
     */
    public int batchReplaceLangValue(@Param("from") String from, @Param("to")  String to);

    /**
     * 删除官网多语言包
     * 
     * @param id 官网多语言包主键
     * @return 结果
     */
    public int deleteWebsiteLangById(Long id);

    /**
     * 批量删除官网多语言包
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWebsiteLangByIds(Long[] ids);
}
