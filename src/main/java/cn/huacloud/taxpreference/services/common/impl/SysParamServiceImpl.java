package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.common.mapper.SysParamMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class SysParamServiceImpl implements SysParamService {

    private final SysParamMapper sysParamMapper;

    @Override
    public SysParamDO selectByParamKey(String paramKey) {
        LambdaQueryWrapper<SysParamDO> queryWrapper = Wrappers.lambdaQuery(SysParamDO.class)
                .eq(SysParamDO::getParamKey, paramKey);
        SysParamDO sysParamDO = sysParamMapper.selectOne(queryWrapper);
        return sysParamDO;
    }

    @Override
    public <T> T getObjectParamByTypes(List<String> sysParamTypes, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> Map<String, T> getMapParamByTypes(Class<T> clazz, String... args) {
        return null;
    }
}
