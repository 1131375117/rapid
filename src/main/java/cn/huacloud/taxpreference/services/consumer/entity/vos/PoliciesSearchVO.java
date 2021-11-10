package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 政策法规检索视图
 * @author wangkh
 */
@Data
public class PoliciesSearchVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("文号")
    private String docCode;

    @ApiModelProperty("所属区域")
    private SysCodeSearchVO area;

    @ApiModelProperty("来源")
    private String docSource;

    @ApiModelProperty("所属税种")
    private SysCodeSearchVO taxCategories;

    @ApiModelProperty("纳税人资格认定类型")
    private List<SysCodeSearchVO> taxpayerIdentifyTypes;

    @ApiModelProperty("适用企业类型名称")
    private List<SysCodeSearchVO> enterpriseTypes;

    @ApiModelProperty("适用行业名称")
    private List<SysCodeSearchVO> industries;

    @ApiModelProperty("有效性")
    private SysCodeSearchVO validity;

    @ApiModelProperty("发布日期")
    private String releaseDate;

    @ApiModelProperty("摘要")
    private String digest;

    @ApiModelProperty("正文")
    private String content;

    @ApiModelProperty("标签集合")
    private List<String> labels;
}
