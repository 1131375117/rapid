package cn.huacloud.taxpreference.services.common.mapper;

import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.entity.dos.SysCodeDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统码值数据操作
 * @author wangkh
 */
@Repository
public interface SysCodeMapper extends BaseMapper<SysCodeDO> {

    default List<SysCodeDO> getSysCodeDOByType(SysCodeType sysCodeType) {
        LambdaQueryWrapper<SysCodeDO> queryWrapper = Wrappers.lambdaQuery(SysCodeDO.class)
                .eq(SysCodeDO::getCodeType, sysCodeType);
        return selectList(queryWrapper);
    }
}
