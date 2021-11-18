package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesDigestSearchVO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferencePoliciesDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * 根据税收优惠ID获取政策摘要信息
     * @param taxPreferenceId 税收优惠ID
     * @return 政策法规摘要视图
     */
    List<PoliciesDigestSearchVO> getPoliciesDigestSearchVOList(@Param("taxPreferenceId") Long taxPreferenceId);
}
