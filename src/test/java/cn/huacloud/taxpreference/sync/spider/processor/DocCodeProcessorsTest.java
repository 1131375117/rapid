package cn.huacloud.taxpreference.sync.spider.processor;

import cn.huacloud.taxpreference.services.producer.entity.vos.DocCodeVO;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DocCodeProcessorsTest {

    /**
     * 记录问题
     * 1. 一篇文章有多个文号，意为多个单位联合发文，对应提出的多文号拆多篇文章的需求先记录；
     * 2.
     */

    @Test
    public void test() throws Exception {
        ClassPathResource resource = new ClassPathResource("tsv/source_doc_code.tsv");
        List<String> rawCodes = IOUtils.readLines(resource.getInputStream(), StandardCharsets.UTF_8);
        List<Wrapper> success = new ArrayList<>();
        List<Wrapper> fail = new ArrayList<>();

        for (String rawCode : rawCodes) {
            Wrapper wrapper = new Wrapper();

            DocCodeVO docCodeVO = null;
            try {
                docCodeVO = DocCodeProcessors.docCode.apply(rawCode);
                wrapper.setDocCodeVO(docCodeVO)
                        .setDocCode(docCodeVO.toString())
                        .setRawCode(rawCode);
                success.add(wrapper);
            } catch (Exception e) {
                wrapper.setRawCode(rawCode)
                        .setException(e);
                fail.add(wrapper);
            }
        }
        log.info("");
    }

    @Accessors(chain = true)
    @Data
    public static class Wrapper {
        private String rawCode;
        private String docCode;
        private DocCodeVO docCodeVO;
        private Exception exception;

        @Override
        public String toString() {
            return docCodeVO.toString();
        }
    }
}