package cn.huacloud.taxpreference.sample;

import cn.huacloud.taxpreference.common.enums.DocType;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * json测试类
 * @author wangkh
 */
@Slf4j
public class JsonTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testConvert() throws Exception {
        for (int i = 0; i < 100000; i++) {
            List<String> list = new ArrayList<>();
            list.add("POLICIES_EXPLAIN");
            String str = objectMapper.writeValueAsString(list);
            CollectionLikeType type = objectMapper.getTypeFactory().constructCollectionLikeType(ArrayList.class, DocType.class);
            List<DocType> target = objectMapper.readValue(str, type);
            DocType next = target.iterator().next();
            log.info(next.getName());
        }
    }

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
