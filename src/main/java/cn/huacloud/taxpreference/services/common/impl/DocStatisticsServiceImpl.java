package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
import cn.huacloud.taxpreference.services.common.entity.dtos.DocStatisticsPlus;
import cn.huacloud.taxpreference.services.common.mapper.DocStatisticsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 统计
 *
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class DocStatisticsServiceImpl implements DocStatisticsService {
    private final DocStatisticsMapper docStatisticsMapper;

    @Override
    public DocStatisticsDO selectDocStatistics(Long docId, DocType docType) {
        LambdaQueryWrapper<DocStatisticsDO> queryWrapper = Wrappers.lambdaQuery(DocStatisticsDO.class)
                .eq(DocStatisticsDO::getDocType, docType)
                .eq(DocStatisticsDO::getDocId, docId);
        return docStatisticsMapper.selectOne(queryWrapper);
    }


    @Override
    public void saveOrUpdateDocStatisticsService(DocStatisticsPlus docStatisticsPlus) {
        DocStatisticsDO docStatisticsDO = new DocStatisticsDO();
        docStatisticsDO.setDocType(docStatisticsPlus.getDocType());
        docStatisticsDO.setCollections(docStatisticsPlus.getCollectionsPlus());
        docStatisticsDO.setViews(docStatisticsPlus.getViewsPlus());
        docStatisticsDO.setDocId(docStatisticsPlus.getDocId());
        if (exists(docStatisticsDO)) {
            docStatisticsDO.setViews(docStatisticsDO.getViews()==null?0L:docStatisticsDO.getViews());
            docStatisticsDO.setCollections(docStatisticsDO.getCollections()==null?0L:docStatisticsDO.getCollections());
            insertDocStatisticsService(docStatisticsDO);
        } else {
            updateDocStatisticsService(docStatisticsDO);
        }
    }

    private boolean exists(DocStatisticsDO docStatisticsDO) {
        return selectCount(docStatisticsDO) == 0;
    }

    private void updateDocStatisticsService(DocStatisticsDO docStatisticsDO) {
        docStatisticsMapper.update(docStatisticsDO);
    }

    private void insertDocStatisticsService(DocStatisticsDO docStatisticsDO) {
        docStatisticsMapper.insert(docStatisticsDO);
    }

    private Long selectCount(DocStatisticsDO docStatisticsDO) {
        LambdaQueryWrapper<DocStatisticsDO> queryWrapper = Wrappers.lambdaQuery(DocStatisticsDO.class)
                .eq(DocStatisticsDO::getDocType, docStatisticsDO.getDocType())
                .eq(DocStatisticsDO::getDocId, docStatisticsDO.getDocId());
        return docStatisticsMapper.selectCount(queryWrapper);
    }
}
