package cn.huacloud.taxpreference.sync.spider.job;

import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.enums.JobType;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.common.utils.DocCodeUtil;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.common.entity.dos.AttachmentDO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeVO;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import cn.huacloud.taxpreference.services.producer.entity.vos.DocCodeVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceCountVO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.huacloud.taxpreference.sync.es.trigger.impl.PoliciesEventTrigger;
import cn.huacloud.taxpreference.sync.spider.DataSyncJob;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyAttachmentDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyDataDO;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.PoliciesCombineDTO;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.SpiderPolicyCombineDTO;
import cn.huacloud.taxpreference.sync.spider.processor.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 政策数据同步作业
 *
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class PoliciesDataSyncJob implements DataSyncJob<SpiderPolicyCombineDTO, PoliciesCombineDTO> {

    private final PoliciesMapper policiesMapper;

    private final SysCodeProcessors sysCodeProcessors;

    private final AttachmentProcessors attachmentProcessors;

    private final AttachmentService attachmentService;
    private final PoliciesEventTrigger policiesEventTrigger;
    private final TaxPreferenceService taxPreferenceService;

    public static final int DIGEST_MAX_LENGTH = 200;

    @Override
    public int order() {
        return -1;
    }

    @Override
    public DocType getDocType() {
        return DocType.POLICIES;
    }

    @Override
    public String getSyncIdsQuerySql() {
        return "SELECT id FROM policy_data WHERE spider_time BETWEEN ? AND ? ";
    }

    @Override
    public boolean needReSync(Long docId) {
        PoliciesDO policiesDO = policiesMapper.selectById(docId);
        return policiesDO == null || policiesDO.getPoliciesStatus() != PoliciesStatusEnum.PUBLISHED;
    }

    @Override
    public SpiderPolicyCombineDTO getSourceData(String sourceId, JdbcTemplate jdbcTemplate) {
        String policySql = "SELECT * FROM policy_data WHERE id = ?";
        SpiderPolicyDataDO spiderPolicyDataDO = jdbcTemplate.queryForObject(policySql, DataClassRowMapper.newInstance(SpiderPolicyDataDO.class), sourceId);
        String attachmentSql = "SELECT * FROM policy_attachment WHERE doc_id = ? AND attachment_type = '政策'";
        List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList = jdbcTemplate.query(attachmentSql, DataClassRowMapper.newInstance(SpiderPolicyAttachmentDO.class), sourceId);
        SpiderPolicyCombineDTO spiderPolicyCombineDTO = new SpiderPolicyCombineDTO()
                .setSpiderPolicyDataDO(spiderPolicyDataDO)
                .setSpiderPolicyAttachmentDOList(spiderPolicyAttachmentDOList);
        spiderPolicyCombineDTO.setSpiderUrl(spiderPolicyDataDO.getUrl());
        return spiderPolicyCombineDTO;
    }

    @Override
    public PoliciesCombineDTO process(SpiderPolicyCombineDTO sourceData, JobType jobType) {
        SpiderPolicyDataDO policy = sourceData.getSpiderPolicyDataDO();
        LocalDateTime now = LocalDateTime.now();
        PoliciesDO policiesDO = new PoliciesDO()
                .setPoliciesStatus(PoliciesStatusEnum.REPTILE_SYNCHRONIZATION)
                .setCreateTime(now)
                .setUpdateTime(now)
                .setInputUserId(-1L);
        // 正文
        String content = policy.getNextContent();
        if (StringUtils.isEmpty(content)) {
            content = policy.getContent();
        }
        Document document = HtmlProcessors.content.apply(content);

        //是否删除
        policiesDO.setDeleted(policy.getDeleteMark());

        // 附件
        List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList = sourceData.getSpiderPolicyAttachmentDOList();
        Pair<Document, List<AttachmentDO>> pair = attachmentProcessors.processContentAndAttachment(document, spiderPolicyAttachmentDOList, AttachmentType.POLICIES);

        // 设置正文
        policiesDO.setContent(pair.getFirst().html());
        if (JobType.UPDATE.equals(jobType)) {
            policiesDO.setPoliciesStatus(null);
            policiesDO.setCreateTime(null);
            return new PoliciesCombineDTO()
                    .setPoliciesDO(policiesDO)
                    .setAttachmentDOList(pair.getSecond());
        }

        // 标题
        policiesDO.setTitle(policy.getTitle());

        // 文号
        DocCodeVO docCodeVO = DocCodeProcessors.docCode.apply(policy.getDocumentNumber());
        CustomBeanUtil.copyProperties(docCodeVO, policiesDO);
        policiesDO.setDocCode(DocCodeUtil.getDocCode(docCodeVO));

        // 税种
        if (!StringUtils.isEmpty(policy.getTaxClassName())) {
            List<String> taxClassNames = Arrays.stream(policy.getTaxClassName().split(",")).collect(Collectors.toList());
            List<String> categoriesNames = new ArrayList<>();
            List<String> categoriesCodes = new ArrayList<>();
            for (String taxClassName : taxClassNames) {
                SysCodeVO taxCategories = sysCodeProcessors.taxCategories.apply(taxClassName);
                categoriesNames.add(taxCategories.getCodeName());
                categoriesCodes.add(taxCategories.getCodeValue());
            }
            policiesDO.setTaxCategoriesNames(StringUtils.join(categoriesNames, ","));
            policiesDO.setTaxCategoriesCodes(StringUtils.join(categoriesCodes, ","));
        }
        // 所属区域
        SysCodeVO area = sysCodeProcessors.area.apply(policy.getRegion());
        policiesDO.setAreaCode(area.getCodeValue());
        policiesDO.setAreaName(area.getCodeName());

        // 来源
        policiesDO.setDocSource(policy.getContentSource());

        // 有效性
        policiesDO.setValidity(ValidityEnum.getByName(policy.getContentIsValid()));

        // 发布日期
        policiesDO.setReleaseDate(DateProcessors.releaseDate.apply(policy.getPublishDate()));

/*        // 正文
        String content = policy.getNextContent();
        if(StringUtils.isEmpty(content)){
            content=policy.getContent();
        }
        Document document = HtmlProcessors.content.apply(content);

        // 附件
        List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList = sourceData.getSpiderPolicyAttachmentDOList();
        Pair<Document, List<AttachmentDO>> pair = attachmentProcessors.processContentAndAttachment(document, spiderPolicyAttachmentDOList, AttachmentType.POLICIES);

        // 设置正文
        policiesDO.setContent(pair.getFirst().html());*/

        // 设置摘要信息
        String digest = pair.getFirst().text();
        if (digest.length() > DIGEST_MAX_LENGTH) {
            digest = digest.substring(0, DIGEST_MAX_LENGTH);
        }
        policiesDO.setDigest(digest);

        return new PoliciesCombineDTO()
                .setPoliciesDO(policiesDO)
                .setAttachmentDOList(pair.getSecond());
    }

    @Override
    public boolean isDocExist(Long docId) {
        return policiesMapper.selectCount(Wrappers.lambdaQuery(PoliciesDO.class).eq(PoliciesDO::getId, docId)) > 0;
    }

    @Transactional
    @Override
    public Long saveProcessData(PoliciesCombineDTO processData) {
        PoliciesDO policiesDO = processData.getPoliciesDO();
        policiesMapper.insert(policiesDO);
        // 保存附件
        List<AttachmentDO> attachmentDOList = processData.getAttachmentDOList();
        attachmentService.saveSpiderAttachmentList(policiesDO.getId(), attachmentDOList);
        return policiesDO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateProcessData(Long docId, PoliciesCombineDTO processData) {
        PoliciesDO policiesDO = processData.getPoliciesDO();
        policiesDO.setId(docId);
        policiesMapper.updateById(policiesDO);
        if (policiesDO.getDeleted()) {
            //涉及税收优惠需要将税收优惠撤回
            log.info("需要撤回的docid:{}", docId);
            //根据政策法规id找税收优惠id
            List<TaxPreferenceCountVO> taxPreferenceIdVOs = taxPreferenceService.getTaxPreferenceId(docId);
            for (TaxPreferenceCountVO taxPreferenceIdVO : taxPreferenceIdVOs) {
                taxPreferenceService.reTaxPreference(taxPreferenceIdVO.getTaxPreferenceId());
            }
            policiesEventTrigger.deleteEvent(docId);
        }
        // 保存附件
        List<AttachmentDO> attachmentDOList = processData.getAttachmentDOList();
        attachmentService.saveSpiderAttachmentList(policiesDO.getId(), attachmentDOList);
    }

}
