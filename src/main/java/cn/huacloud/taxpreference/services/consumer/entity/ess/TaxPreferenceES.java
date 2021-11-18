package cn.huacloud.taxpreference.services.consumer.entity.ess;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.AbstractCombinePlainContent;
import cn.huacloud.taxpreference.services.consumer.entity.CombineText;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesDigestSearchVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.SubmitConditionSearchVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangkh
 */
@Data
public class TaxPreferenceES extends AbstractCombinePlainContent<Long> {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 和 taxPreferenceName 相同多索引检索冗余字段
     */
    private String title;

    /**
     * 优惠事项名称
     */
    private String taxPreferenceName;

    /**
     * 所属税种
     */
    private SysCodeSimpleVO taxCategories;

    /**
     * 纳税人登记注册类型
     */
    private SysCodeSimpleVO taxpayerRegisterType;

    /**
     * 纳税人类型
     */
    private SysCodeSimpleVO taxpayerType;

    /**
     * 适用行业
     */
    private List<SysCodeSimpleVO> industries;

    /**
     * 适用企业类型
     */
    private List<SysCodeSimpleVO> enterpriseTypes;

    /**
     * 纳税信用等级
     */
    private List<String> taxpayerCreditRatings;

    /**
     * 减免事项
     */
    private String taxPreferenceItem;

    /**
     * 有效性
     */
    private SysCodeSimpleVO validity;

    /**
     * 政策
     */
    private List<PoliciesDigestSearchVO> policies;

    /**
     * 申报条件
     */
    private List<SubmitConditionSearchVO> submitConditions;

    /**
     * 留存备查资料
     */
    private String keepQueryData;

    /**
     * 提交税务机关资料
     */
    private String submitTaxData;

    /**
     * 资料报送时限
     */
    private String submitTimeLimit;

    /**
     * 申报表填写简要说明
     */
    private String submitDescription;

    /**
     * 标签管理
     */
    private List<String> labels;


    @Override
    public List<CombineText> combineTextList() {
        List<CombineText> list = new ArrayList<>();
        for (PoliciesDigestSearchVO policy : policies) {
            list.add(CombineText.ofText(policy.getTitle()));
            list.add(CombineText.ofText(policy.getDocCode()));
            list.add(CombineText.ofText(policy.getDigest()));
        }
        list.add(CombineText.ofText(keepQueryData));
        list.add(CombineText.ofText(submitTaxData));
        list.add(CombineText.ofText(submitDescription));
        return list;
    }
}
