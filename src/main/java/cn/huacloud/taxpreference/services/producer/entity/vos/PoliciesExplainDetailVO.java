package cn.huacloud.taxpreference.services.producer.entity.vos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 政策解读详情VO
 *
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesExplainDetailVO {

    /**
     * 主键ID
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 政策ID
     */
    @ApiModelProperty("id")
    private Long policiesId;

    /**
     * 标题
     */
    @ApiModelProperty("id")
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
     * 正文
     */
    @ApiModelProperty("id")
    private String content;

    /**
     * 录入人用户ID
     */
    private Long inputUserId;

}
