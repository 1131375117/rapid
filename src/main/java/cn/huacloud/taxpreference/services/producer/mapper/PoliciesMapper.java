package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceCountVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 政策法规
 *
 * @author wuxin
 */
@Repository
public interface PoliciesMapper extends BaseMapper<PoliciesDO> {

    /**
     * 查询政策法规列表
     *
     * @param page
     * @param queryPoliciesDTO
     * @param sort
     * @return
     */
    IPage<PoliciesVO> queryPoliciesVOList(@Param("page") Page<PoliciesVO> page, @Param("queryPoliciesDTO") QueryPoliciesDTO queryPoliciesDTO, @Param("sort") String sort);

    /**
     * 根据政策法规id查询政策解读id
     *
     * @param policiesId
     * @return
     */
    List<Long> selectExplainId(Long policiesId);

    /**
     * 根据政策法规id查询热门问答id
     *
     * @param id
     * @return
     */
    List<FrequentlyAskedQuestionDO> selectFrequentlyAskedQuestionId(Long id);

    List<TaxPreferenceCountVO> selectTaxPreferenceId(Long id);

    /**
     * 根据政策法规id查询税收优惠id
     *
     * @param id
     * @return
     */

}
