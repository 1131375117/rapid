package cn.huacloud.taxpreference.services.openapi;

import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiStatisticsDO;

public interface MonitorService {
    /**
     * 新增
     *
     * @param apiStatisticsDO
     */
    void insert(ApiStatisticsDO apiStatisticsDO);

    /**
     * 查询是否存在
     *
     * @param apiStatisticsDO
     * @return
     */
    Long queryCount(ApiStatisticsDO apiStatisticsDO);

    /**
     * 查询一条记录
     *
     * @param apiStatisticsDO
     * @return
     */
    ApiStatisticsDO queryOne(ApiStatisticsDO apiStatisticsDO);

    /**
     * 更新记录
     *
     * @param userStatisticsDO
     */
    void update(ApiStatisticsDO userStatisticsDO);
}
