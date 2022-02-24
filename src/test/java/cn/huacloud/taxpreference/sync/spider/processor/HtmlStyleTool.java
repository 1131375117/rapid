package cn.huacloud.taxpreference.sync.spider.processor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author fuhua
 **/
public class HtmlStyleTool {
    public static void main(String[] args) throws IOException {
        ClassPathResource resource = new ClassPathResource("html/48156.html");
        Document document = Jsoup.parse(resource.getInputStream(), "UTF-8", "");
        Elements elements = document.select("a");
        for (Element element : elements) {
            element.attr("style", "text-decoration: none");
            element.select("span").removeAttr("style");
        }
        System.out.println(document);
    }
}
