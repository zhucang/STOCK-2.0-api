package com.ruoyi.system.utils.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.TimeUnit;

public class JsoupUtil {

    public static void main(String[] args) throws Exception {
        String website = "https://www.voachinese.com";
        String url ="https://www.voachinese.com/z/1748";// "https://www.voachinese.com/z/1748";
        Document doc = Jsoup.connect(url)
                .proxy("127.0.0.1",10900)
                .referrer("www.voachinese.com")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        Element body = doc.body();
        //Elements elements = doc.select("#ordinaryItems > li:nth-child(1) > div");
        Elements elements = doc.select(".media-block-wrap");
        Element first = elements.first();
        Elements ordinaryItems = first.select("#ordinaryItems");
        Element ordinaryItem = ordinaryItems.first();
        Elements news = ordinaryItem.select("#ordinaryItems > li > div > div");
        for (Element item : news) {
            Element date = item.select("span").first();
            Element link = item.select("a").first();
            Element title = link.select("h4").first();
            System.out.println(date.text()+" === " +website+link.attr("href") +" === " +title.attr("title"));
            String href = website + link.attr("href");
            TimeUnit.SECONDS.sleep(1);
            if (!"".equals(href)){
                Document docDetail = Jsoup.connect(href)
                        .proxy("127.0.0.1",10900)
                        .referrer("www.voachinese.com")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .timeout(5000)
                        .get();
                Element content = docDetail.select("#article-content > .wsw").first();
                System.out.println(content.html());
            }
        }
    }
}
