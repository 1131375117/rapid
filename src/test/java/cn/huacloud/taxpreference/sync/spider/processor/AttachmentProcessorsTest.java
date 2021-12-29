package cn.huacloud.taxpreference.sync.spider.processor;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class AttachmentProcessorsTest {

    @Test
    public void testReplaceAttribute() throws Exception {
        ClassPathResource resource = new ClassPathResource("html/attachment_01.html");

        Document document = Jsoup.parse(resource.getInputStream(), "UTF-8", "");
        Elements elements = document.select("a");
        for (Element element : elements) {
            String href = element.attr("href");
            element.attr("href", "这是替换后的href地址");
            log.info(href);
        }
        String html = document.html();
        log.info(html);
    }
}