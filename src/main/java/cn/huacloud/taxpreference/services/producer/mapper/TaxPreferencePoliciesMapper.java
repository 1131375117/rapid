package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferencePoliciesDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

/**
 * 政策法规关联
 *
 * @author fuhua
 */
@Repository
public interface TaxPreferencePoliciesMapper extends BaseMapper<TaxPreferencePoliciesDO> {
    /**
     * 新增政策法规关联表数据
     * @param preferencePoliciesDO
     */
    void insertTaxPreferencePolicies(TaxPreferencePoliciesDO preferencePoliciesDO);

    /**
     * 根据优惠税收id修改政策法规数据
     * @param preferencePoliciesDO
     */
    default void updateByTaxPreferenceId(TaxPreferencePoliciesDO preferencePoliciesDO) {

        LambdaUpdateWrapper<TaxPreferencePoliciesDO> updateWrapper = Wrappers.lambdaUpdate(TaxPreferencePoliciesDO.class)
                .eq(TaxPreferencePoliciesDO::getTaxPreferenceId, preferencePoliciesDO.getTaxPreferenceId())
                .set(TaxPreferencePoliciesDO::getPoliciesId, preferencePoliciesDO.getPoliciesId())
                .set(TaxPreferencePoliciesDO::getValidityEndDate, preferencePoliciesDO.getValidityEndDate());
        update(preferencePoliciesDO, updateWrapper);

    }
}
