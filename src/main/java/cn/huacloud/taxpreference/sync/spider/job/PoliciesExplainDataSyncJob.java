package cn.huacloud.taxpreference.sync.spider.job;

import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.common.entity.dos.AttachmentDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesExplainStatusEnum;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesExplainMapper;
import cn.huacloud.taxpreference.services.sync.entity.dos.SpiderDataSyncDO;
import cn.huacloud.taxpreference.services.sync.mapper.SpiderDataSyncMapper;
import cn.huacloud.taxpreference.sync.spider.DataSyncJob;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyAttachmentDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyExplainDataDO;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.PoliciesExplainCombineDTO;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.SpiderPolicyExplainCombineDTO;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * 政策解读数据同步作业
 *
 * @author wuxin
 */
@RequiredArgsConstructor
@Component
public class PoliciesExplainDataSyncJob implements DataSyncJob<SpiderPolicyExplainCombineDTO, PoliciesExplainCombineDTO> {

	private final PoliciesExplainMapper policiesExplainMapper;

	private final SpiderDataSyncMapper spiderDataSyncMapper;

	private final AttachmentProcessors attachmentProcessors;

	private final AttachmentService attachmentService;

	@Override
	public int order() {
		return 1;
	}

	@Override
	public DocType getDocType() {
		return DocType.POLICIES_EXPLAIN;
	}

	@Override
	public String getSyncIdsQuerySql() {
		return "SELECT id FROM policy_data WHERE spider_time BETWEEN ? AND ?  AND related_interpretation_title != '' AND related_interpretation_title IS NOT NULL";
	}

	@Override
	public boolean needReSync(Long docId) {
		PoliciesExplainDO policiesExplainDO = policiesExplainMapper.selectById(docId);
		return policiesExplainDO == null || policiesExplainDO.getPoliciesExplainStatus() != PoliciesExplainStatusEnum.PUBLISHED;
	}

	@Override
	public SpiderPolicyExplainCombineDTO getSourceData(String sourceId, JdbcTemplate jdbcTemplate) {
		String policyExplainSql = "SELECT * FROM policy_data WHERE id = ?";
		SpiderPolicyExplainDataDO spiderPolicyExplainDataDO = jdbcTemplate.queryForObject(policyExplainSql, DataClassRowMapper.newInstance(SpiderPolicyExplainDataDO.class), sourceId);
		String attachmentSql = "SELECT * FROM policy_attachment WHERE doc_id = ? AND attachment_type = '政策'";
		List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList = jdbcTemplate.query(attachmentSql, DataClassRowMapper.newInstance(SpiderPolicyAttachmentDO.class), sourceId);
		SpiderPolicyExplainCombineDTO spiderPolicyExplainCombineDTO = new SpiderPolicyExplainCombineDTO()
				.setSpiderPolicyExplainDataDO(spiderPolicyExplainDataDO)
				.setSpiderPolicyAttachmentDOList(spiderPolicyAttachmentDOList);
		spiderPolicyExplainCombineDTO.setSpiderUrl(spiderPolicyExplainDataDO.getRelatedInterpretationUrl());
		return spiderPolicyExplainCombineDTO;
	}

	@Override
	public PoliciesExplainCombineDTO process(SpiderPolicyExplainCombineDTO sourceData) {
		SpiderPolicyExplainDataDO policyExplain = sourceData.getSpiderPolicyExplainDataDO();
		SpiderDataSyncDO spiderDataSyncDO = spiderDataSyncMapper.getSpiderDataSyncDO(DocType.POLICIES, policyExplain.getId());
		LocalDateTime now = LocalDateTime.now();
		PoliciesExplainDO policiesExplainDO = new PoliciesExplainDO()
				.setPoliciesExplainStatus(PoliciesExplainStatusEnum.REPTILE_SYNCHRONIZATION)
				.setCreateTime(now)
				.setUpdateTime(now)
				.setInputUserId(-1L)
				.setDeleted(false)
				.setPoliciesId(spiderDataSyncDO.getDocId());

		//标题
		policiesExplainDO.setTitle(policyExplain.getRelatedInterpretationTitle());

		//来源
		policiesExplainDO.setDocSource(policyExplain.getRelatedInterpretationSource());

		// 发布日期
		policiesExplainDO.setReleaseDate(DateProcessors.releaseDate.apply(policyExplain.getRelatedInterpretationDate()));

		//正文
		String content = policyExplain.getRelatedInterpretationContent();
		Document document = HtmlProcessors.content.apply(content);


		List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList = sourceData.getSpiderPolicyAttachmentDOList();

		Pair<Document, List<AttachmentDO>> pair = attachmentProcessors.processContentAndAttachment(document, spiderPolicyAttachmentDOList, AttachmentType.POLICIES);

		policiesExplainDO.setContent(pair.getFirst().html());

		return new PoliciesExplainCombineDTO()
				.setPoliciesExplainDO(policiesExplainDO)
				.setAttachmentDOList(pair.getSecond());
	}

	@Override
	public boolean isDocExist(Long docId) {
		return policiesExplainMapper.selectCount(Wrappers.lambdaQuery(PoliciesExplainDO.class).eq(PoliciesExplainDO::getId, docId)) > 0;
	}

	@Override
	public Long saveProcessData(PoliciesExplainCombineDTO processData) {
		PoliciesExplainDO policiesExplainDO = processData.getPoliciesExplainDO();
		policiesExplainMapper.insert(policiesExplainDO);
		// 保存附件
		List<AttachmentDO> attachmentDOList = processData.getAttachmentDOList();
		attachmentService.saveSpiderAttachmentList(policiesExplainDO.getId(), attachmentDOList);
		return policiesExplainDO.getId();
	}

	@Override
	public void updateProcessData(Long docId, PoliciesExplainCombineDTO processData) {
		PoliciesExplainDO policiesExplainDO = processData.getPoliciesExplainDO();
		policiesExplainDO.setId(docId);
		policiesExplainMapper.updateById(policiesExplainDO);
		// 保存附件
		List<AttachmentDO> attachmentDOList = processData.getAttachmentDOList();
		attachmentService.saveSpiderAttachmentList(policiesExplainDO.getId(), attachmentDOList);

	}
}
