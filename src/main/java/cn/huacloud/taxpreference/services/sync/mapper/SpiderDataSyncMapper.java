package cn.huacloud.taxpreference.services.sync.mapper;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.sync.entity.dos.SpiderDataSyncDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

/**
 * 爬虫数据同步记录数据操作
 * @author wangkh
 */
@Repository
public interface SpiderDataSyncMapper extends BaseMapper<SpiderDataSyncDO> {

    default SpiderDataSyncDO getSpiderDataSyncDO(DocType docType, String spiderDataId) {
        LambdaQueryWrapper<SpiderDataSyncDO> queryWrapper = Wrappers.lambdaQuery(SpiderDataSyncDO.class)
                .eq(SpiderDataSyncDO::getDocType, docType)
                .eq(SpiderDataSyncDO::getSpiderDataId, spiderDataId);
        return selectOne(queryWrapper);
    }
}
