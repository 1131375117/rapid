package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.enums.ProcessStatus;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.ProcessService;
import cn.huacloud.taxpreference.services.producer.entity.dos.ProcessDO;
import cn.huacloud.taxpreference.services.producer.mapper.ProcessServiceMapper;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @description: 流程接口impl
 * @author: fuhua
 * @create: 2021-10-25 09:45
 **/
@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {
    private final ProcessServiceMapper processServiceMapper;

    @Override
    public ResultVO<Void> insertProcessService(Long taxPreferenceId, LoginUserVO currentUser) {
        ProcessDO processDO = new ProcessDO();
        processDO.setTaxPreferenceId(taxPreferenceId);
        processDO.setCreateTime(LocalDateTime.now());
        processDO.setLatestProcess(true);
        processDO.setCreatorId(currentUser.getId());
        processDO.setCreatorName(currentUser.getUsername());
        processDO.setProcessStatus(ProcessStatus.NOT_APPROVED.getValue());
        processServiceMapper.insert(processDO);
        return ResultVO.ok();
    }
}
