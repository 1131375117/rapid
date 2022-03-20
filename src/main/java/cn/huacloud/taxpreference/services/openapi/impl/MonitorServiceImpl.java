package cn.huacloud.taxpreference.services.openapi.impl;

import cn.huacloud.taxpreference.services.openapi.MonitorService;
import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiStatisticsDO;
import cn.huacloud.taxpreference.services.openapi.mapper.ApiUserStatisticsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService {

    private final ApiUserStatisticsMapper apiUserStatisticsMapper;

    @Override
    public void insert(ApiStatisticsDO apiStatisticsDO) {
        apiUserStatisticsMapper.insert(apiStatisticsDO);
    }

    @Override
    public Long queryCount(ApiStatisticsDO apiStatisticsDO) {
        LambdaQueryWrapper<ApiStatisticsDO> queryWrapper = Wrappers.lambdaQuery(ApiStatisticsDO.class)
                .eq(ApiStatisticsDO::getAkId, apiStatisticsDO.getAkId())
                .eq(ApiStatisticsDO::getRequestMethod, apiStatisticsDO.getRequestMethod())
                .eq(ApiStatisticsDO::getPath, apiStatisticsDO.getPath());
        Long count = apiUserStatisticsMapper.selectCount(queryWrapper);
        return count;
    }

    @Override
    public ApiStatisticsDO queryOne(ApiStatisticsDO apiStatisticsDO) {
        LambdaQueryWrapper<ApiStatisticsDO> queryWrapper = Wrappers.lambdaQuery(ApiStatisticsDO.class)
                .eq(ApiStatisticsDO::getAkId, apiStatisticsDO.getAkId())
                .eq(ApiStatisticsDO::getRequestMethod, apiStatisticsDO.getRequestMethod())
                .eq(ApiStatisticsDO::getPath, apiStatisticsDO.getPath());
        return apiUserStatisticsMapper.selectOne(queryWrapper);
    }

    @Override
    public void update(ApiStatisticsDO userStatisticsDO) {
        LambdaUpdateWrapper<ApiStatisticsDO> updateWrapper = Wrappers.lambdaUpdate(ApiStatisticsDO.class)
                .eq(ApiStatisticsDO::getAkId, userStatisticsDO.getAkId())
                .eq(ApiStatisticsDO::getRequestMethod, userStatisticsDO.getRequestMethod())
                .eq(ApiStatisticsDO::getPath, userStatisticsDO.getPath());
        apiUserStatisticsMapper.update(userStatisticsDO, updateWrapper);
    }
}
