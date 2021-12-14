package cn.huacloud.taxpreference.services.consumer.entity.ess;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.AbstractCombinePlainContent;
import cn.huacloud.taxpreference.services.consumer.entity.CombineText;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * 政策解读检索视图
 * @author wangkh
 */
@Data
public class PoliciesExplainES extends AbstractCombinePlainContent<Long> {

    /**
     * ID主键
     */
    private Long id;
    /**
     * 政策法规ID
     */
    private Long policiesId;
    /**
     * 标题
     */
    private String title;
    /**
     * 所属区域
     */
    private SysCodeSimpleVO area;
    /**
     * 来源
     */
    private String docSource;
    /**
     * 所属税种
     */
    private List<SysCodeSimpleVO> taxCategories;
    /**
     * 纳税人资格认定类型
     */
    private List<SysCodeSimpleVO> taxpayerIdentifyTypes;
    /**
     * 适用企业类型
     */
    private List<SysCodeSimpleVO> enterpriseTypes;
    /**
     * 适用行业
     */
    private List<SysCodeSimpleVO> industries;
    /**
     * 发布日期
     */
    private LocalDate releaseDate;
    /**
     * 正文
     */
    private String content;

    /**
     * 浏览量
     */
    private Long views;

    /**
     * 收藏量
     */
    private Long collections;

    @Override
    public List<CombineText> combineTextList() {
        return Collections.singletonList(CombineText.ofHtml(content));
    }
}
