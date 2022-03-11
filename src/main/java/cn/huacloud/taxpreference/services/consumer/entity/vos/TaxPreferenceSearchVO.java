package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 税收优惠检索视图
 *
 * @author wangkh
 */
@Data
public class TaxPreferenceSearchVO extends UserCollectionInfo{

    @ApiModelProperty(value = "主键ID", example = "187")
    private Long id;

    @ApiModelProperty(value = "优惠事项名称", example = "士大夫")
    private String taxPreferenceName;

    @ApiModelProperty(value = "发文日期", example = "2022-01-13")
    private LocalDate releaseDate;

    @ApiModelProperty("所属税种")
    private List<SysCodeSimpleVO> taxCategories;

    @ApiModelProperty("纳税人登记注册类型")
    private SysCodeSimpleVO taxpayerRegisterType;

    @ApiModelProperty("纳税人类型")
    private SysCodeSimpleVO taxpayerType;

    @ApiModelProperty("适用行业")
    private List<SysCodeSimpleVO> industries;

    @ApiModelProperty(value = "适用企业类型", example = "第三方企业")
    private String enterpriseType;

    @ApiModelProperty(value = "减免事项", example = "第三方减免事项")
    private String taxPreferenceItem;

    @ApiModelProperty("有效性")
    private SysCodeSimpleVO validity;

    @ApiModelProperty("政策")
    private List<PoliciesDigestSearchVO> policies;

    @ApiModelProperty("申报条件")
    private List<ConditionSearchVO> conditions;

    @ApiModelProperty(value = "留存备查资料", example = "留存备查资料")
    private String keepQueryData;

    @ApiModelProperty(value = "提交税务机关资料", example = "提交税务机关资料")
    private String submitTaxData;

    @ApiModelProperty(value = "资料报送时限", example = "资料报送时限")
    private String submitTimeLimit;

    @ApiModelProperty(value = "申报表填写简要说明", example = "申报表填写简要说明")
    private String submitDescription;

    @ApiModelProperty("标签管理")
    private List<String> labels;

    @ApiModelProperty(value = "无装饰的组合文本", example = "关于XXXX的公告内容无装饰组合文本")
    private String combinePlainContent;

    @ApiModelProperty("上一篇、下一篇")
    PreviousNextVO<Long> previousNext;

    @ApiModelProperty(value = "浏览量", example = "0")
    private Long views;

    @ApiModelProperty(value = "收藏量", example = "0")
    private Long collections;
}
