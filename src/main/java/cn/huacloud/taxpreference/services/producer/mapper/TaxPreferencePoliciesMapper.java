package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferencePoliciesDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 政策法规关联
 * @author fuhua
 */
@Repository
public interface TaxPreferencePoliciesMapper extends BaseMapper<TaxPreferencePoliciesDO> {

    void insertTaxPreferencePolicies(TaxPreferencePoliciesDO preferencePoliciesDO);
}
