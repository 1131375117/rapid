package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 热点问答
 * @author wuxin
 */
@Repository
public interface FrequentlyAskedQuestionMapper extends BaseMapper<FrequentlyAskedQuestionDO> {
}
