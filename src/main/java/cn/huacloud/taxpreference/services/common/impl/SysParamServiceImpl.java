package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.common.mapper.SysParamMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
