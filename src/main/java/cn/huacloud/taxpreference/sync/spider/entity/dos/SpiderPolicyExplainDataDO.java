package cn.huacloud.taxpreference.sync.spider.entity.dos;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wuxin
 */
@Data
public class SpiderPolicyExplainDataDO {
    private String id;
    private String relatedInterpretationUrl;
    private String relatedInterpretationTitle;
    private String relatedInterpretationDate;
    private String relatedInterpretationSource;
    private String relatedInterpretationContent;
    private String relatedInterpretationHtml;
}
