package cn.huacloud.taxpreference.sample;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * @author wangkh
 */
@Slf4j
public class JsoupTest {

    @Test
    public void testPaser() {
        Document doc = Jsoup.parse("这是一段纯文本");
        String text = doc.text();
        log.info(text);
    }
}
