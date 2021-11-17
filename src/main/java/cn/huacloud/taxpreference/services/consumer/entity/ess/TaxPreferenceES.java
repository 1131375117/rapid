package cn.huacloud.taxpreference.services.consumer.entity.ess;

import cn.huacloud.taxpreference.common.enums.taxpreference.PreferenceValidation;
import cn.huacloud.taxpreference.common.enums.taxpreference.TaxPreferenceStatus;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import cn.huacloud.taxpreference.sync.es.consumer.IDGetter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangkh
 */
@Data
public class TaxPreferenceES implements IDGetter<Long> {
    /**
     * 主键ID
     */
    private Long id;

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
     * 优惠事项名称
     */
    private String taxPreferenceName;

    /**
     * 减免事项
     */
    private String taxPreferenceItem;

    /**
     * 具体优惠内容摘要
     */
    private String digest;

    /**
     * 有效性
     */
    private SysCodeSimpleVO validity;

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

    @Data
    public static class Policies {

    }
}
