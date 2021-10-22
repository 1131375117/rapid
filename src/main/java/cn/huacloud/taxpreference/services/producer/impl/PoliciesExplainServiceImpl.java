package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesExplainMapper;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 政策解读服务实现类
 *
 * @author wuxin
 */
@RequiredArgsConstructor
@Service
public class PoliciesExplainServiceImpl implements PoliciesExplainService {

    private final PoliciesExplainMapper policiesExplainMapper;

    private final PoliciesMapper policiesMapper;

    /**
     * 新增政策解读
     *
     * @param policiesExplainDTO
     * @param id
     */
    @Override
    public void insertPoliciesExplain(PoliciesExplainDTO policiesExplainDTO, Long id) {
        PoliciesExplainDO policiesExplainDO = new PoliciesExplainDO();
        //设置值
        policiesExplainDO.setPoliciesId(policiesExplainDTO.getPoliciesId());
        policiesExplainDO.setInputUserId(id);
        policiesExplainDO.setCreateTime(LocalDateTime.now());
        policiesExplainDO.setUpdateTime(LocalDateTime.now());
        policiesExplainDO.setDeleted(0);
        //转换
        BeanUtils.copyProperties(policiesExplainDTO, policiesExplainDO);
        policiesExplainMapper.insert(policiesExplainDO);
    }
}
