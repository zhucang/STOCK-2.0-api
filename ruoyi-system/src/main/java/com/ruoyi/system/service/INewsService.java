package com.ruoyi.system.service;

import com.ruoyi.system.domain.News;

import java.io.IOException;
import java.util.List;

/**
 * 新闻数据Service接口
 * 
 * @author ruoyi
 * @date 2023-12-04
 */
public interface INewsService 
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
     * 批量删除新闻数据
     * 
     * @param ids 需要删除的新闻数据主键集合
     * @return 结果
     */
    public int deleteNewsByIds(Long[] ids);

    /**
     * 删除新闻数据信息
     * 
     * @param id 新闻数据主键
     * @return 结果
     */
    public int deleteNewsById(Long id);

    /**
     * 增加浏览器
     * @param newsId 新闻id
     * @return
     */
    public int addViews(Long newsId);

    /**
     * 新闻抓取任务中文
     */
    void newInfoTaskZH() throws IOException;

    /**
     * 新闻抓取任务英文
     */
    void newInfoTaskEN() throws IOException;

    /**
     * 新闻抓取任务繁体
     */
    void newInfoTaskTC() throws IOException;

    /**
     * 新闻抓取任务德语
     */
    void newInfoTaskDE() throws IOException;

    /**
     * 新闻资讯抓取西班牙
     */
    void NewsInfoTaskES() throws IOException;

    /**
     * 新闻资讯抓取法语
     */
    void NewsInfoTaskFR() throws IOException;

    /**
     * 新闻资讯抓取印度尼西亚
     */
    void NewsInfoTaskIDN() throws IOException;

    /**
     * 新闻资讯抓取日语
     */
    void NewsInfoTaskJP() throws IOException;

    /**
     * 新闻资讯抓取韩语
     */
    void NewsInfoTaskKO() throws IOException;

    /**
     * 新闻资讯抓取马来西亚
     */
    void NewsInfoTaskMY() throws IOException;

    /**
     * 新闻资讯抓取泰国(优化版本)
     */
    void NewsInfoTaskTH() throws IOException;

    /**
     * 新闻资讯抓取越南(优化版本)
     */
    void NewsInfoTaskVI() throws IOException;

    /**
     * 新闻资讯抓取葡萄牙(优化版本)
     */
    void NewsInfoTaskPT() throws IOException;

    /**
     * 新闻资讯抓取俄语(优化版本)
     */
    void NewsInfoTaskRUS() throws IOException;

    /**
     * 清除旧数据
     */
    public int clearOldNews();
}
