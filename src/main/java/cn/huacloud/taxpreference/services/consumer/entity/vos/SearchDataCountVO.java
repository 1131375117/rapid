package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 首页数据统计视图
 * @author wangkh
 */
@Data
public class SearchDataCountVO {
    @ApiModelProperty("中央政策总数")
    private Long centralPoliciesCount;
    @ApiModelProperty("地方政策总数")
    private Long localPoliciesCount;
    @ApiModelProperty("税收优惠总数")
    private Long taxPreferenceCount;
    @ApiModelProperty("官方问答总数")
    private Long faqCont;
    @ApiModelProperty("热门咨询总数")
    private ConsultationCountVO consultationCountVO;
}
