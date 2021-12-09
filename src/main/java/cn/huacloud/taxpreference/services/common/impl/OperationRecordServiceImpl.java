package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.OperationRecordService;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
import cn.huacloud.taxpreference.services.common.entity.dos.OperationRecordDO;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.services.common.mapper.OperationRecordMapper;
import cn.huacloud.taxpreference.services.common.watch.WatchViews;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final WatchViews watchSubject;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationRecord(OperationRecordDTO operationRecordDTO, ProducerLoginUserVO currentUser) {

        /*
         * 插入操作记录表
         * */
        OperationRecordDO operationRecordDO = new OperationRecordDO();
        BeanUtils.copyProperties(operationRecordDTO, operationRecordDO);
        operationRecordDO.setConsumerUserId(currentUser.getId());
        operationRecordMapper.insert(operationRecordDO);

        /*
         * 获取文档类型,并进行逻辑校验
         */
        SysParamDO sysParamDO = sysParamService.selectByParamKey(operationRecordDO.getOperationParam());
        if (sysParamDO == null) {
            throw BizCode._4501.exception();
        }

        /*
         * 插入统计表
         * */
        DocStatisticsDO docStatisticsDO = new DocStatisticsDO()
                .setDocType(DocType.valueOf(sysParamDO.getParamValue()))
                .setDocId(Long.valueOf(operationRecordDO.getOperationParam()))
                .setViews(1L);
        docStatisticsService.saveOrUpdateDocStatisticsService(docStatisticsDO);

        /*
         * 写入es
         * */
        watchSubject.apply(docStatisticsDO.getDocType(), operationRecordDTO);

    }


}

