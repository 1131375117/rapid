package cn.huacloud.taxpreference.common.entity.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

@Slf4j
public class KeywordPageQueryDTOTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testStringParamNullOrTrim() throws Exception {
        SampleQuery sampleQuery = new SampleQuery();
        sampleQuery.setKeyword("keyword ");
        sampleQuery.setName(" name");
        sampleQuery.setCode("  ");
        sampleQuery.paramReasonable();
        log.info("sampleQuery: {}", objectMapper.writeValueAsString(sampleQuery));
    }

    @Getter
    @Setter
    public static class SampleQuery extends KeywordPageQueryDTO {

        private String name;

        private String code;

        private String value;

        private Boolean flag;

        private List<String> list;

        @Override
        public void paramReasonable() {
            super.paramReasonable();
            stringParamNullOrTrim();
        }
    }
}