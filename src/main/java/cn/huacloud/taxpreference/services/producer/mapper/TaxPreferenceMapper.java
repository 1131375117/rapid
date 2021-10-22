package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryTaxPrefrencesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryTaxPrefrencesVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description: 税收优惠
 * @author: fuhua
 * @create: 2021-10-21 10:58
 **/
@Repository
public interface TaxPreferenceMapper  extends BaseMapper<TaxPreferenceDO> {

    int insertTaxPreference(TaxPreferenceDO taxPreferenceDO);


    IPage<QueryTaxPrefrencesVO> queryTaxPreferenceVOList(@Param("page") Page<QueryTaxPrefrencesVO> page,@Param("queryTaxPrefrencesDTO") QueryTaxPrefrencesDTO queryTaxPrefrencesDTO);
}
