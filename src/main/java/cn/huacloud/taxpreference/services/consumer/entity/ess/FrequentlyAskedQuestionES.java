package cn.huacloud.taxpreference.services.consumer.entity.ess;

import cn.huacloud.taxpreference.services.consumer.entity.AbstractCombinePlainContent;
import cn.huacloud.taxpreference.services.consumer.entity.CombineText;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wangkh
 */
@Data
public class FrequentlyAskedQuestionES extends AbstractCombinePlainContent<Long> {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 政策ID集合
     */
    private List<String> policiesIds;

    /**
     * 标题
     */
    private String title;

    /**
     * 回答
     */
    private String content;

    /**
     * 来源
     */
    private String docSource;

    /**
     * 发布日期
     */
    private LocalDate releaseDate;

    @Override
    public List<CombineText> combineTextList() {
        return Collections.singletonList(CombineText.ofHtml(content));
    }
}
