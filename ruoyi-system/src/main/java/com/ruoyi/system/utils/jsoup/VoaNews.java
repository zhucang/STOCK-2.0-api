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
import java.util.List;

//美国之音新闻爬虫
@Component
public class VoaNews {


    @Resource
    private NewsMapper newsInfoMapper;

    private Integer port = 10900;


//    //中文
//    public void voaNewsZH() throws IOException {
//        String website = "https://www.voachinese.com";
//        String url ="https://www.voachinese.com/z/1748";
//        Document doc = Jsoup.connect(url)
////                .proxy("127.0.0.1",port)
//                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
//                .timeout(5000)
//                .get();
//        Elements elements = doc.select(".media-block-wrap");
//        Element first = elements.first();
//        Elements news = first.select("li > div > div > a");
//        for (Element item : news) {
//            String href = website + item.attr("href");
//            if (!"".equals(href)) {
//                Document docDetail = Jsoup.connect(href)
////                        .proxy("127.0.0.1", port)
//                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
//                        .timeout(5000)
//                        .get();
//                Element title = docDetail.select(".col-title").first();
//                if (title == null) {
//                    continue;
//                }
//                Element desc = docDetail.select(".intro").first();
//                Element content = docDetail.select("#article-content > .wsw").first();
//                if (content == null) {
//                    continue;
//                }
//                Element dateDetail = docDetail.select(".date").first();
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
//                newsInfo.setLanguageId(1L);
//                System.out.println(dateFormat + " === " + href + " === " + title.text());
//                News newsInfoVo = new News();
//                newsInfoVo.setTitle(title.text());
//                List<News> newsInfos = newsInfoMapper.selectNewsList(newsInfoVo);
//                //如果此文章已存在
//                if (newsInfos.size() >= 1){
//                    continue;
//                }else {
//                    try {
//                        newsInfoMapper.insertNews(newsInfo);
//                    }catch (Exception e){
//                        continue;
//                    }
//                }
//            }
//        }
//    }

