package cn.huacloud.taxpreference.services.common.mapper;

import cn.huacloud.taxpreference.common.enums.SysCodeStatus;
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

    /**
     * 获取所有有效的系统码值
     * @return 有效的系统码值
     */
    default List<SysCodeDO> getAllValid() {
        LambdaQueryWrapper<SysCodeDO> queryWrapper = Wrappers.lambdaQuery(SysCodeDO.class)
                .eq(SysCodeDO::getCodeStatus, SysCodeStatus.VALID)
                .orderByAsc(SysCodeDO::getSort);
        return selectList(queryWrapper);
    }
}
