package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
        log.info("新增流程参数:taxPreferenceIds={},currentUser={}",taxPreferenceIds,currentUser);
        for (Long taxPreferenceId : taxPreferenceIds) {
            judgeProcessIng(taxPreferenceId);
            //新增先查询是否存在并更新流程状态
            processServiceMapper.updateByTaxPreferenceId(taxPreferenceId);
            ProcessDO processDO = new ProcessDO();
            processDO.setTaxPreferenceId(taxPreferenceId);
            processDO.setCreateTime(LocalDateTime.now());
            processDO.setLatestProcess(true);
            processDO.setCreatorId(currentUser.getId());
            processDO.setCreatorName(currentUser.getUsername());
            processDO.setProcessStatus(ProcessStatus.NOT_APPROVED);
            processServiceMapper.insert(processDO);
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO<PageVO<ProcessListVO>> queryProcessList(ProcessListDTO processListDTO, Long userId) {
        processListDTO.paramReasonable();
        log.info("查询条件:processListDTO={},userId={}",processListDTO,userId);
        Page<ProcessListVO> page = new Page<>(processListDTO.getPageNum(), processListDTO.getPageSize());
        IPage<ProcessListVO> iPage = processServiceMapper.queryProcessList(page, processListDTO);
        log.info("iPage-查询结果{}",iPage);
        PageVO<ProcessListVO> pageVO = PageVO.createPageVO(iPage, iPage.getRecords());
        return ResultVO.ok(pageVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> submitProcess(ProcessSubmitDTO processSubmitDTO, LoginUserVO currentUser) {
        log.info("发布申请dto={},currentUser={}",processSubmitDTO,currentUser);
        ProcessDO processDO = getProcessDO(processSubmitDTO, currentUser);
        processServiceMapper.updateById(processDO);
        processDO = processServiceMapper.selectById(processDO.getId());
        if(processDO!=null){
            TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(processDO);
            taxPreferenceMapper.updateById(taxPreferenceDO);
        }
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

    @Override
    public void judgeProcessIng(Long id) {
        LambdaQueryWrapper<ProcessDO> queryWrapper = Wrappers.lambdaQuery(ProcessDO.class)
                .eq(ProcessDO::getTaxPreferenceId, id)
                .eq(ProcessDO::getLatestProcess, true)
                .eq(ProcessDO::getProcessStatus, ProcessStatus.NOT_APPROVED);

        ProcessDO processDO = processServiceMapper.selectOne(queryWrapper);
        if(processDO!=null){
            throw BizCode._4312.exception();
        }
    }

    /**
     * 封装流程对象
     */
    private ProcessDO getProcessDO(ProcessSubmitDTO processSubmitDTO, LoginUserVO currentUser) {
        ProcessDO processDO = new ProcessDO();
        BeanUtils.copyProperties(processSubmitDTO, processDO);
        processDO.setApproverId(currentUser.getId());
        processDO.setApproverName(currentUser.getUsername());
        processDO.setApprovalTime(LocalDateTime.now());
        processDO.setProcessStatus(processSubmitDTO.getTaxPreferenceStatus());
        log.info("封装结果:processDO:{}", processDO);
        return processDO;
    }

    /**
     * 封装税收优惠
     */
    private TaxPreferenceDO getTaxPreferenceDO( ProcessDO processDO) {
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        taxPreferenceDO.setId(processDO.getTaxPreferenceId());
        if (ProcessStatus.APPROVED.equals(processDO.getProcessStatus())) {
            taxPreferenceDO.setTaxPreferenceStatus(TaxPreferenceStatus.RELEASED);
        } else {
            taxPreferenceDO.setTaxPreferenceStatus(TaxPreferenceStatus.UNRELEASED);
        }
        log.info("封装结果:taxPreferenceDO:{}", taxPreferenceDO);
        return taxPreferenceDO;
    }


}
