package cn.huacloud.taxpreference.sync.spider;

import cn.huacloud.taxpreference.common.enums.DocType;

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
     * @param sourceId 数据同步id
     * @return docId
     */
    default Long doSync(String sourceId) {
        T sourceData = getSourceData(sourceId);
        R processData = process(sourceData);
        return saveProcessData(processData);
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
    T getSourceData(String sourceId);

    /**
     * 处理原始数据，生成处理数据
     * @param sourceData 原始数据
     * @return 处理好的数据
     */
    R process(T sourceData);

    /**
     * 保存处理好的数据
     * @param processData 处理好的数据
     * @return 数据记录主键ID
     */
    Long saveProcessData(R processData);

}
