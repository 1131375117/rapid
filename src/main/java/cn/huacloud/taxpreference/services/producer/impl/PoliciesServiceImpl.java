package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesListDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 政策法规服务实现类
 *
 * @author wuxin
 */
@RequiredArgsConstructor
@Service
public class PoliciesServiceImpl implements PoliciesService {


    private final PoliciesMapper policiesMapper;

    private final PoliciesExplainService policiesExplainService;

    private final FrequentlyAskedQuestionService frequentlyAskedQuestionService;

    /**
     * 政策列表查询
     *
     * @param queryDTO
     * @return
     */
    @Override
    public IPage<PoliciesDO> getPolices(QueryDTO queryDTO) {

        QueryWrapper<Object> queryWrapper = new QueryWrapper<>()
                .orderBy(true, true, queryDTO.getSort());
        IPage<PoliciesDO> policiesDoPage = policiesMapper.selectPage(new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()), null);
        PoliciesVO policiesVO = new PoliciesVO();
        BeanUtils.copyProperties(policiesDoPage, policiesVO);
        return policiesDoPage;
    }

    /**
     * 新增政策法规
     *
     * @param policiesListDTO
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertPolicies(PoliciesListDTO policiesListDTO, Long id) {
        //新增政策法规
        PoliciesDO policiesDO = new PoliciesDO();
        BeanUtils.copyProperties(policiesListDTO, policiesDO);
        policiesDO.setInputUserId(id);
        policiesDO.setPoliciesStatus("");
        policiesDO.setReleaseDate(LocalDate.now());
        policiesDO.setCreateTime(LocalDateTime.now());
        policiesDO.setUpdateTime(LocalDateTime.now());
        policiesDO.setDeleted(0);
        policiesMapper.insert(policiesDO);
        //新增政策解读
        PoliciesExplainDTO policiesExplainDTO = new PoliciesExplainDTO();
        BeanUtils.copyProperties(policiesListDTO, policiesExplainDTO);
        policiesExplainDTO.setPoliciesId(policiesDO.getId());
        policiesExplainService.insertPoliciesExplain(policiesExplainDTO, id);
        //新增热点问答
        FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO = new FrequentlyAskedQuestionDTO();
        BeanUtils.copyProperties(policiesListDTO, frequentlyAskedQuestionDTO);
        frequentlyAskedQuestionService.insertFrequentlyAskedQuestion(frequentlyAskedQuestionDTO, id);
    }

    /**
     * 根据政策法规id获取详细信息
     *
     * @param id
     * @return
     */
    @Override
    public PoliciesVO getPoliciesById(Long id) {
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        PoliciesVO policiesVO = new PoliciesVO();
        BeanUtils.copyProperties(policiesDO, policiesVO);
        return policiesVO;
    }

    /**
     * 修改政策法规
     *
     * @param policiesListDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePolicies(PoliciesListDTO policiesListDTO) {
        //修改政策法规
        PoliciesDO policiesDO = new PoliciesDO();
        BeanUtils.copyProperties(policiesListDTO, policiesDO);
        policiesMapper.updateById(policiesDO);
        //修改政策解读
        PoliciesExplainDTO policiesExplainDTO = new PoliciesExplainDTO();
        BeanUtils.copyProperties(policiesListDTO,policiesExplainDTO);
        policiesExplainService.updatePolicesExplain(policiesExplainDTO);
        //修改热点问答
        FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO = new FrequentlyAskedQuestionDTO();
        BeanUtils.copyProperties(policiesListDTO,frequentlyAskedQuestionDTO);
        frequentlyAskedQuestionService.updateFrequentlyAskedQuestion(frequentlyAskedQuestionDTO);

    }
}
