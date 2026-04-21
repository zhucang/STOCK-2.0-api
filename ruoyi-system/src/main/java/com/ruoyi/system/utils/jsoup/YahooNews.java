package com.ruoyi.system.utils.jsoup;

import com.ruoyi.system.domain.News;
import com.ruoyi.system.mapper.NewsMapper;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//雅虎新闻爬虫
@Component
public class YahooNews {

    private Integer port = 10900;


    @Resource
    private NewsMapper newsInfoMapper;

    //英文
    public void yahooNewsEN() throws IOException {
        String website = "https://finance.yahoo.com";
        String url ="https://finance.yahoo.com/topic/stock-market-news/";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements li = doc.select("#Fin-Stream > ul > li");
        Elements news = li.select("div > div > div > h3 > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)) {
                Document docDetail = null;
                try {
                    docDetail = Jsoup.connect(href)
//                            .proxy("127.0.0.1", port)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                            .timeout(5000)
                            .get();
                }catch (Exception e){
                    continue;
                }
                Element title = docDetail.select(".caas-title-wrapper").first();
                if (title == null) {
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Elements content = docDetail.select(".caas-content-wrapper > .caas-body");
                if (content == null) {
                    continue;
                }
                Element dateDetail = docDetail.select(".caas-attr-time-style > time").first();
                Element time = dateDetail.select("time").first();
                String datetime = time.attr("datetime");
                Date dateFormat = new DateTime(datetime).toDate();
                //存入新闻信息
                News newsInfo = new News();
                newsInfo.setContent(content.html());
                newsInfo.setTitle(title.text());
                newsInfo.setShowTime(dateFormat);
                newsInfo.setCreateTime(new Date());
                if (desc == null) {
                    newsInfo.setDescription(null);
                } else {
                    newsInfo.setDescription(desc.text());
                }
                newsInfo.setLanguageId(2L);
                System.out.println(dateFormat + " === " + href + " === " + title.text());
                News newsInfoVo = new News();
                newsInfoVo.setTitle(title.text());
                List<News> newsInfos = newsInfoMapper.selectNewsList(newsInfoVo);
                //如果此文章已存在
                if (newsInfos.size() == 0){
                    try {
                        newsInfoMapper.insertNews(newsInfo);
                    }catch (Exception e){
                        
                    }
                }
            }
        }
    }

//    //繁体
//    public void yahooNewsTC() throws IOException {
//        String website = "https://finance.yahoo.com";
//        String url ="https://finance.yahoo.com/topic/stock-market-news/";
//        Document doc = Jsoup.connect(url)
////                .proxy("127.0.0.1",port)
//                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
//                .timeout(5000)
//                .get();
//        Elements li = doc.select("#Fin-Stream > ul > li");
//        Elements news = li.select("div > div > div > h3 > a");
//        for (Element item : news) {
//            String href = website + item.attr("href");
//            if (!"".equals(href)) {
//                Document docDetail = null;
//                try {
//                    docDetail = Jsoup.connect(href)
////                            .proxy("127.0.0.1", port)
//                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
//                            .timeout(5000)
//                            .get();
//                }catch (Exception e){
//                    continue;
//                }
//                Element title = docDetail.select(".caas-title-wrapper").first();
//                if (title == null) {
//                    continue;
//                }
//                Element desc = docDetail.select(".intro").first();
//                Elements content = docDetail.select(".caas-content-wrapper > .caas-body");
//                if (content == null) {
//                    continue;
//                }
//                Element dateDetail = docDetail.select(".caas-attr-time-style > time").first();
//                Element time = dateDetail.select("time").first();
//                String datetime = time.attr("datetime");
//                Date dateFormat = new DateTime(datetime).toDate();
//                //存入新闻信息
//                News newsInfo = new News();
//                newsInfo.setContent(content.html());
//                newsInfo.setTitle(title.text());
//                newsInfo.setShowTime(dateFormat);
//                newsInfo.setCreateTime();(new Date());
//                if (desc == null) {
//                    newsInfo.setDescription(null);
//                } else {
//                    newsInfo.setDescription(desc.text());
//                }
//
//                newsInfo.setLanguageId(3L);
//                System.out.println(dateFormat + " === " + href + " === " + title.text());
//                News newsInfoVo = new News();
//                newsInfoVo.setTitle(title.text());
//                List<News> newsInfos = newsInfoMapper.selectNewsList(newsInfoVo);
//                //如果此文章已存在
//                if (newsInfos.size() == 0){
//                    try {
//                        newsInfoMapper.insertNews(newsInfo);
//                    }catch (Exception e){
//
//                    }
//                }
//            }
//        }
//    }

