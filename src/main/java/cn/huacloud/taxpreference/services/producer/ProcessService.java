package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessListDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessSubmitDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessInfoVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessListVO;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;

import java.util.List;

/**
 * @author fuhua
 */
public interface ProcessService {
    /**
     * 流程新增
     *
     * @param taxPreferenceIds
     * @param currentUser
     * @return resultVO
     */
    ResultVO<Void> insertProcessService(Long[] taxPreferenceIds, LoginUserVO currentUser);

    /**
     * 查询流程列表
     *
     * @param processListDTO
     * @param userId
     * @return ResultVO
     */
    ResultVO<PageVO<ProcessListVO>> queryProcessList(ProcessListDTO processListDTO, Long userId);

    /**
     * 审核提交
     *
     * @param taxPreferenceId
     * @param currentUser
     * @return Void
     */
    ResultVO<Void> submitProcess(ProcessSubmitDTO taxPreferenceId, LoginUserVO currentUser);

    /**
     * 税收流程审批信息
     *
     * @param id
     * @return
     */
    ResultVO<List<ProcessInfoVO>> queryProcessInfo(Long id);
}
