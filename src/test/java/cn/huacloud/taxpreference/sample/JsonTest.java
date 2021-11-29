package cn.huacloud.taxpreference.sample;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * json测试类
 * @author wangkh
 */
@Slf4j
public class JsonTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testWrite() throws Exception {
        Doc doc = new Doc();
        doc.setHtmlContent("这是一段正文内容");
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(doc);
        log.info(value);
    }

    @Test
    public void testRead() throws Exception {
        String json = "{\"title\": \"标题\", \"htmlContent\": \"这是一段正文内容\"}";
        Doc doc = objectMapper.readValue(json, Doc.class);
        log.info("{}", doc);
    }

    @Data
    public static class Doc {
        private String title;
        @JsonAlias("htmlContent")
        @JsonProperty("content")
        private String htmlContent;
        private String plainContent;
    }
}
