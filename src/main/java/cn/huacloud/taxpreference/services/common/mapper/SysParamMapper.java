package cn.huacloud.taxpreference.services.common.mapper;

import cn.huacloud.taxpreference.common.enums.SysParamStatus;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hua-cloud
 */
@Repository
public interface SysParamMapper extends BaseMapper<SysParamDO> {
    /**
     * 根据系统参数类型获取系统参数集合
     * @param paramType 系统参数类型
     * @return 系统参数集合
     */
    default List<SysParamDO> getSysParamDOListByType(String paramType) {
        LambdaQueryWrapper<SysParamDO> queryWrapper = Wrappers.lambdaQuery(SysParamDO.class)
                .eq(SysParamDO::getParamType, paramType)
                .eq(SysParamDO::getParamStatus, SysParamStatus.VALID);
        return selectList(queryWrapper);
    }
}
