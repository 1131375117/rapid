package cn.huacloud.taxpreference.services.producer.entity.dtos;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.Date;

/**
 * 政策解读实体
 * @author wuxin
 */
@Data
@TableName("t_policies_explain")
public class PoliciesExplainDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("来源")
    private String docSource;

    @ApiModelProperty("发布日期")
    private Date releaseDate;

    @ApiModelProperty("正文")
    private String content;

    @ApiModelProperty("政策法规id")
    private Long policiesId;
}
