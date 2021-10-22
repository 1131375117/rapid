package cn.huacloud.taxpreference.services.producer.entity.vos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 热点问答实体
 * @author wuxin
 */
@Data
@TableName("t_ frequently_asked_question")
public class FrequentlyAskedQuestionVO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 政策ID集合
     */
    private String policiesIds;

    /**
     * 问题
     */
    private String question;

    /**
     * 回答
     */
    private String answer;

    /**
     * 来源
     */
    private String docSource;

    /**
     * 发布日期
     */
    private LocalDate releaseDate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;
}
