package cn.huacloud.taxpreference.sync.spider;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.enums.sync.SyncStatus;
import cn.huacloud.taxpreference.common.utils.CustomStringUtil;
import cn.huacloud.taxpreference.services.sync.entity.dos.SpiderDataSyncDO;
import cn.huacloud.taxpreference.services.sync.mapper.SpiderDataSyncMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangkh
 */
@Slf4j
public class DefaultDataSyncJobExecutor {

    private static final int MAX_HISTORY_SIZE = 10;

    private JdbcTemplate jdbcTemplate;

    private SpiderDataSyncMapper spiderDataSyncMapper;

    public DefaultDataSyncJobExecutor(JdbcTemplate jdbcTemplate, SpiderDataSyncMapper spiderDataSyncMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.spiderDataSyncMapper = spiderDataSyncMapper;
    }

    public void execute(DataSyncJob<?, ?> dataSyncJob, DataSyncJobParam jobParam) {

        // 获取数据保存范围
        String syncIdsQuerySql = dataSyncJob.getSyncIdsQuerySql();

        List<String> spiderDataIds = jdbcTemplate.query(syncIdsQuerySql, SingleColumnRowMapper.newInstance(String.class), jobParam.getFrom(), jobParam.getTo());

        for (String spiderDataId : spiderDataIds) {
            SpiderDataSyncDO spiderDataSyncDO = null;
            try {
                // 前置处理
                spiderDataSyncDO = preHandle(dataSyncJob, spiderDataId);
                // 执行同步
                Long docId = dataSyncJob.doSync(spiderDataId);
                // 后置处理
                afterHandle(spiderDataSyncDO, docId);
            } catch (Exception e) {
                log.error("数据同步出错", e);
                if (spiderDataSyncDO != null) {
                    spiderDataSyncDO.setSyncStatus(SyncStatus.FAILED);
                    spiderDataSyncMapper.updateById(spiderDataSyncDO);
                }
            }
        }
    }

    private SpiderDataSyncDO preHandle(DataSyncJob dataSyncJob, String spiderDataId) {
        DocType docType = dataSyncJob.getDocType();
        SpiderDataSyncDO spiderDataSyncDO = spiderDataSyncMapper.getSpiderDataSyncDO(docType, spiderDataId);
        LocalDateTime now = LocalDateTime.now();
        if (spiderDataSyncDO == null) {
            // 保存同步记录
            spiderDataSyncDO = new SpiderDataSyncDO()
                    .setDocType(docType)
                    .setSpiderDataId(spiderDataId)
                    .setCreateTime(now)
                    .setUpdateTime(now)
                    .setSyncStatus(SyncStatus.STARTING);
            spiderDataSyncMapper.insert(spiderDataSyncDO);
        } else {
            // 跳过不需要重新同步的数据
            if (spiderDataSyncDO.getDocId() == null || dataSyncJob.needReSync(spiderDataSyncDO.getDocId())) {
                spiderDataSyncDO.setSyncStatus(SyncStatus.STARTING)
                        .setUpdateTime(now);
                setSyncHistory(spiderDataSyncDO);
                spiderDataSyncMapper.updateById(spiderDataSyncDO);
            }
        }
        return spiderDataSyncDO;
    }

    private void afterHandle(SpiderDataSyncDO spiderDataSyncDO, Long docId) {

    }

    private void setSyncHistory(SpiderDataSyncDO spiderDataSyncDO) {
        List<String> historyList = CustomStringUtil.spiltStringToList(spiderDataSyncDO.getSyncHistory());

        if (historyList.size() >= MAX_HISTORY_SIZE - 1) {
            historyList.remove(0);
        }

        historyList.add(LocalDateTime.now().toString());
        String join = String.join(",", historyList);
        spiderDataSyncDO.setSyncHistory(join);
    }
}
