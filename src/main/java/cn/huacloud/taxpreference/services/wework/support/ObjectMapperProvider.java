package cn.huacloud.taxpreference.services.wework.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author wangkh
 */
public class ObjectMapperProvider {

    public static ObjectMapper getObjectMapper() {
        return Foo.objectMapper;
    }

    public static XmlMapper getXmlMapper() {
        return Foo.xmlMapper;
    }

    private static class Foo {
        public static ObjectMapper objectMapper;
        public static XmlMapper xmlMapper;

        static {
            objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            xmlMapper = new XmlMapper();
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
    }

    public static String writeJsonPrettyString(Object object) {
        try {
            return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
