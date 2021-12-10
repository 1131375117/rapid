package cn.huacloud.taxpreference.services.common.mapper;

import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * @author hua-cloud
 */
@Repository
public interface SysParamMapper extends BaseMapper<SysParamDO> {
    /**
     * 查询sysParamDOList
     * @param paramTypes
     * @return
     */
    default List<SysParamDO> getSysParamDOList(String... paramTypes) {
        LambdaQueryWrapper<SysParamDO> queryWrapper = Wrappers.lambdaQuery(SysParamDO.class);
        queryWrapper.eq(SysParamDO::getParamStatus, "VALID");
        queryWrapper.in(SysParamDO::getParamType,Arrays.asList(paramTypes));
        return selectList(queryWrapper);

    }
    /**
     * 查询sysParamDOList
     * @param paramTypes
     * @return
     */
    default List<SysParamDO> getSysParamDOList(List<String> paramTypes) {
        LambdaQueryWrapper<SysParamDO> queryWrapper = Wrappers.lambdaQuery(SysParamDO.class);
        queryWrapper.eq(SysParamDO::getParamStatus, "VALID");
        queryWrapper.in(SysParamDO::getParamType,paramTypes);
        return selectList(queryWrapper);

    }
}
