package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.services.common.entity.dos.ApiUserStatisticsDO;

public interface MonitorService {
    /**
     * 新增
     *
     * @param apiUserStatisticsDO
     */
    void insert(ApiUserStatisticsDO apiUserStatisticsDO);

    /**
     * 查询是否存在
     *
     * @param apiUserStatisticsDO
     * @return
     */
    Long queryCount(ApiUserStatisticsDO apiUserStatisticsDO);

    /**
     * 查询一条记录
     *
     * @param apiUserStatisticsDO
     * @return
     */
    ApiUserStatisticsDO queryOne(ApiUserStatisticsDO apiUserStatisticsDO);

    /**
     * 更新记录
     *
     * @param userStatisticsDO
     */
    void update(ApiUserStatisticsDO userStatisticsDO);
}
