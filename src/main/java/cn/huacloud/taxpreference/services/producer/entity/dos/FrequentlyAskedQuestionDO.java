package cn.huacloud.taxpreference.services.producer.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 热点问答实体
 *
 * @author wuxin
 */
@Data
@TableName("t_frequently_asked_question")
public class FrequentlyAskedQuestionDO {
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
    private Date releaseDate;

    /**
     * 录入人用户id
     */
    private Long inputUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;
}
