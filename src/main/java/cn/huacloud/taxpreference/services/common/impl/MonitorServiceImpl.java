package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.services.common.MonitorService;
import cn.huacloud.taxpreference.services.common.entity.dos.ApiUserStatisticsDO;
import cn.huacloud.taxpreference.services.common.mapper.ApiUserStatisticsMapper;
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
    public void insert(ApiUserStatisticsDO apiUserStatisticsDO) {
        apiUserStatisticsMapper.insert(apiUserStatisticsDO);
    }

    @Override
    public Long queryCount(ApiUserStatisticsDO apiUserStatisticsDO) {
        LambdaQueryWrapper<ApiUserStatisticsDO> queryWrapper = Wrappers.lambdaQuery(ApiUserStatisticsDO.class)
                .eq(ApiUserStatisticsDO::getAkId, apiUserStatisticsDO.getAkId())
                .eq(ApiUserStatisticsDO::getRequestMethod, apiUserStatisticsDO.getRequestMethod())
                .eq(ApiUserStatisticsDO::getPath, apiUserStatisticsDO.getPath());
        Long count = apiUserStatisticsMapper.selectCount(queryWrapper);
        return count;
    }

    @Override
    public ApiUserStatisticsDO queryOne(ApiUserStatisticsDO apiUserStatisticsDO) {
        LambdaQueryWrapper<ApiUserStatisticsDO> queryWrapper = Wrappers.lambdaQuery(ApiUserStatisticsDO.class)
                .eq(ApiUserStatisticsDO::getAkId, apiUserStatisticsDO.getAkId())
                .eq(ApiUserStatisticsDO::getRequestMethod, apiUserStatisticsDO.getRequestMethod())
                .eq(ApiUserStatisticsDO::getPath, apiUserStatisticsDO.getPath());
        return apiUserStatisticsMapper.selectOne(queryWrapper);
    }

    @Override
    public void update(ApiUserStatisticsDO userStatisticsDO) {
        LambdaUpdateWrapper<ApiUserStatisticsDO> updateWrapper = Wrappers.lambdaUpdate(ApiUserStatisticsDO.class)
                .eq(ApiUserStatisticsDO::getAkId, userStatisticsDO.getAkId())
                .eq(ApiUserStatisticsDO::getRequestMethod, userStatisticsDO.getRequestMethod())
                .eq(ApiUserStatisticsDO::getPath, userStatisticsDO.getPath());
        apiUserStatisticsMapper.update(userStatisticsDO, updateWrapper);
    }
}
