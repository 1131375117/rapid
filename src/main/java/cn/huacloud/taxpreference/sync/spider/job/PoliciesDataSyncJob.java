package cn.huacloud.taxpreference.sync.spider.job;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeVO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import cn.huacloud.taxpreference.services.producer.entity.vos.DocCodeVO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.huacloud.taxpreference.services.sync.entity.dos.SpiderDataSyncDO;
import cn.huacloud.taxpreference.sync.spider.DataSyncJob;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyAttachmentDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyDataDO;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.PoliciesCombineDTO;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.SpiderPolicyCombineDTO;
import cn.huacloud.taxpreference.sync.spider.processor.DateProcessors;
import cn.huacloud.taxpreference.sync.spider.processor.DocCodeProcessors;
import cn.huacloud.taxpreference.sync.spider.processor.SysCodeProcessors;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 政策数据同步作业
 *
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class PoliciesDataSyncJob implements DataSyncJob<SpiderPolicyCombineDTO, PoliciesCombineDTO> {

    private final PoliciesMapper policiesMapper;

    private final SysCodeProcessors sysCodeProcessors;

    @Override
    public int order() {
        return 0;
    }

    @Override
    public DocType getDocType() {
        return DocType.POLICIES;
    }

    @Override
    public String getSyncIdsQuerySql() {
        return "SELECT id FROM policy_data WHERE spider_time BETWEEN ? AND ?";
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
        return new SpiderPolicyCombineDTO()
                .setSpiderPolicyDataDO(spiderPolicyDataDO)
                .setSpiderPolicyAttachmentDOList(spiderPolicyAttachmentDOList);
    }

    @Override
    public PoliciesCombineDTO process(SpiderPolicyCombineDTO sourceData) {
        SpiderPolicyDataDO policy = sourceData.getSpiderPolicyDataDO();

        LocalDateTime now = LocalDateTime.now();
        PoliciesDO policiesDO = new PoliciesDO()
                .setPoliciesStatus(PoliciesStatusEnum.REPTILE_SYNCHRONIZATION)
                .setCreateTime(now)
                .setUpdateTime(now)
                .setInputUserId(-1L)
                .setDeleted(false);

        // 标题
        policiesDO.setTitle(policy.getTitle());

        // 文号
        DocCodeVO docCodeVO = DocCodeProcessors.docCode.apply(policy.getDocumentNumber());
        CustomBeanUtil.copyProperties(docCodeVO, policiesDO);

        // 所属区域
        SysCodeVO area = sysCodeProcessors.area.apply(policy.getRegion());
        policiesDO.setAreaCode(area.getCodeValue());
        policiesDO.setAreaName(area.getCodeValue());

        // 来源
        policiesDO.setDocSource(policy.getContentSource());

        // 有效性
        policiesDO.setValidity(ValidityEnum.getByName(policy.getContentIsValid()));

        // 发布日期
        policiesDO.setReleaseDate(DateProcessors.releaseDate.apply(policy.getPublishDate()));

        // 正文
        // TODO
        policiesDO.setContent(policy.getContent());


        // 附件
        List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList = sourceData.getSpiderPolicyAttachmentDOList();

        return new PoliciesCombineDTO()
                .setPoliciesDO(policiesDO)
                .setAttachmentDOList(new ArrayList<>());
    }

    @Override
    public Long saveProcessData(SpiderDataSyncDO spiderDataSyncDO, PoliciesCombineDTO processData) {
        // TODO 不要模仿这里的保存逻辑，还需要修改
        Long docId = spiderDataSyncDO.getDocId();
        PoliciesDO policiesDO = processData.getPoliciesDO();
        if (docId != null) {
            Long count = policiesMapper.selectCount(Wrappers.lambdaQuery(PoliciesDO.class).eq(PoliciesDO::getId, docId));
            if (count > 0) {
                policiesDO.setId(docId);
                policiesMapper.updateById(policiesDO);
            } else {
                policiesMapper.insert(policiesDO);
            }
        } else {
            policiesMapper.insert(policiesDO);
        }
        return policiesDO.getId();
    }
}
