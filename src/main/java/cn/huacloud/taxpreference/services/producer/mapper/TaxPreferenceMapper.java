package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @description: 税收优惠
 * @author: fuhua
 * @create: 2021-10-21 10:58
 **/
@Repository
public interface TaxPreferenceMapper  extends BaseMapper<TaxPreferenceDO> {

    int insertTaxPreference(TaxPreferenceDO taxPreferenceDO);


}
