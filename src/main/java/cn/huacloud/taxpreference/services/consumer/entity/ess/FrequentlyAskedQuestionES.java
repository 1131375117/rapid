package cn.huacloud.taxpreference.services.consumer.entity.ess;

import cn.huacloud.taxpreference.sync.es.consumer.IDGetter;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wangkh
 */
@Data
public class FrequentlyAskedQuestionES implements IDGetter<Long> {
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
}
