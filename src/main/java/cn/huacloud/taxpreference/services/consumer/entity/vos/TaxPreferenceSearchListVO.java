package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 税收优惠检索视图
 * @author wangkh
 */
@Data
public class TaxPreferenceSearchListVO {

    @ApiModelProperty("序号-列表用")
    private Long num;

    @ApiModelProperty("主键ID-列表用")
    private Long id;

    @ApiModelProperty("优惠事项名称-列表用")
    private String taxPreferenceName;

    @ApiModelProperty("发文日期")
    private LocalDate releaseDate;

    @ApiModelProperty("所属税种-列表用")
    private List<SysCodeSimpleVO> taxCategories;

    @ApiModelProperty("纳税人登记注册类型")
    private SysCodeSimpleVO taxpayerRegisterType;

    @ApiModelProperty("纳税人类型")
    private SysCodeSimpleVO taxpayerType;

    @ApiModelProperty("适用行业")
    private List<SysCodeSimpleVO> industries;

    @ApiModelProperty("企业类型")
    private String enterpriseType;

    @ApiModelProperty("减免事项")
    private String taxPreferenceItem;

    @ApiModelProperty("有效性")
    private SysCodeSimpleVO validity;

    @ApiModelProperty("政策")
    private List<PoliciesDigestSearchVO> policies;

    @ApiModelProperty("申报条件")
    private List<ConditionSearchVO> submitConditions;

    @ApiModelProperty("留存备查资料")
    private String keepQueryData;

    @ApiModelProperty("提交税务机关资料")
    private String submitTaxData;

    @ApiModelProperty("资料报送时限")
    private String submitTimeLimit;

    @ApiModelProperty("申报表填写简要说明")
    private String submitDescription;

    @ApiModelProperty("标签管理")
    private List<String> labels;

    @ApiModelProperty("无装饰的组合文本")
    private String combinePlainContent;

    @ApiModelProperty("浏览量")
    private Long views;

    @ApiModelProperty("收藏量")
    private Long collections;

    @ApiModelProperty("优惠政策（文件名/文号）-列表用")
    private String combinePoliciesTitle;

    @ApiModelProperty("摘要-列表用")
    private String combinePoliciesDigest;
}
