package cn.huacloud.taxpreference.sync.spider.job;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesStatusEnum;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.huacloud.taxpreference.sync.spider.DataSyncJob;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.PoliciesCombineDTO;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.SpiderPolicyCombineDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 政策数据同步作业
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class PoliciesDataSyncJob implements DataSyncJob<SpiderPolicyCombineDTO, PoliciesCombineDTO> {

    private final PoliciesMapper policiesMapper;

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
    public SpiderPolicyCombineDTO getSourceData(String sourceId) {
        return null;
    }

    @Override
    public PoliciesCombineDTO process(SpiderPolicyCombineDTO sourceData) {
        return null;
    }

    @Override
    public Long saveProcessData(PoliciesCombineDTO processData) {
        return null;
    }
}
