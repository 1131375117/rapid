package cn.huacloud.taxpreference.sample;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangkh
 */
@Slf4j
public class JsoupTest {

    @Test
    public void testParse() {
        Document doc = Jsoup.parse("这是一段纯文本");
        String text = doc.text();
        log.info(text);
    }

    @Test
    public void testGetElementById() throws Exception {
        ClassPathResource resource = new ClassPathResource("html/policies_content_01.html");
        String html = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
        Document document = Jsoup.parse(html);
        String key = "attachment_id";
        Elements elements = document.getElementsByAttribute(key);
        List<Long> ids = elements.stream().map(element -> element.attributes().get(key))
                .filter(StringUtils::isNotBlank)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        log.info("{}", ids);
    }
}