    //繁体
    public void yahooNewsTC() throws IOException {
        String website = "https://tw.stock.yahoo.com/news/";
        String url ="https://tw.stock.yahoo.com/intl-markets";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",10900)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements li = doc.select("#YDC-Stream > ul > li");
        Elements news = li.select("div > div > div > h3 > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)) {
                Document docDetail = null;
                try {
                    docDetail = Jsoup.connect(href)
//                            .proxy("127.0.0.1", 10900)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                            .timeout(5000)
                            .get();
                }catch (Exception e){
                    continue;
                }
                Element title = docDetail.select(".caas-title-wrapper").first();
                if (title == null) {
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Elements content = docDetail.select(".caas-content-wrapper > .caas-body");
                if (content == null) {
                    continue;
                }
                Element dateDetail = docDetail.select(".caas-attr-time-style > time").first();
                Element time = dateDetail.select("time").first();
                String datetime = time.attr("datetime");
                Date dateFormat = new DateTime(datetime).toDate();
                //存入新闻信息
                News newsInfo = new News();
                newsInfo.setContent(content.html());
                newsInfo.setTitle(title.text());
                newsInfo.setShowTime(dateFormat);
                newsInfo.setCreateTime(new Date());
                if (desc == null) {
                    newsInfo.setDescription(null);
                } else {
                    newsInfo.setDescription(desc.text());
                }
                newsInfo.setLanguageId(3L);
                System.out.println(dateFormat + " === " + href + " === " + title.text());
                News newsInfoVo = new News();
                newsInfoVo.setTitle(title.text());
                List<News> newsInfos = newsInfoMapper.selectNewsList(newsInfoVo);
                //如果此文章已存在
                if (newsInfos.size() == 0){
                    try {
                        newsInfoMapper.insertNews(newsInfo);
                    }catch (Exception e){

                    }
                }
            }
        }
    }

    //德语
    public void yahooNewsDE() throws IOException {
        Map<String,String> map = new HashMap<>();
        map.put("GUCS","AXirll5O");
        map.put("EuConsent","CPpfVkAPpfVkAAOACKDEC9CgAAAAAAAAACiQAAAAAABhoAMAAQSkEQAYAAglIKgAwABBKQA");
        map.put("A3","d=AQABBJhLxWMCENP-fdqFtwJUiHIfbcdbSnoFEgABCAEJKGRZZK-0b2UBAiAAAAcImEvFY8dbSno&S=AQAAAhvpSO2urTTazI5VCyi5RF4");
        map.put("GUC","AQABCAFkKAlkWUIj4gVR&s=AQAAAEL9suGV&g=ZCbDwQ");
        map.put("cmp","t=1680262076&j=1&u=1---&v=76");
        String website = "https://de.finance.yahoo.com/";
        String url ="https://de.finance.yahoo.com/nachrichten/";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .cookies(map)
                .get();
        Elements li = doc.select("#Fin-Stream > ul > li");
        Elements news = li.select("div > div > div > h3 > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)) {
                Document docDetail = null;
                try {
                    docDetail = Jsoup.connect(href)
//                            .proxy("127.0.0.1", port)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                            .timeout(5000)
                            .cookies(map)
                            .get();
                }catch (Exception e){
                    continue;
                }
                Element title = docDetail.select(".caas-title-wrapper").first();
                if (title == null) {
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Elements content = docDetail.select(".caas-content-wrapper > .caas-body");
                if (content == null) {
                    continue;
                }
                Element dateDetail = docDetail.select(".caas-attr-time-style > time").first();
                Element time = dateDetail.select("time").first();
                String datetime = time.attr("datetime");
                Date dateFormat = new DateTime(datetime).toDate();
                //存入新闻信息
                News newsInfo = new News();
                newsInfo.setContent(content.html());
                newsInfo.setTitle(title.text());
                newsInfo.setShowTime(dateFormat);
                newsInfo.setCreateTime(new Date());
                if (desc == null) {
                    newsInfo.setDescription(null);
                } else {
                    newsInfo.setDescription(desc.text());
                }
                newsInfo.setLanguageId(4L);
                System.out.println(dateFormat + " === " + href + " === " + title.text());
                News newsInfoVo = new News();
                newsInfoVo.setTitle(title.text());
                List<News> newsInfos = newsInfoMapper.selectNewsList(newsInfoVo);
                //如果此文章已存在
                if (newsInfos.size() == 0){
                    try {
                        newsInfoMapper.insertNews(newsInfo);
                    }catch (Exception e){
                        
                    }
                }
            }
        }
    }

