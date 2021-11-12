package cn.huacloud.taxpreference.services.consumer.entity.ess;

import cn.huacloud.taxpreference.services.consumer.entity.vos.SysCodeSearchVO;
import cn.huacloud.taxpreference.sync.es.consumer.GetID;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 政策法规检索视图
 * @author wangkh
 */
@Data
public class PoliciesES implements GetID<Long> {

    /**
     * ID主键
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 文号
     */
    private String docCode;
    /**
     * 所属区域
     */
    private SysCodeSearchVO area;
    /**
     * 来源
     */
    private String docSource;
    /**
     * 所属税种
     */
    private SysCodeSearchVO taxCategories;
    /**
     * 纳税人资格认定类型
     */
    private List<SysCodeSearchVO> taxpayerIdentifyTypes;
    /**
     * 适用企业类型
     */
    private List<SysCodeSearchVO> enterpriseTypes;
    /**
     * 适用行业
     */
    private List<SysCodeSearchVO> industries;
    /**
     * 有效性
     */
    private SysCodeSearchVO validity;
    /**
     * 发布日期
     */
    private LocalDate releaseDate;
    /**
     * 摘要
     */
    private String digest;
    /**
     * 正文
     */
    private String content;
    /**
     * 标签集合
     */
    private List<String> labels;
}
