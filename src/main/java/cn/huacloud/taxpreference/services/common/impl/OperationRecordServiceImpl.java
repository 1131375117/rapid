package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.enums.ViewType;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.OperationRecordService;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.OperationRecordDO;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.common.entity.dtos.DocStatisticsPlus;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.services.common.entity.dtos.ViewQueryDTO;
import cn.huacloud.taxpreference.services.common.entity.vos.OperationRecordVO;
import cn.huacloud.taxpreference.services.common.mapper.OperationRecordMapper;
import cn.huacloud.taxpreference.services.common.watch.WatcherViewService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * 操作记录服务实现类
 *
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class OperationRecordServiceImpl implements OperationRecordService {

    private final OperationRecordMapper operationRecordMapper;

    private DocStatisticsService docStatisticsService;

    @Autowired
    public void setDocStatisticsService(DocStatisticsService docStatisticsService) {
        this.docStatisticsService = docStatisticsService;
    }

    private final SysParamService sysParamService;
    private final WatcherViewService watchSubject;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOperationRecord(OperationRecordDTO operationRecordDTO, Long consumerUserId) {

        OperationRecordDO operationRecordDO = new OperationRecordDO();
        BeanUtils.copyProperties(operationRecordDTO, operationRecordDO);

        /*
         * 获取文档类型,并进行逻辑校验
         */
        SysParamDO sysParamDO = sysParamService.getSysParamDO(SysParamTypes.OPERATION_VIEWS, operationRecordDTO.getOperationType());
        if (sysParamDO == null) {
            throw BizCode._4501.exception();
        }
        /*
         * 插入操作记录表
         * */
        operationRecordDO.setConsumerUserId(consumerUserId);
        operationRecordDO.setCreateTime(LocalDateTime.now());
        operationRecordMapper.insert(operationRecordDO);

        /*
         * 插入统计表
         * */
        DocStatisticsPlus docStatisticsPlus = new DocStatisticsPlus();
        docStatisticsPlus.setDocType(DocType.valueOf(sysParamDO.getParamValue()));
        docStatisticsPlus.setDocId(Long.valueOf(operationRecordDO.getOperationParam()));
        docStatisticsPlus.setViewsPlus(1L);
        docStatisticsService.saveOrUpdateDocStatisticsService(docStatisticsPlus);

        /*
         * 写入es
         * */
        watchSubject.apply(docStatisticsPlus.getDocType(), operationRecordDTO);

    }

    @Override
    public PageVO<OperationRecordVO> queryOperationRecord(ViewQueryDTO pageQueryDTO, Long userId) {
        Page<OperationRecordVO> page = new Page<>(pageQueryDTO.getPageNum(), pageQueryDTO.getPageSize());
        IPage<OperationRecordVO> operationRecordVOIPage;
        if (ViewType.CASE_ANALYSIS.equals(pageQueryDTO.getViewType())) {
            operationRecordVOIPage = operationRecordMapper.selectCaseAnalysisByDocType(page, ViewType.CASE_ANALYSIS.name, userId);
        } else if (ViewType.FREQUENTLY_ASKED_QUESTION.equals(pageQueryDTO.getViewType())) {
            operationRecordVOIPage = operationRecordMapper.selectFrequentlyAskedQuestionByDocType(page, ViewType.FREQUENTLY_ASKED_QUESTION.name, userId);
        } else if (ViewType.POLICIES_EXPLAIN.equals(pageQueryDTO.getViewType())) {
            operationRecordVOIPage = operationRecordMapper.selectPoliciesExplainByDocType(page, ViewType.POLICIES_EXPLAIN.name, userId);
        } else if (ViewType.TAX_PREFERENCE.equals(pageQueryDTO.getViewType())) {
            operationRecordVOIPage = operationRecordMapper.selectTaxPreferenceByDocType(page,ViewType.TAX_PREFERENCE.name, userId);
        } else {
            operationRecordVOIPage = operationRecordMapper.selectPoliciesByDocType(page,ViewType.POLICIES.name, userId);
        }
        if (operationRecordVOIPage.getTotal() > 500) {
            operationRecordVOIPage.setTotal(500);
        }

        return PageVO.createPageVO(operationRecordVOIPage, operationRecordVOIPage.getRecords().stream().limit(500).collect(Collectors.toList()));
    }


}

