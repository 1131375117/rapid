package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.services.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesExplainMapper;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
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
    public void insertPoliciesExplain(PoliciesExplainDTO policiesExplainDTO,Long id) {
        //查询当前用户的政策法规id
        LambdaQueryWrapper<PoliciesDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PoliciesDO::getInputUserId,id);
        PoliciesDO policiesDO = policiesMapper.selectOne(queryWrapper);
        PoliciesExplainDO policiesExplainDO = new PoliciesExplainDO();
        //设置值
        policiesExplainDO.setPoliciesId(policiesDO.getId());
        policiesExplainDO.setInputUserId(id);
        policiesExplainDO.setCreateTime(LocalDateTime.now());
        policiesExplainDO.setUpdateTime(LocalDateTime.now());
        policiesExplainDO.setDeleted(0);
        //转换
        BeanUtils.copyProperties(policiesExplainDTO,policiesExplainDO);
        policiesExplainMapper.insert(policiesExplainDO);
    }
}
