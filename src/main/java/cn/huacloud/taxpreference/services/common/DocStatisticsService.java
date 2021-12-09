package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;

/**
 * 统计服务
 *
 * @author hua-cloud
 */
public interface DocStatisticsService {
    /**
     * 统计服务数据插入
     *
     * @param docStatisticsDO
     */
    void insertDocStatisticsService(DocStatisticsDO docStatisticsDO);

    /**
     * 查询
     *
     * @param docStatisticsDO
     */
    DocStatisticsDO selectOne(DocStatisticsDO docStatisticsDO);

    /**
     * 查询个数
     *
     * @param docStatisticsDO
     */
    Long selectCount(DocStatisticsDO docStatisticsDO);

    /**
     * 统计服务数据修改
     *
     * @param docStatisticsDO
     */
    void updateDocStatisticsService(DocStatisticsDO docStatisticsDO);

    /**
     * 统计服务数据修改或保存
     *
     * @param docStatisticsDO
     */
    void saveOrUpdateDocStatisticsService(DocStatisticsDO docStatisticsDO);
}
