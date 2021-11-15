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
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void backup() throws SQLException {
        applicationContext.getBeansOfType(Tax.class).values().forEach(tax ->
                sourceMap.put(tax.sourceType(), tax)
        );
        //新增热门问答
        List<Entity> policy_qa_datas = Db.use().findAll("popular_qa_data");
        //新增政策和政策解读
        List<Entity> policyList = Db.use().findAll("policy_data");
        insertQA(policy_qa_datas);
        policyList.forEach(policy -> {
            PoliciesDO policiesDO = insertPolicies(policy);
            insertExplains(policy, policiesDO);
        });
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
        policiesExplainDO.setContent((policy.getStr("related_interpretation_html")));
        //相关解读-来源
        policiesExplainDO.setDocSource(policy.getStr("related_interpretation_source"));
        policiesExplainDO.setCreateTime(LocalDateTime.now());
        policiesExplainDO.setReleaseDate(LocalDate.parse(policy.getStr("related_interpretation_date")));
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
                .setValidity(ValidityEnum.valueOf(policy.getStr("content_is_valid")))
                .setReleaseDate(LocalDate.parse(policy.getStr("publish_date")))
                .setDigest("")
                .setLabels("")
                .setPoliciesStatus(PoliciesStatusEnum.valueOf((policy.getStr("content_is_valid"))))
                .setAbolishNote("").setInputUserId(1L)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now())
                .setDeleted(false)
                .setContent(sourceMap.get(policy.getStr("site_name")).parseHtml(policy.getStr("html")))
                .setDocCode(policy.getStr("document_number"))
        ;
        policiesMapper.insert(policiesDO);
        return policiesDO;
    }

    /**
     * 新增QA
     */
    private void insertQA(List<Entity> policy_qa_datas) {
        policy_qa_datas.forEach(policy_qa -> {
            FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = new FrequentlyAskedQuestionDO();
            try {
                List<Entity> find = Db.use().find(Entity.create("policy_popular_data").set("policy_id", policy_qa.getStr("id")));
                if(find!=null&&find.size()>0){
                    frequentlyAskedQuestionDO.setPoliciesIds(find.get(0).getStr("policy_id"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            frequentlyAskedQuestionDO.setTitle(policy_qa.getStr("title"));
            frequentlyAskedQuestionDO.setContent(sourceMap.get(policy_qa.getStr("site_name"))==null?"":sourceMap.get(policy_qa.getStr("site_name")).parseQA(policy_qa.getStr("html")));
            frequentlyAskedQuestionDO.setDocSource(policy_qa.getStr("content_source"));
            frequentlyAskedQuestionDO.setReleaseDate((StringUtils.isBlank(policy_qa.getStr("publish_date")) ?null:LocalDate.parse(policy_qa.getStr("publish_date"))));
            frequentlyAskedQuestionDO.setCreateTime(LocalDateTime.now());
            frequentlyAskedQuestionDO.setUpdateTime(LocalDateTime.now());
            frequentlyAskedQuestionDO.setInputUserId(1L);
            frequentlyAskedQuestionDO.setDeleted(false);
            frequentlyAskedQuestionMapper.insert(frequentlyAskedQuestionDO);
        });
    }

}
