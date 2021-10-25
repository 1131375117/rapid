package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.process.ProcessStatus;
import cn.huacloud.taxpreference.common.enums.taxpreference.TaxPreferenceStatus;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.ProcessService;
import cn.huacloud.taxpreference.services.producer.entity.dos.ProcessDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessListDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessSubmitDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessListVO;
import cn.huacloud.taxpreference.services.producer.mapper.ProcessServiceMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferenceMapper;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TaxPreferenceMapper taxPreferenceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> insertProcessService(Long[] taxPreferenceIds, LoginUserVO currentUser) {
        for (Long taxPreferenceId : taxPreferenceIds) {
            //新增先查询是否存在并更新流程状态
            processServiceMapper.updateByTaxPreferenceId(taxPreferenceId);
            ProcessDO processDO = new ProcessDO();
            processDO.setTaxPreferenceId(taxPreferenceId);
            processDO.setCreateTime(LocalDateTime.now());
            processDO.setLatestProcess(true);
            processDO.setCreatorId(currentUser.getId());
            processDO.setCreatorName(currentUser.getUsername());
            processDO.setProcessStatus(ProcessStatus.NOT_APPROVED.getValue());
            processServiceMapper.insert(processDO);
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO<PageVO<ProcessListVO>> queryProcessList(ProcessListDTO processListDTO) {
        processListDTO.paramReasonable();
        Page<ProcessListVO> page = new Page<>(processListDTO.getPageNum(), processListDTO.getPageSize());
        IPage<ProcessListVO> iPage = processServiceMapper.queryProcessList(page, processListDTO);
        PageVO<ProcessListVO> pageVO = PageVO.createPageVO(iPage, iPage.getRecords());
        return ResultVO.ok(pageVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> submitProcess(ProcessSubmitDTO processSubmitDTO, LoginUserVO currentUser) {
        ProcessDO processDO = getProcessDO(processSubmitDTO, currentUser);
        processServiceMapper.updateById(processDO);
        processDO = processServiceMapper.selectById(processDO.getId());
        TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(processDO);
        taxPreferenceMapper.updateById(taxPreferenceDO);
        return ResultVO.ok();
    }

    /**
     * 封装流程对象
     * */
    private ProcessDO getProcessDO(ProcessSubmitDTO processSubmitDTO, LoginUserVO currentUser) {
        ProcessDO processDO = new ProcessDO();
        BeanUtils.copyProperties(processSubmitDTO,processDO);
        processDO.setApproverId(currentUser.getId());
        processDO.setApproverName(currentUser.getUsername());
        processDO.setCreateTime(LocalDateTime.now());
        if(ProcessStatus.NOT_APPROVED.name.equals(processDO.getProcessStatus())){
            processDO.setProcessStatus(TaxPreferenceStatus.RELEASED.getValue());
        }else{
            processDO.setProcessStatus(TaxPreferenceStatus.UNRELEASED.getValue());
        }

        return processDO;
    }

    /**
     * 封装税收优惠
     * */
    private TaxPreferenceDO getTaxPreferenceDO(ProcessDO processDO) {
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        taxPreferenceDO.setId(processDO.getTaxPreferenceId());
        taxPreferenceDO.setTaxPreferenceStatus(processDO.getProcessStatus());
        return taxPreferenceDO;
    }
}
