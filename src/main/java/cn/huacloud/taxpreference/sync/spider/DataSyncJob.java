package cn.huacloud.taxpreference.sync.spider;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.sync.entity.dos.SpiderDataSyncDO;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据同步作业
 * @author wangkh
 */
public interface DataSyncJob<T, R> {
    /**
     * 作业排序
     * @return 排序值
     */
    int order();

    /**
     * 执行同步
     * @param spiderDataSyncDO 数据同步记录
     * @return docId
     */
    default Long doSync(SpiderDataSyncDO spiderDataSyncDO, JdbcTemplate jdbcTemplate) {
        // 获取原始数据
        T sourceData = getSourceData(spiderDataSyncDO.getSpiderDataId(), jdbcTemplate);
        // 数据处理
        R processData = process(sourceData);
        // 数据持久化
        Long docId = spiderDataSyncDO.getDocId();
        if (docId != null && isDocExist(docId)) {
            // 执行更新方法
            updateProcessData(docId, processData);
            return docId;
        } else {
            // 执行保存方法
            return saveProcessData(processData);
        }
    }

    /**
     * 获取文档类型
     * @return 文档类型
     */
    DocType getDocType();

    /**
     * 获取爬虫数据要同步的ID主键查询SQL
     * @return SQL语句
     */
    String getSyncIdsQuerySql();

    /**
     * 是否需要重新同步，经过数据加工的数据不需要再同步
     * @param docId 文档ID
     * @return 是否需要重新同步
     */
    boolean needReSync(Long docId);

    /**
     * 获取原始数据
     * @param sourceId 原始ID
     * @return 原始数据
     */
    T getSourceData(String sourceId, JdbcTemplate jdbcTemplate);

    /**
     * 处理原始数据，生成处理数据
     * @param sourceData 原始数据
     * @return 处理好的数据
     */
    R process(T sourceData);

    /**
     * 文档数据是否已经存在
     * @param docId 文档ID
     * @return 数据是否已经存在
     */
    boolean isDocExist(Long docId);

    /**
     * 保存处理好的数据
     *
     * @param processData 处理好的数据
     * @return 数据记录主键ID
     */
    Long saveProcessData(R processData);

    /**
     * 更新处理好的数据
     * @param docId 文档ID
     * @param processData 处理好的数据
     */
    void updateProcessData(Long docId, R processData);

}
