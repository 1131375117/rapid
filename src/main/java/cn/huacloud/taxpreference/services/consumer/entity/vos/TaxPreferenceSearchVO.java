package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 税收优惠检索视图
 * @author wangkh
 */
@Data
public class TaxPreferenceSearchVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("优惠事项名称")
    private String taxPreferenceName;

    @ApiModelProperty("所属税种")
    private SysCodeSimpleVO taxCategories;

    @ApiModelProperty("纳税人登记注册类型")
    private SysCodeSimpleVO taxpayerRegisterType;

    @ApiModelProperty("纳税人类型")
    private SysCodeSimpleVO taxpayerType;

    @ApiModelProperty("适用行业")
    private List<SysCodeSimpleVO> industries;

    @ApiModelProperty("适用企业类型")
    private List<SysCodeSimpleVO> enterpriseTypes;

    @ApiModelProperty("纳税信用等级")
    private List<String> taxpayerCreditRatings;

    @ApiModelProperty("减免事项")
    private String taxPreferenceItem;

    @ApiModelProperty("具体优惠内容摘要")
    private String digest;

    @ApiModelProperty("有效性")
    private SysCodeSimpleVO validity;

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
}
