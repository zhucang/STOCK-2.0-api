package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.News;

import java.util.List;

/**
 * 新闻数据Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-04
 */
public interface NewsMapper 
{
    /**
     * 查询新闻数据
     * 
     * @param id 新闻数据主键
     * @return 新闻数据
     */
    public News selectNewsById(Long id);

    /**
     * 查询新闻数据列表
     * 
     * @param news 新闻数据
     * @return 新闻数据集合
     */
    public List<News> selectNewsList(News news);

    /**
     * 新增新闻数据
     * 
     * @param news 新闻数据
     * @return 结果
     */
    public int insertNews(News news);

    /**
     * 修改新闻数据
     * 
     * @param news 新闻数据
     * @return 结果
     */
    public int updateNews(News news);

    /**
     * 删除新闻数据
     * 
     * @param id 新闻数据主键
     * @return 结果
     */
    public int deleteNewsById(Long id);

    /**
     * 批量删除新闻数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNewsByIds(Long[] ids);

    /**
     * 增加浏览量
     * @param newsId 新闻id
     * @return
     */
    int addViews(Long newsId);

    /**
     * 清除旧数据
     */
    public int clearOldNews();
}
