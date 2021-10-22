package cn.huacloud.taxpreference.services.user.mapper;

import cn.huacloud.taxpreference.services.user.entity.dos.ProducerUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

/**
 * 生产者用户数据操作
 * @author wangkh
 */
@Repository
public interface ProducerUserMapper extends BaseMapper<ProducerUserDO> {
    /**
     * 根据用户ID查询后台用户信息
     * @param userId 用户ID
     * @return
     */
    default ProducerUserDO selectByUserId(Long userId) {
        LambdaQueryWrapper<ProducerUserDO> queryWrapper = Wrappers.lambdaQuery(ProducerUserDO.class)
                .eq(ProducerUserDO::getUserId, userId);
        return selectOne(queryWrapper);
    }
}
