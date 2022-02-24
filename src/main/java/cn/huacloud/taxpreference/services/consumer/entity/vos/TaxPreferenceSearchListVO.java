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

    @ApiModelProperty(value = "序号-列表展示用",example = "1")
    private Long num;

    @ApiModelProperty(value = "主键ID-列表展示用",example = "205")
    private Long id;

    @ApiModelProperty(value = "优惠事项名称-列表展示用",example = "税收优惠名称")
    private String taxPreferenceName;

    @ApiModelProperty(value = "发文日期",example = "2022-01-26")
    private LocalDate releaseDate;

    @ApiModelProperty("所属税种-列表展示用")
    private List<SysCodeSimpleVO> taxCategories;

    @ApiModelProperty("纳税人登记注册类型")
    private SysCodeSimpleVO taxpayerRegisterType;

    @ApiModelProperty("纳税人类型")
    private SysCodeSimpleVO taxpayerType;

    @ApiModelProperty("适用行业")
    private List<SysCodeSimpleVO> industries;

    @ApiModelProperty(value = "企业类型",example = "企业类型")
    private String enterpriseType;

    @ApiModelProperty(value = "减免事项",example = "关于XX的减免事项")
    private String taxPreferenceItem;

    @ApiModelProperty(value = "有效性")
    private SysCodeSimpleVO validity;

    @ApiModelProperty("政策")
    private List<PoliciesDigestSearchVO> policies;

    @ApiModelProperty("申报条件")
    private List<ConditionSearchVO> submitConditions;

    @ApiModelProperty(value = "留存备查资料",example = "留存被查资料")
    private String keepQueryData;

    @ApiModelProperty(value = "提交税务机关资料",example = "税务机关资料")
    private String submitTaxData;

    @ApiModelProperty(value = "资料报送时限",example = "资料送报时限")
    private String submitTimeLimit;

    @ApiModelProperty(value = "申报表填写简要说明",example = "申报表简要信息")
    private String submitDescription;

    @ApiModelProperty("标签管理")
    private List<String> labels;

    @ApiModelProperty(value = "无装饰的组合文本",example = "无装饰文本")
    private String combinePlainContent;

    @ApiModelProperty(value = "浏览量",example = "0")
    private Long views;

    @ApiModelProperty(value = "收藏量",example = "0")
    private Long collections;

    @ApiModelProperty(value = "优惠政策（文件名/文号）-列表展示用",example = "国家税务总局长治市税务局公告〔2018〕5号")
    private String combinePoliciesTitle;

    @ApiModelProperty(value = "摘要-列表展示用",example = "国家税务总局长治市税务局关于废止税收规范性文件的公告")
    private String combinePoliciesDigest;
}
