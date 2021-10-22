package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author wuxin
 */
@Data
@ApiModel
public class QueryDTO extends KeywordPageQueryDTO {

    @ApiModelProperty("查询标题")
    private String title;

    @ApiModelProperty("查询所属税种码值")
    private String taxCategoriesCode;

    @ApiModelProperty("查询纳税人资格认定类型码")
    private String taxpayerIdentifyTypeCode;

    @ApiModelProperty("查询适用企业类型码值")
    private String enterpriseTypeCode;

    @ApiModelProperty("查询适用行业码值")
    private String industryCode;

    @ApiModelProperty("查询所属区域码值")
    private String areaCode;

    @ApiModelProperty("查询有效性")
    private String validity;

    @ApiModelProperty("查询发布日期")
    private Date releaseDate;



}
