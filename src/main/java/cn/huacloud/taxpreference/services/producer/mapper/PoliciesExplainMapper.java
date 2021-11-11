package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 政策解读
 *
 * @author wuxin
 */
@Repository
public interface PoliciesExplainMapper extends BaseMapper<PoliciesExplainDO> {
    /**
     * 查询该政策解读是否被关联了政策法规
     * @param
     * @return
     */
    List<PoliciesTitleVO> getRelatedPolicy();

}
