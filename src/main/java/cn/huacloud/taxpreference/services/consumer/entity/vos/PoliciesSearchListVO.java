package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 政策法规检索列表视图
 * @author wangkh
 */
@Data
public class PoliciesSearchListVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("文号")
    private String docCode;

    @ApiModelProperty("所属区域")
    private SysCodeSimpleVO area;

    @ApiModelProperty("来源")
    private String docSource;

    @ApiModelProperty("所属税种")
    private SysCodeSimpleVO taxCategories;

    @ApiModelProperty("纳税人资格认定类型")
    private List<SysCodeSimpleVO> taxpayerIdentifyTypes;

    @ApiModelProperty("适用企业类型名称")
    private List<SysCodeSimpleVO> enterpriseTypes;

    @ApiModelProperty("适用行业名称")
    private List<SysCodeSimpleVO> industries;

    @ApiModelProperty("有效性")
    private SysCodeSimpleVO validity;

    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;

    @ApiModelProperty("摘要")
    private String digest;

    @ApiModelProperty("标签集合")
    private List<String> labels;

    @ApiModelProperty("无装饰的组合文本")
    private String combinePlainContent;
}
