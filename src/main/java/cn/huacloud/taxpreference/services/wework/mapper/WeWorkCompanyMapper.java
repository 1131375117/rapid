package cn.huacloud.taxpreference.services.wework.mapper;

import cn.huacloud.taxpreference.services.wework.entity.dos.WeWorkCompanyDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

/**
 * @author wangkh
 */
@Repository
public interface WeWorkCompanyMapper extends BaseMapper<WeWorkCompanyDO> {

    default WeWorkCompanyDO findByCropIdAndAgentId(String cropId, String suiteId) {
        LambdaQueryWrapper<WeWorkCompanyDO> queryWrapper = Wrappers.lambdaQuery(WeWorkCompanyDO.class)
                .eq(WeWorkCompanyDO::getCorpId, cropId)
                .eq(WeWorkCompanyDO::getSuiteId, suiteId)
                .eq(WeWorkCompanyDO::getDeleted, false);

        return selectOne(queryWrapper);
    }
}
