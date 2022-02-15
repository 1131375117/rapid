package cn.huacloud.taxpreference.sync.spider.entity.dos;

import lombok.Data;

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
    private Boolean deleteMark;
    private String nextRelatedContent;
}
