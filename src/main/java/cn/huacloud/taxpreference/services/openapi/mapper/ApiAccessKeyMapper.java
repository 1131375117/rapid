package cn.huacloud.taxpreference.services.openapi.mapper;

import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiAccessKeyDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

/**
 * @author wangkh
 */
@Repository
public interface ApiAccessKeyMapper extends BaseMapper<ApiAccessKeyDO> {

    default ApiAccessKeyDO queryByAccessKeyIdAndAccessKeySecret(String accessKeyId, String accessKeySecret) {
        LambdaQueryWrapper<ApiAccessKeyDO> queryWrapper = Wrappers.lambdaQuery(ApiAccessKeyDO.class)
                .eq(ApiAccessKeyDO::getAccessKeyId, accessKeyId)
                .eq(ApiAccessKeyDO::getAccessKeySecret, accessKeySecret);
        return selectOne(queryWrapper);
    }
}
