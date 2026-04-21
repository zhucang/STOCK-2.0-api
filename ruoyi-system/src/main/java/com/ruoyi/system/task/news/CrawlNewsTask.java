package com.ruoyi.system.task.news;

import com.ruoyi.system.service.INewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 新闻抓取
 */
@Component
public class CrawlNewsTask {
    private static final Logger log = LoggerFactory.getLogger(CrawlNewsTask.class);

    @Autowired
    private INewsService newsService;

    /**
     * 新闻资讯抓取(全部)
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void NewsInfoTask(){
        try {
            newsService.newInfoTaskZH();
        }catch (Exception e){
            log.info("中文新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.newInfoTaskEN();
        }catch (Exception e){
            log.info("英文新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.newInfoTaskTC();
        }catch (Exception e){
            log.info("繁体新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.newInfoTaskDE();
        }catch (Exception e){
            log.info("德语新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.NewsInfoTaskES();
        }catch (Exception e){
            log.info("西班牙新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.NewsInfoTaskFR();
        }catch (Exception e){
            log.info("法语新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.NewsInfoTaskIDN();
        }catch (Exception e){
            log.info("印度尼西亚新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.NewsInfoTaskJP();
        }catch (Exception e){
            log.info("日语新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.NewsInfoTaskKO();
        }catch (Exception e){
            log.info("韩语新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.NewsInfoTaskMY();
        }catch (Exception e){
            log.info("马来西亚新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.NewsInfoTaskTH();
        }catch (Exception e){
            log.info("泰语新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.NewsInfoTaskVI();
        }catch (Exception e){
            log.info("越南语新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.NewsInfoTaskPT();
        }catch (Exception e){
            log.info("葡萄牙新闻抓取异常"+e.getMessage());
        }
        try {
            newsService.NewsInfoTaskRUS();
        }catch (Exception e){
            log.info("俄罗斯新闻抓取异常"+e.getMessage());
        }
    }

    /**
     * 清除旧数据
     */
    @Scheduled(cron = "0 0 0 1/7 * ?")
    public void clearOldNews(){
        newsService.clearOldNews();
    }

}
