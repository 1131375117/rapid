package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.process.ProcessStatus;
import cn.huacloud.taxpreference.common.enums.taxpreference.TaxPreferenceStatus;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.ValidationUtil;
import cn.huacloud.taxpreference.services.producer.ProcessService;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dos.ProcessDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessListDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessSubmitDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessInfoVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessListVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceVO;
import cn.huacloud.taxpreference.services.producer.mapper.ProcessServiceMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferenceMapper;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;
import cn.huacloud.taxpreference.sync.es.trigger.impl.TaxPreferenceEventTrigger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.huacloud.taxpreference.services.producer.impl.TaxPreferenceServiceImpl.TAX_PREFERENCE_ID;

/**
 * 流程接口impl
 *
 * @author fuhua
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final ProcessServiceMapper processServiceMapper;

    private final TaxPreferenceMapper taxPreferenceMapper;

    private final TaxPreferenceEventTrigger taxPreferenceEventTrigger;

    private  TaxPreferenceService taxPreferenceService;

    @Autowired
    public void setTaxPreferenceService(TaxPreferenceService taxPreferenceService) {
        this.taxPreferenceService = taxPreferenceService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> insertProcessService(Long[] taxPreferenceIds, ProducerLoginUserVO currentUser) throws MethodArgumentNotValidException {
        log.info("新增流程参数:taxPreferenceIds={},currentUser={}", taxPreferenceIds, currentUser);
        for (Long taxPreferenceId : taxPreferenceIds) {
            //校验税收优惠是否存在必填未校验
            TaxPreferenceVO taxPreferenceVO = taxPreferenceService.queryTaxPreferenceInfo(taxPreferenceId).getData();
                ValidationUtil.validate(taxPreferenceVO, ValidationGroup.Manual.class);
            TaxPreferenceDTO taxPreferenceDTO = new TaxPreferenceDTO();
            BeanUtils.copyProperties(taxPreferenceVO,taxPreferenceDTO);
            // 判断是否存在
            taxPreferenceService.judgeExists(taxPreferenceDTO);
            // 检测纳税人类型和标签管理
            taxPreferenceService.checkLabels(taxPreferenceDTO);
            //税收优惠
            judgeProcessIng(taxPreferenceId);
            //新增先查询是否存在并更新流程状态
            processServiceMapper.updateByTaxPreferenceId(taxPreferenceId);
            ProcessDO processDO = new ProcessDO();
            processDO.setTaxPreferenceId(taxPreferenceId);
            processDO.setCreateTime(LocalDateTime.now());
            processDO.setLatestProcess(true);
            processDO.setCreatorId(currentUser.getId());
            processDO.setCreatorName(currentUser.getUsername());
            processDO.setApprovalNote("系统自动审批");
            processDO.setApproverName("系统管理员");
            processDO.setApprovalTime(LocalDateTime.now());
            //直接修改为审批通过
            processDO.setProcessStatus(ProcessStatus.APPROVED);
            processServiceMapper.insert(processDO);
            TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(processDO);
            taxPreferenceMapper.updateById(taxPreferenceDO);
            //同步到es
            taxPreferenceEventTrigger.saveEvent(taxPreferenceDO.getId());
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO<PageVO<ProcessListVO>> queryProcessList(ProcessListDTO processListDTO, Long userId) {
        processListDTO.paramReasonable();
        log.info("查询条件:processListDTO={},userId={}", processListDTO, userId);
        Page<ProcessListVO> page = new Page<>(processListDTO.getPageNum(), processListDTO.getPageSize());
        IPage<ProcessListVO> iPage = processServiceMapper.queryProcessList(page, processListDTO);
        log.info("iPage-查询结果{}", iPage);
        PageVO<ProcessListVO> pageVO = PageVO.createPageVO(iPage, iPage.getRecords());
        return ResultVO.ok(pageVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> submitProcess(ProcessSubmitDTO processSubmitDTO, ProducerLoginUserVO currentUser) {
        log.info("发布申请dto={},currentUser={}", processSubmitDTO, currentUser);
        ProcessDO processDO = getProcessDO(processSubmitDTO, currentUser);
        processServiceMapper.updateById(processDO);
        processDO = processServiceMapper.selectById(processDO.getId());
        if (processDO != null) {
            TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(processDO);
            taxPreferenceMapper.updateById(taxPreferenceDO);
            // 事件触发
            taxPreferenceEventTrigger.saveEvent(taxPreferenceDO.getId());
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO<List<ProcessInfoVO>> queryProcessInfo(Long id) {
        log.info("查询条件:id-{}", id);
        List<ProcessInfoVO> processInfoVOList = new ArrayList<>();
        Map<String, Object> keymap = new HashMap<>(16);
        keymap.put(TAX_PREFERENCE_ID, id);
        LambdaQueryWrapper<ProcessDO> queryWrapper = Wrappers.lambdaQuery(ProcessDO.class);
        queryWrapper.eq(ProcessDO::getTaxPreferenceId, id)
                .orderByDesc(ProcessDO::getApprovalTime);
        List<ProcessDO> processDOList = processServiceMapper.selectList(queryWrapper);
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
        if (processDO != null) {
            throw BizCode._4312.exception();
        }
    }

    /**
     * 封装流程对象
     */
    private ProcessDO getProcessDO(ProcessSubmitDTO processSubmitDTO, ProducerLoginUserVO currentUser) {
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
    private TaxPreferenceDO getTaxPreferenceDO(ProcessDO processDO) {
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
