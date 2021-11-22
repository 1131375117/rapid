package cn.huacloud.taxpreference.tool;

import cn.huacloud.taxpreference.BaseApplicationTest;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.backup.Tax;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeVO;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesExplainStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import cn.huacloud.taxpreference.services.producer.mapper.FrequentlyAskedQuestionMapper;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesExplainMapper;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * @author fuhua
 **/
@Slf4j
public class PolicyBackupTool extends BaseApplicationTest {
    @Autowired
    FrequentlyAskedQuestionMapper frequentlyAskedQuestionMapper;
    @Autowired
    SysCodeService sysCodeService;
    @Autowired
    PoliciesMapper policiesMapper;
    @Autowired
    PoliciesExplainMapper policiesExplainMapper;
    private final HashMap<String, Tax> sourceMap = new HashMap<>();
    @Autowired
    ApplicationContext applicationContext;

    @Test
    // @Transactional
    public void backup() throws SQLException {
        applicationContext.getBeansOfType(Tax.class).values().forEach(tax ->
                sourceMap.put(tax.sourceType(), tax)
        );
        //新增热门问答
        List<Entity> policyQAList = Db.use().findAll("popular_qa_data");
        //新增政策和政策解读
        List<Entity> policyList = Db.use().findAll("policy_data");
         insertQA(policyQAList);

        for (Entity policy : policyList) {
            PoliciesDO policiesDO = insertPolicies(policy);
            if (!explainIsNull(policy)) {
                try {
                    insertExplains(policy, policiesDO);
                }catch (Exception e){
                    log.error("新增一条policy数据失败:{}",policy);
                    continue;
                }
            }
        }
    }

    /**
     * 新增政策解读
     */
    private void insertExplains(Entity policy, PoliciesDO policiesDO) {
        PoliciesExplainDO policiesExplainDO = new PoliciesExplainDO();
        policiesExplainDO.setPoliciesId(policiesDO.getId());
        //相关解读-标题
        policiesExplainDO.setTitle(policy.getStr("related_interpretation_title"));
        //相关解读-源码
        policiesExplainDO.setContent((policy.getStr("related_interpretation_content")));
        //相关解读-来源
        policiesExplainDO.setDocSource(policy.getStr("related_interpretation_source"));
        policiesExplainDO.setCreateTime(LocalDateTime.now());
        policiesExplainDO.setReleaseDate(StringUtils.isBlank(policy.getStr("related_interpretation_date")) ? null : LocalDate.parse(policy.getStr("related_interpretation_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        policiesExplainDO.setUpdateTime(LocalDateTime.now());
        policiesExplainDO.setDeleted(false);
        policiesExplainDO.setInputUserId(1L);
        policiesExplainDO.setPoliciesExplainStatus(PoliciesExplainStatusEnum.REPTILE_SYNCHRONIZATION);
        policiesExplainMapper.insert(policiesExplainDO);
    }

    private Boolean explainIsNull(Entity policy) {
        String url = policy.getStr("related_interpretation_url");
        return StringUtils.isBlank(url);
    }

    @NotNull
    /**
     * 新增政策法规
     * */
    private PoliciesDO insertPolicies(Entity policy) {
        log.info("新增数据id:{}", policy.getStr("id"));
        PoliciesDO policiesDO = new PoliciesDO();
        //获取区域
        SysCodeVO areaVO = sysCodeService.getCodeVOByCodeName(SysCodeType.AREA, policy.getStr("region"));
        //获取税后种类
        SysCodeVO taxClassName = sysCodeService.getCodeVOByCodeName(SysCodeType.AREA, policy.getStr("tax_class_name"));
        //获取有效性
        String valid = ValidityEnum.ValidMap().get(("".equals(policy.getStr("content_is_valid")) || policy.getStr("content_is_valid") == null) ? "未知" : policy.getStr("content_is_valid"));
        //获取content
        String content = policy.getStr("content");
        policiesDO.setTitle(policy.getStr("title"))
                .setDocSource(policy.getStr("content_source"))
                .setAreaName(policy.getStr("region"))
                .setAreaCode(areaVO == null ? "" : areaVO.getCodeValue())
                .setTaxCategoriesName(policy.getStr("tax_class_name"))
                .setTaxCategoriesCode(taxClassName == null ? "" : taxClassName.getCodeValue())
                .setTaxpayerIdentifyTypeCodes("")
                .setTaxpayerIdentifyTypeNames("")
                .setEnterpriseTypeCodes("")
                .setEnterpriseTypeNames("")
                .setIndustryCodes("")
                .setIndustryNames("")
                .setValidity(
                        valid == null ? null:
                                ValidityEnum.valueOf(
                                        valid
                                )
                )
                .setReleaseDate(StringUtils.isBlank(policy.getStr("publish_date")) ? LocalDate.now() : LocalDate.parse(policy.getStr("publish_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .setDigest("")
                .setLabels("")
                .setPoliciesStatus(PoliciesStatusEnum.REPTILE_SYNCHRONIZATION)
                .setAbolishNote("").setInputUserId(1L)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now())
                .setDeleted(false)
                .setContent(content == null ? "" : content)
                .setDocCode(policy.getStr("document_number"))
        ;
        policiesMapper.insert(policiesDO);
        return policiesDO;
    }

    /**
     * 新增QA
     */
    private void insertQA(List<Entity> policyQAList) {
        policyQAList.forEach(policy_qa -> {
            FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = new FrequentlyAskedQuestionDO();
            log.info("热门问答QA:{}", policy_qa.getInt("id"));
            try {
                List<Entity> find = Db.use().find(Entity.create("policy_popular_data").set("popular_qa_id", policy_qa.getStr("id")));
                if (find != null && find.size() > 0) {
                    frequentlyAskedQuestionDO.setPoliciesIds(find.get(0).getStr("policy_id"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            frequentlyAskedQuestionDO.setTitle(policy_qa.getStr("title"));
            frequentlyAskedQuestionDO.setContent(policy_qa.getStr("content"));
            frequentlyAskedQuestionDO.setDocSource(policy_qa.getStr("content_source"));
            frequentlyAskedQuestionDO.setReleaseDate((StringUtils.isBlank(policy_qa.getStr("publish_date")) ? null : LocalDate.parse(policy_qa.getStr("publish_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            frequentlyAskedQuestionDO.setCreateTime(LocalDateTime.now());
            frequentlyAskedQuestionDO.setUpdateTime(LocalDateTime.now());
            frequentlyAskedQuestionDO.setInputUserId(1L);
            frequentlyAskedQuestionDO.setDeleted(false);
            frequentlyAskedQuestionMapper.insert(frequentlyAskedQuestionDO);
        });
    }

}
