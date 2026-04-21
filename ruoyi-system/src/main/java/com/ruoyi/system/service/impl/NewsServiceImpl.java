package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.News;
import com.ruoyi.system.mapper.NewsMapper;
import com.ruoyi.system.service.INewsService;
import com.ruoyi.system.utils.jsoup.VoaNews;
import com.ruoyi.system.utils.jsoup.YahooNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 新闻数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-04
 */
@Service
public class NewsServiceImpl implements INewsService 
{
    @Resource
    private NewsMapper newsMapper;

    @Autowired
    private VoaNews voaNews;

    @Autowired
    private YahooNews yahooNews;


    /**
     * 查询新闻数据
     * 
     * @param id 新闻数据主键
     * @return 新闻数据
     */
    @Override
    public News selectNewsById(Long id)
    {
        return newsMapper.selectNewsById(id);
    }

    /**
     * 查询新闻数据列表
     * 
     * @param news 新闻数据
     * @return 新闻数据
     */
    @Override
    public List<News> selectNewsList(News news)
    {
        return newsMapper.selectNewsList(news);
    }

    /**
     * 新增新闻数据
     * 
     * @param news 新闻数据
     * @return 结果
     */
    @Override
    public int insertNews(News news)
    {
        news.setCreateTime(DateUtils.getNowDate());
        return newsMapper.insertNews(news);
    }

    /**
     * 修改新闻数据
     * 
     * @param news 新闻数据
     * @return 结果
     */
    @Override
    public int updateNews(News news)
    {
        return newsMapper.updateNews(news);
    }

    /**
     * 批量删除新闻数据
     * 
     * @param ids 需要删除的新闻数据主键
     * @return 结果
     */
    @Override
    public int deleteNewsByIds(Long[] ids)
    {
        return newsMapper.deleteNewsByIds(ids);
    }

    /**
     * 删除新闻数据信息
     * 
     * @param id 新闻数据主键
     * @return 结果
     */
    @Override
    public int deleteNewsById(Long id)
    {
        return newsMapper.deleteNewsById(id);
    }

    /**
     * 增加浏览器
     * @param newsId 新闻id
     * @return
     */
    @Override
    public int addViews(Long newsId){
        newsMapper.addViews(newsId);
        return 1;
    }

    @Override
    public void newInfoTaskZH() throws IOException {
        voaNews.voaNewsZH();
    }

    @Override
    public void newInfoTaskEN() throws IOException {
        voaNews.voaNewsEN();
    }

    @Override
    public void newInfoTaskTC() throws IOException {
        yahooNews.yahooNewsTC();
    }

    @Override
    public void newInfoTaskDE() throws IOException {
        yahooNews.yahooNewsDE();
    }

    @Override
    public void NewsInfoTaskES() throws IOException {
        voaNews.voaNewsES();
    }

    @Override
    public void NewsInfoTaskFR() throws IOException {
        voaNews.voaNewsFR();
    }

    @Override
    public void NewsInfoTaskIDN() throws IOException {
        voaNews.voaNewsIDN();
    }

    @Override
    public void NewsInfoTaskJP() throws IOException {
        yahooNews.yahooNewsJP();
    }

    @Override
    public void NewsInfoTaskKO() throws IOException {
        voaNews.voaNewsKO();
    }

    @Override
    public void NewsInfoTaskMY() throws IOException {
        yahooNews.yahooNewsMY();
    }

    @Override
    public void NewsInfoTaskTH() throws IOException {
        voaNews.voaNewsTH();
    }

    @Override
    public void NewsInfoTaskVI() throws IOException {
        voaNews.voaNewsVI();
    }

    @Override
    public void NewsInfoTaskPT() throws IOException {
        voaNews.voaNewsPT();
    }

    @Override
    public void NewsInfoTaskRUS() throws IOException {
        voaNews.voaNewsRUS();
    }

    /**
     * 清除旧数据
     */
    @Override
    public int clearOldNews(){
        return newsMapper.clearOldNews();
    }
}
