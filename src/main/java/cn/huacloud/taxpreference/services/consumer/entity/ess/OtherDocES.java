package cn.huacloud.taxpreference.services.consumer.entity.ess;

import cn.huacloud.taxpreference.sync.es.consumer.IDGetter;
import lombok.Data;

import java.time.LocalDate;

/**
 * 案例ES检索
 *
 * @author fuhua
 **/
@Data
public class OtherDocES implements IDGetter<Object> {
    private Long id;
    /**
     * 文档类型：CASE_ANALYSIS-案例分析
     */
    private String docType;
    /**
     * 标题
     */
    private String title;
    /**
     * 来源
     */
    private String docSource;
    /**
     * 发布日期
     */
    private LocalDate releaseDate;
    /**
     * 富文本内容
     */
    private String htmlContent;
    /**
     * 无装饰文本内容
     */
    private String plainContent;
    /**
     * 扩展属性1
     */
    private String extendsField1;
    /**
     * 扩展属性2
     */
    private String extendsField2;


}
