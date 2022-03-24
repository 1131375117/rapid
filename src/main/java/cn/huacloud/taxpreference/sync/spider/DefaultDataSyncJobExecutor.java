package cn.huacloud.taxpreference.sync.spider;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.enums.JobType;
import cn.huacloud.taxpreference.common.enums.sync.SyncStatus;
import cn.huacloud.taxpreference.common.utils.CustomStringUtil;
import cn.huacloud.taxpreference.services.sync.entity.dos.SpiderDataSyncDO;
import cn.huacloud.taxpreference.services.sync.mapper.SpiderDataSyncMapper;
import cn.huacloud.taxpreference.sync.spider.entity.dtos.DataSyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 默认数据数据同步任务执行器
 *
 * @author wangkh
 */
@Slf4j
public class DefaultDataSyncJobExecutor {

    /**
     * 历史记录最大容量
     */
    private static final int MAX_HISTORY_SIZE = 10;

    /**
     * 爬虫库数据查询
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * 爬虫数据同步记录数据操作
     */
    private SpiderDataSyncMapper spiderDataSyncMapper;

    public DefaultDataSyncJobExecutor(JdbcTemplate jdbcTemplate, SpiderDataSyncMapper spiderDataSyncMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.spiderDataSyncMapper = spiderDataSyncMapper;
    }

    /**
     * 执行任务
     *
     * @param dataSyncJob 数据同步任务
     * @param jobParam    任务参数
     */
    public void execute(DataSyncJob<?, ?> dataSyncJob, DataSyncJobParam jobParam) {

        // 获取数据同步范围
        String syncIdsQuerySql = dataSyncJob.getSyncIdsQuerySql();

        List<String> spiderDataIds = jdbcTemplate.query(syncIdsQuerySql, SingleColumnRowMapper.newInstance(String.class), jobParam.getFrom(), jobParam.getTo());

        for (String spiderDataId : spiderDataIds) {
            SpiderDataSyncDO spiderDataSyncDO = null;
            try {
                // 前置处理
                spiderDataSyncDO = preHandle(dataSyncJob, spiderDataId, JobType.INSERT);
                // 跳过已经完成的任务
                if (spiderDataSyncDO.getSyncStatus() == SyncStatus.COMPLETED) {
                    continue;
                }
                // 执行同步
                DataSyncResult dataSyncResult = dataSyncJob.doSync(spiderDataSyncDO, jdbcTemplate, JobType.INSERT);
                // 后置处理
                afterHandle(spiderDataSyncDO, dataSyncResult);
            } catch (Exception e) {
                log.error("数据同步出错", e);
                if (spiderDataSyncDO != null) {
                    spiderDataSyncDO.setSyncStatus(SyncStatus.FAILED);
                    spiderDataSyncMapper.updateById(spiderDataSyncDO);
                }
            }
        }
    }


    /**
     * 执行更新任务
     *
     * @param dataSyncJob 数据同步任务
     * @param jobParam    任务参数
     */
    public void executeUpdate(DataSyncJob<?, ?> dataSyncJob, DataSyncJobParam jobParam) {

        // 获取数据同步范围
        String syncIdsQuerySql = dataSyncJob.getSyncIdsQuerySql();

        List<String> spiderDataIds = jdbcTemplate.query(syncIdsQuerySql, SingleColumnRowMapper.newInstance(String.class), jobParam.getFrom(), jobParam.getTo());

        for (String spiderDataId : spiderDataIds) {
            SpiderDataSyncDO spiderDataSyncDO = null;
            try {
                // 前置处理
                spiderDataSyncDO = preHandle(dataSyncJob, spiderDataId, JobType.UPDATE);

                // 执行同步
                DataSyncResult dataSyncResult = dataSyncJob.doSync(spiderDataSyncDO, jdbcTemplate, JobType.UPDATE);

                // 后置处理
                afterHandle(spiderDataSyncDO, dataSyncResult);
            } catch (Exception e) {
                log.error("数据同步出错", e);
                if (spiderDataSyncDO != null) {
                    spiderDataSyncDO.setSyncStatus(SyncStatus.FAILED);
                    spiderDataSyncMapper.updateById(spiderDataSyncDO);
                }
            }
        }
    }

    /**
     * 前置处理
     * 查询或创建数据同步记录并判断是否需要同步
     *
     * @param dataSyncJob  数据同步任务
     * @param spiderDataId 爬虫数据ID
     * @return 爬虫数据记录
     */
    private SpiderDataSyncDO preHandle(DataSyncJob<?, ?> dataSyncJob, String spiderDataId, JobType jobType) {
        DocType docType = dataSyncJob.getDocType();
        SpiderDataSyncDO spiderDataSyncDO = spiderDataSyncMapper.getSpiderDataSyncDO(docType, spiderDataId);
        LocalDateTime now = LocalDateTime.now();
        if (spiderDataSyncDO == null) {
            if (JobType.INSERT.equals(jobType)) {
                // 保存同步记录
                spiderDataSyncDO = new SpiderDataSyncDO()
                        .setDocType(docType)
                        .setSpiderDataId(spiderDataId)
                        .setCreateTime(now)
                        .setUpdateTime(now)
                        .setSyncStatus(SyncStatus.STARTING);
                spiderDataSyncMapper.insert(spiderDataSyncDO);
            }

        } else {
            // 跳过不需要重新同步的数据
            if (spiderDataSyncDO.getDocId() == null || dataSyncJob.needReSync(spiderDataSyncDO.getDocId())) {
                spiderDataSyncDO.setSyncStatus(SyncStatus.STARTING)
                        .setUpdateTime(now);
                setSyncHistory(spiderDataSyncDO);
                spiderDataSyncMapper.updateById(spiderDataSyncDO);
            }
            //更新
            if (JobType.UPDATE.equals(jobType)) {
                spiderDataSyncDO.setSyncStatus(SyncStatus.STARTING)
                        .setUpdateTime(now);
                setSyncHistory(spiderDataSyncDO);
                spiderDataSyncMapper.updateById(spiderDataSyncDO);
            }
        }
        return spiderDataSyncDO;
    }

    /**
     * 后置处理
     *
     * @param spiderDataSyncDO 数据同步记录
     * @param dataSyncResult   数据同步结果
     */
    private void afterHandle(SpiderDataSyncDO spiderDataSyncDO, DataSyncResult dataSyncResult) {
        spiderDataSyncDO.setDocId(dataSyncResult.getDocId())
                .setSpiderUrl(dataSyncResult.getSpiderUrl())
                .setSyncStatus(SyncStatus.COMPLETED)
                .setUpdateTime(LocalDateTime.now());
        setSyncHistory(spiderDataSyncDO);
        // 设置数据同步历史
        spiderDataSyncMapper.updateById(spiderDataSyncDO);
    }

    private DateTimeFormatter historyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 设置数据同步历史
     *
     * @param spiderDataSyncDO 数据同步记录
     */
    private void setSyncHistory(SpiderDataSyncDO spiderDataSyncDO) {
        // 字符串转list
        List<String> historyList = CustomStringUtil.spiltStringToList(spiderDataSyncDO.getSyncHistory());

        // 如果历史记录大于等于10次则移除最早一次
        if (historyList.size() >= MAX_HISTORY_SIZE - 1) {
            historyList.remove(0);
        }

        historyList.add(LocalDateTime.now().format(historyFormatter));
        String join = String.join(",", historyList);
        spiderDataSyncDO.setSyncHistory(join);
    }
}
