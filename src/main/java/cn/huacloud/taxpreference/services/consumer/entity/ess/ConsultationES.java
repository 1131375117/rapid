package cn.huacloud.taxpreference.services.consumer.entity.ess;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationContentESVO;
import cn.huacloud.taxpreference.sync.es.consumer.IDGetter;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 专家咨询检索
 *
 * @author fuhua
 **/
@Data
public class ConsultationES implements IDGetter<Object> {
    private Long id;
    /**
     * 咨询者id
     */
    private Long customerUserId;
    /**
     * 专家id
     */
    private Long professorUserId;
    /**
     * 税务实务
     */
    private String taxPractices;
    /**
     * 答复时间
     */
    private LocalDateTime finishTime;
    /**
     * 税种
     */
    private List<SysCodeSimpleVO> taxCategories;
    /**
     * 行业
     */
    private List<SysCodeSimpleVO> industries;
    /**
     * 正文内容
     */
    private List<ConsultationContentESVO> consultationContent;

    /**
     * 浏览量
     */
    private Long views;

    /**
     * 收藏量
     */
    private Long collections;
}
