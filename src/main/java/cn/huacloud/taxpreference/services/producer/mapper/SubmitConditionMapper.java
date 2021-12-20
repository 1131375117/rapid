package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.SubmitConditionDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 申报条件
 *
 * @author fuhua
 */
@Repository
public interface SubmitConditionMapper extends BaseMapper<SubmitConditionDO> {

    /**
     *
     * @param taxPreferenceId
     * @return
     */
    default List<SubmitConditionDO> getSubmitConditions(Long taxPreferenceId) {
        LambdaQueryWrapper<SubmitConditionDO> queryWrapper = Wrappers.lambdaQuery(SubmitConditionDO.class)
                .eq(SubmitConditionDO::getTaxPreferenceId, taxPreferenceId);
        return selectList(queryWrapper);
    }

    String selectByTaxPreferenceId(@Param("id") Long id,@Param("taxpayerCreditRatings") List<String> taxpayerCreditRatings);
}
