package cn.huacloud.taxpreference.services.wwx.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.jayway.jsonpath.internal.filter.ValueNodes;
import org.junit.Test;

import static org.junit.Assert.*;

public class RESTUtilTest {

    @Test
    public void get() {
        String url = "https://tax-open.hua-cloud.net.cn/open-api/v1/sys/codeTypes?Token=1fa28c9f-2bc1-40fa-a42b-efc1e261112c";
        ObjectNode objectNode = RESTUtil.get(url);
        System.out.println(objectNode.toPrettyString());
    }

    @Test
    public void post() {
        String url = "https://tax-open.hua-cloud.net.cn/open-api/v1/search/allDocCount?Token=1fa28c9f-2bc1-40fa-a42b-efc1e261112c";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode body = objectMapper.createObjectNode()
                .put("keyword", "小规模")
                .put("pageNum", 1)
                .put("pageSize", 2)
                .put("preciseQuery", true)
                .put("searchScope", "TITLE_AND_CONTENT");
        JsonNode result = RESTUtil.post(url, body);
        System.out.println(result.toPrettyString());
    }
}