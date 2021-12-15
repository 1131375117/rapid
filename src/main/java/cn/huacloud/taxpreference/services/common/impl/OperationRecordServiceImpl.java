package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.OperationRecordService;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.OperationRecordDO;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.common.entity.dtos.DocStatisticsPlus;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.services.common.mapper.OperationRecordMapper;
import cn.huacloud.taxpreference.services.common.watch.WatcherViewService;
import cn.huacloud.taxpreference.services.user.entity.vos.ConsumerLoginUserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public void operationRecord(OperationRecordDTO operationRecordDTO, ConsumerLoginUserVO currentUser) {

        OperationRecordDO operationRecordDO = new OperationRecordDO();
        BeanUtils.copyProperties(operationRecordDTO, operationRecordDO);

        /*
         * 获取文档类型,并进行逻辑校验
         */
        SysParamDO sysParamDO = sysParamService.getSysParamDO(SysParamTypes.OPERATION_VIEWS, operationRecordDTO.getOperationType());
        if (sysParamDO == null) {
            throw BizCode._4501.exception();
        }

        //查询操作记录表是否存在该条数据，不存在则插入
        LambdaQueryWrapper<OperationRecordDO> queryWrapper = Wrappers.lambdaQuery(OperationRecordDO.class);
        queryWrapper.eq(OperationRecordDO::getOperationType, operationRecordDTO.getOperationType());
        queryWrapper.eq(OperationRecordDO::getOperationParam, operationRecordDTO.getOperationParam());
        Long count = operationRecordMapper.selectCount(queryWrapper);

        /*
         * 插入操作记录表
         * */
        if (count == 0) {
            operationRecordDO.setConsumerUserId(1L);
            operationRecordDO.setCreateTime(LocalDateTime.now());
            operationRecordMapper.insert(operationRecordDO);
        }

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


}

