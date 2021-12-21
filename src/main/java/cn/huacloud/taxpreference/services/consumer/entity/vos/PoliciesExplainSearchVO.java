package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 政策解读检索列表视图
 * @author wangkh
 */
@Data
public class PoliciesExplainSearchVO extends UserCollectionInfo {
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("所属区域")
    private SysCodeSimpleVO area;

    @ApiModelProperty("来源")
    private String docSource;

    @ApiModelProperty("所属税种")
    private List<SysCodeSimpleVO> taxCategories;

    @ApiModelProperty("纳税人资格认定类型")
    private List<SysCodeSimpleVO> taxpayerIdentifyTypes;

    @ApiModelProperty("适用企业类型名称")
    private List<SysCodeSimpleVO> enterpriseTypes;

    @ApiModelProperty("适用行业名称")
    private List<SysCodeSimpleVO> industries;

    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;

    @ApiModelProperty("正文")
    private String content;

    @ApiModelProperty("上一篇、下一篇")
    PreviousNextVO<Long> previousNext;

    @ApiModelProperty("浏览量")
    private Long views;

    @ApiModelProperty("收藏量")
    private Long collections;
}