    //中文
    public void voaNewsZH() throws IOException {
        String website = "https://www.voachinese.com";
        String url ="https://www.voachinese.com/p/6317.html";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements elements = doc.select("#wrowblock-19340_56");
        Element first = elements.first();
        Elements news = first.select("li > div > div > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)) {
                Document docDetail = Jsoup.connect(href)
//                        .proxy("127.0.0.1", port)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .timeout(5000)
                        .get();
                Element title = docDetail.select(".col-title").first();
                if (title == null) {
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Element content = docDetail.select("#article-content > .wsw").first();
                if (content == null) {
                    continue;
                }
                Element dateDetail = docDetail.select(".date").first();
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
                newsInfo.setLanguageId(1L);
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


//    //英文
//    public void voaNewsEN() throws IOException {
//        String website = "https://www.voanews.com";
//        String url ="https://www.voanews.com/p/6288.html";
//        Document doc = Jsoup.connect(url)
////                .proxy("127.0.0.1",port)
//                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
//                .timeout(5000)
//                .get();
//        Elements elements = doc.select("#content");
//        Element first = elements.first();
//        Elements news = first.select("li > div > div > a");
//        for (Element item : news) {
//            String href = website + item.attr("href");
//            if (!"".equals(href)) {
//                Document docDetail = Jsoup.connect(href)
////                        .proxy("127.0.0.1", port)
//                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
//                        .timeout(5000)
//                        .get();
//                Element title = docDetail.select(".col-title").first();
//                if (title == null) {
//                    continue;
//                }
//                Element desc = docDetail.select(".intro").first();
//                Element content = docDetail.select("#article-content > .wsw").first();
//                if (content == null) {
//                    continue;
//                }
//                Element dateDetail = docDetail.select(".date").first();
//                Element time = dateDetail.select("time").first();
//                String datetime = time.attr("datetime");
//                Date dateFormat = new DateTime(datetime).toDate();
//                //存入新闻信息
//                News newsInfo = new News();
//                newsInfo.setContent(content.html());
//                newsInfo.setTitle(title.text());
//                newsInfo.setShowTime(dateFormat);
//                newsInfo.setCreateTime(new Date());
//                if (desc == null) {
//                    newsInfo.setDescription(null);
//                } else {
//                    newsInfo.setDescription(desc.text());
//                }
//                newsInfo.setLanguageId(2L);
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

    //英文
    public void voaNewsEN() throws IOException {
        String url ="https://coinpaper.com/news";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements elements = doc.select("#__next");
        Element first = elements.first();

        for (int i = 1; i <= 5; i++) {
            Elements news = first.select("main > div > div.DataFeedstyles__DataContainer-sc-1kln1dd-5.iKgDKr > div:nth-child(" + i + ") > div.Contentstyles__Content-sc-xf5sgw-0.buofKA > a");
            if (!news.isEmpty()){
                String href = news.get(0).attr("href");
                if (!"".equals(href)) {
                    Document docDetail = Jsoup.connect(href)
//                        .proxy("127.0.0.1", port)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                            .timeout(5000)
                            .get();
                    Element title = docDetail.select("#__next > main > div > article > div.ArticleHeaderstyles__Container-sc-130h7nw-8.coTvIY > header > h1").first();
                    if (title == null) {
                        continue;
                    }
                    Element desc = docDetail.select("#__next > main > div > article > div.ArticleHeaderstyles__Container-sc-130h7nw-8.coTvIY > header > p").first();
                    Element content = docDetail.select("#__next > main > div > article > div.ArticleContentstyles__Container-sc-18g3ov6-0.iXZlNs > div.ArticleContentstyles__Content-sc-18g3ov6-1.kOsAub > div.RenderHTMLTextstyles__Container-sc-1txeoca-5.kdyfr.wysiwyg-text").first();
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
                    newsInfo.setLanguageId(2L);
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
    }

    //韩语
    public void voaNewsKO() throws IOException {
        String website = "https://www.voakorea.com";
        String url ="https://www.voakorea.com/z/2768";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements elements = doc.select("#content");
        Element first = elements.first();
        Elements news = first.select("li > div > div > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)) {
                Document docDetail = Jsoup.connect(href)
//                        .proxy("127.0.0.1", port)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .timeout(5000)
                        .get();
                Element title = docDetail.select(".col-title").first();
                if (title == null) {
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Element content = docDetail.select("#article-content > .wsw").first();
                if (content == null) {
                    continue;
                }
                Element dateDetail = docDetail.select(".date").first();
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
                newsInfo.setLanguageId(9L);
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

    //西班牙
    public void voaNewsES() throws IOException {
        String website = "https://www.vozdeamerica.com";
        String url ="https://www.vozdeamerica.com/economia";//
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements elements = doc.select("#content");
        Element first = elements.first();
        Elements news = first.select("li > div > div > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)) {
                Document docDetail = Jsoup.connect(href)
//                        .proxy("127.0.0.1", port)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .timeout(5000)
                        .get();
                Element title = docDetail.select(".col-title").first();
                if (title == null) {
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Element content = docDetail.select("#article-content > .wsw").first();
                if (content == null) {
                    continue;
                }
                Element dateDetail = docDetail.select(".date").first();
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
                newsInfo.setLanguageId(5L);
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

    //法语
    public void voaNewsFR() throws IOException {
        String website = "https://www.voaafrique.com";
        String url ="https://www.voaafrique.com/monde";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements elements = doc.select("#content > .container");
        Elements li = elements.select("li");
        Elements news = li.select("li > div > div > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)) {
                Document docDetail = Jsoup.connect(href)
//                        .proxy("127.0.0.1", port)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .timeout(5000)
                        .get();
                Element title = docDetail.select(".col-title").first();
                if (title == null) {
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Element content = docDetail.select("#article-content > .wsw").first();
                if (content == null) {
                    continue;
                }
                Element dateDetail = docDetail.select(".date").first();
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
                newsInfo.setLanguageId(6L);
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

    //印度尼西亚
    public void voaNewsIDN() throws IOException {
        String website = "https://www.voaindonesia.com";
        String url ="https://www.voaindonesia.com/BeritaIndonesia";//
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements elements = doc.select("#content > .container");
        Element first = elements.first();
        Elements news = first.select("li > div > div > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)){
                Document docDetail = Jsoup.connect(href)
//                        .proxy("127.0.0.1",port)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .timeout(5000)
                        .get();
                Element title = docDetail.select(".col-title").first();
                if (title == null){
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Element content = docDetail.select("#article-content > .wsw").first();
                if (content == null){
                    continue;
                }
                Element dateDetail = docDetail.select(".date").first();
                Element time = dateDetail.select("time").first();
                String datetime = time.attr("datetime");
                Date dateFormat = new DateTime(datetime).toDate();
                //存入新闻信息
                News newsInfo = new News();
                newsInfo.setContent(content.html());
                newsInfo.setTitle(title.text());
                newsInfo.setShowTime(dateFormat);
                newsInfo.setCreateTime(new Date());
                if (desc == null){
                    newsInfo.setDescription(null);
                }else {
                    newsInfo.setDescription(desc.text());
                }
                newsInfo.setLanguageId(7L);
                System.out.println(dateFormat+" === " + href+" === " +title.text());
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

    //泰语
    public void voaNewsTH() throws IOException {
        String website = "https://www.voathai.com";
        String url ="https://www.voathai.com/z/1822";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements elements = doc.select(".media-block-wrap");
        Element first = elements.first();
        Elements news = first.select("li > div >  a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)){
                Document docDetail = Jsoup.connect(href)
//                        .proxy("127.0.0.1",port)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .timeout(5000)
                        .get();
                Element title = docDetail.select(".col-title").first();
                if (title == null){
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Element content = docDetail.select("#article-content > .wsw").first();
                if (content == null){
                    continue;
                }
                Element dateDetail = docDetail.select(".date").first();
                Element time = dateDetail.select("time").first();
                String datetime = time.attr("datetime");
                Date dateFormat = new DateTime(datetime).toDate();
                //存入新闻信息
                News newsInfo = new News();
                newsInfo.setContent(content.html());
                newsInfo.setTitle(title.text());
                newsInfo.setShowTime(dateFormat);
                newsInfo.setCreateTime(new Date());
                if (desc == null){
                    newsInfo.setDescription(null);
                }else {
                    newsInfo.setDescription(desc.text());
                }
                newsInfo.setLanguageId(11L);
                System.out.println(dateFormat+" === " + href+" === " +title.text());
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

    //越南语
    public void voaNewsVI() throws IOException {
        String website = "https://www.voatiengviet.com";
        String url ="https://www.voatiengviet.com/z/1799";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements news = doc.select("#articleItems > li > div > div > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)){
                Document docDetail = Jsoup.connect(href)
//                        .proxy("127.0.0.1",port)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .timeout(5000)
                        .get();
                Element title = docDetail.select(".col-title").first();
                if (title == null){
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Element content = docDetail.select("#article-content > .wsw").first();
                if (content == null){
                    continue;
                }
                Element dateDetail = docDetail.select(".date").first();
                Element time = dateDetail.select("time").first();
                String datetime = time.attr("datetime");
                Date dateFormat = new DateTime(datetime).toDate();
                //存入新闻信息
                News newsInfo = new News();
                newsInfo.setContent(content.html());
                newsInfo.setTitle(title.text());
                newsInfo.setShowTime(dateFormat);
                newsInfo.setCreateTime(new Date());
                if (desc == null){
                    newsInfo.setDescription(null);
                }else {
                    newsInfo.setDescription(desc.text());
                }
                newsInfo.setLanguageId(12L);
                System.out.println(dateFormat+" === " + href+" === " +title.text());
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

    //葡萄牙
    public void voaNewsPT() throws IOException {
        String website = "https://www.voaportugues.com";
        String url ="https://www.voaportugues.com/mocambique";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements elements = doc.select(".media-block-wrap");
        Element first = elements.first();
        Elements news = first.select("li > div > div > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)){
                Document docDetail = Jsoup.connect(href)
//                        .proxy("127.0.0.1",port)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .timeout(5000)
                        .get();
                Element title = docDetail.select(".col-title").first();
                if (title == null){
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Element content = docDetail.select("#article-content > .wsw").first();
                if (content == null){
                    continue;
                }
                Element dateDetail = docDetail.select(".date").first();
                Element time = dateDetail.select("time").first();
                String datetime = time.attr("datetime");
                Date dateFormat = new DateTime(datetime).toDate();
                //存入新闻信息
                News newsInfo = new News();
                newsInfo.setContent(content.html());
                newsInfo.setTitle(title.text());
                newsInfo.setShowTime(dateFormat);
                newsInfo.setCreateTime(new Date());
                if (desc == null){
                    newsInfo.setDescription(null);
                }else {
                    newsInfo.setDescription(desc.text());
                }
                newsInfo.setLanguageId(13L);
                System.out.println(dateFormat+" === " + href+" === " +title.text());
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

    //俄语
    public void voaNewsRUS() throws IOException {
        String website = "https://www.golosameriki.com";
        String url ="https://www.golosameriki.com/z/1610";
        Document doc = Jsoup.connect(url)
//                .proxy("127.0.0.1",port)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Elements elements = doc.select(".media-block-wrap");
        Element first = elements.first();
        Elements news = first.select("li > div > div > a");
        for (Element item : news) {
            String href = website + item.attr("href");
            if (!"".equals(href)){
                Document docDetail = Jsoup.connect(href)
//                        .proxy("127.0.0.1",port)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .timeout(5000)
                        .get();
                Element title = docDetail.select(".col-title").first();
                if (title == null){
                    continue;
                }
                Element desc = docDetail.select(".intro").first();
                Element content = docDetail.select("#article-content > .wsw").first();
                if (content == null){
                    continue;
                }
                Element dateDetail = docDetail.select(".date").first();
                Element time = dateDetail.select("time").first();
                String datetime = time.attr("datetime");
                Date dateFormat = new DateTime(datetime).toDate();
                //存入新闻信息
                News newsInfo = new News();
                newsInfo.setContent(content.html());
                newsInfo.setTitle(title.text());
                newsInfo.setShowTime(dateFormat);
                newsInfo.setCreateTime(new Date());
                if (desc == null){
                    newsInfo.setDescription(null);
                }else {
                    newsInfo.setDescription(desc.text());
                }
                newsInfo.setLanguageId(14L);
                System.out.println(dateFormat+" === " + href+" === " +title.text());
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
}
