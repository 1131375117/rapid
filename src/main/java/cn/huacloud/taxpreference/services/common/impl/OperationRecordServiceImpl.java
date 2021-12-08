package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.services.common.OperationRecordService;
import cn.huacloud.taxpreference.services.common.entity.dos.OperationRecordDO;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.services.common.mapper.OperationRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationRecord(OperationRecordDTO operationRecordDTO) {

        /*
         * 插入操作记录表
         * */
        OperationRecordDO operationRecordDO = new OperationRecordDO();
        BeanUtils.copyProperties(operationRecordDTO, operationRecordDO);
        operationRecordMapper.insert(operationRecordDO);

        /*
         * 获取文档类型,并进行逻辑校验
         */

        /*
        * 插入统计表
        * */


    }
}
