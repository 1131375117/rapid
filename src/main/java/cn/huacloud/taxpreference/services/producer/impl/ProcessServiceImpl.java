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
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessInfoVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessListVO;
import cn.huacloud.taxpreference.services.producer.mapper.ProcessServiceMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferenceMapper;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.huacloud.taxpreference.services.producer.impl.TaxPreferenceServiceImpl.TAX_PREFERENCE_ID;

/**
 * @description: 流程接口impl
 * @author: fuhua
 * @create: 2021-10-25 09:45
 **/
@Slf4j
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
    public ResultVO<PageVO<ProcessListVO>> queryProcessList(ProcessListDTO processListDTO, Long userId) {
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

    @Override
    public ResultVO<List<ProcessInfoVO>> queryProcessInfo(Long id) {
        log.info("查询条件:id-{}", id);
        List<ProcessInfoVO> processInfoVOList = new ArrayList<>();
        Map<String, Object> keymap = new HashMap<>(16);
        keymap.put(TAX_PREFERENCE_ID, id);
        List<ProcessDO> processDOList = processServiceMapper.selectByMap(keymap);
        log.info("查询结果:processDOList:{}", processDOList);
        processDOList.forEach(processDO -> {
            ProcessInfoVO processInfoVO = new ProcessInfoVO();
            BeanUtils.copyProperties(processDO, processInfoVO);
            processInfoVOList.add(processInfoVO);
        });
        log.info("返回结果:processInfoVOList:{}", processDOList);
        return ResultVO.ok(processInfoVOList);
    }

    /**
     * 封装流程对象
     */
    private ProcessDO getProcessDO(ProcessSubmitDTO processSubmitDTO, LoginUserVO currentUser) {
        ProcessDO processDO = new ProcessDO();
        BeanUtils.copyProperties(processSubmitDTO, processDO);
        processDO.setApproverId(currentUser.getId());
        processDO.setApproverName(currentUser.getUsername());
        processDO.setCreateTime(LocalDateTime.now());
        if (ProcessStatus.NOT_APPROVED.name.equals(processDO.getProcessStatus())) {
            processDO.setProcessStatus(ProcessStatus.RETURNED.getValue());
        } else if (ProcessStatus.APPROVED.name.equals(processDO.getProcessStatus())) {
            processDO.setProcessStatus(ProcessStatus.APPROVED.getValue());
            processDO.setApprovalTime(LocalDateTime.now());
        }

        return processDO;
    }

    /**
     * 封装税收优惠
     */
    private TaxPreferenceDO getTaxPreferenceDO(ProcessDO processDO) {
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        taxPreferenceDO.setId(processDO.getTaxPreferenceId());
        taxPreferenceDO.setTaxPreferenceStatus(processDO.getProcessStatus());
        if (ProcessStatus.APPROVED.getValue().equals(processDO.getProcessStatus())) {
            taxPreferenceDO.setTaxPreferenceStatus(TaxPreferenceStatus.RELEASED.getValue());
        } else {
            taxPreferenceDO.setTaxPreferenceStatus(TaxPreferenceStatus.UNRELEASED.getValue());
        }
        return taxPreferenceDO;
    }
}
