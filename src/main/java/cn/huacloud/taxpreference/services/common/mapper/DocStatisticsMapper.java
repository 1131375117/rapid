package cn.huacloud.taxpreference.services.common.mapper;

import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author fuhua
 **/
@Repository
public interface DocStatisticsMapper extends BaseMapper<DocStatisticsDO> {
    /**
     * 修改统计
     * @param docStatisticsDO
     */
    void update(DocStatisticsDO docStatisticsDO);
}
