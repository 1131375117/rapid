package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesExplainMapper;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
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
     * 政策解读列表
     *
     * @param queryPoliciesExplainDTO
     * @return
     */
    @Override
    public PageVO<PoliciesExplainDO> getPoliciesExplainList(QueryPoliciesExplainDTO queryPoliciesExplainDTO) {
        LambdaQueryWrapper<PoliciesExplainDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!StringUtils.isEmpty(queryPoliciesExplainDTO.getTitle()), PoliciesExplainDO::getTitle, queryPoliciesExplainDTO.getTitle());
        IPage<PoliciesExplainDO> policiesExplainDOPage = policiesExplainMapper.selectPage(new Page<PoliciesExplainDO>(queryPoliciesExplainDTO.getPageNum(), queryPoliciesExplainDTO.getPageSize()), lambdaQueryWrapper);
        
        
        return PageVO.createPageVO(policiesExplainDOPage,policiesExplainDOPage.getRecords());
    }

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
        policiesExplainDO.setReleaseDate(LocalDate.now());
        policiesExplainDO.setCreateTime(LocalDateTime.now());
        policiesExplainDO.setUpdateTime(LocalDateTime.now());
        policiesExplainDO.setDeleted(0);
        //转换
        BeanUtils.copyProperties(policiesExplainDTO, policiesExplainDO);
        policiesExplainMapper.insert(policiesExplainDO);
    }

    /**
     * 修改政策解读
     *
     * @param policiesExplainDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePolicesExplain(PoliciesExplainDTO policiesExplainDTO) {
        //修改政策解读
        PoliciesExplainDO policiesExplainDO = new PoliciesExplainDO();
        BeanUtils.copyProperties(policiesExplainDTO,policiesExplainDO);
        policiesExplainMapper.updateById(policiesExplainDO);
    }

    /**
     * 根据id查询政策解读详情
     *
     * @param id
     * @return
     */
    @Override
    public PoliciesExplainDetailVO getPoliciesById(Long id) {
        PoliciesExplainDO policiesExplainDO = policiesExplainMapper.selectById(id);
        PoliciesExplainDetailVO policiesExplainDetailVO = new PoliciesExplainDetailVO();
        BeanUtils.copyProperties(policiesExplainDO,policiesExplainDetailVO);
        return policiesExplainDetailVO;
    }
}
