package cn.huacloud.taxpreference.services.wwx.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author wangkh
 */
@Slf4j
public class RESTUtil {

    private static final RestTemplate restTemplate = new RestTemplate();

    public static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static ObjectNode createBody() {
        return objectMapper.createObjectNode();
    }

    public static ObjectNode get(String url) {
        ResponseEntity<ObjectNode> responseEntity = restTemplate.getForEntity(url, ObjectNode.class);
        serverIsRight(responseEntity);
        return responseEntity.getBody();
    }

    public static ObjectNode post(String url, ObjectNode body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ObjectNode> request = new HttpEntity<>(body, headers);
        ResponseEntity<ObjectNode> response = restTemplate.postForEntity(url, request, ObjectNode.class);
        serverIsRight(response);
        return response.getBody();
    }

    private static void serverIsRight(ResponseEntity<ObjectNode> response) {
        if (response.getStatusCodeValue() == 200) {
            log.info("服务器请求成功：{}", response.getStatusCodeValue());
        } else {
            log.error("服务器请求异常：{}", response.getStatusCodeValue());
        }
    }
}
