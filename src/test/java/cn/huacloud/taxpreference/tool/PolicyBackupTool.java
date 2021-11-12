package cn.huacloud.taxpreference.tool;

import cn.huacloud.taxpreference.BaseApplicationTest;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.backup.Tax;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeVO;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @author: fuhua
 * @create: 2021-11-10 14:09
 **/
@Slf4j
public class PolicyBackupTool extends BaseApplicationTest  {
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
    public void backup() throws SQLException {
        applicationContext.getBeansOfType(Tax.class).values().forEach(tax ->
                sourceMap.put(tax.sourceType(), tax)
        );
        List<Entity> policy_qa_datas = Db.use().findAll("popular_qa_datas");
        List<Entity> policyList = Db.use().findAll("policy_datas");
        insertQA(policy_qa_datas);
        for (Entity policy : policyList) {
            PoliciesDO policiesDO = insertPolicies(policy);
            insertExplains(policy, policiesDO);
        }

    }

    /**
     * 新增政策解读
     */
    private void insertExplains(Entity policy, PoliciesDO policiesDO) {
        PoliciesExplainDO policiesExplainDO = new PoliciesExplainDO();
        policiesExplainDO.setPoliciesId(policiesDO.getId());
        policiesExplainDO.setTitle(policiesDO.getTitle());
        policiesExplainDO.setContent(DelTagsUtil.getTextFromHtml(policy.getStr("related_interpretation_html")));
        policiesExplainDO.setDocSource(policy.getStr("content_source"));
        policiesExplainDO.setCreateTime(LocalDateTime.now());
        policiesExplainDO.setReleaseDate(LocalDate.now());
        policiesExplainDO.setUpdateTime(LocalDateTime.now());
        policiesExplainDO.setDeleted(false);
        policiesExplainDO.setInputUserId(1L);
        policiesExplainMapper.insert(policiesExplainDO);
    }

    @NotNull
    /**
     * 新增政策法规
     * */
    private PoliciesDO insertPolicies(Entity policy) {
        log.info("新增数据id:{}", policy.getStr("id"));
        PoliciesDO policiesDO = new PoliciesDO();
        SysCodeVO areaVO = sysCodeService.getCodeVOByCodeName(SysCodeType.AREA, policy.getStr("region"));
        SysCodeVO taxClassName = sysCodeService.getCodeVOByCodeName(SysCodeType.AREA, policy.getStr("tax_class_name"));
        policiesDO.setTitle(policy.getStr("title")).setDocSource(policy.getStr("content_source")).setAreaName(policy.getStr("region"))
                .setAreaCode(areaVO == null ? "" : areaVO.getCodeValue())
                .setTaxCategoriesName(policy.getStr("tax_class_name"))
                .setTaxCategoriesCode(taxClassName == null ? "" : taxClassName.getCodeValue()).setTaxpayerIdentifyTypeCodes("").setTaxpayerIdentifyTypeNames("")
                .setEnterpriseTypeCodes("").setEnterpriseTypeNames("").setIndustryCodes("").setIndustryNames("")
                .setValidity(ValidityEnum.FULL_TEXT_VALID).setReleaseDate(LocalDate.now()).setDigest("").setLabels("").setPoliciesStatus(PoliciesStatusEnum.FULL_TEXT_REPEAL)
                .setAbolishNote("").setInputUserId(1L).setCreateTime(LocalDateTime.now()).setUpdateTime(LocalDateTime.now()).setDeleted(false)
                .setContent(sourceMap.get(policy.getStr("site_name")).parseHtml(policy.getStr("html")))
                .setDocCode("文号")
        ;
        policiesMapper.insert(policiesDO);
        return policiesDO;
    }

    /**
     * 新增QA
     */
    private void insertQA(List<Entity> policy_qa_datas) {
        for (Entity policy_qa : policy_qa_datas) {
            FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = new FrequentlyAskedQuestionDO();
            frequentlyAskedQuestionDO.setTitle(policy_qa.getStr("popular_qa_title"));
            frequentlyAskedQuestionDO.setContent(sourceMap.get(policy_qa.getStr("site_name")).parseQA(policy_qa.getStr("popular_qa_html")));
            frequentlyAskedQuestionDO.setDocSource(policy_qa.getStr("content_source"));
            frequentlyAskedQuestionDO.setReleaseDate(LocalDate.now());
            frequentlyAskedQuestionDO.setCreateTime(LocalDateTime.now());
            frequentlyAskedQuestionDO.setUpdateTime(LocalDateTime.now());
            frequentlyAskedQuestionDO.setInputUserId(1L);
            frequentlyAskedQuestionDO.setDeleted(false);
            frequentlyAskedQuestionMapper.insert(frequentlyAskedQuestionDO);
        }
    }

}
