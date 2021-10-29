package cn.huacloud.taxpreference.services.common.mapper;

import cn.huacloud.taxpreference.services.common.entity.dos.AttachmentDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 附件数据操作
 * @author wangkh
 */
@Repository
public interface AttachmentMapper extends BaseMapper<AttachmentDO> {

}
