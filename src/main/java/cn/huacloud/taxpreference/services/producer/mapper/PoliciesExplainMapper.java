package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 政策解读
 *
 * @author wuxin
 */
@Repository
public interface PoliciesExplainMapper extends BaseMapper<PoliciesExplainDO> {

}
