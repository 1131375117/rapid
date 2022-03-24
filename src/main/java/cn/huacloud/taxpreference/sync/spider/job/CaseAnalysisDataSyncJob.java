package cn.huacloud.taxpreference.sync.spider.job;

import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.enums.JobType;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.common.entity.dos.AttachmentDO;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dos.OtherDocDO;
import cn.huacloud.taxpreference.services.producer.mapper.OtherDocMapper;
import cn.huacloud.taxpreference.sync.es.trigger.impl.PoliciesEventTrigger;
import cn.huacloud.taxpreference.sync.spider.DataSyncJob;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderCaseDataDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyAttachmentDO;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.CaseAnalysisCombineDTO;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.SpiderCaseAnalysisCombineDTO;
import cn.huacloud.taxpreference.sync.spider.processor.AttachmentProcessors;
import cn.huacloud.taxpreference.sync.spider.processor.DateProcessors;
import cn.huacloud.taxpreference.sync.spider.processor.HtmlProcessors;
import cn.huacloud.taxpreference.sync.spider.processor.SysCodeProcessors;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 政策数据同步作业
 *
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class CaseAnalysisDataSyncJob implements DataSyncJob<SpiderCaseAnalysisCombineDTO, CaseAnalysisCombineDTO> {

    private final OtherDocMapper otherDocMapper;

    private final SysCodeProcessors sysCodeProcessors;

    private final AttachmentProcessors attachmentProcessors;

    private final AttachmentService attachmentService;
    private final PoliciesEventTrigger policiesEventTrigger;
    private final TaxPreferenceService taxPreferenceService;

    public static final int DIGEST_MAX_LENGTH = 200;

    @Override
    public int order() {
        return 999;
    }

    @Override
    public DocType getDocType() {
        return DocType.CASE_ANALYSIS;
    }

    @Override
    public String getSyncIdsQuerySql() {
        return "SELECT uuid  FROM policy_case_data WHERE spider_time BETWEEN ? AND ?";
    }

    @Override
    public boolean needReSync(Long docId) {
        OtherDocDO otherDocDO = otherDocMapper.selectById(docId);
        return otherDocDO == null;
        //  return policiesDO==null;
    }

    @Override
    public SpiderCaseAnalysisCombineDTO getSourceData(String sourceId, JdbcTemplate jdbcTemplate) {
        String policySql = "SELECT * FROM policy_case_data WHERE uuid = ?";
        SpiderCaseDataDO spiderCaseDataDO = jdbcTemplate.queryForObject(policySql, DataClassRowMapper.newInstance(SpiderCaseDataDO.class), sourceId);
        String attachmentSql = "SELECT * FROM policy_attachment WHERE doc_id = ? AND attachment_type = '税屋'";
        List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList = jdbcTemplate.query(attachmentSql, DataClassRowMapper.newInstance(SpiderPolicyAttachmentDO.class), sourceId);
        SpiderCaseAnalysisCombineDTO spiderCaseAnalysisCombineDTO = new SpiderCaseAnalysisCombineDTO()
                .setSpiderCaseDataDO(spiderCaseDataDO)
                .setSpiderPolicyAttachmentDOList(spiderPolicyAttachmentDOList);
        spiderCaseAnalysisCombineDTO.setSpiderUrl(spiderCaseDataDO.getUrl());
        return spiderCaseAnalysisCombineDTO;
    }

    @Override
    public CaseAnalysisCombineDTO process(SpiderCaseAnalysisCombineDTO sourceData, JobType jobType) {
        SpiderCaseDataDO spiderCaseDataDO = sourceData.getSpiderCaseDataDO();
        LocalDateTime now = LocalDateTime.now();
        OtherDocDO otherDocDO = new OtherDocDO()
                .setDocType(DocType.CASE_ANALYSIS)
                .setCreateTime(now)
                .setUpdateTime(now);

        // 正文
        String content = spiderCaseDataDO.getContentHtml();
        Document document = HtmlProcessors.content.apply(content);

        // 附件
        List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList = sourceData.getSpiderPolicyAttachmentDOList();
        Pair<Document, List<AttachmentDO>> pair = attachmentProcessors.processContentAndAttachment(document, spiderPolicyAttachmentDOList, AttachmentType.CASE_ANALYSIS);

        // 设置正文
        otherDocDO.setHtmlContent(pair.getFirst().html());
        otherDocDO.setPlainContent(spiderCaseDataDO.getContentText());

        if (JobType.UPDATE.equals(jobType)) {
            otherDocDO.setCreateTime(null);
            return new CaseAnalysisCombineDTO()
                    .setOtherDocDO(otherDocDO)
                    .setAttachmentDOList(pair.getSecond());
        }
        // 标题
        otherDocDO.setTitle(spiderCaseDataDO.getTitle());
        // 来源
        otherDocDO.setDocSource(spiderCaseDataDO.getContentSource());

        // 发布日期
        otherDocDO.setReleaseDate(DateProcessors.releaseDate.apply(spiderCaseDataDO.getPublishTime()));

        //设置caseType
        otherDocDO.setExtendsField1(spiderCaseDataDO.getCaseType());

        return new CaseAnalysisCombineDTO()
                .setOtherDocDO(otherDocDO)
                .setAttachmentDOList(pair.getSecond());
    }

    @Override
    public boolean isDocExist(Long docId) {
        return otherDocMapper.selectCount(Wrappers.lambdaQuery(OtherDocDO.class).eq(OtherDocDO::getId, docId)) > 0;
    }

    @Transactional
    @Override
    public Long saveProcessData(CaseAnalysisCombineDTO processData) {
        OtherDocDO otherDocDO = processData.getOtherDocDO();
        otherDocMapper.insert(otherDocDO);
        // 保存附件
        List<AttachmentDO> attachmentDOList = processData.getAttachmentDOList();
        attachmentService.saveSpiderAttachmentList(otherDocDO.getId(), attachmentDOList);
        return otherDocDO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateProcessData(Long docId, CaseAnalysisCombineDTO processData) {
        OtherDocDO otherDocDO = processData.getOtherDocDO();
        otherDocDO.setId(docId);
        otherDocMapper.updateById(otherDocDO);
        // 保存附件
        List<AttachmentDO> attachmentDOList = processData.getAttachmentDOList();
        attachmentService.saveSpiderAttachmentList(otherDocDO.getId(), attachmentDOList);
    }

}
