package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.SubmitConditionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryTaxPreferencesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryTaxPreferencesVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceCountVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: 税收优惠
 * @author: fuhua
 * @create: 2021-10-21 10:58
 **/
@Repository
public interface TaxPreferenceMapper  extends BaseMapper<TaxPreferenceDO> {
    /**
     * 税收优惠新增
     * @param taxPreferenceDO
     * @return int
     */
    int insertTaxPreference(TaxPreferenceDO taxPreferenceDO);

    /**
     * 税收优惠查询
     * @param page
     * @param queryTaxPreferencesDTO
     * @param sort
     * @param userId
     * @return queryTaxPreferencesVO
     */
    IPage<QueryTaxPreferencesVO> queryTaxPreferenceVOList(@Param("page") Page<QueryTaxPreferencesVO> page, @Param("queryTaxPreferencesDTO") QueryTaxPreferencesDTO queryTaxPreferencesDTO, @Param("sort") String sort,@Param("userId") Long userId);

    /**
     * 税收优惠删除
     * @param id
     */
    void updateDeletedById(Long id);

    /**
     * 查询税收优惠的标题
     * @param policiesId 政策法规id
     * @return
     */
    List<TaxPreferenceDO> selectByIdList(Long policiesId);

    /**
     * 根据政策法规id查询关联的税收优惠
     * @param policiesId 政策法规id
     * @return
     */
    List<TaxPreferenceCountVO> selectTaxPreferenceId(Long policiesId);

}
