package cn.huacloud.taxpreference.services.wwx.utils;

import cn.huacloud.taxpreference.services.wwx.entity.dos.WWXThirdCompanyDO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class WWXBizHelperTest {

    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    public void getPermanentCode() {
    }

    @Test
    public void permanentCodeMap2Company() throws Exception {
        String json = IOUtils.toString(new ClassPathResource("json/permanent_code.json").getInputStream(), StandardCharsets.UTF_8);
        WWXThirdCompanyDO companyDO = objectMapper.readValue(json, WWXThirdCompanyDO.class);
        JsonNode jsonNode = objectMapper.readTree(json);
        System.out.println(companyDO);
    }
}