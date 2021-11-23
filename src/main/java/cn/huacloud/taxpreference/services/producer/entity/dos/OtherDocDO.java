package cn.huacloud.taxpreference.services.producer.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 案例表实体
 *
 * @author fuhua
 **/
@Data
@TableName("t_other_doc")
@Accessors(chain = true)
public class OtherDocDO {
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
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
