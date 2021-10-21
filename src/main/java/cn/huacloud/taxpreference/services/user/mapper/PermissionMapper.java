package cn.huacloud.taxpreference.services.user.mapper;

import cn.huacloud.taxpreference.services.user.entity.dos.PermissionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 权限数据操作
 * @author wangkh
 */
@Repository
public interface PermissionMapper extends BaseMapper<PermissionDO> {

}
