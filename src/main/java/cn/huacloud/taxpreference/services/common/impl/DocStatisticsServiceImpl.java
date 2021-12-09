package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
import cn.huacloud.taxpreference.services.common.mapper.DocStatisticsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private  DocStatisticsService docStatisticsService;

    @Autowired
    public void setDocStatisticsService(DocStatisticsService docStatisticsService) {
        this.docStatisticsService = docStatisticsService;
    }

    @Override
    public void insertDocStatisticsService(DocStatisticsDO docStatisticsDO) {
        docStatisticsMapper.insert(docStatisticsDO);
    }

    @Override
    public DocStatisticsDO selectOne(DocStatisticsDO docStatisticsDO) {
        LambdaQueryWrapper<DocStatisticsDO> queryWrapper = Wrappers.lambdaQuery(DocStatisticsDO.class)
                .eq(DocStatisticsDO::getDocType, docStatisticsDO.getDocType())
                .eq(DocStatisticsDO::getDocId, docStatisticsDO.getDocId());
        return docStatisticsMapper.selectOne(queryWrapper);
    }

    @Override
    public Long selectCount(DocStatisticsDO docStatisticsDO) {
        LambdaQueryWrapper<DocStatisticsDO> queryWrapper = Wrappers.lambdaQuery(DocStatisticsDO.class)
                .eq(DocStatisticsDO::getDocType, docStatisticsDO.getDocType())
                .eq(DocStatisticsDO::getDocId, docStatisticsDO.getDocId());
        return docStatisticsMapper.selectCount(queryWrapper);
    }

    @Override
    public void updateDocStatisticsService(DocStatisticsDO docStatisticsDO) {
        docStatisticsMapper.update(docStatisticsDO);
    }

    @Override
    public void saveOrUpdateDocStatisticsService(DocStatisticsDO docStatisticsDO) {
        if (exists(docStatisticsDO)) {
            docStatisticsService.insertDocStatisticsService(docStatisticsDO);
        } else {
            docStatisticsService.updateDocStatisticsService(docStatisticsDO);
        }
    }

    private boolean exists(DocStatisticsDO docStatisticsDO) {
        return docStatisticsService.selectCount(docStatisticsDO) == 0;
    }
}
