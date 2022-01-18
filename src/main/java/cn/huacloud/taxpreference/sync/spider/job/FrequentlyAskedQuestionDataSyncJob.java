package cn.huacloud.taxpreference.sync.spider.job;

import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.common.entity.dos.AttachmentDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.enums.FrequentlyAskedQuestionStatusEnum;
import cn.huacloud.taxpreference.services.producer.mapper.FrequentlyAskedQuestionMapper;
import cn.huacloud.taxpreference.sync.es.trigger.impl.FAQEventTrigger;
import cn.huacloud.taxpreference.sync.spider.DataSyncJob;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyAttachmentDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyDataDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPopularQaDataDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderQaRealationDataDO;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.FrequentlyAskedQuestionCombineDTO;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.SpiderPopularQaDataCombineDTO;
import cn.huacloud.taxpreference.sync.spider.processor.AttachmentProcessors;
import cn.huacloud.taxpreference.sync.spider.processor.DateProcessors;
import cn.huacloud.taxpreference.sync.spider.processor.HtmlProcessors;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhaoqiankun
 * @date 2021/12/29
 * 热门回答数据同步作业
 */
@RequiredArgsConstructor
@Component
public class FrequentlyAskedQuestionDataSyncJob implements
        DataSyncJob<SpiderPopularQaDataCombineDTO, FrequentlyAskedQuestionCombineDTO> {

    private final FrequentlyAskedQuestionMapper frequentlyAskedQuestionMapper;

    private final AttachmentProcessors attachmentProcessors;

    private final AttachmentService attachmentService;
    private final FAQEventTrigger faqEventTrigger;

    @Override
    public int order() {
        return 0;
    }

    @Override
    public DocType getDocType() {
        return DocType.FREQUENTLY_ASKED_QUESTION;
    }

    @Override
    public String getSyncIdsQuerySql() {
        return "SELECT id FROM popular_qa_data WHERE spider_time BETWEEN ? AND ?";
    }

    @Override
    public boolean needReSync(Long docId) {
        FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = frequentlyAskedQuestionMapper.selectById(docId);
        return frequentlyAskedQuestionDO == null || frequentlyAskedQuestionDO.getFrequentlyAskedQuestionStatus()
                != FrequentlyAskedQuestionStatusEnum.PUBLISHED;
    }

    @Override
    public SpiderPopularQaDataCombineDTO getSourceData(String sourceId, JdbcTemplate jdbcTemplate) {
        String popularQaSql = "SELECT * FROM popular_qa_data WHERE id = ?";
        SpiderPopularQaDataDO spiderPopularQaDataDO = jdbcTemplate.queryForObject(popularQaSql,
                DataClassRowMapper.newInstance(SpiderPopularQaDataDO.class), sourceId);
        String attachmentSql = "SELECT * FROM policy_attachment WHERE doc_id = ? AND attachment_type = '热门回答'";
        List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList = jdbcTemplate.query(attachmentSql,
                DataClassRowMapper.newInstance(SpiderPolicyAttachmentDO.class), sourceId);
        SpiderQaRealationDataDO spiderQaRealationDataDO = new SpiderQaRealationDataDO();
        try {
            String qaSql = "SELECT * FROM policy_popular_data WHERE popular_qa_id = ?";
            spiderQaRealationDataDO = jdbcTemplate.queryForObject(qaSql,
                    DataClassRowMapper.newInstance(SpiderQaRealationDataDO.class), sourceId);
            String policySql = "SELECT * FROM policy_data WHERE id = ?";
            SpiderPolicyDataDO spiderPolicyDataDO = jdbcTemplate.queryForObject(policySql, DataClassRowMapper.newInstance(SpiderPolicyDataDO.class), spiderQaRealationDataDO.getPolicyId());
            if (spiderPolicyDataDO != null && spiderPolicyDataDO.getDeleteMark()) {
                spiderQaRealationDataDO.setPolicyId("");
            }
        } catch (Exception e) {

        }

        SpiderPopularQaDataCombineDTO spiderPopularQaDataCombineDTO = new SpiderPopularQaDataCombineDTO()
                .setSpiderPolicyAttachmentDOList(spiderPolicyAttachmentDOList)
                .setSpiderPopularQaDataDO(spiderPopularQaDataDO)
                .setRealationDataDO(spiderQaRealationDataDO);

        // 设置爬虫url
        spiderPopularQaDataCombineDTO.setSpiderUrl(spiderPopularQaDataDO.getUrl());

        return spiderPopularQaDataCombineDTO;
    }

    @Override
    public FrequentlyAskedQuestionCombineDTO process(SpiderPopularQaDataCombineDTO sourceData) {
        // 爬虫库 ，热门问答对象
        SpiderPopularQaDataDO spiderPopularQaDataDO = sourceData.getSpiderPopularQaDataDO();

        LocalDateTime now = LocalDateTime.now();

        // 设置热门问答对象
        FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = new FrequentlyAskedQuestionDO()
                .setFrequentlyAskedQuestionStatus(FrequentlyAskedQuestionStatusEnum.REPTILE_SYNCHRONIZATION)
                .setCreateTime(now)
                .setUpdateTime(now)
                .setInputUserId(-1L)
                .setDeleted(false);

        // 标题
        frequentlyAskedQuestionDO.setTitle(spiderPopularQaDataDO.getTitle());

        // 来源
        frequentlyAskedQuestionDO.setDocSource(spiderPopularQaDataDO.getContentSource());

        // 发布日期
        frequentlyAskedQuestionDO.setReleaseDate(
                DateProcessors.releaseDate.apply(spiderPopularQaDataDO.getPublishDate()));

        // 网站名称
        frequentlyAskedQuestionDO.setAnswerOrganization(spiderPopularQaDataDO.getSiteName());

        // 正文
        String content = spiderPopularQaDataDO.getContent();
        Document document = HtmlProcessors.content.apply(content);

        // 附件
        List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList = sourceData.getSpiderPolicyAttachmentDOList();
        Pair<Document, List<AttachmentDO>> pair = attachmentProcessors.processContentAndAttachment(document,
                spiderPolicyAttachmentDOList, AttachmentType.FREQUENTLY_ASKED_QUESTION);

        // 设置正文
        frequentlyAskedQuestionDO.setContent(pair.getFirst().html());

        //设置关联关系
        frequentlyAskedQuestionDO.setPoliciesIds(sourceData.getRealationDataDO().getPolicyId());

        return new FrequentlyAskedQuestionCombineDTO().setFrequentlyAskedQuestionDO(frequentlyAskedQuestionDO)
                .setAttachmentDOList(pair.getSecond());


    }


    @Override
    public boolean isDocExist(Long docId) {
        return frequentlyAskedQuestionMapper.selectCount(
                Wrappers.lambdaQuery(FrequentlyAskedQuestionDO.class).eq(FrequentlyAskedQuestionDO::getId, docId)) > 0;
    }

    @Transactional
    @Override
    public Long saveProcessData(FrequentlyAskedQuestionCombineDTO processData) {
        FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = processData.getFrequentlyAskedQuestionDO();
        frequentlyAskedQuestionMapper.insert(frequentlyAskedQuestionDO);
        // 保存附件
        List<AttachmentDO> attachmentDOList = processData.getAttachmentDOList();
        attachmentService.saveSpiderAttachmentList(frequentlyAskedQuestionDO.getId().longValue(), attachmentDOList);
        return frequentlyAskedQuestionDO.getId().longValue();


    }

    @Transactional
    @Override
    public void updateProcessData(Long docId, FrequentlyAskedQuestionCombineDTO processData) {
        FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = processData.getFrequentlyAskedQuestionDO();
        frequentlyAskedQuestionDO.setId(docId);
        frequentlyAskedQuestionMapper.updateById(frequentlyAskedQuestionDO);
       // faqEventTrigger.saveEvent(docId);
        // 保存附件
        List<AttachmentDO> attachmentDOList = processData.getAttachmentDOList();
        attachmentService.saveSpiderAttachmentList(frequentlyAskedQuestionDO.getId(), attachmentDOList);
    }


}