    //日语
    public void yahooNewsJP() throws IOException {
        String website = "";
        String url ="https://finance.yahoo.co.jp/news/world";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();

        Elements list = doc.select("#newslist");
        Elements news = list.select(".newsList__1Q1e").select("li > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)) {
                Document docDetail = null;
                try {
                    docDetail = Jsoup.connect(href)
//                            .proxy("127.0.0.1", port)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                            .timeout(5000)
                            .get();
                }catch (Exception e){
                    continue;
                }
                Element title = docDetail.select("#artcldtl > h1").first();
                if (title == null) {
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Elements content = docDetail.select("#artcldtl > div.article__2NrT > div");
                if (content == null) {
                    continue;
                }
                //存入新闻信息
                News newsInfo = new News();
                newsInfo.setContent(content.html());
                newsInfo.setTitle(title.text());
                newsInfo.setShowTime(new Date());
                newsInfo.setCreateTime(new Date());
                if (desc == null) {
                    newsInfo.setDescription(null);
                } else {
                    newsInfo.setDescription(desc.text());
                }
                newsInfo.setLanguageId(8L);
                System.out.println(new Date() + " === " + href + " === " + title.text());
                News newsInfoVo = new News();
                newsInfoVo.setTitle(title.text());
                List<News> newsInfos = newsInfoMapper.selectNewsList(newsInfoVo);
                //如果此文章已存在
                if (newsInfos.size() == 0){
                    try {
                        newsInfoMapper.insertNews(newsInfo);
                    }catch (Exception e){
                        
                    }
                }
            }
        }
    }

    //马来西亚
    public void yahooNewsMY() throws IOException {
        String website = "https://sg.finance.yahoo.com";
        String url ="https://sg.finance.yahoo.com/topic/stocks/";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements li = doc.select("#Fin-Stream > ul > li");
        Elements news = li.select("div > div > div > h3 > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)) {
                Document docDetail = null;
                try {
                    docDetail = Jsoup.connect(href)
//                            .proxy("127.0.0.1", port)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                            .timeout(5000)
                            .get();
                }catch (Exception e){
                    continue;
                }
                Element title = docDetail.select(".caas-title-wrapper").first();
                if (title == null) {
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Elements content = docDetail.select(".caas-content-wrapper > .caas-body");
                if (content == null) {
                    continue;
                }
                Element dateDetail = docDetail.select(".caas-attr-time-style > time").first();
                Element time = dateDetail.select("time").first();
                String datetime = time.attr("datetime");
                Date dateFormat = new DateTime(datetime).toDate();
                //存入新闻信息
                News newsInfo = new News();
                newsInfo.setContent(content.html());
                newsInfo.setTitle(title.text());
                newsInfo.setShowTime(dateFormat);
                newsInfo.setCreateTime(new Date());
                if (desc == null) {
                    newsInfo.setDescription(null);
                } else {
                    newsInfo.setDescription(desc.text());
                }
                newsInfo.setLanguageId(10L);
                System.out.println(dateFormat + " === " + href + " === " + title.text());
                News newsInfoVo = new News();
                newsInfoVo.setTitle(title.text());
                List<News> newsInfos = newsInfoMapper.selectNewsList(newsInfoVo);
                //如果此文章已存在
                if (newsInfos.size() == 0){
                    try {
                        newsInfoMapper.insertNews(newsInfo);
                    }catch (Exception e){
                        
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

    }
}
