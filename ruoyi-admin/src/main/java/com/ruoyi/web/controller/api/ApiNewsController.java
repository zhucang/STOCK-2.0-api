package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.News;
import com.ruoyi.system.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 新闻数据Controller
 * 
 * @author ruoyi
 * @date 2023-12-04
 */
@RestController
@RequestMapping("/api/news")
public class ApiNewsController extends BaseController
{
    @Autowired
    private INewsService newsService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询新闻数据列表
     */
    @GetMapping("/list")
    public TableDataInfo list(News news)
    {
        Long languageId = HttpUtils.getLanguageId();
        //缓存key
        String cacheKey = "cacheable:newsListAppCache:"+ "languageId/"+languageId+",pageNum/"+news.getParams().get("pageNum")+",pageSize/"+news.getParams().get("pageSize");
        //缓存数据
        List<News> cacheList = redisCache.getCacheObject(cacheKey);
        //如果没有缓存数据
        if (cacheList == null){
            news.setStatus(0);
            news.setLanguageId(languageId);
            startPage();
            startOrderBy("id desc");
            cacheList = newsService.selectNewsList(news);
            //保存缓存，并且设置有效时间15分钟
            redisCache.setCacheObject(cacheKey,cacheList,1, TimeUnit.HOURS);
        }
        return getDataTable(cacheList);
    }

    /**
     * 增加浏览量
     */
    @PostMapping("/addViews")
    public AjaxResult addViews(Long newsId)
    {
        int count = newsService.addViews(newsId);
        if (count > 0){
            //缓存key
            String cacheKey = "newsListAppCache:*";
            //当浏览量数据变更时，清空列表缓存
            redisCache.deleteObject(redisCache.keys(cacheKey));
        }
        return toAjax(count);
    }

}
