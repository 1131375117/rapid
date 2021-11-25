package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 案例表实体
 *
 * @author fuhua
 **/
@Data
@Accessors(chain = true)
public class OtherDocVO {
    private Long id;
    /**
     * 文档类型：CASE_ANALYSIS-案例分析
     */
    private SysCodeSimpleVO docType;
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

}
