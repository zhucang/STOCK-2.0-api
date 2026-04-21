package com.ruoyi.system.utils.jsoup;


import cn.hutool.core.date.DateTime;
import com.ruoyi.system.domain.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

public class Test {

    public static Integer port = 10900;
    public static void main(String[] args) throws IOException {
        String website = "https://www.voachinese.com";
        String url ="https://www.voachinese.com/p/6317.html";
        Document doc = Jsoup.connect(url)
                .proxy("127.0.0.1",port)
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
                        .proxy("127.0.0.1", port)
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
                Date dateFormat = new DateTime(datetime).toSqlDate();
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
            }
        }
    }
}
