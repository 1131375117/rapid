package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 热点问答
 *
 * @author wuxin
 */
@Repository
public interface FrequentlyAskedQuestionMapper extends BaseMapper<FrequentlyAskedQuestionDO> {



	/**
	 * 热门问答分页列表查询
	 * @param page 查询对象
	 * @param sort 排序字段
	 * @param queryPoliciesExplainDTO 返回视图对象
	 * @return 返回值
	 */
	IPage<PoliciesExplainDetailVO> selectPageList(@Param("page") IPage<PoliciesExplainDetailVO> page, @Param("sort") String sort, @Param("queryPoliciesExplainDTO") QueryPoliciesExplainDTO queryPoliciesExplainDTO);
}
