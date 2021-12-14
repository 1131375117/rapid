package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
import cn.huacloud.taxpreference.services.common.entity.dtos.DocStatisticsPlus;

/**
 * 统计服务
 *
 * @author hua-cloud
 */
public interface DocStatisticsService {


    /**
     * 查询
     * @param docId
     * @param doctype
     * @return
     */
    DocStatisticsDO selectDocStatistics(Long docId , DocType doctype);

    /**
     * 修改或保存统计
     * @param docStatisticsPlus
     */
    void saveOrUpdateDocStatisticsService(DocStatisticsPlus docStatisticsPlus);
}
