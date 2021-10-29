package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

}
