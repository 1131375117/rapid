package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;

/**
 * @author fuhua
 */
public interface ProcessService {
    /**
     * 流程新增
     * @param taxPreferenceId
     * @param currentUser
     * @return resultVO
     */
    ResultVO<Void> insertProcessService(Long taxPreferenceId, LoginUserVO currentUser);
}
