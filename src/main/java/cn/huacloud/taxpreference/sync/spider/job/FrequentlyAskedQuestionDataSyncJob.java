package cn.huacloud.taxpreference.sync.spider.job;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.enums.FrequentlyAskedQuestionStatusEnum;
import cn.huacloud.taxpreference.services.producer.mapper.FrequentlyAskedQuestionMapper;
import cn.huacloud.taxpreference.sync.spider.DataSyncJob;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPopularQaDataDO;
import cn.huacloud.taxpreference.sync.spider.processor.DateProcessors;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zhaoqiankun
 * @date 2021/12/29
 *         热门回答数据同步作业
 */
@RequiredArgsConstructor
@Component
public class FrequentlyAskedQuestionDataSyncJob implements
        DataSyncJob<SpiderPopularQaDataDO, FrequentlyAskedQuestionDO> {


    private final FrequentlyAskedQuestionMapper frequentlyAskedQuestionMapper;

    private final AttachmentService attachmentService;


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
    public SpiderPopularQaDataDO getSourceData(String sourceId, JdbcTemplate jdbcTemplate) {
        String popularQaSql = "SELECT * FROM popular_qa_data WHERE id = ?";
        SpiderPopularQaDataDO spiderPolicyDataDO = jdbcTemplate.queryForObject(popularQaSql,
                DataClassRowMapper.newInstance(SpiderPopularQaDataDO.class), sourceId);
        return spiderPolicyDataDO;
    }

    @Override
    public FrequentlyAskedQuestionDO process(SpiderPopularQaDataDO sourceData) {
        // 爬虫库 ，热门问答对象
        SpiderPopularQaDataDO spiderPopularQaDataDO = sourceData;

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

        // 关联附件信息
        //        attachmentService.setAttachmentDocId(
        //                frequentlyAskedQuestionDO.getId(),
        //                AttachmentType.POLICIES,
        //                frequentlyAskedQuestionDO.getContent());

        // 网站名称
        frequentlyAskedQuestionDO.setAnswerOrganization(spiderPopularQaDataDO.getSiteName());

        // 正文
        // TODO
        frequentlyAskedQuestionDO.setContent(spiderPopularQaDataDO.getContent());

        return frequentlyAskedQuestionDO;
    }

    @Override
    public boolean isDocExist(Long docId) {
        return frequentlyAskedQuestionMapper.selectCount(
                Wrappers.lambdaQuery(FrequentlyAskedQuestionDO.class).eq(FrequentlyAskedQuestionDO::getId, docId)) > 0;
    }

    @Override
    public Long saveProcessData(FrequentlyAskedQuestionDO processData) {
        FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = processData;
        frequentlyAskedQuestionMapper.insert(frequentlyAskedQuestionDO);
        return frequentlyAskedQuestionDO.getId();
    }

    @Override
    public void updateProcessData(Long docId, FrequentlyAskedQuestionDO processData) {
        FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = processData;
        frequentlyAskedQuestionDO.setId(docId);
        frequentlyAskedQuestionMapper.updateById(frequentlyAskedQuestionDO);
    }


}
